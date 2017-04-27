package tekkan.synappz.com.tekkan.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.custom.ListFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestKitStep2Fragment extends ListFragment<Object, RecyclerView.ViewHolder> {

    private static final int
            TYPE_PROFILE_FIELDS = 0,
            TYPE_PET = 1;

    @Override
    public List<Object> onCreateItems(Bundle savedInstanceState) {
        ArrayList<Object> listItems = new ArrayList<>();

        //first item null to accommodate profile
        listItems.add(null);

        for(int i=0;i<4;i++){
            listItems.add(new TestKitStep2Fragment.Pet("Pet #"+(i+1),(i+1)));
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

    class ProfileFieldVH extends RecyclerView.ViewHolder {

        ProfileFieldVH(View itemView) {
            super(itemView);
        }
    }

    class PetVH extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_pet_name)
        TextView mPetNameTV;


        PetVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void bind(TestKitStep2Fragment.Pet pet) {
            mPetNameTV.setText(pet.getName());

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