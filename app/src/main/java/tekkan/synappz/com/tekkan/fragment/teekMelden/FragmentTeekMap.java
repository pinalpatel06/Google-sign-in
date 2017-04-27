package tekkan.synappz.com.tekkan.fragment.teekMelden;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.custom.nestedfragments.ContainerNodeFragment;
import tekkan.synappz.com.tekkan.custom.nestedfragments.FragmentChangeCallback;

/**
 * Created by Tejas Sherdiwala on 4/26/2017.
 * &copy; Knoxpo
 */

public class FragmentTeekMap extends ContainerNodeFragment{

    private static final String
            TAG = FragmentTeekMap.class.getSimpleName(),
            ARGS_LOCATION_TYPE = TAG + ".ARGS_LOCATION_TYPE";

    private int mLocationType = LOCATION_CURRENT;

    private static final int
            LOCATION_CURRENT = 0,
            LOCATION_CUSTOM = 1,
            NO_BOTTOM_VIEW = 2;

    @BindView(R.id.lt_teek_melden_map_bottom_view)
    LinearLayout mBottomViewLT;
    @BindView(R.id.rt_teek_melden_map_bottom_view)
    RelativeLayout mCustomBottomView;
    @BindView(R.id.btn_location_close)
    Button mCloseLocationBtn;
    @BindView(R.id.btn_customize)
    Button mCustomizeLocationBtn;
    @BindView(R.id.iv_close)
    ImageView mCloseBottomViewIV;

    public static FragmentTeekMap newInstance(int locationType) {

        Bundle args = new Bundle();
        args.putInt(ARGS_LOCATION_TYPE,locationType);
        FragmentTeekMap fragment = new FragmentTeekMap();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mLocationType = args.getInt(ARGS_LOCATION_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_teek_map, container,false);
        ButterKnife.bind(this,v);
        v.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        if(savedInstanceState != null){
            mLocationType = savedInstanceState.getInt(ARGS_LOCATION_TYPE);
        }
        updateUI();
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARGS_LOCATION_TYPE,mLocationType);
    }

    private void updateUI(){
        switch (mLocationType){
            case 0:
                mBottomViewLT.setVisibility(View.VISIBLE);
                mCustomBottomView.setVisibility(View.GONE);
                break;
            case 1:
                mBottomViewLT.setVisibility(View.GONE);
                mCustomBottomView.setVisibility(View.VISIBLE);
                break;
            case 2:
                mBottomViewLT.setVisibility(View.GONE);
                mCustomBottomView.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btn_location_close)
    public void onCloseLocationClick(){
        setChild(new FragmentResearchToolkit());
    }

    @OnClick(R.id.btn_customize)
    public void onCustomizeLocationClick(){
        mLocationType = LOCATION_CUSTOM;
        updateUI();
    }

    @OnClick(R.id.iv_close)
    public void closeBottomView(){
        mLocationType = NO_BOTTOM_VIEW;
        updateUI();
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public int getContainerId() {
        return R.id.fragment_container;
    }

    @Override
    public FragmentChangeCallback getChangeCallback() {
        return null;
    }
}
