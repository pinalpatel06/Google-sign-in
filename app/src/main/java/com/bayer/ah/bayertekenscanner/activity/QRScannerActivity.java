package com.bayer.ah.bayertekenscanner.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.bayer.ah.bayertekenscanner.R;
import com.bayer.ah.bayertekenscanner.fragment.QRScannerFragment;

/**
 * Created by Tejas Sherdiwala on 5/2/2017.
 * &copy; Knoxpo
 */

public class QRScannerActivity extends ToolbarActivity {
    private static final String TAG = QRScannerActivity.class.getSimpleName();

    public static final String EXTRA_TEEK_BUNDLE = TAG + ".EXTRA_TEEK_BUNDLE";

    @Override
    protected Fragment getFragment() {
        return QRScannerFragment.newInstance(getIntent().getBundleExtra(EXTRA_TEEK_BUNDLE));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.qr_scanner_app_title));
    }
}
