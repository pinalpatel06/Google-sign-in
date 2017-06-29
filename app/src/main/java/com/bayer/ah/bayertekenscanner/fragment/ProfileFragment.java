package com.bayer.ah.bayertekenscanner.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bayer.ah.bayertekenscanner.R;
import com.bayer.ah.bayertekenscanner.activity.ConditionsActivity;
import com.bayer.ah.bayertekenscanner.activity.EditPetActivity;
import com.bayer.ah.bayertekenscanner.activity.ProfileActivity;
import com.bayer.ah.bayertekenscanner.activity.ViewPetActivity;
import com.bayer.ah.bayertekenscanner.custom.CircleNetworkImageView;
import com.bayer.ah.bayertekenscanner.custom.ListFragment;
import com.bayer.ah.bayertekenscanner.custom.nestedfragments.CommonNodeInterface;
import com.bayer.ah.bayertekenscanner.custom.network.TekenErrorListener;
import com.bayer.ah.bayertekenscanner.custom.network.TekenJsonArrayRequest;
import com.bayer.ah.bayertekenscanner.custom.network.TekenResponseListener;
import com.bayer.ah.bayertekenscanner.custom.network.TekenStringRequest;
import com.bayer.ah.bayertekenscanner.dialogs.AlertDialogFragment;
import com.bayer.ah.bayertekenscanner.dialogs.ConfirmDialogFragment;
import com.bayer.ah.bayertekenscanner.model.Pet;
import com.bayer.ah.bayertekenscanner.model.User;
import com.bayer.ah.bayertekenscanner.utils.Constants;
import com.bayer.ah.bayertekenscanner.utils.LoginUtils;
import com.bayer.ah.bayertekenscanner.utils.VolleyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static java.lang.String.valueOf;


