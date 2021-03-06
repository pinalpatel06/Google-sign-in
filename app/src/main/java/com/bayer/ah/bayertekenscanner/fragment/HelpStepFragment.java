package com.bayer.ah.bayertekenscanner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bayer.ah.bayertekenscanner.R;
import com.bayer.ah.bayertekenscanner.activity.MainActivity;
import com.bayer.ah.bayertekenscanner.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tejas Sherdiwala on 4/20/2017.
 * &copy; Knoxpo
 */

public class HelpStepFragment extends Fragment{

    private static final String
            TAG = HelpStepFragment.class.getSimpleName(),
            ARGS_STRING = TAG + ".ARGS_STRING";


    private String mStr;

    @BindView(R.id.text1)
    TextView mText1;
    @BindView(R.id.rl_step1)
    RelativeLayout mStep1RT;

    public static HelpStepFragment newInstance(String str) {

        Bundle args = new Bundle();
        args.putString(ARGS_STRING,str);
        HelpStepFragment fragment = new HelpStepFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_close:
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit()
                        .putBoolean(Constants.SP.IS_PROFILE_SETUP_FINISHED, true)
                        .apply();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                getActivity().finish();
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mStr = getArguments().getString(ARGS_STRING);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragement_step_1,container,false);
        ButterKnife.bind(this,v);
        updateUI();
        setHasOptionsMenu(true);
        return v;
    }

    private void updateUI(){
        if(mStr != null) {
            mText1.setText(mStr);
        }
    }
}
