package tekkan.synappz.com.tekkan.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.activity.ViewPetActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditPetFragment extends Fragment implements View.OnClickListener {

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
    private String[] mBreedList = {"A", "B", "C"};

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
                    bundle.putString(TAG_IS_CAT_OR_DOG, mIsCatOrDogEt.getText().toString());
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
        ButterKnife.bind(this, v);
        setHasOptionsMenu(true);
        mDateOfBirthEt.setOnClickListener(this);
        mIsCatOrDogEt.setOnClickListener(this);
        mBreedEt.setOnClickListener(this);
        mGenderEt.setOnClickListener(this);
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
                picker(mBreedEt, mBreedList);
                break;

            case R.id.tv_gender:
                picker(mGenderEt, mIsMaleOrFemaleList);
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