public class ProfileFragment extends ListFragment<Object, RecyclerView.ViewHolder>
        implements CommonNodeInterface,
        TekenResponseListener, TekenErrorListener, ConfirmDialogFragment.ConfirmDialogFragmentListener {

    private static final String
            TAG = ProfileFragment.class.getSimpleName(),
            TAG_DIALOG = TAG + ".TAG_DIALOG",
            ARGS_NEW_PROFILE = TAG + ".ARGS_NEW_PROFILE",
            TAG_ALERT = TAG + ".TAG_ALERT",
            ARGS_CAN_EDIT = TAG + "ARGS_CAN_EDIT";

    private static final int
            TYPE_PROFILE_FIELDS = 0,
            TYPE_PET = 1,
            REQUEST_PET = 2,
            REQUEST_USER = 3;

    ArrayList<Object> mListItems;

    private boolean mCanEdit = false;
    private boolean isTermsAccepted = false;
    private String mOldEmail = null;
    Set<String> mResearchList;

    public static ProfileFragment newInstance(boolean profileType, boolean canEdit) {
        Bundle args = new Bundle();
        args.putBoolean(ARGS_CAN_EDIT, canEdit);
        args.putBoolean(ARGS_NEW_PROFILE, profileType);
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItemDone = menu.findItem(R.id.action_done);
        menuItemDone.setVisible(mCanEdit);
        MenuItem menuItemLogOut = menu.findItem(R.id.action_logout);
        menuItemLogOut.setVisible(!mCanEdit);
        MenuItem menuItemChangePwd = menu.findItem(R.id.action_change_pwd);
        menuItemChangePwd.setVisible(!mCanEdit);
        MenuItem menuItemCustomize = menu.findItem(R.id.action_customize);
        menuItemCustomize.setVisible(!mCanEdit);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ConfirmDialogFragment fragment;
        switch (item.getItemId()) {
            case R.id.action_done:
                if (isUserValid()) {
                    createOrUpdateUser();
                    /*mCanEdit = false;
                    notifyDatasetChanged();
                    getActivity().invalidateOptionsMenu();
                    Constants.closeKeyBoard(getActivity());*/
                } else {
                    Toast.makeText(
                            getActivity(),
                            "Data missing",
                            Toast.LENGTH_SHORT
                    ).show();
                }

                return true;
            case R.id.action_logout:
                fragment = ConfirmDialogFragment.newInstance(R.string.logout, R.string.logout_message, R.string.ja, R.string.nee);
                fragment.show(getFragmentManager(), TAG_DIALOG);
                fragment.setListener(this);
                return true;
            case android.R.id.home:
                fragment = ConfirmDialogFragment.newInstance(R.string.confirmation_title, R.string.confirmation, R.string.stop, R.string.procced);
                fragment.show(getFragmentManager(), TAG_DIALOG);
                fragment.setListener(new ConfirmDialogFragment.ConfirmDialogFragmentListener() {
                    @Override
                    public void onPositiveClicked(DialogInterface dialog) {
                        getActivity().onBackPressed();
                    }

                    @Override
                    public void onNegativeClicked() {

                    }
                });

                return true;
            case R.id.action_customize:
                // mCanEdit = true;
                // notifyDatasetChanged();
                // getActivity().invalidateOptionsMenu();
                // return true;
            case R.id.action_change_pwd:
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra(ProfileActivity.EXTRA_NEW_PROFILE, false);
                intent.putExtra(ProfileActivity.EXTRA_CAN_EDIT, true);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mCanEdit = args.getBoolean(ARGS_CAN_EDIT);
        if (User.getInstance(getActivity()).isLoaded()) {
            mOldEmail = User.getInstance(getActivity()).getEmail();
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mResearchList = sp.getStringSet(Constants.SP.RESEARCH_LIST, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (User.getInstance(getActivity()).isLoaded() && !mCanEdit) {
            fetchUserPetData();
        }
    }

    @Override
    protected boolean canSwipe(int viewType) {
        switch (viewType) {
            case TYPE_PROFILE_FIELDS:
                return false;
            default:
                return super.canSwipe(viewType);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        v.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));

        setSwipe(
                android.R.color.white,
                android.R.color.holo_blue_light,
                R.drawable.ic_delete_white_24dp,
                android.R.color.holo_red_light,
                R.drawable.ic_delete_white_24dp
        );

        setHasOptionsMenu(true);
        return v;
    }


    @Override
    public void onDestroy() {
        VolleyHelper.getInstance(getActivity()).getRequestQueue().cancelAll(this);
        super.onDestroy();
    }

    @Override
    public List<Object> onCreateItems(Bundle savedInstanceState) {
        mListItems = new ArrayList<>();
        mListItems.add(User.getInstance(getActivity()));
        return mListItems;
    }

    private void logout() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.edit()
                .remove(Constants.SP.JSON_USER)
                .apply();

        sp.edit()
                .remove(Constants.SP.RESEARCH_LIST)
                .apply();

        Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment != null) {
            getFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .commitNow();
        }

        User.getInstance(getActivity()).unloadUser();

    }

    @Override
    protected boolean hasDecoration() {
        return true;
    }

    @Override
    protected int[] getViewTypesForDecoration() {
        return new int[]{TYPE_PET};
    }

    private boolean isUserValid() {
        if (User.getInstance(getActivity()).getFirstName() != null &&
                User.getInstance(getActivity()).getFirstName().equals("") &&
                User.getInstance(getActivity()).getLastName() != null &&
                User.getInstance(getActivity()).getLastName().equals("") &&
                User.getInstance(getActivity()).getEmail() != null &&
                User.getInstance(getActivity()).getEmail().equals("") &&
                User.getInstance(getActivity()).getStreet() != null &&
                User.getInstance(getActivity()).getStreet().equals("") &&
                User.getInstance(getActivity()).getPostalCode() != null &&
                User.getInstance(getActivity()).getPostalCode().equals("") &&
                User.getInstance(getActivity()).getPostalAddress() != null &&
                User.getInstance(getActivity()).getPostalAddress().equals("") &&
                User.getInstance(getActivity()).getMobile() == 0 &&
                User.getInstance(getActivity()).getPassword() != null &&
                User.getInstance(getActivity()).getPassword().equals("")) {
            return false;
        }
        if (User.getInstance(getActivity()).getGender() == null) {
            return false;
        }
        return true;
    }

    public void createOrUpdateUser() {

        String url = Constants.Api.getUrl(Constants.Api.FUNC_CREATE_USER);

        String PARM_GENDER = "gender",
                PARM_FIRST_NAME = "firstname",
                PARM_LAST_NAME = "lastname",
                PARM_STREET_NAME = "street",
                PARM_POSTAL_CODE = "postalcode",
                PARM_PLACE_NAME = "postaladdress",
                PARM_MOBILE_NO = "mobile",
                PARM_EMAIL = "email",
                PARM_PASSWORD = "password",
                PARM_OLD_EMAIL = "old_email",
                PARM_NEW_EMAIL = "new_email";

        if (User.getInstance(getActivity()).isLoaded()) {
            url = Constants.Api.getUrl(Constants.Api.FUNC_EDIT_USER);
        }

        if (isTermsAccepted || User.getInstance(getActivity()).isLoaded()) {
            TekenStringRequest request = new TekenStringRequest(
                    Request.Method.POST,
                    url,
                    new TekenResponseListener<String>() {
                        @Override
                        public void onResponse(int requestCode, String response) {
                            Log.d(TAG, "success");

                            if (User.getInstance(getActivity()).isLoaded()) {
                                PreferenceManager.getDefaultSharedPreferences(getActivity())
                                        .edit()
                                        .putString(Constants.SP.JSON_USER, User.getInstance(getActivity()).toJSON())
                                        .apply();

                            } else {
                                User.getInstance(getActivity()).unloadUser();
                            }
                            getActivity().finish();
                        }
                    },
                    new TekenErrorListener() {
                        @Override
                        public void onErrorResponse(int requestCode, VolleyError error, int status, String message) {
                            Log.d(TAG, status + " failure " + message);
                            if (!User.getInstance(getActivity()).isLoaded()) {
                                User.getInstance(getActivity()).unloadUser();
                            }
                        }
                    },
                    REQUEST_USER
            );
            request.addParam(PARM_GENDER, User.getInstance(getActivity()).getGender().toApi());
            request.addParam(PARM_FIRST_NAME, User.getInstance(getActivity()).getFirstName());
            request.addParam(PARM_LAST_NAME, User.getInstance(getActivity()).getLastName());
            request.addParam(PARM_STREET_NAME, User.getInstance(getActivity()).getStreet());
            request.addParam(PARM_POSTAL_CODE, User.getInstance(getActivity()).getPostalCode());
            request.addParam(PARM_PLACE_NAME, User.getInstance(getActivity()).getPostalAddress());
            request.addParam(PARM_MOBILE_NO, String.valueOf(User.getInstance(getActivity()).getMobile()));

            // For Edit email of the Profile

            if (User.getInstance(getActivity()).isLoaded() && !mOldEmail.equals(User.getInstance(getActivity()).getEmail())) {
                request.addParam(PARM_OLD_EMAIL, mOldEmail);
                request.addParam(PARM_NEW_EMAIL, User.getInstance(getActivity()).getEmail());
                request.addParam(PARM_EMAIL, mOldEmail);
            } else {
                request.addParam(PARM_EMAIL, User.getInstance(getActivity()).getEmail());
            }

            request.addParam(PARM_PASSWORD, (LoginUtils.encode(User.getInstance(getActivity()).getPassword())));


            VolleyHelper.getInstance(getActivity()).addToRequestQueue(request);
        } else {
            AlertDialogFragment fragment = AlertDialogFragment.newInstance(confirmation_title, R.string.terms_not_accepted);
            fragment.show(getFragmentManager(), TAG_ALERT);
        }
    }

    @Override
    protected int getItemViewType(int position) {
        return position == 0 ? TYPE_PROFILE_FIELDS : TYPE_PET;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        switch (viewType) {
            case TYPE_PROFILE_FIELDS:
                return R.layout.item_profile_fields;
            case TYPE_PET:
                return R.layout.item_pet_with_count;
            default:
                throw new UnsupportedOperationException("No such view type:" + viewType);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(View v, int viewType) {
        switch (viewType) {
            case TYPE_PROFILE_FIELDS:
                return new ProfileFieldVH(v);
            case TYPE_PET:
                return new PetVH(v);
            default:
                throw new UnsupportedOperationException("No such view type:" + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, Object item) {
        if (holder instanceof PetVH && item instanceof Pet) {
            ((PetVH) holder).bind((Pet) item);
        } else if (holder instanceof ProfileFieldVH && item instanceof User) {
            ((ProfileFieldVH) holder).bind((User) item);
        }
    }

    @Override
    public String getTitle() {
        return getString(R.string.profile_title);
    }

    @Override
    public boolean shouldDisplayHomeAsUpEnabled() {
        return false;
    }

    private void fetchUserPetData() {
        String email = User.getInstance(getActivity()).getEmail();
        TekenJsonArrayRequest request = new TekenJsonArrayRequest(
                Request.Method.GET,
                Constants.Api.getUrl(Constants.Api.FUNC_GET_ANIMALS_BY_USER),
                this,
                this,
                REQUEST_PET
        );
        request.addParam(Constants.Api.QUERY_PARAMETER1, email);
        request.setTag(this);
        VolleyHelper.getInstance(getActivity()).addToRequestQueue(request);
    }

    @Override
    public void onResponse(int requestCode, Object response) {
        if (requestCode == REQUEST_PET) {
            mListItems = new ArrayList<>();
            HashSet<String> researchList = new HashSet<>();
            mListItems.add(User.getInstance(getActivity()));
            JSONArray jsonArray = (JSONArray) response;
            Pet pet;
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    pet = new Pet(jsonObject);
                    mListItems.add(pet);
                    if (pet.isResearch()) {
                        researchList.add(String.valueOf(pet.getId()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

            if (sp.getStringSet(Constants.SP.RESEARCH_LIST, null) == null) {
                PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .edit()
                        .putStringSet(Constants.SP.RESEARCH_LIST, researchList)
                        .apply();

                mResearchList = researchList;
            }

            loadNewItems(mListItems);
        }
    }

    @Override
    public void onErrorResponse(int requestCode, VolleyError error, int status, String message) {
        Log.d(TAG, status + " " + message);
    }

    @Override
    public void onPositiveClicked(DialogInterface dialog) {
        logout();
    }

    @Override
    public void onNegativeClicked() {

    }

    @Override
    protected void onSwipeCompleted(final int position, int direction, Object item) {
        if (position == 0) {
            return;
        }

        final String JSON_ANIMAL_ID = "animals_id";
        TekenStringRequest request = new TekenStringRequest(
                Request.Method.POST,
                Constants.Api.getUrl(Constants.Api.FUNC_DELETE_ANIMAL),
                new TekenResponseListener<String>() {
                    @Override
                    public void onResponse(int requestCode, String response) {
                        removeItem(position);
                    }
                },
                new TekenErrorListener() {
                    @Override
                    public void onErrorResponse(int requestCode, VolleyError error, int status, String message) {

                    }
                },
                0
        );

        request.addParam(JSON_ANIMAL_ID, String.valueOf(((Pet) item).getId()));
        VolleyHelper.getInstance(getActivity()).addToRequestQueue(request);
    }

    class ProfileFieldVH extends RecyclerView.ViewHolder {
        @BindView(R.id.lt_conditions)
        LinearLayout mConditionsLT;

        @BindView(R.id.tv_add_pet)
        TextView mAddPetTV;

        @BindView(R.id.radio_group_gender)
        RadioGroup mGenderRadioGroup;

        @BindView(R.id.et_first_name)
        EditText mFirstNameET;

        @BindView(R.id.et_last_name)
        EditText mLastNameET;

        @BindView(R.id.et_email)
        EditText mEmailET;

        @BindView(R.id.et_password)
        EditText mPasswordET;

        @BindView(R.id.et_confirm_password)
        EditText mConfirmPasswordET;

        @BindView(R.id.et_street_name)
        EditText mStreetNameET;

        @BindView(R.id.et_postal_code)
        EditText mPostalCodeEt;

        @BindView(R.id.et_place)
        EditText mPlaceNameET;

        @BindView(R.id.et_tel_no)
        EditText mTelNoET;

        @BindView(R.id.cb_term_conditions)
        CheckBox mTermsConditionCb;


        ProfileFieldVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(User item) {

            com.bayer.ah.bayertekenscanner.databinding.ItemProfileFieldsBinding binding = DataBindingUtil.bind(itemView);
            binding.setUser(item);

            if (!mCanEdit) {
                mPasswordET.setText("123456");
                mConfirmPasswordET.setVisibility(View.GONE);
            }

            for (int i = 0; i < mGenderRadioGroup.getChildCount(); i++) {
                View v = mGenderRadioGroup.getChildAt(i);
                v.setEnabled(mCanEdit);
            }

            mFirstNameET.setEnabled(mCanEdit);
            mLastNameET.setEnabled(mCanEdit);
            mEmailET.setEnabled(mCanEdit);
            mStreetNameET.setEnabled(mCanEdit);
            mPostalCodeEt.setEnabled(mCanEdit);
            mPlaceNameET.setEnabled(mCanEdit);
            mTelNoET.setEnabled(mCanEdit);
            mPasswordET.setEnabled(mCanEdit);
            mConfirmPasswordET.setEnabled(mCanEdit);
            mAddPetTV.setEnabled(!mCanEdit);

            mConditionsLT.setVisibility(
                    User.getInstance(getActivity()).isLoaded() ?
                            View.GONE :
                            View.VISIBLE
            );

        }

        @OnClick({R.id.lt_conditions, R.id.tv_add_pet})
        public void showConditions(View v) {
            switch (v.getId()) {
                case R.id.lt_conditions:
                    Intent intent = new Intent(getActivity(), ConditionsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.tv_add_pet:
                    Intent editPetIntent = new Intent(getActivity(), EditPetActivity.class);
                    startActivityForResult(editPetIntent, REQUEST_PET);
            }
        }

        @OnTextChanged({R.id.et_first_name,
                R.id.et_last_name,
                R.id.et_email,
                R.id.et_password,
                R.id.et_tel_no,
                R.id.et_confirm_password,
                R.id.et_postal_code,
                R.id.et_place,
                R.id.et_street_name})
        public void onTextChange(Editable s) {
            if (s == mFirstNameET.getText() && s.length() > 0) {

                User.getInstance(getActivity()).setFirstName(mFirstNameET.getText().toString());

            } else if (s == mLastNameET.getText() && s.length() > 0) {

                User.getInstance(getActivity()).setLastName(mLastNameET.getText().toString());

            } else if (s == mEmailET.getText() && s.length() > 0) {

                if (TextUtils.isEmpty(mEmailET.getText().toString().trim()) ||
                        !Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {

                    mEmailET.setError(getString(R.string.err_email));

                } else {
                    mEmailET.setError(null, null);
                    User.getInstance(getActivity()).setEmail(mEmailET.getText().toString());
                }
            } else if (s == mStreetNameET.getText() && s.length() > 0) {

                User.getInstance(getActivity()).setStreet(mStreetNameET.getText().toString());

            } else if (s == mPostalCodeEt.getText() && s.length() > 0) {

                User.getInstance(getActivity()).setPostalCode((mPostalCodeEt.getText().toString()));

            } else if (s == mPlaceNameET.getText() && s.length() > 0) {

                User.getInstance(getActivity()).setPostalAddress(mPlaceNameET.getText().toString());

            } else if (s == mTelNoET.getText() && s.length() > 0) {

                User.getInstance(getActivity()).setMobile(Long.valueOf(mTelNoET.getText().toString()));

            } else if (s == mConfirmPasswordET.getText() && s.length() > 0) {

                if (!(mConfirmPasswordET.getText().toString().equals(mPasswordET.getText().toString()))) {
                    mConfirmPasswordET.setError(getString(R.string.err_password));

                } else {
                    mConfirmPasswordET.setError(null, null);
                    User.getInstance(getActivity()).setPassword(mPasswordET.getText().toString());
                }
            }
        }

        @OnCheckedChanged({R.id.rb_male, R.id.rb_female, R.id.cb_term_conditions})
        public void onGenderChange(CompoundButton button, boolean isChecked) {
            if (isChecked) {
                switch (button.getId()) {
                    case R.id.rb_male:
                        User.getInstance(getActivity()).setGender(Constants.Gender.MALE);
                        Log.d(TAG, User.getInstance(getActivity()).getGender().toApi());
                        break;
                    case R.id.rb_female:
                        User.getInstance(getActivity()).setGender(Constants.Gender.FEMALE);
                        Log.d(TAG, User.getInstance(getActivity()).getGender().toApi());
                        break;
                    case R.id.cb_term_conditions:
                        isTermsAccepted = true;
                }
            }
        }

    }

    class PetVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_pet_name)
        TextView mPetNameTV;
        @BindView(R.id.tv_pet_count)
        TextView mPetCountTV;
        @BindView(R.id.iv_pet_image)
        CircleNetworkImageView mPetImageIV;
        Pet mPet;


        PetVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(Pet pet) {
            mPet = pet;
            mPetNameTV.setText(pet.getName());


            if (pet.isResearch() && mResearchList != null && mResearchList.contains(String.valueOf(pet.getId()))) {
                mPetCountTV.setText(valueOf("1"));
            } else {
                mPetCountTV.setVisibility(View.GONE);
            }

            if (pet.getType() == Constants.PetType.DOG) {
                mPetImageIV.setDefaultImageResId(R.drawable.ic_dog_placeholder);
                mPetImageIV.setErrorImageResId(R.drawable.ic_dog_placeholder);
            } else {
                mPetImageIV.setErrorImageResId(R.drawable.ic_cat_placeholder);
                mPetImageIV.setDefaultImageResId(R.drawable.ic_cat_placeholder);
            }

            if (mPet.getPhoto() != null && !mPet.getPhoto().equals("null")) {
                mPetImageIV.setImageUrl(pet.getPhoto(), VolleyHelper.getInstance(getActivity()).getImageLoader());
            }
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), ViewPetActivity.class);
            intent.putExtra(ViewPetActivity.EXTRA_PET, mPet);
            startActivity(intent);
        }
    }
}
