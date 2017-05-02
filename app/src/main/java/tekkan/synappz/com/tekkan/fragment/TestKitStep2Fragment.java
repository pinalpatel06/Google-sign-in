package tekkan.synappz.com.tekkan.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.activity.PetProfileActivity;
import tekkan.synappz.com.tekkan.custom.ListFragment;
import tekkan.synappz.com.tekkan.custom.nestedfragments.CommonNodeInterface;
import tekkan.synappz.com.tekkan.custom.nestedfragments.ContainerNodeInterface;
import tekkan.synappz.com.tekkan.custom.nestedfragments.FragmentChangeCallback;
import tekkan.synappz.com.tekkan.custom.nestedfragments.NestedFragmentUtil;
import tekkan.synappz.com.tekkan.fragment.teekMelden.FragmentApplyPet;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestKitStep2Fragment extends ListFragment<Object, RecyclerView.ViewHolder> implements ContainerNodeInterface {


    private static final int
            TYPE_PROFILE_FIELDS = 0,
            TYPE_PET = 1;

    private FragmentChangeCallback mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (FragmentChangeCallback) getActivity();
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    @Override
    public List<Object> onCreateItems(Bundle savedInstanceState) {
        ArrayList<Object> listItems = new ArrayList<>();

        //first item null to accommodate profile
        listItems.add(null);

        for (int i = 0; i < 4; i++) {
            listItems.add(new TestKitStep2Fragment.Pet("Pet #" + (i + 1), (i + 1)));
        }

        return listItems;
    }

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_test_kit_step2;
    }

    @Override
    protected int getItemViewType(int position) {
        return position == 0 ? TYPE_PROFILE_FIELDS : TYPE_PET;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        switch (viewType) {
            case TYPE_PROFILE_FIELDS:
                return R.layout.item_test_kit_fields_step2;
            case TYPE_PET:
                return R.layout.item_test_kit_add_pet;
            default:
                throw new UnsupportedOperationException("No such view type:" + viewType);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(View v, int viewType) {
        switch (viewType) {
            case TYPE_PROFILE_FIELDS:
                return new TestKitStep2Fragment.ProfileFieldVH(v);
            case TYPE_PET:
                return new TestKitStep2Fragment.PetVH(v);
            default:
                throw new UnsupportedOperationException("No such view type:" + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, Object item) {
        if (holder instanceof TestKitStep2Fragment.PetVH && item instanceof TestKitStep2Fragment.Pet) {
            ((TestKitStep2Fragment.PetVH) holder).bind((TestKitStep2Fragment.Pet) item);
        }
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public boolean onBackPressed() {
        return NestedFragmentUtil.onBackPressed(getContainerId(),getChildFragmentManager(),getChangeCallback());
    }

    @Override
    public void setChild(CommonNodeInterface fragment) {
        NestedFragmentUtil.setChild(fragment,getContainerId(),getChildFragmentManager(),getChangeCallback());
    }

    @Override
    public int getContainerId() {
        return R.id.fragment_container;
    }

    @Override
    public FragmentChangeCallback getChangeCallback() {
        return mCallback;
    }

    class ProfileFieldVH extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_investigate_title)
        TextView mTitleTV;
        @BindView(R.id.tv_my_pet)
        TextView mMyPetTV;

        ProfileFieldVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.tv_my_pet)
        public void showPetProfile() {
            Intent intent = new Intent(getActivity(), PetProfileActivity.class);
            startActivity(intent);
        }
    }

    class PetVH extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_pet_name)
        TextView mPetNameTV;
        @BindView(R.id.rt_item_pet)
        RelativeLayout mRelativeLayout;

        PetVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(TestKitStep2Fragment.Pet pet) {
            mPetNameTV.setText(pet.getName());
        }

        @OnClick(R.id.rt_item_pet)
        public void showApplyPetActivity(){
            setChild(new FragmentApplyPet());
        }

    }

    class Pet {
        private String mName;

        public Pet(String name, int count) {
            mName = name;
        }

        public String getName() {
            return mName;
        }
    }

}
