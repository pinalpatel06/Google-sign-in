package tekkan.synappz.com.tekkan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

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
import tekkan.synappz.com.tekkan.model.Pet;
import tekkan.synappz.com.tekkan.model.User;
import tekkan.synappz.com.tekkan.utils.Constants;


public class ProfileFragment extends ListFragment<Object, RecyclerView.ViewHolder> implements CommonNodeInterface {

    private static final String
            TAG = ProfileFragment.class.getSimpleName(),
            ARGS_PROFILE_TYPE = TAG + "ARGS_PROFILE_TYPE",
            TAG_PROGRESS_DIALOG = TAG + ".TAG_PROGRESS_DIALOG";

    private static final int
            TYPE_PROFILE_FIELDS = 0,
            TYPE_PET = 1;

    private boolean mIsNewProfile = false;
    ArrayList<Object> mListItems;

    public static ProfileFragment newInstance(boolean profileType) {

        Bundle args = new Bundle();
        args.putBoolean(ARGS_PROFILE_TYPE, profileType);
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
                /*if(mProfileViewHolder.isValidFields()){
                    createOrUpdateUser();
                }*/

                return true;
            case R.id.action_logout:
                /*PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .edit()
                        .putBoolean(Constants.SP.BOOLEAN_LOGED_IN, false)
                        .apply();*/

                getActivity().onBackPressed();
                return true;
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;

            case R.id.action_customize:
                //mProfileViewHolder.setEditableFields(true);
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

    public void createOrUpdateUser() {

        /*String url = Constants.Api.getUrl(Constants.Api.FUNC_CREATE_USER);

        String  PARM_GENDER = "gender",
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

        if(!mIsNewProfile){
            url = Constants.Api.getUrl(Constants.Api.FUNC_EDIT_USER);
        }

        TekenStringRequest request = new TekenStringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG , "Success");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG , "Failure");
            }
        }
        );
        request.addParam(PARM_GENDER,mProfileItem.getGender());
        request.addParam(PARM_FIRST_NAME,mProfileItem.getFirstName());
        request.addParam(PARM_LAST_NAME,mProfileItem.getLastName());
        request.addParam(PARM_STREET_NAME,mProfileItem.getAddress());
        request.addParam(PARM_POSTAL_CODE,mProfileItem.getPinCode());
        request.addParam(PARM_PLACE_NAME,mProfileItem.getPlace());
        request.addParam(PARM_MOBILE_NO,mProfileItem.getPhoneNo());
        request.addParam(PARM_EMAIL,mProfileItem.getEmail());
        request.addParam(PARM_PASSWORD, LoginUtils.encode(mProfileItem.getPhoneNo()));

        // For Edit email of the Profile

      *//*  if(!mIsNewProfile){
            request.addParam(PARM_OLD_EMAIL,"");
            request.addParam(PARM_NEW_EMAIL,"");
        }*//*

        VolleyHelper.getInstance(getActivity()).addToRequestQueue(request);*/
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

    class ProfileFieldVH extends RecyclerView.ViewHolder {
        @BindView(R.id.lt_conditions)
        LinearLayout mConditionsLT;
        @BindView(R.id.tv_add_pet)
        TextView mAddPetTV;

        @BindView(R.id.radio_group_gender)
        RadioGroup mGenderRadioGroup;

        @BindView(R.id.et_first_name)
        EditText mFirstNameEt;

        @BindView(R.id.et_last_name)
        EditText mLastNameEt;

        @BindView(R.id.et_email)
        EditText mEmailEt;

        @BindView(R.id.et_password)
        EditText mPasswordEt;

        @BindView(R.id.et_confirm_password)
        EditText mConfirmPasswordEt;

        @BindView(R.id.et_street_name)
        EditText mStreetNameEt;

        @BindView(R.id.et_postal_code)
        EditText mPostalCodeEt;

        @BindView(R.id.et_place)
        EditText mPlaceNameEt;

        @BindView(R.id.et_tel_no)
        EditText mTelNoEt;

        @BindView(R.id.cb_term_condetions)
        CheckBox mTermsCondetionCb;

        ProfileFieldVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mAddPetTV.setEnabled(!mIsNewProfile);
        }

        void bind(User item) {

            if(item.getGender() == Constants.Gender.MALE) {
                mGenderRadioGroup.check(R.id.rb_male);
            }else{
                mGenderRadioGroup.check(R.id.rb_female);
            }
            mFirstNameEt.setText(item.getFirstName());
            mLastNameEt.setText(item.getLastName());
            mEmailEt.setText(item.getEmail());
            mStreetNameEt.setText(item.getStreet());
            mPostalCodeEt.setText(item.getPostalCode());
            mPlaceNameEt.setText(item.getPostalAddress());
            mTelNoEt.setText(String.valueOf(item.getMobile()));
            mPasswordEt.setText("123456");
            mConfirmPasswordEt.setText("123456");
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
            mPetCountTV.setText(String.valueOf("1"));
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), ViewPetActivity.class);
            startActivity(intent);

        }
    }
}
