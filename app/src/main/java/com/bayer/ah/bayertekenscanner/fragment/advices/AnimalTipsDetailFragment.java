package com.bayer.ah.bayertekenscanner.fragment.advices;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.bayer.ah.bayertekenscanner.R;
import com.bayer.ah.bayertekenscanner.custom.nestedfragments.CommonNodeInterface;
import com.bayer.ah.bayertekenscanner.model.TipsItem;
import com.bayer.ah.bayertekenscanner.utils.VolleyHelper;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * Created by Tejas Sherdiwala on 4/25/2017.
 * &copy; Knoxpo
 */

public class AnimalTipsDetailFragment extends Fragment implements CommonNodeInterface {
    private static final String
            TAG = AnimalTipsDetailFragment.class.getSimpleName(),
            ARGS_ANIMAL_TYPE = TAG + ".ARGS_ANIMAL_TYPE",
            ARGS_TIPS = TAG + ".ARGS_TIPS";

    @BindView(R.id.iv_tips_image)
    NetworkImageView mTipsIV;
    @BindView(R.id.tv_tips_details)
    TextView mDetailsTV;
    private TipsItem mTipsItem;

    public static AnimalTipsDetailFragment newInstance(TipsItem item) {
        Bundle args = new Bundle();
        args.putParcelable(ARGS_TIPS, item);
        AnimalTipsDetailFragment fragment = new AnimalTipsDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_animal_tips_details,container,false);
        v.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        init(v);
        updateUI();
        setHasOptionsMenu(true);
        return v;
    }

    private void init(View v){
        ButterKnife.bind(this,v);
        mTipsItem = getArguments().getParcelable(ARGS_TIPS);
    }
    private void updateUI(){
        if(mTipsItem != null) {
            mTipsIV.setImageUrl(mTipsItem.getBannerUrl(), VolleyHelper.getInstance(getActivity()).getImageLoader());
            mDetailsTV.setText(mTipsItem.getContent());
        }
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_tips);
    }

    @Override
    public boolean shouldDisplayHomeAsUpEnabled() {
        return true;
    }
}
