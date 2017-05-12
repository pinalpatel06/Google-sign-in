package tekkan.synappz.com.tekkan.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.activity.ProfileActivity;
import tekkan.synappz.com.tekkan.custom.nestedfragments.ContainerNodeFragment;
import tekkan.synappz.com.tekkan.custom.nestedfragments.FragmentChangeCallback;
import tekkan.synappz.com.tekkan.custom.nestedfragments.NestedFragmentUtil;
import tekkan.synappz.com.tekkan.custom.network.TekenStringRequest;
import tekkan.synappz.com.tekkan.dialogs.AlertDialogFragment;
import tekkan.synappz.com.tekkan.dialogs.ProgressDialogFragment;
import tekkan.synappz.com.tekkan.utils.Constants;
import tekkan.synappz.com.tekkan.utils.LoginUtils;
import tekkan.synappz.com.tekkan.utils.VolleyHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends ContainerNodeFragment implements Response.Listener<String>, Response.ErrorListener {

    private static final String
            TAG = LoginFragment.class.getSimpleName(),
            TAG_ALERT_DIALOG = TAG + ".TAG_PROGRESS_DIALOG";
    public static final String  TAG_PROGRESS_DIALOG = TAG + ".TAG_ALERT_DIALOG";

    @BindView(R.id.btn_log_in)
    Button mLoginBtn;
    @BindView(R.id.tv_create_account)
    TextView mCreateAccountTV;
    @BindView(R.id.et_email)
    EditText mEmailET;
    @BindView(R.id.et_password)
    EditText mPasswordET;

    private FragmentChangeCallback mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (FragmentChangeCallback) getActivity();
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    @Override
    public void setHasOptionsMenu(boolean hasMenu) {
        super.setHasOptionsMenu(hasMenu);
        Fragment childFragment = getChild();
        if (childFragment != null) {
            childFragment.setHasOptionsMenu(hasMenu);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    private final String
            PARAM_EMAIL = "email",
            PARAM_PASSWORD = "password";

    @OnClick(R.id.btn_log_in)
    public void logIn() {
        String email = mEmailET.getText().toString();
        String encrPassword = LoginUtils.encode(mPasswordET.getText().toString());
        Log.d(TAG, encrPassword);

        final ProgressDialogFragment fragment = ProgressDialogFragment.newInstance(getString(R.string.login));
        fragment.show(getFragmentManager(), TAG_PROGRESS_DIALOG);

        TekenStringRequest request = new TekenStringRequest(
                Request.Method.POST,
                Constants.Api.getUrl(Constants.Api.FUNC_LOGIN),
                this,
                this
        );
        request.addParam(PARAM_EMAIL, email);
        request.addParam(PARAM_PASSWORD, encrPassword);
        VolleyHelper.getInstance(getActivity()).addToRequestQueue(request);
    }

    @OnClick(R.id.tv_create_account)
    public void showProfile() {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra(ProfileActivity.EXTRA_NEW_PROFILE, true);
        startActivity(intent);
    }

    @Override
    public String getTitle() {
        return getString(R.string.profile_title);
    }

    @Override
    public int getContainerId() {
        return R.id.fragment_container;
    }

    @Override
    public FragmentChangeCallback getChangeCallback() {
        return mCallback;
    }

    @Override
    public boolean shouldDisplayHomeAsUpEnabled() {
        return NestedFragmentUtil.shouldDisplayHomeAsUpEnabled(getContainerId(), false, getChildFragmentManager());
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d(TAG, "Failure");
        ProgressDialogFragment fragment = (ProgressDialogFragment) getFragmentManager().findFragmentByTag(TAG_PROGRESS_DIALOG);
        fragment.dismiss();
        AlertDialogFragment fragment1 = AlertDialogFragment.newInstance(R.string.error, R.string.invalid_login);
        fragment1.show(getFragmentManager(), TAG_ALERT_DIALOG);
        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit()
                .putBoolean(Constants.SP.BOOLEAN_LOGED_IN, true)
                .apply();
    }

    @Override
    public void onResponse(String response) {
        Log.d(TAG, "Success");
        /*ProgressDialogFragment fragment = (ProgressDialogFragment) getFragmentManager().findFragmentByTag(TAG_PROGRESS_DIALOG);
        fragment.dismiss();*/
        setChild(ProfileFragment.newInstance(false,mEmailET.getText().toString()));
    }
}
