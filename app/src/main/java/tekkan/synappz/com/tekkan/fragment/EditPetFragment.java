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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.activity.ViewPetActivity;
import tekkan.synappz.com.tekkan.custom.CustomSpinner;
import tekkan.synappz.com.tekkan.custom.network.TekenJsonArrayRequest;
import tekkan.synappz.com.tekkan.model.Pet;
import tekkan.synappz.com.tekkan.utils.Constants;
import tekkan.synappz.com.tekkan.utils.VolleyHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditPetFragment extends Fragment
        implements View.OnClickListener, CustomSpinner.OnItemChosenListener {

    private static final String TAG = EditPetFragment.class.getSimpleName();

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
    @BindView(R.id.et_breed)
    EditText mBreedEt;
    @BindView(R.id.sp_breed_type)
    CustomSpinner mBreedSP;
    @BindView(R.id.tv_weight)
    EditText mWeightEt;
    @BindView(R.id.tv_gender)
    EditText mGenderEt;
    @BindView(R.id.sp_animal_gender)
    CustomSpinner mAnimalGenderSP;

    private Pet mPet;
    private HashMap<String, Integer> mBreedListForCat;
    private HashMap<String, Integer> mBreedListForDog;

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
                    mPet = new Pet();
                    mPet.setName(mPetNameEt.getText().toString());
                    mPet.setAnimalType(mAnimalType.getText().toString());
                    mPet.setBreedId(mBreedListForCat.get(mBreedEt.getText().toString()));
                    String dateStr = mDateOfBirthEt.getText().toString();
                    Calendar c = Pet.parseDate(dateStr);
                    if (c != null) {
                        mPet.setDateOfBirth(c.getTimeInMillis());
                    }
                    mPet.setGender(mGenderEt.getText().toString());
                    mPet.setWeight(Integer.valueOf(mWeightEt.getText().toString()));

                    Intent intent = new Intent(getActivity(), ViewPetActivity.class);
                    intent.putExtra(ViewPetActivity.EXTRA_PET_DATA, mPet);
                    startActivity(intent);
                    getActivity().finish();
                }
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

        ButterKnife.bind(this, v);
        setHasOptionsMenu(true);

        mDateOfBirthEt.setOnClickListener(this);
        mAnimalType.setOnClickListener(this);
        mBreedEt.setOnClickListener(this);
        mGenderEt.setOnClickListener(this);
        mAnimalTypeSP.setOnItemChoosenListener(this);
        mBreedSP.setOnItemChoosenListener(this);
        mAnimalGenderSP.setOnItemChoosenListener(this);

        return v;
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

            case R.id.et_breed:
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

    private static final String JSON_S_BREED_NAME = "name";

    @Override
    public void onItemChosen(int position, int id) {
        Log.d(TAG, (String) mAnimalTypeSP.getSelectedItem());
        switch (id) {
            case R.id.sp_animal_type:
                String selectedAnimal = (String) mAnimalTypeSP.getSelectedItem();
                mAnimalType.setText(selectedAnimal);

                TekenJsonArrayRequest request = new TekenJsonArrayRequest(
                        Request.Method.GET,
                        Constants.Api.getUrl(Constants.Api.FUNC_GET_BREEDS),
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d(TAG, "Success");
                                mBreedListForCat = new HashMap<>();
                                String[] breedTypes = new String[response.length()];
                                JSONObject jsonObject;
                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        jsonObject = response.getJSONObject(i);
                                        breedTypes[i] = jsonObject.getString(JSON_S_BREED_NAME);
                                        mBreedListForCat.put(jsonObject.getString(JSON_S_BREED_NAME), jsonObject.getInt("id"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                        getContext(), android.R.layout.simple_spinner_item, breedTypes);
                                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                                mBreedSP.setAdapter(adapter);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "failure");
                            }
                        }
                );

                if (selectedAnimal.equalsIgnoreCase(getString(R.string.dog))) {
                    request.addParam(Constants.Api.QUERY_PARAMETER1, Constants.Api.ANIMAL_HOND);
                } else {
                    request.addParam(Constants.Api.QUERY_PARAMETER1, Constants.Api.ANIMAL_CAT);
                }
                VolleyHelper.getInstance(getActivity()).addToRequestQueue(request);
                break;
            case R.id.sp_breed_type:
                mBreedEt.setText((String) mBreedSP.getSelectedItem());
                break;
            case R.id.sp_animal_gender:
                mGenderEt.setText((String) mAnimalGenderSP.getSelectedItem());
        }
    }
}
