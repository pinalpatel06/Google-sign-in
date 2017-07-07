package com.bayer.ah.bayertekenscanner.fragment.teekMelden;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bayer.ah.bayertekenscanner.R;
import com.bayer.ah.bayertekenscanner.activity.IsCureTakenActivity;
import com.bayer.ah.bayertekenscanner.dialogs.AlertDialogFragment;
import com.bayer.ah.bayertekenscanner.fragment.TestKitResearchList;
import com.bayer.ah.bayertekenscanner.model.Pet;
import com.bayer.ah.bayertekenscanner.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Tejas Sherdiwala on 7/7/2017.
 * &copy; Knoxpo
 */

public class NoOfSignReportedFragment extends Fragment{
    private static final String
            TAG = NoOfSignReportedFragment.class.getSimpleName(),
            ARGS_PET_DATA = TAG + ".ARGS_PET_DATA",
            TAG_ALERT = TAG + ".TAG_ALERT",
            ARGS_BUNDLE = TAG + ".ARGS_BUNDLE";

    @BindView(R.id.tv_sign_title)
    TextView mTitleTV;
    @BindView(R.id.et_no_sign)
    TextView mNoSignTV;

    public static NoOfSignReportedFragment newInstance(Bundle bundle) {

        Bundle args = new Bundle();
        args.putBundle(ARGS_BUNDLE,bundle);
        NoOfSignReportedFragment fragment = new NoOfSignReportedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_no_of_sign_reported,container,false);
        ButterKnife.bind(this,v);
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

        mTitleTV.setText(getString(R.string.sign_reported_title, petType));
    }


    @OnClick(R.id.tv_confirm)
    public void onClicked(){
        if(!TextUtils.isEmpty(mNoSignTV.getText())) {
            Intent intent = new Intent(getActivity(), IsCureTakenActivity.class);
            intent.putExtra(IsCureTakenActivity.EXTRA_PET_BUNDLE, getArguments().getBundle(ARGS_BUNDLE));
            startActivity(intent);
        }else{
            AlertDialogFragment fragment = AlertDialogFragment.newInstance(R.id.no_of_sign_alert);
            fragment.show(getFragmentManager(),TAG_ALERT);
        }
    }
}
