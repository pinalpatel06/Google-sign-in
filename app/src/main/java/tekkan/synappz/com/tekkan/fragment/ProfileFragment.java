package tekkan.synappz.com.tekkan.fragment;

import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.activity.ConditionsActivity;
import tekkan.synappz.com.tekkan.activity.EditPetActivity;
import tekkan.synappz.com.tekkan.activity.ViewPetActivity;
import tekkan.synappz.com.tekkan.custom.ListFragment;
import tekkan.synappz.com.tekkan.custom.nestedfragments.CommonNodeInterface;
import tekkan.synappz.com.tekkan.custom.network.TekenErrorListener;
import tekkan.synappz.com.tekkan.custom.network.TekenJsonArrayRequest;
import tekkan.synappz.com.tekkan.custom.network.TekenResponseListener;
import tekkan.synappz.com.tekkan.custom.network.TekenStringRequest;
import tekkan.synappz.com.tekkan.databinding.ItemProfileFieldsBinding;
import tekkan.synappz.com.tekkan.model.Pet;
import tekkan.synappz.com.tekkan.model.User;
import tekkan.synappz.com.tekkan.utils.Constants;
import tekkan.synappz.com.tekkan.utils.LoginUtils;
import tekkan.synappz.com.tekkan.utils.VolleyHelper;

import static java.lang.String.valueOf;


public class ProfileFragment extends ListFragment<Object, RecyclerView.ViewHolder>
        implements CommonNodeInterface,
        TekenResponseListener, TekenErrorListener {

    private static final String
            TAG = ProfileFragment.class.getSimpleName(),
            ARGS_CAN_EDIT = TAG + "ARGS_CAN_EDIT";

    private static final int
            TYPE_PROFILE_FIELDS = 0,
            TYPE_PET = 1,
            REQUEST_PET = 2,
            REQUEST_USER = 3;

    ArrayList<Object> mListItems;

    private boolean mCanEdit = false;
    private String mOldEmail = null;

    public static ProfileFragment newInstance(boolean profileType) {
        Bundle args = new Bundle();
        args.putBoolean(ARGS_CAN_EDIT, profileType);
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
        switch (item.getItemId()) {
            case R.id.action_done:
                if (isUserValid()) {
                    createOrUpdateUser();
                } else {
                    Toast.makeText(
                            getActivity(),
                            "Data missing",
                            Toast.LENGTH_SHORT
                    ).show();
                }
                mCanEdit = false;
                notifyDatasetChanged();
                getActivity().invalidateOptionsMenu();
                Constants.closeKeyBoard(getActivity());
                return true;
            case R.id.action_logout:
                logout();
                return true;
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            case R.id.action_customize:
                mCanEdit = true;
                notifyDatasetChanged();
                getActivity().invalidateOptionsMenu();
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
    }

    @Override
    public void onResume() {
        super.onResume();
        if (User.getInstance(getActivity()).isLoaded()) {
            fetchUserPetData();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        v.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        setHasOptionsMenu(true);
        return v;
    }


    @Override
    public List<Object> onCreateItems(Bundle savedInstanceState) {
        mListItems = new ArrayList<>();
        mListItems.add(User.getInstance(getActivity()));
        return mListItems;
    }

    private void logout() {
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .edit()
                .remove(Constants.SP.JSON_USER)
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

    private boolean isUserValid() {
        if (User.getInstance(getActivity()).getFirstName().equals("") &&
                User.getInstance(getActivity()).getFirstName().equals("") &&
                User.getInstance(getActivity()).getEmail().equals("") &&
                User.getInstance(getActivity()).getStreet().equals("") &&
                User.getInstance(getActivity()).getPostalCode().equals("") &&
                User.getInstance(getActivity()).getPostalAddress().equals("") &&
                User.getInstance(getActivity()).getMobile() == 0 &&
                User.getInstance(getActivity()).getPassword().equals("")) {
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
                            getActivity().finish();
                        }
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

        Log.d(TAG, (User.getInstance(getActivity()).getPassword()));
        request.addParam(PARM_PASSWORD, (LoginUtils.encode(User.getInstance(getActivity()).getPassword())));


        VolleyHelper.getInstance(getActivity()).addToRequestQueue(request);
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

        VolleyHelper.getInstance(getActivity()).addToRequestQueue(request);
    }

    @Override
    public void onResponse(int requestCode, Object response) {
        if (requestCode == REQUEST_PET) {
            mListItems = new ArrayList<>();
            mListItems.add(User.getInstance(getActivity()));
            JSONArray jsonArray = (JSONArray) response;
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    mListItems.add(new Pet(jsonObject));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            loadNewItems(mListItems);
        }
    }

    @Override
    public void onErrorResponse(int requestCode, VolleyError error, int status, String message) {
        Log.d(TAG, status + " " + message);
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

            ItemProfileFieldsBinding binding = DataBindingUtil.bind(itemView);
            binding.setUser(item);


            mPasswordET.setText("123456");
            mConfirmPasswordET.setText("123456");

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

        @OnCheckedChanged({R.id.rb_male, R.id.rb_female})
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
                }
            }
        }

        @OnCheckedChanged(R.id.cb_term_conditions)
        public void onTermAccepted(CompoundButton button, boolean isAccepted) {

        }

    }

    class PetVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_pet_name)
        TextView mPetNameTV;
        @BindView(R.id.tv_pet_count)
        TextView mPetCountTV;
        Pet mPet;

        PetVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(Pet pet) {
            mPet = pet;
            mPetNameTV.setText(pet.getName());
            mPetCountTV.setText(valueOf("1"));
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), ViewPetActivity.class);
            intent.putExtra(ViewPetActivity.EXTRA_PET, mPet);
            startActivity(intent);
        }
    }
}
