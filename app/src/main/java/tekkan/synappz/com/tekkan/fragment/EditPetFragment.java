package tekkan.synappz.com.tekkan.fragment;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.custom.CustomSpinner;
import tekkan.synappz.com.tekkan.custom.network.TekenErrorListener;
import tekkan.synappz.com.tekkan.custom.network.TekenJsonArrayRequest;
import tekkan.synappz.com.tekkan.custom.network.TekenResponseListener;
import tekkan.synappz.com.tekkan.custom.network.TekenStringRequest;
import tekkan.synappz.com.tekkan.dialogs.AlertDialogFragment;
import tekkan.synappz.com.tekkan.dialogs.ConfirmDialogFragment;
import tekkan.synappz.com.tekkan.dialogs.ProgressDialogFragment;
import tekkan.synappz.com.tekkan.model.Breed;
import tekkan.synappz.com.tekkan.model.Pet;
import tekkan.synappz.com.tekkan.model.User;
import tekkan.synappz.com.tekkan.utils.Constants;
import tekkan.synappz.com.tekkan.utils.DateUtils;
import tekkan.synappz.com.tekkan.utils.VolleyHelper;

import static tekkan.synappz.com.tekkan.utils.Constants.PetType.CAT;
import static tekkan.synappz.com.tekkan.utils.Constants.PetType.DOG;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditPetFragment extends Fragment implements CustomSpinner.OnItemChosenListener, ConfirmDialogFragment.ConfirmDialogFragmentListener {

    private static final String
            TAG = EditPetFragment.class.getSimpleName(),
            ARGS_PET = TAG + ".ARGS_PET",
            STATE_DOG_BREEDS = TAG + ".STATE_DOG_BREEDS",
            STATE_CAT_BREEDS = TAG + ".STATE_CAT_BREEDS",
            TAG_DIALOG = TAG + ".TAG_DIALOG";

    private static final int
            REQUEST_GET_BREEDS = 0;

    @BindView(R.id.et_pet_name)
    EditText mPetNameET;

    @BindView(R.id.tv_animal_type)
    TextView mAnimalTypeTV;
    @BindView(R.id.sp_animal_type)
    CustomSpinner mAnimalTypeSP;

    @BindView(R.id.tv_breed)
    TextView mBreedTV;
    @BindView(R.id.sp_breed_type)
    CustomSpinner mBreedSP;

    @BindView(R.id.tv_date_of_birth)
    TextView mDateOfBirthTV;

    @BindView(R.id.tv_gender)
    TextView mGenderTV;
    @BindView(R.id.sp_animal_gender)
    CustomSpinner mAnimalGenderSP;

    @BindView(R.id.et_weight)
    EditText mWeightET;

    private BreedSpinnerAdapter mBreedSpinnerAdapter;

    private Pet mPet;

    private ProgressDialogFragment
            mBreedFetchPD,
            mPetUpdatePD;

    private AlertDialogFragment
            mEditFailureAD;

    private ConfirmDialogFragment
            mEditSuccessAD;

    private boolean mIsFetchingDogBreeds = false, mIsFetchingCatBreeds = false;

    private ArrayList<Breed>
            mCatBreeds = new ArrayList<>(),
            mDogBreeds = new ArrayList<>(),
            mCurrentBreeds = new ArrayList<>();

    public static EditPetFragment newInstance(Pet pet) {
        Bundle args = new Bundle();
        args.putParcelable(ARGS_PET, pet);
        EditPetFragment fragment = new EditPetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pet_profile_update, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            case R.id.action_done:
                if (isValidate()) {
                    createOrEditAnimalProfile();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mPet = arguments.getParcelable(ARGS_PET);
            Log.d(TAG , mPet.getName());
        } else {
            mPet = new Pet();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_pet, container, false);
        init(v);

        if (savedInstanceState != null) {
            mDogBreeds = savedInstanceState.getParcelableArrayList(STATE_DOG_BREEDS);
            mCatBreeds = savedInstanceState.getParcelableArrayList(STATE_CAT_BREEDS);
        }

        if (mDogBreeds.isEmpty()) {
            mIsFetchingDogBreeds = true;
            getBreeds(DOG);
        }

        if (mCatBreeds.isEmpty()) {
            mIsFetchingCatBreeds = true;
            getBreeds(CAT);
        }

        if (mIsFetchingCatBreeds || mIsFetchingDogBreeds) {
            mBreedFetchPD.show(getFragmentManager(), TAG_DIALOG);
        }

        mBreedSP.setAdapter(mBreedSpinnerAdapter);

        mAnimalTypeSP.setOnItemChosenListener(this);
        mAnimalGenderSP.setOnItemChosenListener(this);
        mBreedSP.setOnItemChosenListener(this);

        updateUI();
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(STATE_DOG_BREEDS, mDogBreeds);
        outState.putParcelableArrayList(STATE_CAT_BREEDS, mCatBreeds);
        super.onSaveInstanceState(outState);

    }

    private void init(View v) {
        ButterKnife.bind(this, v);
        mBreedSpinnerAdapter = new BreedSpinnerAdapter();
        mBreedFetchPD = ProgressDialogFragment.newInstance(getString(R.string.progress_fetching_breeds));
        mPetUpdatePD = ProgressDialogFragment.newInstance(getString(
                mPet.getId() > 0 ? R.string.progress_updating_pet : R.string.progress_creating_pet
        ));

        mEditSuccessAD = ConfirmDialogFragment.newInstance(mPet.getId() > 0 ? R.string.success_update_pet : R.string.success_create_pet);
        mEditSuccessAD.setListener(this);
        mEditFailureAD = AlertDialogFragment.newInstance(mPet.getId() > 0 ? R.string.error_update_pet : R.string.error_create_pet);
    }

    @OnClick({R.id.tv_date_of_birth, R.id.tv_animal_type, R.id.tv_breed, R.id.tv_gender})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_date_of_birth:
                datePicker();
                break;
            case R.id.tv_animal_type:
                mAnimalTypeSP.performClick();
                break;

            case R.id.tv_breed:
                if(mBreedSP.getAdapter().getCount() > 0) {
                    mBreedSP.performClick();
                }else{
                    Toast.makeText(
                            getActivity(),
                            getString(R.string.select_animal_type),
                            Toast.LENGTH_LONG
                    ).show();
                }
                break;

            case R.id.tv_gender:
                mAnimalGenderSP.performClick();
                break;
        }
    }

    @OnTextChanged(R.id.et_pet_name)
    void onNameChanged(CharSequence text) {
        mPet.setName(text.toString());
    }

    @OnTextChanged(R.id.et_weight)
    void onWeightChanged(CharSequence weight) {
        try {
            int weightInt = Integer.parseInt(weight.toString());
            mPet.setWeight(weightInt);
        } catch (Exception e) {
            mPet.setWeight(0);
        }
    }

    private void updateUI() {

        if (mPet == null) {
            return;
        }

        mPetNameET.setText(mPet.getName());

        Constants.PetType petType = mPet.getType();

        if (petType == CAT) {
            mAnimalTypeTV.setText(R.string.cat);
        } else if (petType == DOG) {
            mAnimalTypeTV.setText(R.string.dog);
        } else {
            mAnimalTypeTV.setText("");
        }

        Breed breed = getBreed(mPet.getType(), mPet.getBreedId());
        if (breed != null) {
            mBreedTV.setText(breed.getName());
        } else {
            mBreedTV.setText("");
        }

        if (mPet.getBirthDate() != null) {
            Log.d(TAG,mPet.getBirthDate().toString());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mPet.getBirthDate());
            mDateOfBirthTV.setText(
                    getString(R.string.date_of_birth_format,
                            calendar.get(Calendar.DATE),
                            calendar.get(Calendar.MONTH) + 1,
                            calendar.get(Calendar.YEAR)
                    )
            );
        }

        Constants.Gender gender = mPet.getGender();

        if (gender == Constants.Gender.MALE) {
            mGenderTV.setText(R.string.male);
        } else if (gender == Constants.Gender.FEMALE) {
            mGenderTV.setText(R.string.female);
        } else {
            mGenderTV.setText("");
        }

        int weight = mPet.getWeight();
        if (weight > 0) {
            mWeightET.setText(String.valueOf(weight));
        } else {
            mWeightET.setText("");
        }

    }

    private void datePicker() {
        int mYear, mMonth, mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DATE, dayOfMonth);
                        mPet.setBirthDate(calendar.getTime());

                        updateUI();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private boolean isValidate() {
        return !TextUtils.isEmpty(mPet.getName())
                && mPet.getType() != null
                && mPet.getBreedId() > 0
                && mPet.getBirthDate() != null
                && mPet.getGender() != null
                && mPet.getWeight() > 0;
    }

    private Breed getBreed(Constants.PetType petType, String name) {

        ArrayList<Breed> breeds = null;

        if (petType == CAT) {
            breeds = mCatBreeds;
        } else if (petType == DOG) {
            breeds = mDogBreeds;
        } else {
            return null;
        }

        if (breeds != null) {
            for (int i = 0; i < breeds.size(); i++) {
                Breed breed = breeds.get(i);
                if (breed.getName().equalsIgnoreCase(name)) {
                    return breed;
                }
            }
        }
        return null;
    }

    private Breed getBreed(Constants.PetType petType, long id) {
        ArrayList<Breed> breeds = null;

        if (petType == CAT) {
            breeds = mCatBreeds;
        } else if (petType == DOG) {
            breeds = mDogBreeds;
        } else {
            return null;
        }

        for (int i = 0; i < breeds.size(); i++) {
            Breed breed = breeds.get(i);
            if (breed.getId() == id) {
                return breed;
            }
        }
        return null;
    }


    @Override
    public void onItemChosen(AdapterView<?> adapterView, int position) {
        switch (adapterView.getId()) {
            case R.id.sp_animal_type:

                String animalType = (String) adapterView.getSelectedItem();

                mCurrentBreeds.clear();

                if (getString(R.string.cat).equalsIgnoreCase(animalType)) {
                    mPet.setType(Constants.PetType.CAT);
                    mCurrentBreeds.addAll(mCatBreeds);
                } else if (getString(R.string.dog).equalsIgnoreCase(animalType)) {
                    mPet.setType(DOG);
                    mCurrentBreeds.addAll(mDogBreeds);
                } else {
                    mPet.setType(null);
                }
                mPet.setBreedId(0);
                mBreedSpinnerAdapter.notifyDataSetChanged();
                break;
            case R.id.sp_breed_type:

                Breed breed = (Breed) adapterView.getSelectedItem();

                if (breed != null) {
                    mPet.setBreedId(breed.getId());
                } else {
                    mPet.setBreedId(0);
                }
                break;
            case R.id.sp_animal_gender:
                String gender = (String) adapterView.getSelectedItem();
                if (getString(R.string.male).equalsIgnoreCase(gender)) {
                    mPet.setGender(Constants.Gender.MALE);
                } else if (getString(R.string.female).equalsIgnoreCase(gender)) {
                    mPet.setGender(Constants.Gender.FEMALE);
                }

        }
        updateUI();
    }

    private void getBreeds(final Constants.PetType petType) {
        TekenJsonArrayRequest request = new TekenJsonArrayRequest(
                Request.Method.GET,
                Constants.Api.getUrl(Constants.Api.FUNC_GET_BREEDS),
                new TekenResponseListener<JSONArray>() {
                    @Override
                    public void onResponse(int requestCode, JSONArray response) {

                        ArrayList<Breed> breeds = null;

                        switch (petType) {
                            case DOG:
                                breeds = mDogBreeds;
                                break;
                            case CAT:
                                breeds = mCatBreeds;
                                break;
                        }

                        if (breeds == null) {
                            Log.e(TAG, "No such breed");
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

                        if (petType == DOG) {
                            mIsFetchingDogBreeds = false;
                        } else if (petType == CAT) {
                            mIsFetchingCatBreeds = false;
                        }

                        if (!mIsFetchingCatBreeds && !mIsFetchingDogBreeds) {
                            mBreedFetchPD.dismiss();
                        }

                        updateUI();
                    }
                },
                new TekenErrorListener() {
                    @Override
                    public void onErrorResponse(int requestCode, VolleyError error, int status, String message) {
                        if (petType == DOG) {
                            mIsFetchingDogBreeds = false;
                        } else if (petType == CAT) {
                            mIsFetchingCatBreeds = false;
                        }

                        if (!mIsFetchingCatBreeds && !mIsFetchingDogBreeds) {
                            mBreedFetchPD.dismiss();
                        }
                    }
                },
                REQUEST_GET_BREEDS
        );
        request.addParam(Constants.Api.QUERY_PARAMETER1, petType.toApi());
        VolleyHelper.getInstance(getActivity()).addToRequestQueue(request);
    }

    private static final String
            PARAM_EMAIL = "email",
            PARAM_NAME = "name",
            PARAM_FAMILY_NAME = "familyname",
            PARAM_TYPE = "type",
            PARAM_BREED = "breed",
            PARAM_BIRTHDATE = "birthdate",
            PARAM_GENDER = "gender",
            PARAM_WEIGHT = "weight",
            PARAM_ANIMAL_ID = "animals_id";

    private void createOrEditAnimalProfile() {

        String url = mPet.getId() > 0 ? Constants.Api.FUNC_EDIT_ANIMAL : Constants.Api.FUNC_CREATE_ANIMAL;

        mPetUpdatePD.show(getFragmentManager(), TAG_DIALOG);

        TekenStringRequest request = new TekenStringRequest(
                Request.Method.POST,
                Constants.Api.getUrl(url),
                new TekenResponseListener<String>() {
                    @Override
                    public void onResponse(int requestCode, String response) {
                        mPetUpdatePD.dismiss();
                        mEditSuccessAD.show(getFragmentManager(), TAG_DIALOG);
                    }
                },
                new TekenErrorListener() {
                    @Override
                    public void onErrorResponse(int requestCode, VolleyError error, int status, String message) {
                        mPetUpdatePD.dismiss();
                        mEditFailureAD.show(getFragmentManager(), TAG_DIALOG);
                    }
                },
                0
        );

        request.addParam(PARAM_EMAIL, User.getInstance(getActivity()).getEmail());
        request.addParam(PARAM_NAME, mPet.getName());
        request.addParam(PARAM_FAMILY_NAME, "");
        request.addParam(PARAM_TYPE, mPet.getType().toApi());
        request.addParam(PARAM_BREED, String.valueOf(mPet.getBreedId()));
        request.addParam(PARAM_BIRTHDATE, DateUtils.toApi(mPet.getBirthDate()));
        request.addParam(PARAM_GENDER, mPet.getGender().toApi());
        request.addParam(PARAM_WEIGHT, String.valueOf(mPet.getWeight()));

        if(mPet.getId() > 0){
            request.addParam(PARAM_ANIMAL_ID,String.valueOf(mPet.getId()));
        }

        VolleyHelper.getInstance(getActivity()).addToRequestQueue(request);

    }

    @Override
    public void onPositiveClicked(DialogInterface dialog) {
        getActivity().finish();
    }

    @Override
    public void onNegativeClicked() {

    }

    private class BreedSpinnerAdapter extends ArrayAdapter<Breed> {
        BreedSpinnerAdapter() {
            super(getActivity(), android.R.layout.simple_spinner_item, mCurrentBreeds);
            setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        }
    }
}
