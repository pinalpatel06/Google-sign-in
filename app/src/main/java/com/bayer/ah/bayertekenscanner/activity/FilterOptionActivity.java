package com.bayer.ah.bayertekenscanner.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.bayer.ah.bayertekenscanner.R;
import com.bayer.ah.bayertekenscanner.fragment.FilterOptionFragment;

/**
 * Created by Tejas Sherdiwala on 4/21/2017.
 * &copy; Knoxpo
 */

public class FilterOptionActivity extends ToolbarActivity {

    @Override
    protected Fragment getFragment() {
        return new FilterOptionFragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.filter_option_toolbar_text));
    }
}
