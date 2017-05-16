package tekkan.synappz.com.tekkan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.activity.MainActivity;

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
        return v;
    }

    private void updateUI(){
        if(mStr != null) {
            mText1.setText(mStr);
        }
    }

    @OnClick(R.id.rl_step1)
    public void click(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        getActivity().finish();
        startActivity(intent);
    }
}
