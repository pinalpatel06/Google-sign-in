package tekkan.synappz.com.tekkan.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
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
import tekkan.synappz.com.tekkan.custom.network.TekenErrorListener;
import tekkan.synappz.com.tekkan.custom.network.TekenJsonArrayRequest;
import tekkan.synappz.com.tekkan.custom.network.TekenResponseListener;
import tekkan.synappz.com.tekkan.databinding.ItemProfileFieldsBinding;
import tekkan.synappz.com.tekkan.model.Pet;
import tekkan.synappz.com.tekkan.model.User;
import tekkan.synappz.com.tekkan.utils.Constants;
import tekkan.synappz.com.tekkan.utils.VolleyHelper;



public class ProfileFragment extends ListFragment<Object, RecyclerView.ViewHolder>
        implements CommonNodeInterface,
        TekenResponseListener, TekenErrorListener {

    private static final String
            TAG = ProfileFragment.class.getSimpleName(),
            ARGS_CAN_EDIT = TAG + "ARGS_CAN_EDIT";

    private static final int
            TYPE_PROFILE_FIELDS = 0,
            TYPE_PET = 1,
            REQUEST_PET = 2;

    ArrayList<Object> mListItems;

    private boolean mCanEdit = false;

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
                }
                mCanEdit = false;
                notifyDatasetChanged();
                getActivity().invalidateOptionsMenu();
                Constants.closeKeyBoard(getActivity());
                return true;
            case R.id.action_logout:
                return true;
            case android.R.id.home:
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
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchUserPetData();
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

    private boolean isUserValid(){
        return true;
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

            for(int i=0;i<mGenderRadioGroup.getChildCount();i++){
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

        @OnClick(R.id.lt_conditions)
        public void showConditions() {
            Intent intent = new Intent(getActivity(), ConditionsActivity.class);
            startActivity(intent);
        }

        @OnClick(R.id.tv_add_pet)
        public void showPetProfile() {
            Intent intent = new Intent(getActivity(), EditPetActivity.class);
            startActivityForResult(intent, REQUEST_PET);
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
            mPetCountTV.setText(String.valueOf("1"));
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), ViewPetActivity.class);
            intent.putExtra(ViewPetActivity.EXTRA_PET, mPet);
            startActivity(intent);

        }
    }
}
