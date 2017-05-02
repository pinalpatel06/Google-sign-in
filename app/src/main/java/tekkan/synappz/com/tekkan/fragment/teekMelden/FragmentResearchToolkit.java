package tekkan.synappz.com.tekkan.fragment.teekMelden;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.activity.LogInAndProfileActivity;
import tekkan.synappz.com.tekkan.activity.QRScannerActivity;
import tekkan.synappz.com.tekkan.custom.nestedfragments.CommonNodeInterface;

/**
 * Created by Tejas Sherdiwala on 4/26/2017.
 * &copy; Knoxpo
 */

public class FragmentResearchToolkit extends Fragment implements CommonNodeInterface {
    private static final String
            TAG = FragmentResearchToolkit.class.getSimpleName(),
            ARGS_LAYOUT_TYPE = TAG + ".ARGS_LAYOUT_TYPE";

    @BindView(R.id.tv_research_kit_detail)
    TextView mKitDetailTextTV;
    @BindView(R.id.btn_apply_kit)
    Button mWantToApplyForKitBtn;
    @BindView(R.id.btn_have_kit)
    Button mHaveKitBtn;
    @BindView(R.id.btn_no)
    Button mNoBtn;

    private int mLayoutType;
    private boolean mIsLogged = false;

    public static FragmentResearchToolkit newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt(ARGS_LAYOUT_TYPE, type);
        FragmentResearchToolkit fragment = new FragmentResearchToolkit();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayoutType = getArguments().getInt(ARGS_LAYOUT_TYPE,0);
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

    private void updateUI(){
        switch (mLayoutType){
            case 0:
                mKitDetailTextTV.setText(getString(R.string.thnaks_detail_text));
                break;
            case 1:
                mKitDetailTextTV.setText(getString(R.string.research_text));
        }
    }

    @OnClick(R.id.btn_apply_kit)
    public void showLogInOrPetDetail(){
        if(mIsLogged){

        }else{
            Intent intent  = new Intent(getActivity(), LogInAndProfileActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.btn_have_kit)
    public void showQRCodeScannerActivity(){
        Intent intent  = new Intent(getActivity(), QRScannerActivity.class);
        startActivity(intent);
    }

    @Override
    public String getTitle() {
        return null;
    }
}
