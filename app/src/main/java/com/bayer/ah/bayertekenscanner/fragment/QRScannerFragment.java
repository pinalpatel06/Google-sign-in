package com.bayer.ah.bayertekenscanner.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bayer.ah.bayertekenscanner.R;
import com.bayer.ah.bayertekenscanner.activity.BarcodeCaptureActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



/**
 * A simple {@link Fragment} subclass.
 */
public class QRScannerFragment extends Fragment{
    private static final String
            TAG = QRScannerFragment.class.getSimpleName(),
            ARGS_TEEK_BUNDLE = TAG + ".ARGS_TEEK_BUNDLE";

    @BindView(R.id.btn_send)
    Button mScanBtn;
    @BindView(R.id.btn_no_code)
    Button mNoCodeBtn;
    @BindView(R.id.iv_code)
    ImageView mCodeIV;

    public static QRScannerFragment newInstance(Bundle bundle) {

        Bundle args = new Bundle();
        args.putBundle(ARGS_TEEK_BUNDLE,bundle);
        QRScannerFragment fragment = new QRScannerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile_setup, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_close:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_qrscanner, container, false);
        ButterKnife.bind(this, v);
        setHasOptionsMenu(true);
        return v;
    }

    @OnClick({R.id.btn_send, R.id.btn_no_code})
    public void scanQRCode(View v) {
        switch (v.getId()){
            case R.id.btn_send:
                Intent intent = new Intent(getActivity(), BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.EXTRA_TEEK_BUNDLE, getArguments().getBundle(ARGS_TEEK_BUNDLE));
                startActivity(intent);
                break;
            case R.id.btn_no_code:
                getActivity().finish();
        }
    }
}
