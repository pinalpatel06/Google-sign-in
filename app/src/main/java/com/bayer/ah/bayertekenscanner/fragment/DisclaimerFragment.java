package com.bayer.ah.bayertekenscanner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bayer.ah.bayertekenscanner.R;
import com.bayer.ah.bayertekenscanner.activity.ProfileSetupActivity;
import com.bayer.ah.bayertekenscanner.utils.Constants;

import butterknife.ButterKnife;
import butterknife.OnClick;



/**
 * Created by Tejas Sherdiwala on 4/19/2017.
 * &copy; Knoxpo
 */

public class DisclaimerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_disclaimer, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @OnClick(R.id.btn_agreement)
    public void acceptAgreement() {
        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit()
                .putBoolean(Constants.SP.HAS_ACCEPTED_DISCLAIMER, true)
                .apply();
        Intent intent = new Intent(getActivity(), ProfileSetupActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
