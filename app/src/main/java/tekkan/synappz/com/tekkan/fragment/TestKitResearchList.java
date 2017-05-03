package tekkan.synappz.com.tekkan.fragment;


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
import tekkan.synappz.com.tekkan.activity.ApplyForPetActivity;
import tekkan.synappz.com.tekkan.activity.EditPetActivity;
import tekkan.synappz.com.tekkan.custom.ListFragment;

/**
 * A simple {@link Fragment} subclass.
 */

//Allow user with test click to choose the animal for research

public class TestKitResearchList extends ListFragment<Object, RecyclerView.ViewHolder> {


    private static final int
            TYPE_HEADER = 0,
            TYPE_PET = 1;

    @Override
    public List<Object> onCreateItems(Bundle savedInstanceState) {
        ArrayList<Object> listItems = new ArrayList<>();

        //first item null to accommodate header
        listItems.add(null);

        for (int i = 0; i < 4; i++) {
            listItems.add(new TestKitResearchList.Pet("Pet #" + (i + 1)));
        }
        return listItems;
    }

    @Override
    protected int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_PET;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return R.layout.item_research_header_with_test_kit;
            case TYPE_PET:
                return R.layout.item_pet;
            default:
                throw new UnsupportedOperationException("No such view type:" + viewType);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(View v, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new TestKitResearchList.ProfileFieldVH(v);
            case TYPE_PET:
                return new TestKitResearchList.PetVH(v);
            default:
                throw new UnsupportedOperationException("No such view type:" + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, Object item) {
        if (holder instanceof TestKitResearchList.PetVH && item instanceof TestKitResearchList.Pet) {
            ((TestKitResearchList.PetVH) holder).bind((TestKitResearchList.Pet) item);
        }
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
        void showPetProfile() {
            Intent intent = new Intent(getActivity(), EditPetActivity.class);
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

        void bind(TestKitResearchList.Pet pet) {
            mPetNameTV.setText(pet.getName());
        }

        @OnClick(R.id.rt_item_pet)
        public void showApplyPetActivity() {
            Intent intent = new Intent(getActivity(), ApplyForPetActivity.class);
            startActivity(intent);
        }

    }

    class Pet {
        private String mName;

        public Pet(String name) {
            mName = name;
        }

        public String getName() {
            return mName;
        }
    }

}
