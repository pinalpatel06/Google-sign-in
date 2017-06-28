package com.bayer.ah.bayertekenscanner.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.bayer.ah.bayertekenscanner.R;
import com.bayer.ah.bayertekenscanner.fragment.teekMelden.ApplyPetFragment;


/**
 * Created by Tejas Sherdiwala on 5/2/2017.
 * &copy; Knoxpo
 */

public class ApplyForPetActivity extends ToolbarActivity {
    private static final String TAG = ApplyForPetActivity.class.getSimpleName();
    public static final String EXTRA_PET_BUNDLE = TAG + ".EXTRA_PET_BUNDLE";

    @Override
    protected Fragment getFragment() {
        return ApplyPetFragment.newInstance(getIntent().getBundleExtra(EXTRA_PET_BUNDLE));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(null);
        setTitle(getString(R.string.investigate_app_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
