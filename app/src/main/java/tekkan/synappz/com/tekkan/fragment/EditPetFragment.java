package tekkan.synappz.com.tekkan.fragment;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.activity.ViewPetActivity;
import tekkan.synappz.com.tekkan.custom.CustomSpinner;
import tekkan.synappz.com.tekkan.custom.network.TekenJsonArrayRequest;
import tekkan.synappz.com.tekkan.utils.Constants;
import tekkan.synappz.com.tekkan.utils.VolleyHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditPetFragment extends Fragment implements View.OnClickListener, CustomSpinner.OnItemChosenListener {

    private static final String TAG = EditPetFragment.class.getSimpleName();

    public static String TAG_PET_NAME = "PET_NAME",
            TAG_BREED = "BREED",
            TAG_DOB = "DOB",
            TAG_IS_CAT_OR_DOG = "IS_CAT_OR_DOG",
            TAG_WEIGHT = "WEIGHT",
            TAG_GENDER = "GENDER",
            ARGS_PET_PROFILE = "PET_PROFILE_DATA";

    private boolean mIsUpdate = false;
    private boolean mIsDone = true;

    @BindView(R.id.et_pet_name)
    EditText mPetNameEt;
    @BindView(R.id.sp_animal_type)
    CustomSpinner mAnimalTypeSP;
    @BindView(R.id.tv_date_of_birth)
    EditText mDateOfBirthEt;
    @BindView(R.id.tv_animal_type)
    EditText mAnimalType;
    @BindView(R.id.tv_breed)
    EditText mBreedEt;
    @BindView(R.id.sp_breed_type)
    CustomSpinner mBreedSP;
    @BindView(R.id.tv_weight)
    EditText mWeightEt;
    @BindView(R.id.tv_gender)
    EditText mGenderEt;
    @BindView(R.id.sp_animal_gender)
    CustomSpinner mAnimalGenderSP;

    private BreedSpinnerAdapter mBreedSpinnerAdapter;

    private ArrayList<Breed>
            mCatBreeds = new ArrayList<>(),
            mDogBreeds = new ArrayList<>(),
            mCurrentBreeds = new ArrayList<>();

    public static EditPetFragment newInstance() {

        Bundle args = new Bundle();
        EditPetFragment fragment = new EditPetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pet_profile_update, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem editItem = menu.findItem(R.id.action_edit);
        MenuItem doneItem = menu.findItem(R.id.action_done);
        editItem.setVisible(mIsUpdate);
        doneItem.setVisible(mIsDone);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            case R.id.action_done:
                mIsUpdate = true;
                mIsDone = false;
                if (isValidate()) {
                    Bundle bundle = new Bundle();
                    bundle.putString(TAG_PET_NAME, mPetNameEt.getText().toString());
                    bundle.putString(TAG_IS_CAT_OR_DOG, mAnimalType.getText().toString());
                    bundle.putString(TAG_BREED, mBreedEt.getText().toString());
                    bundle.putString(TAG_DOB, mDateOfBirthEt.getText().toString());
                    bundle.putString(TAG_GENDER, mGenderEt.getText().toString());
                    bundle.putString(TAG_WEIGHT, mWeightEt.getText().toString());

                    Intent intent = new Intent(getActivity(), ViewPetActivity.class);
                    intent.putExtra(ARGS_PET_PROFILE, bundle);
                    startActivity(intent);
                    getActivity().finish();
                }
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.action_edit:
                mIsDone = true;
                mIsUpdate = false;
                getActivity().invalidateOptionsMenu();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_pet, container, false);

        init(v);

        setHasOptionsMenu(true);

        mDateOfBirthEt.setOnClickListener(this);
        mAnimalType.setOnClickListener(this);
        mBreedEt.setOnClickListener(this);
        mGenderEt.setOnClickListener(this);
        mAnimalTypeSP.setOnItemChosenListener(this);
        mBreedSP.setOnItemChosenListener(this);
        mAnimalGenderSP.setOnItemChosenListener(this);

        mBreedSP.setAdapter(mBreedSpinnerAdapter);

        return v;
    }

    private void init(View v) {
        ButterKnife.bind(this, v);
        mBreedSpinnerAdapter = new BreedSpinnerAdapter();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_date_of_birth:
                datePicker();
                break;
            case R.id.tv_animal_type:
                mAnimalTypeSP.performClick();
                break;

            case R.id.tv_breed:
                mBreedSP.performClick();
                break;

            case R.id.tv_gender:
                mAnimalGenderSP.performClick();
                break;
        }
    }

    private void datePicker() {
        int mYear, mMonth, mDay;
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        mDateOfBirthEt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        mDateOfBirthEt.setError(null);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private boolean isValidate() {

        if (mPetNameEt.getText().toString().equals("")) {
            mPetNameEt.setError(getString(R.string.blank_filed_message));
            return false;
        } else if (mAnimalType.getText().toString().equals("")) {
            mAnimalType.setError(getString(R.string.blank_filed_message));
            return false;
        } else if (mBreedEt.getText().toString().equals("")) {
            mBreedEt.setError(getString(R.string.blank_filed_message));
            return false;
        } else if (mDateOfBirthEt.getText().toString().equals("")) {
            mDateOfBirthEt.setError(getString(R.string.blank_filed_message));
            return false;
        } else if (mGenderEt.getText().toString().equals("")) {
            mGenderEt.setError(getString(R.string.blank_filed_message));
            return false;
        } else if (mWeightEt.getText().toString().equals("")) {
            mWeightEt.setError(getString(R.string.blank_filed_message));
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onItemChosen(AdapterView<?> adapterView, int position) {
        Log.d(TAG, (String) mAnimalTypeSP.getSelectedItem());
        switch (adapterView.getId()) {
            case R.id.sp_animal_type:
                final String selectedAnimal = (String) mAnimalTypeSP.getSelectedItem();
                mAnimalType.setText(selectedAnimal);

                final TekenJsonArrayRequest request = new TekenJsonArrayRequest(
                        Request.Method.GET,
                        Constants.Api.getUrl(Constants.Api.FUNC_GET_BREEDS),
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {

                                ArrayList<Breed> breeds = null;

                                if (selectedAnimal.equalsIgnoreCase(getString(R.string.dog))) {
                                    breeds = mDogBreeds;
                                } else if (selectedAnimal.equalsIgnoreCase(getString(R.string.cat))){
                                    breeds = mCatBreeds;
                                }else{
                                    Log.e(TAG,"No such breed");
                                    return;
                                }


                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        JSONObject breedObject = response.getJSONObject(i);
                                        breeds.add(new Breed(breedObject));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                mCurrentBreeds.clear();
                                mCurrentBreeds.addAll(breeds);

                                mBreedSpinnerAdapter.notifyDataSetChanged();

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "failure");
                            }
                        }
                );

                boolean hasFetched = false;
                if (selectedAnimal.equalsIgnoreCase(getString(R.string.dog))) {
                    if (mDogBreeds.isEmpty()) {
                        request.addParam(Constants.Api.QUERY_PARAMETER1, Constants.Api.ANIMAL_HOND);
                    } else {
                        hasFetched = true;
                    }

                } else {
                    if (mCatBreeds.isEmpty()) {
                        request.addParam(Constants.Api.QUERY_PARAMETER1, Constants.Api.ANIMAL_CAT);
                    } else {
                        hasFetched = true;
                    }
                }

                if (!hasFetched) {
                    VolleyHelper.getInstance(getActivity()).addToRequestQueue(request);
                }
                break;
            case R.id.sp_breed_type:
                mBreedEt.setText((String) mBreedSP.getSelectedItem());
                break;
            case R.id.sp_animal_gender:
                mGenderEt.setText((String) mAnimalGenderSP.getSelectedItem());

        }
    }

    private class BreedSpinnerAdapter extends ArrayAdapter<Breed> {
        BreedSpinnerAdapter() {
            super(getActivity(), android.R.layout.simple_spinner_item, mCurrentBreeds);
            setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        }
    }

    private class Breed {

        private static final String
                JSON_N_ID = "id",
                JSON_S_NAME = "name";

        private long mId;
        private String mName;

        public Breed(JSONObject object) {
            mId = object.optLong(JSON_N_ID);
            mName = object.optString(JSON_S_NAME);
        }

        public long getId() {
            return mId;
        }

        public String getName() {
            return mName;
        }

        @Override
        public String toString() {
            return getName();
        }
    }
}
