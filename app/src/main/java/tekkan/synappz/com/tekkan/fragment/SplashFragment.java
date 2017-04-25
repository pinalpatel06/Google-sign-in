package tekkan.synappz.com.tekkan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.activity.DisclaimerActivity;
import tekkan.synappz.com.tekkan.activity.ProfileSetupActivity;

/**
 * Created by Tejas Sherdiwala on 4/19/2017.
 * &copy; Knoxpo
 */

public class SplashFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_splash,container,false);
        v.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
//        Intent intent = new Intent(getActivity(), DisclaimerActivity.class);
//        startActivity(intent);

        Intent intent = new Intent(getActivity(), ProfileSetupActivity.class);
        startActivity(intent);
    }
}
