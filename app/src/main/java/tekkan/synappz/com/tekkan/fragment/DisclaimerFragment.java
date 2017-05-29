package tekkan.synappz.com.tekkan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.activity.ProfileSetupActivity;
import tekkan.synappz.com.tekkan.utils.Constants;

/**
 * Created by Tejas Sherdiwala on 4/19/2017.
 * &copy; Knoxpo
 */

public class DisclaimerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_disclaimer, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @OnClick(R.id.btn_agreement)
    public void acceptAgreement() {
        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit()
                .putBoolean(Constants.SP.HAS_ACCEPTED_DISCLAIMER, true)
                .apply();
        Intent intent = new Intent(getActivity(), ProfileSetupActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
