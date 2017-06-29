package com.bayer.ah.bayertekenscanner.fragment.teekMelden;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bayer.ah.bayertekenscanner.R;
import com.bayer.ah.bayertekenscanner.activity.InvestigatePetActivity;
import com.bayer.ah.bayertekenscanner.activity.LogInAndProfileActivity;
import com.bayer.ah.bayertekenscanner.activity.QRScannerActivity;
import com.bayer.ah.bayertekenscanner.custom.nestedfragments.CommonNodeInterface;
import com.bayer.ah.bayertekenscanner.custom.network.TekenErrorListener;
import com.bayer.ah.bayertekenscanner.custom.network.TekenJsonObjectRequest;
import com.bayer.ah.bayertekenscanner.custom.network.TekenResponseListener;
import com.bayer.ah.bayertekenscanner.model.User;
import com.bayer.ah.bayertekenscanner.utils.Constants;
import com.bayer.ah.bayertekenscanner.utils.VolleyHelper;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Tejas Sherdiwala on 4/26/2017.
 * &copy; Knoxpo
 */

public class TickReportConfirmFragment extends Fragment implements CommonNodeInterface {
    private static final String
            TAG = TickReportConfirmFragment.class.getSimpleName(),
            ARGS_BUNDLE = TAG + "ARGS_BUNDLE",
            ARGS_LAYOUT_TYPE = TAG + ".ARGS_LAYOUT_TYPE";

    public static final String ARGS_APPLY_RESEARCH = TAG + ".ARGS_APPLY_RESEARCH";
    private static final int REQUEST_FREE_CODE = 0;

    @BindView(R.id.tv_research_kit_detail)
    TextView mKitDetailTextTV;
    @BindView(R.id.btn_apply_kit)
    Button mWantToApplyForKitBtn;
    @BindView(R.id.btn_have_kit)
    Button mHaveKitBtn;
    @BindView(R.id.btn_no)
    Button mNoBtn;

    private int mLayoutType;

    public static TickReportConfirmFragment newInstance(Bundle bundle, int type) {

        Bundle args = new Bundle();
        args.putInt(ARGS_LAYOUT_TYPE, type);
        args.putBundle(ARGS_BUNDLE, bundle);
        TickReportConfirmFragment fragment = new TickReportConfirmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayoutType = getArguments().getInt(ARGS_LAYOUT_TYPE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_research_kit, container, false);
        v.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_blurred));
        ButterKnife.bind(this, v);
        updateUI();
        return v;
    }

    private void updateUI() {
        switch (mLayoutType) {
            case 0:
                mKitDetailTextTV.setText(getString(R.string.thanks_detail_text));
                break;
            case 1:
                mKitDetailTextTV.setText(getString(R.string.research_text));
        }
    }

    @OnClick(R.id.btn_apply_kit)
    public void showLogInOrPetDetail() {
        TekenJsonObjectRequest request = new TekenJsonObjectRequest(
                Request.Method.GET,
                Constants.Api.getUrl(Constants.Api.FUNC_COUNT_FREE_CODES),
                new TekenResponseListener<JSONObject>() {
                    @Override
                    public void onResponse(int requestCode, JSONObject response) {
                        if (response != null) {
                            int code = response.optInt("counted");
                            if (code > 0) {
                                Bundle bundle = getArguments().getBundle(ARGS_BUNDLE);

                                if (User.getInstance(getActivity()).isLoaded()) {
                                    bundle.putString(ARGS_APPLY_RESEARCH, "N");
                                    Intent intent = new Intent(getActivity(), InvestigatePetActivity.class);
                                    intent.putExtra(InvestigatePetActivity.EXTRA_TEEK_BUNDLE, bundle);
                                    startActivity(intent);
                                } else {
                                    bundle.putString(ARGS_APPLY_RESEARCH, "N");
                                    Intent intent = new Intent(getActivity(), LogInAndProfileActivity.class);
                                    intent.putExtra(LogInAndProfileActivity.EXTRA_TEEK_BUNDLE, bundle);
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(
                                        getActivity(),
                                        R.string.no_free_code,
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                    }
                },
                new TekenErrorListener() {
                    @Override
                    public void onErrorResponse(int requestCode, VolleyError error, int status, String message) {

                    }
                },
                REQUEST_FREE_CODE
        );

        VolleyHelper.getInstance(getActivity()).addToRequestQueue(request);
    }

    @OnClick(R.id.btn_have_kit)
    public void showQRCodeScannerActivity() {
        Bundle bundle = getArguments().getBundle(ARGS_BUNDLE);
        bundle.putString(ARGS_APPLY_RESEARCH, "Y");
        Intent intent = new Intent(getActivity(), QRScannerActivity.class);
        intent.putExtra(QRScannerActivity.EXTRA_TEEK_BUNDLE, bundle);
        startActivity(intent);
    }

    @OnClick(R.id.btn_no)
    public void reset() {
        TickMapFragment fragment = (TickMapFragment) getParentFragment();
        fragment.onReset();
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public boolean shouldDisplayHomeAsUpEnabled() {
        return true;
    }
}
