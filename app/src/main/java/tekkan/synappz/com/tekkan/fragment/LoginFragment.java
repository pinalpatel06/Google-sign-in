package tekkan.synappz.com.tekkan.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.activity.ProfileActivity;
import tekkan.synappz.com.tekkan.custom.nestedfragments.ContainerNodeFragment;
import tekkan.synappz.com.tekkan.custom.nestedfragments.FragmentChangeCallback;
import tekkan.synappz.com.tekkan.custom.nestedfragments.NestedFragmentUtil;
import tekkan.synappz.com.tekkan.dialogs.AlertDialogFragment;
import tekkan.synappz.com.tekkan.dialogs.ProgressDialogFragment;
import tekkan.synappz.com.tekkan.model.User;
import tekkan.synappz.com.tekkan.utils.LoginUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends ContainerNodeFragment implements LoginUtils.Listener {

    private static final String
            TAG = LoginFragment.class.getSimpleName(),
            TAG_ALERT_DIALOG = TAG + ".TAG_PROGRESS_DIALOG";

    public static final String TAG_PROGRESS_DIALOG = TAG + ".TAG_ALERT_DIALOG";

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
    public void onResume() {
        super.onResume();
        if(User.getInstance(getActivity()).isLoaded()){
            setChild(ProfileFragment.newInstance(false,false));
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

    @OnClick(R.id.btn_log_in)
    public void logIn() {
        String email = mEmailET.getText().toString();
        String password = mPasswordET.getText().toString();

        final ProgressDialogFragment fragment = ProgressDialogFragment.newInstance(getString(R.string.login));
        fragment.show(getFragmentManager(), TAG_PROGRESS_DIALOG);

        LoginUtils.logIn(getActivity(), email, password, this);
    }

    @OnClick(R.id.tv_create_account)
    public void showProfile() {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra(ProfileActivity.EXTRA_NEW_PROFILE, true);
        intent.putExtra(ProfileActivity.EXTRA_CAN_EDIT, true);
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
    public void onSuccess() {
        ProgressDialogFragment fragment = (ProgressDialogFragment) getFragmentManager().findFragmentByTag(TAG_PROGRESS_DIALOG);
        fragment.dismiss();

        mEmailET.setText("");
        mPasswordET.setText("");

        setChild(ProfileFragment.newInstance(false, false));
    }

    @Override
    public void onFailure(VolleyError error, int errorCode, String errorString) {
        ProgressDialogFragment fragment = (ProgressDialogFragment) getFragmentManager().findFragmentByTag(TAG_PROGRESS_DIALOG);
        fragment.dismiss();

        AlertDialogFragment fragment1 = AlertDialogFragment.newInstance(R.string.error, R.string.invalid_login);
        fragment1.show(getFragmentManager(), TAG_ALERT_DIALOG);
    }

    @Override
    public boolean onBackPressed() {
        if(User.getInstance(getActivity()).isLoaded()){
            return false;
        }else {
            return super.onBackPressed();
        }
    }
}
