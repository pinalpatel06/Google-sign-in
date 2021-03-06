package com.bayer.ah.bayertekenscanner.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.bayer.ah.bayertekenscanner.R;
import com.bayer.ah.bayertekenscanner.fragment.ViewPetFragment;
import com.bayer.ah.bayertekenscanner.model.Pet;



/**
 * Created by Admin on 04/05/17.
 */

public class ViewPetActivity extends ToolbarActivity {
    private static final String TAG = ViewPetActivity.class.getSimpleName();
    public static final String EXTRA_PET = TAG + ".EXTRA_PET";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.pet_profile_app_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    protected Fragment getFragment() {
        if (getIntent().hasExtra(EXTRA_PET)) {
            return ViewPetFragment.newInstance((Pet) getIntent().getParcelableExtra(EXTRA_PET));
        } else {
            return new ViewPetFragment();
        }
    }
}
