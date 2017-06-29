package com.bayer.ah.bayertekenscanner.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.bayer.ah.bayertekenscanner.R;
import com.bayer.ah.bayertekenscanner.fragment.ProfileFragment;


/**
 * Created by Tejas Sherdiwala on 5/2/2017.
 * &copy; Knoxpo
 */

public class ProfileActivity extends ToolbarActivity {
    private static final String TAG = ProfileActivity.class.getSimpleName();
    public static final String
            EXTRA_NEW_PROFILE = TAG + ".EXTRA_NEW_PROFILE",
            EXTRA_CAN_EDIT = TAG + ".EXTRA_CAN_EDIT";


    @Override
    protected Fragment getFragment() {
        if (getIntent().hasExtra(EXTRA_NEW_PROFILE)) {
            return ProfileFragment.newInstance(
                    getIntent().getBooleanExtra(EXTRA_NEW_PROFILE, false),getIntent().getBooleanExtra(EXTRA_CAN_EDIT,false));
        } else {
            return new ProfileFragment();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.profile_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
