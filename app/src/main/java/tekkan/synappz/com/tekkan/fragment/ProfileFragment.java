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
import android.widget.LinearLayout;
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


public class ProfileFragment extends ListFragment<Object, RecyclerView.ViewHolder> implements CommonNodeInterface {

    private static final String
            TAG = ProfileFragment.class.getSimpleName(),
            ARGS_PROFILE_TYPE = TAG + "ARGS_PROFILE_TYPE";

    private static final int
            TYPE_PROFILE_FIELDS = 0,
            TYPE_PET = 1;

    private boolean mIsNewProfile = false;

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
                return true;
            case R.id.action_logout:
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
        ArrayList<Object> listItems = new ArrayList<>();

        //first item null to accommodate profile
        if (!mIsNewProfile) {
            listItems.add(null);
            for (int i = 0; i < 4; i++) {
                listItems.add(new Pet("Pet #" + (i + 1), (i + 1)));
            }
        } else {
            listItems.add(new ProfileItem(1, "Jhon", "Smith", "jhon@anb.com", "303", "1234PR", "Greece", "000111000"));
        }
        return listItems;
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

        ProfileFieldVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mAddPetTV.setEnabled(!mIsNewProfile);
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
        private int mTitle;
        private String mFirstName, mLastName;
        private String mEmail;
        private String mAddress, mPinCode, mPlace;
        private String mPhoneNo;
        private static final int
                MR = 0,
                MRS = 1;


        public ProfileItem(int title, String firstName, String lastName, String email, String address, String pinCode, String place, String phoneNo) {
            mTitle = title;
            mFirstName = firstName;
            mLastName = lastName;
            mEmail = email;
            mAddress = address;
            mPinCode = pinCode;
            mPlace = place;
            mPhoneNo = phoneNo;
        }

        public int getTitle() {
            return mTitle;
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
