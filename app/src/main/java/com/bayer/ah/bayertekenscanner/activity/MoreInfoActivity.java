package com.bayer.ah.bayertekenscanner.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.bayer.ah.bayertekenscanner.R;

import com.bayer.ah.bayertekenscanner.fragment.MoreInfoFragment;

/**
 * Created by Tejas Sherdiwala on 4/22/2017.
 * &copy; Knoxpo
 */

public class MoreInfoActivity extends ToolbarActivity {

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
        return new MoreInfoFragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(null);
        setTitle(getString(R.string.more_info_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
