package tekkan.synappz.com.tekkan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.activity.ConditionsActivity;
import tekkan.synappz.com.tekkan.activity.EditPetActivity;
import tekkan.synappz.com.tekkan.activity.ViewPetActivity;
import tekkan.synappz.com.tekkan.custom.ListFragment;
import tekkan.synappz.com.tekkan.custom.nestedfragments.CommonNodeInterface;
import tekkan.synappz.com.tekkan.custom.network.TekenJsonObjectRequest;
import tekkan.synappz.com.tekkan.dialogs.ProgressDialogFragment;
import tekkan.synappz.com.tekkan.utils.Constants;
import tekkan.synappz.com.tekkan.utils.VolleyHelper;


public class ProfileFragment extends ListFragment<Object, RecyclerView.ViewHolder> implements CommonNodeInterface {

    private static final String
            TAG = ProfileFragment.class.getSimpleName(),
            ARGS_PROFILE_TYPE = TAG + "ARGS_PROFILE_TYPE",
            ARGS_EMAIL = TAG + "ARGS_EMAIL",
            TAG_PROGRESS_DIALOG = TAG + ".TAG_PROGRESS_DIALOG";

    private static final int
            TYPE_PROFILE_FIELDS = 0,
            TYPE_PET = 1;

    private boolean mIsNewProfile = false;
    private String mEmail = null;
    ArrayList<Object> mListItems;

    public static ProfileFragment newInstance(boolean profileType, String email) {

        Bundle args = new Bundle();
        args.putBoolean(ARGS_PROFILE_TYPE, profileType);
        args.putString(ARGS_EMAIL, email);
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
        menuItemDone.setVisible(mIsNewProfile);
        MenuItem menuItemLogOut = menu.findItem(R.id.action_logout);
        menuItemLogOut.setVisible(!mIsNewProfile);
        MenuItem menuItemChangePwd = menu.findItem(R.id.action_change_pwd);
        menuItemChangePwd.setVisible(!mIsNewProfile);
        MenuItem menuItemCustomize = menu.findItem(R.id.action_customize);
        menuItemCustomize.setVisible(!mIsNewProfile);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                return true;
            case R.id.action_logout:
                PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .edit()
                        .putBoolean(Constants.SP.BOOLEAN_LOGED_IN, false)
                        .apply();

                getActivity().onBackPressed();
                return true;
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mIsNewProfile = args.getBoolean(ARGS_PROFILE_TYPE);
        mEmail = args.getString(ARGS_EMAIL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        v.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        setHasOptionsMenu(true);
        if(!mIsNewProfile) {
            fetchProfileData();
        }
        return v;
    }

    private void fetchProfileData() {

        TekenJsonObjectRequest request = new TekenJsonObjectRequest(
                Request.Method.GET,
                Constants.Api.getUrl(Constants.Api.FUNC_USER),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Success " + response.toString());
                        mListItems = new ArrayList<>();
                        mListItems.add(new ProfileItem(response));
                        if (!mIsNewProfile) {
                            for (int i = 0; i < 4; i++) {
                                mListItems.add(new Pet("Pet #" + (i + 1), (i + 1)));
                            }
                        }
                        loadNewItems(mListItems);

                        ProgressDialogFragment fragment = (ProgressDialogFragment) getParentFragment().getFragmentManager().findFragmentByTag(LoginFragment.TAG_PROGRESS_DIALOG);
                        if(fragment != null) {
                            fragment.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Failure ");
                    }
                }
        );

        request.addParam(Constants.Api.QUERY_PARAMETER1, mEmail);
        VolleyHelper.getInstance(getActivity()).addToRequestQueue(request);
    }

