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

import com.bayer.ah.bayertekenscanner.R;
import com.bayer.ah.bayertekenscanner.custom.CircleNetworkImageView;
import com.bayer.ah.bayertekenscanner.custom.nestedfragments.CommonNodeInterface;
import com.bayer.ah.bayertekenscanner.model.Pet;
import com.bayer.ah.bayertekenscanner.utils.Constants;
import com.bayer.ah.bayertekenscanner.utils.VolleyHelper;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Tejas Sherdiwala on 4/25/2017.
 * &copy; Knoxpo
 */

public class ResearchOutcomeFragment extends Fragment implements CommonNodeInterface {
    private static final String
            TAG = ResearchOutcomeFragment.class.getSimpleName(),
            ARGS_PET_INFO = TAG + ".ARGS_PET_INFO";

    @BindView(R.id.tv_pet_name)
    TextView mPetNameTV;

    @BindView(R.id.iv_pet_pic)
    CircleNetworkImageView mPetPicIV;

    @BindView(R.id.tv_comment)
    TextView mCommentTV;

    private Pet mPet;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static ResearchOutcomeFragment newInstance(Pet pet) {

        Bundle args = new Bundle();
        args.putParcelable(ARGS_PET_INFO, pet);
        ResearchOutcomeFragment fragment = new ResearchOutcomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragement_research_outcome, container, false);
        ButterKnife.bind(this, v);
        v.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        setHasOptionsMenu(true);
        updateUI();
        return v;
    }

    private void updateUI() {
        mPet = getArguments().getParcelable(ARGS_PET_INFO);
        if (mPet != null) {

            if (mPet.getType().equals(Constants.PetType.DOG)) {
                mPetPicIV.setDefaultImageResId(R.drawable.ic_dog_placeholder);
                mPetPicIV.setErrorImageResId(R.drawable.ic_dog_placeholder);
            } else {
                mPetPicIV.setDefaultImageResId(R.drawable.ic_cat_placeholder);
                mPetPicIV.setErrorImageResId(R.drawable.ic_cat_placeholder);
            }

            if (!mPet.getPhoto().equals("null") && mPet.getPhoto() != null) {
                mPetPicIV.setImageUrl(mPet.getPhoto(), VolleyHelper.getInstance(getActivity()).getImageLoader());
            }

            mPetNameTV.setText(mPet.getName());
            if (!mPet.getComment().isEmpty() && !mPet.getComment().equals("null")) {
                mCommentTV.setText(mPet.getComment());
            }
        }
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_research_outcome);
    }

    @Override
    public boolean shouldDisplayHomeAsUpEnabled() {
        return true;
    }
}
