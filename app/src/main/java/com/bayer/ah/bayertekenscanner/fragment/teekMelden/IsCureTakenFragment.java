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
import com.bayer.ah.bayertekenscanner.activity.DrugSelectionActivity;
import com.bayer.ah.bayertekenscanner.activity.HitStatusActivity;
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

public class IsCureTakenFragment extends ListFragment<String,IsCureTakenFragment.StringVH> {
    private static final String
            TAG = IsCureTakenFragment.class.getSimpleName(),
            ARGS_PET_DATA = TAG + ".ARGS_PET_DATA",
            ARGS_BUNDLE = TAG + ".ARGS_BUNDLE";

    private ArrayList<String> mList;
    private Pet mPet;

    @BindView(R.id.tv_pet_title)
    TextView mTitleTV;

    public static IsCureTakenFragment newInstance(Bundle bundle) {

        Bundle args = new Bundle();
        args.putBundle(ARGS_BUNDLE,bundle);
        IsCureTakenFragment fragment = new IsCureTakenFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public List<String> onCreateItems(Bundle savedInstanceState) {
        mList = new ArrayList<>();
        mList.add(getString(R.string.ja));
        mList.add(getString(R.string.nee));

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

    @Override
    protected boolean hasDecoration() {
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this,v);
        updateUI();
        return v;
    }

    private void updateUI(){
        Bundle bundle = getArguments().getBundle(ARGS_BUNDLE);

        if(bundle != null) {
            mPet = bundle.getParcelable(TestKitResearchList.ARGS_PET_DATA);
        }
        String petType;
        if (mPet != null && mPet.getType().equals(Constants.PetType.DOG)) {
            petType = getString(R.string.dog).toLowerCase();
        } else {
            petType = getString(R.string.cat).toLowerCase();
        }

        mTitleTV.setText(getString(R.string.is_cure_taken_text,petType));
    }

    public class StringVH extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_title)
        TextView mTitle;


        public StringVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void onBind(String title){
            mTitle.setText(title);
        }

        @OnClick(R.id.tv_title)
        public void onClicked(){
            Intent intent;
            Bundle bundle = getArguments().getBundle(ARGS_BUNDLE);
            switch (getAdapterPosition()){
                case 0:

                    intent = new Intent(getActivity(), DrugSelectionActivity.class);
                    intent.putExtra(DrugSelectionActivity.EXTRA_PET_BUNDLE, bundle);
                    startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(getActivity(), HitStatusActivity.class);
                    intent.putExtra(HitStatusActivity.EXTRA_PET_BUNDLE, bundle);
                    startActivity(intent);

            }
        }
    }
}
