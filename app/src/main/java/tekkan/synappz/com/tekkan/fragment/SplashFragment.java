package tekkan.synappz.com.tekkan.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.activity.DisclaimerActivity;
import tekkan.synappz.com.tekkan.activity.MainActivity;
import tekkan.synappz.com.tekkan.activity.ProfileSetupActivity;
import tekkan.synappz.com.tekkan.utils.Constants;

/**
 * Created by Tejas Sherdiwala on 4/19/2017.
 * &copy; Knoxpo
 */

public class SplashFragment extends Fragment {

    private static final int
            TIMEOUT_MILLIS = 2000;

    private Handler mTimeoutHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            goToNextScreen();
            return true;
        }
    });

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_splash, container, false);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mTimeoutHandler.sendEmptyMessageDelayed(0, TIMEOUT_MILLIS);
    }

    @Override
    public void onPause() {
        mTimeoutHandler.removeCallbacksAndMessages(null);
        super.onPause();
    }

    private void goToNextScreen() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean hasAcceptedDisclaimer = preferences.getBoolean(Constants.SP.HAS_ACCEPTED_DISCLAIMER, false);
        boolean isProfileSetupFinished = preferences.getBoolean(Constants.SP.IS_PROFILE_SETUP_FINISHED, false);
        Class c = hasAcceptedDisclaimer ? isProfileSetupFinished ? MainActivity.class : ProfileSetupActivity.class : DisclaimerActivity.class;
        Intent intent = new Intent(getActivity(), c);
        startActivity(intent);
        getActivity().finish();
    }
}
