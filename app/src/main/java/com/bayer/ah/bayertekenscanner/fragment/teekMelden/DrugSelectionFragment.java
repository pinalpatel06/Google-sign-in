package com.bayer.ah.bayertekenscanner.fragment.teekMelden;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bayer.ah.bayertekenscanner.R;
import com.bayer.ah.bayertekenscanner.activity.DrugDateSelectionActivity;
import com.bayer.ah.bayertekenscanner.custom.ListFragment;
import com.bayer.ah.bayertekenscanner.fragment.TestKitResearchList;
import com.bayer.ah.bayertekenscanner.model.Pet;
import com.bayer.ah.bayertekenscanner.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Tejas Sherdiwala on 7/5/2017.
 * &copy; Knoxpo
 */

public class DrugSelectionFragment extends ListFragment<String, DrugSelectionFragment.StringVH> {
    private static final String
            TAG = DrugSelectionFragment.class.getSimpleName(),
            ARGS_PET_DATA = TAG + ".ARGS_PET_DATA",
            ARGS_BUNDLE = TAG + ".ARGS_BUNDLE";

    @BindView(R.id.tv_pet_title)
    TextView mTitleTV;

    private  ArrayList<String> mList;
    public static DrugSelectionFragment newInstance(Bundle bundle) {

        Bundle args = new Bundle();
        args.putBundle(ARGS_BUNDLE, bundle);
        DrugSelectionFragment fragment = new DrugSelectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public List<String> onCreateItems(Bundle savedInstanceState) {
        mList = new ArrayList<>();
        mList.add("Test Product1");
        mList.add("Test Product2");
        return mList;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_title;
    }

    @Override
    public StringVH onCreateViewHolder(View v, int viewType) {
        return new StringVH(v);
    }

    @Override
    public void onBindViewHolder(StringVH holder, String item) {
        holder.onBind(item);
    }

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_is_cure_taken;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, v);
        updateUI();
        return v;
    }

    private void updateUI() {
        Pet pet = null;
        Bundle bundle = getArguments().getBundle(ARGS_BUNDLE);
        if (bundle != null) {
            pet = bundle.getParcelable(TestKitResearchList.ARGS_PET_DATA);
        }

        String petType;
        if (pet != null && pet.getType().equals(Constants.PetType.DOG)) {
            petType = getString(R.string.dog).toLowerCase();
        } else {
            petType = getString(R.string.cat).toLowerCase();
        }

        mTitleTV.setText(getString(R.string.drug_text, petType));
    }

    public class StringVH extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView mTitleTV;

        public StringVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBind(String title) {
            mTitleTV.setText(title);
        }

        @OnClick(R.id.tv_title)
        public void onClick(){
            Intent intent = new Intent(getActivity(), DrugDateSelectionActivity.class);
            intent.putExtra(DrugDateSelectionActivity.EXTRA_PET_BUNDLE, getArguments().getBundle(ARGS_BUNDLE));
            startActivity(intent);
        }
    }
}
