package com.bayer.ah.bayertekenscanner.fragment.teekMelden;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bayer.ah.bayertekenscanner.R;
import com.bayer.ah.bayertekenscanner.activity.ApplyForPetActivity;
import com.bayer.ah.bayertekenscanner.custom.ListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Tejas Sherdiwala on 7/7/2017.
 * &copy; Knoxpo
 */

public class CountrySelectionFragment extends ListFragment<String, CountrySelectionFragment.StringVH> {

    private static final String
            TAG = CountrySelectionFragment.class.getSimpleName(),
            ARGS_BUNDLE = TAG + ".ARGS_BUNDLE";

    private ArrayList<String> mList;

    public static CountrySelectionFragment newInstance(Bundle bundle) {

        Bundle args = new Bundle();
        args.putBundle(ARGS_BUNDLE, bundle);
        CountrySelectionFragment fragment = new CountrySelectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public List<String> onCreateItems(Bundle savedInstanceState) {
        mList = new ArrayList<>();
        mList.add("Nederland");
        mList.add("Belgie");
        mList.add("Frankrijk");
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

    public class StringVH extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView mTitle;

        public StringVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBind(String title) {
            mTitle.setText(title);
        }

        @OnClick(R.id.tv_title)
        public void onClicked() {
            Intent intent = new Intent(getActivity(), ApplyForPetActivity.class);
            Bundle bundle = getArguments().getBundle(ARGS_BUNDLE);
            intent.putExtra(ApplyForPetActivity.EXTRA_PET_BUNDLE, bundle);
            startActivity(intent);
        }
    }
}
