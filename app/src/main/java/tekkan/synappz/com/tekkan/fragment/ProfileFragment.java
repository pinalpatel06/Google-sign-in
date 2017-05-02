package tekkan.synappz.com.tekkan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.activity.ConditionsActivity;
import tekkan.synappz.com.tekkan.activity.PetProfileActivity;
import tekkan.synappz.com.tekkan.custom.ListFragment;


public class ProfileFragment extends ListFragment<Object, RecyclerView.ViewHolder> {


    private static final int
            TYPE_PROFILE_FIELDS = 0,
            TYPE_PET = 1;

    @Override
    public List<Object> onCreateItems(Bundle savedInstanceState) {
        ArrayList<Object> listItems = new ArrayList<>();

        //first item null to accommodate profile
        listItems.add(null);

        for (int i = 0; i < 4; i++) {
            listItems.add(new Pet("Pet #" + (i + 1), (i + 1)));
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
                return R.layout.list_item_add_new_pet;
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

    class ProfileFieldVH extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_conditions)
        TextView mConditionsTV;
        @BindView(R.id.tv_add_pet)
        TextView mAddPetTV;

        ProfileFieldVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.tv_conditions)
        public void showConditions() {
            Intent intent = new Intent(getActivity(), ConditionsActivity.class);
            startActivity(intent);
        }

        @OnClick(R.id.tv_add_pet)
        public void showPetProfile(){
            Intent intent = new Intent(getActivity(), PetProfileActivity.class);
            startActivity(intent);
        }
    }

    class PetVH extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_pet_name)
        TextView mPetNameTV;
        @BindView(R.id.tv_pet_count)
        TextView mPetCountTV;

        PetVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Pet pet) {
            mPetNameTV.setText(pet.getName());
            mPetCountTV.setText(String.valueOf(pet.getCount()));
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