    @Override
    public List<Object> onCreateItems(Bundle savedInstanceState) {
        mListItems = new ArrayList<>();
        mListItems.add(new ProfileItem());
        return mListItems;
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
        } else if (holder instanceof ProfileFieldVH && item instanceof ProfileItem) {
            ((ProfileFieldVH) holder).bind((ProfileItem) item);
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

    class ProfileFieldVH extends RecyclerView.ViewHolder {
        @BindView(R.id.lt_conditions)
        LinearLayout mConditionsLT;
        @BindView(R.id.tv_add_pet)
        TextView mAddPetTV;
        @BindView(R.id.rb_male)
        RadioButton mMaleRB;
        @BindView(R.id.rb_female)
        RadioButton mFemaleRB;
        @BindView(R.id.et_first_name)
        EditText mFirstNameET;
        @BindView(R.id.et_last_name)
        EditText mLastNameET;
        @BindView(R.id.et_email)
        EditText mEmailET;
        @BindView(R.id.et_password)
        EditText mPasswordET;
        @BindView(R.id.et_match_password)
        EditText mMatchPasswordET;
        @BindView(R.id.et_street)
        EditText mStreetET;
        @BindView(R.id.et_postal_code)
        EditText mPostalCodeET;
        @BindView(R.id.et_place)
        EditText mPlaceET;
        @BindView(R.id.et_tel_no)
        EditText mPhoneNoET;

        ProfileFieldVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mAddPetTV.setEnabled(!mIsNewProfile);
        }

        void bind(ProfileItem item) {
            mMaleRB.setChecked(item.getGender() == ProfileItem.MR);
            mFemaleRB.setChecked(item.getGender() == ProfileItem.MRS);
            mFirstNameET.setText(item.getFirstName());
            mLastNameET.setText(item.getLastName());
            mEmailET.setText(item.getEmail());
            mStreetET.setText(item.getAddress());
            mPostalCodeET.setText(item.getPinCode());
            mPlaceET.setText(item.getPlace());
            mPhoneNoET.setText(item.getPhoneNo());
            mPasswordET.setText("123456");
            mMatchPasswordET.setText("123456");
        }

        @OnClick(R.id.lt_conditions)
        public void showConditions() {
            Intent intent = new Intent(getActivity(), ConditionsActivity.class);
            startActivity(intent);
        }

        @OnClick(R.id.tv_add_pet)
        public void showPetProfile() {
            Intent intent = new Intent(getActivity(), EditPetActivity.class);
            startActivity(intent);
        }
    }

    class ProfileItem {
        private String mFirstName, mLastName;
        private String mEmail;
        private String mAddress, mPinCode, mPlace;
        private String mPhoneNo;
        private int mGender;
        public static final int
                MR = 0,
                MRS = 1;

        private static final String
                JSON_S_GENDER = "gender",
                JSON_S_FIRST_NAME = "firstname",
                JSON_S_LAST_NAME = "lastname",
                JSON_S_STREET = "street",
                JSON_S_POSTAL_CODE = "postalcode",
                JSON_S_POSTAL_ADDRESS = "postaladdress",
                JSON_S_EMAIL = "email",
                JSON_N_MOBILE = "mobile";

        public ProfileItem() {
        }

        public ProfileItem(JSONObject jSonObject) {
            mGender = jSonObject.optString(JSON_S_GENDER).equalsIgnoreCase("M") ? MR : MRS;
            mFirstName = jSonObject.optString(JSON_S_FIRST_NAME);
            mLastName = jSonObject.optString(JSON_S_LAST_NAME);
            mLastName = jSonObject.optString(JSON_S_STREET);
            mAddress = jSonObject.optString(JSON_S_STREET);
            mPinCode = jSonObject.optString(JSON_S_POSTAL_CODE);
            mPlace = jSonObject.optString(JSON_S_POSTAL_ADDRESS);
            mEmail = jSonObject.optString(JSON_S_EMAIL);
            mPhoneNo = String.valueOf(jSonObject.optInt(JSON_N_MOBILE));
        }

        public int getGender() {
            return mGender;
        }

        public String getFirstName() {
            return mFirstName;
        }

        public String getLastName() {
            return mLastName;
        }

        public String getEmail() {
            return mEmail;
        }

        public String getAddress() {
            return mAddress;
        }

        public String getPinCode() {
            return mPinCode;
        }

        public String getPlace() {
            return mPlace;
        }

        public String getPhoneNo() {
            return mPhoneNo;
        }
    }

    class PetVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_pet_name)
        TextView mPetNameTV;
        @BindView(R.id.tv_pet_count)
        TextView mPetCountTV;

        PetVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(Pet pet) {
            mPetNameTV.setText(pet.getName());
            mPetCountTV.setText(String.valueOf(pet.getCount()));
        }

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(getContext(), ViewPetActivity.class);
            startActivity(intent);

        }
    }

    class Pet {
        private String mName;
        private int mCount;

        public Pet(String name, int count) {
            mName = name;
            mCount = count;
        }

        public String getName() {
            return mName;
        }

        public int getCount() {
            return mCount;
        }
    }
}
