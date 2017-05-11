package tekkan.synappz.com.tekkan.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import butterknife.BindView;
import butterknife.ButterKnife;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.activity.ViewPetActivity;
import tekkan.synappz.com.tekkan.custom.network.TekenJsonArrayRequest;
import tekkan.synappz.com.tekkan.custom.network.TekenJsonObjectRequest;
import tekkan.synappz.com.tekkan.custom.network.TekenStringRequest;
import tekkan.synappz.com.tekkan.utils.Constants;
import tekkan.synappz.com.tekkan.utils.LoginUtils;
import tekkan.synappz.com.tekkan.utils.VolleyHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditPetFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = EditPetFragment.class.getSimpleName();


    private boolean mIsNewProfile = true;
    Bundle mArgs;
    String mPetId;
    public static String TAG_PET_NAME = "PET_NAME",
            TAG_BREED = "BREED",
            TAG_DOB = "DOB",
            TAG_IS_CAT_OR_DOG = "IS_CAT_OR_DOG",
            TAG_WEIGHT = "WEIGHT",
            TAG_GENDER = "GENDER",
            TAG_PET_ID = "ID",
            TAG_PROFILE_TYPE = "PROFILE_TYPE",
            ARGS_PET_PROFILE = "PET_PROFILE_DATA";

    private boolean mIsUpdate = false;
    private boolean mIsDone = true;

    @BindView(R.id.et_pet_name)
    EditText mPetNameEt;

    @BindView(R.id.tv_date_of_birth)
    EditText mDateOfBirthEt;

    @BindView(R.id.tv_is_cat_or_dog)
    EditText mIsCatOrDogEt;

    @BindView(R.id.tv_breed)
    EditText mBreedEt;

    @BindView(R.id.tv_weight)
    EditText mWeightEt;

    @BindView(R.id.tv_gender)
    EditText mGenderEt;


    private String[] mIsMaleOrFemaleList = {"Male", "Female"};
    private String[] mIsCatOrDogList = {"Cat", "Dog"};
    private String[] mBreedList;
            //= {"1", "2", "3"};

    public static EditPetFragment newInstance(boolean profileType) {

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
                    createOrEditAnimalProfile();
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
        ButterKnife.bind(this, v);
        setHasOptionsMenu(true);
        mDateOfBirthEt.setOnClickListener(this);
        mIsCatOrDogEt.setOnClickListener(this);
        mBreedEt.setOnClickListener(this);
        mGenderEt.setOnClickListener(this);


        if (mArgs != null) {
            if(!mArgs.getBoolean(TAG_PROFILE_TYPE)){
                mIsNewProfile = false;
                updateUI();
            }else {
                mIsNewProfile = true;

            }

        }
        return v;
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.tv_date_of_birth:
                datePicker();
                break;
            case R.id.tv_is_cat_or_dog:
                picker(mIsCatOrDogEt, mIsCatOrDogList);

                break;

            case R.id.tv_breed:

                getBreed();
                break;

            case R.id.tv_gender:
                picker(mGenderEt, mIsMaleOrFemaleList);
                break;
        }


    }


    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        mArgs = args;
    }


    private void updateUI() {
        mPetNameEt.setText(mArgs.getString(TAG_PET_NAME));
        mIsCatOrDogEt.setText(mArgs.getString(TAG_IS_CAT_OR_DOG));
        mBreedEt.setText(mArgs.getString(TAG_BREED));
        mDateOfBirthEt.setText(mArgs.getString(TAG_DOB));
        mGenderEt.setText(mArgs.getString(TAG_GENDER));
        mWeightEt.setText(mArgs.getString(TAG_WEIGHT));
        mPetId = mArgs.getString(TAG_PET_ID);

    }


    public void getBreed(){

        String type;

        if(mIsCatOrDogEt.getText().toString().equals("Cat")){
            type = "C";
        }else{
            type = "D";

        }

            String  PARM_BREED_TYPE = "p1";


        TekenJsonArrayRequest request = new TekenJsonArrayRequest(Request.Method.GET, Constants.Api.getUrl(Constants.Api.FUNC_GET_BREEDS), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG , "Success"+response);

                mBreedList = new String[response.length()];
                for(int i = 0; i < response.length();i++){
                    String breedName;
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        breedName = jsonObject.getString("name");
                        mBreedList[i] = breedName;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                picker(mBreedEt, mBreedList);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG , "Failure");
            }
        });
            request.addParam(PARM_BREED_TYPE,type);
            VolleyHelper.getInstance(getActivity()).addToRequestQueue(request);


    }


    public void createOrEditAnimalProfile(){

        String url;

        if(mIsNewProfile){
            url = Constants.Api.getUrl(Constants.Api.FUNC_CREATE_ANIMAL);
        }else{
            url = Constants.Api.getUrl(Constants.Api.FUNC_EDIT_ANIMAL);
        }

        String email = "test@test.com",
                name = mPetNameEt.getText().toString(),
                familyName = "",
                type = "",
                breed = "1",
                birthdate = mDateOfBirthEt.getText().toString(),
                gender = "",
                weight = mWeightEt.getText().toString();

        if(mGenderEt.getText().toString().equals("Male")){
            gender = "M";
        }else{
            gender = "F";

        }

        if(mIsCatOrDogEt.getText().toString().equals("Cat")){
            type = "C";
        }else{
            type = "D";

        }

        String  PARM_EMAIL = "email",
                PARM_NAME = "name",
                PARM_FAMILY_NAME = "familyname",
                PARM_TYPE = "type",
                PARM_BREED = "breed",
                PARM_BIRTHDATE = "birthdate",
                PARM_GENDER = "gender",
                PARM_WEIGHT = "weight",
                PARM_ID = "animals_id";

        TekenStringRequest request = new TekenStringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                         Log.d(TAG , "Success");
                         onViewPetProfile(response);

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG , "Failure");
            }
        }
        );
        request.addParam(PARM_EMAIL,email);
        request.addParam(PARM_NAME,name);
        request.addParam(PARM_FAMILY_NAME,familyName);
        request.addParam(PARM_TYPE,type);
        request.addParam(PARM_BREED,breed);
        request.addParam(PARM_BIRTHDATE,birthdate);
        request.addParam(PARM_GENDER,gender);
        request.addParam(PARM_WEIGHT,weight);
        if(!mIsNewProfile){
            request.addParam(PARM_ID,mPetId);
        }

        VolleyHelper.getInstance(getActivity()).addToRequestQueue(request);
    }


    private void onViewPetProfile(String id){
        Bundle bundle = new Bundle();
        bundle.putString(TAG_PET_NAME, mPetNameEt.getText().toString());
        bundle.putString(TAG_IS_CAT_OR_DOG, mIsCatOrDogEt.getText().toString());
        bundle.putString(TAG_BREED, mBreedEt.getText().toString());
        bundle.putString(TAG_DOB, mDateOfBirthEt.getText().toString());
        bundle.putString(TAG_GENDER, mGenderEt.getText().toString());
        bundle.putString(TAG_WEIGHT, mWeightEt.getText().toString());
        if(mIsNewProfile){
            bundle.putString(TAG_PET_ID,id);
        }else{
         bundle.putString(TAG_PET_ID,mPetId);
        }

        Intent intent = new Intent(getActivity(), ViewPetActivity.class);
        intent.putExtra(ARGS_PET_PROFILE, bundle);
        startActivity(intent);
        getActivity().finish();
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

                        mDateOfBirthEt.setText(year + "-" + dayOfMonth + "-" + (monthOfYear + 1));
                        mDateOfBirthEt.setError(null);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    private void picker(final EditText editText, final String[] pickerItems) {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_number_picker);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancel_btn);
        Button setButton = (Button) dialog.findViewById(R.id.set_btn);
        final NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(pickerItems.length - 1);
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setDisplayedValues(pickerItems);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(pickerItems[numberPicker.getValue()]);
                editText.setError(null);
                dialog.dismiss();
            }
        });
        dialog.show();

    }


    private boolean isValidate() {

        if (mPetNameEt.getText().toString().equals("")) {
            mPetNameEt.setError(getString(R.string.blank_filed_message));
            return false;
        } else if (mIsCatOrDogEt.getText().toString().equals("")) {
            mIsCatOrDogEt.setError(getString(R.string.blank_filed_message));
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


}
