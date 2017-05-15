package tekkan.synappz.com.tekkan.fragment.teekMelden;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.activity.InvestigatePetActivity;
import tekkan.synappz.com.tekkan.activity.ProfileActivity;

/**
 * Created by Tejas Sherdiwala on 5/2/2017.
 * &copy; Knoxpo
 */

public class LogInAndProfileFragment extends Fragment {
    private static final String
            TAG = LogInAndProfileFragment.class.getSimpleName(),
            ARGS_TEEK_BUNDLE = TAG + ".ARGS_TEEK_BUNDLE";


    @BindView(R.id.btn_log_in)
    Button mLogInBtn;
    @BindView(R.id.btn_profile)
    Button mProfileBtn;

    public static LogInAndProfileFragment newInstance(Bundle bundle) {

        Bundle args = new Bundle();
        args.putBundle(ARGS_TEEK_BUNDLE,bundle);
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

    @OnClick(R.id.btn_log_in)
    public void logIn() {
        //after successfully Login
        Intent intent = new Intent(getActivity(), InvestigatePetActivity.class);
        intent.putExtra(InvestigatePetActivity.EXTRA_TEEK_BUNDLE,getArguments().getBundle(ARGS_TEEK_BUNDLE));
        startActivity(intent);
    }

    @OnClick(R.id.btn_profile)
    public void showProfile() {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra(ProfileActivity.EXTRA_NEW_PROFILE,true);
        startActivity(intent);
    }
}
