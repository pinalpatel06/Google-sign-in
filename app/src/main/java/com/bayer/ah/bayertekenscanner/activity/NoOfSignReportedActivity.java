package com.bayer.ah.bayertekenscanner.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.bayer.ah.bayertekenscanner.R;
import com.bayer.ah.bayertekenscanner.fragment.teekMelden.NoOfSignReportedFragment;

/**
 * Created by Tejas Sherdiwala on 7/7/2017.
 * &copy; Knoxpo
 */

public class NoOfSignReportedActivity extends ToolbarActivity {
    private static final String TAG = NoOfSignReportedActivity.class.getSimpleName();
    public static final String EXTRA_PET_BUNDLE = TAG + ".EXTRA_PET_BUNDLE";


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected Fragment getFragment() {
        return NoOfSignReportedFragment.newInstance(getIntent().getBundleExtra(EXTRA_PET_BUNDLE));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(null);
        setTitle(getString(R.string.investigate_app_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
