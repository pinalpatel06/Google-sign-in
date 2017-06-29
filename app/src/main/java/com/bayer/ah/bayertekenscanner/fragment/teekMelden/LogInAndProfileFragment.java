package com.bayer.ah.bayertekenscanner.fragment.teekMelden;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.bayer.ah.bayertekenscanner.R;
import com.bayer.ah.bayertekenscanner.activity.InvestigatePetActivity;
import com.bayer.ah.bayertekenscanner.activity.ProfileActivity;
import com.bayer.ah.bayertekenscanner.dialogs.AlertDialogFragment;
import com.bayer.ah.bayertekenscanner.dialogs.ProgressDialogFragment;
import com.bayer.ah.bayertekenscanner.utils.LoginUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Tejas Sherdiwala on 5/2/2017.
 * &copy; Knoxpo
 */

public class LogInAndProfileFragment extends Fragment implements LoginUtils.Listener {
    private static final String
            TAG = LogInAndProfileFragment.class.getSimpleName(),
            ARGS_TEEK_BUNDLE = TAG + ".ARGS_TEEK_BUNDLE",
            TAG_PROGRESS_DIALOG = TAG + ".TAG_PROGRESS_DIALOG",
            TAG_ALERT_DIALOG = TAG + ".TAG_ALERT_DIALOG";

    @BindView(R.id.btn_log_in)
    Button mLogInBtn;
    @BindView(R.id.btn_profile)
    Button mProfileBtn;
    @BindView(R.id.et_email)
    EditText mEmailET;
    @BindView(R.id.et_password)
    EditText mPasswordET;

    public static LogInAndProfileFragment newInstance(Bundle bundle) {

        Bundle args = new Bundle();
        args.putBundle(ARGS_TEEK_BUNDLE, bundle);
        LogInAndProfileFragment fragment = new LogInAndProfileFragment();
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
        View v = inflater.inflate(R.layout.fragment_login_and_profile, container, false);
        ButterKnife.bind(this, v);
        setHasOptionsMenu(true);
        return v;
    }

    @OnClick({R.id.btn_log_in,R.id.btn_profile})
    public void onButtonClicked(View v) {
        switch (v.getId()){
            case R.id.btn_log_in:
                String email = mEmailET.getText().toString();
                String password = mPasswordET.getText().toString();

                final ProgressDialogFragment fragment = ProgressDialogFragment.newInstance(getString(R.string.login));
                fragment.show(getFragmentManager(), TAG_PROGRESS_DIALOG);

                LoginUtils.logIn(getActivity(), email, password, this);
                break;
            case R.id.btn_profile:
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra(ProfileActivity.EXTRA_NEW_PROFILE, true);
                intent.putExtra(ProfileActivity.EXTRA_CAN_EDIT, true);
                startActivity(intent);
        }

    }

    @Override
    public void onSuccess() {
        ProgressDialogFragment fragment = (ProgressDialogFragment) getFragmentManager().findFragmentByTag(TAG_PROGRESS_DIALOG);
        fragment.dismiss();

        mEmailET.setText("");
        mPasswordET.setText("");

        Intent intent = new Intent(getActivity(), InvestigatePetActivity.class);
        intent.putExtra(InvestigatePetActivity.EXTRA_TEEK_BUNDLE, getArguments().getBundle(ARGS_TEEK_BUNDLE));
        getActivity().finish();
        startActivity(intent);
    }

    @Override
    public void onFailure(VolleyError error, int errorCode, String errorString) {
        Log.d(TAG , errorCode + " " + errorString);
        ProgressDialogFragment fragment = (ProgressDialogFragment) getFragmentManager().findFragmentByTag(TAG_PROGRESS_DIALOG);
        fragment.dismiss();
        AlertDialogFragment fragment1 = AlertDialogFragment.newInstance(R.string.error, R.string.loading_profile);
        fragment1.show(getFragmentManager(), TAG_ALERT_DIALOG);
    }
}
