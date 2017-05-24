package tekkan.synappz.com.tekkan.fragment.teekMelden;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

public class LocationSelectionFragment extends ContainerNodeFragment implements ParentFragmentCallback {

    private static final String
            TAG = LocationSelectionFragment.class.getSimpleName(),
            ARGS_PET_TYPE = TAG + ".ARGS_PET_TYPE";

    private static final int
            LOCATION_CURRENT = 0,
            LOCATION_CUSTOM = 1;

    private int mAnimalType;

    @BindView(R.id.tv_teek_melden_detail)
    TextView mDetailsTV;
    @BindView(R.id.btn_current_location)
    Button mCurrentLocationBtn;
    @BindView(R.id.btn_other_location)
    Button mOtherLocationBtn;

    private FragmentChangeCallback mCallback;

    public static LocationSelectionFragment newInstance(int petType) {

        Bundle args = new Bundle();
        args.putInt(ARGS_PET_TYPE, petType);
        LocationSelectionFragment fragment = new LocationSelectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mAnimalType = args.getInt(ARGS_PET_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pet_location_selection, container, false);
        ButterKnife.bind(this, v);
        updateUI();
        v.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_blurred));
        setHasOptionsMenu(true);
        return v;
    }

    private void updateUI() {
        switch (mAnimalType) {
            case 0:
                mDetailsTV.setText(getString(R.string.pet_hond_location_text));
                break;
            case 1:
                mDetailsTV.setText(getString(R.string.pet_kat_location_text));
                break;
        }
    }

    @OnClick({R.id.btn_current_location, R.id.btn_other_location})
    public void showMyLocationOnMap(View v) {
        switch (v.getId()) {
            case R.id.btn_current_location:
                setChild(TickMapFragment.newInstance(LOCATION_CURRENT));
                break;
            case R.id.btn_other_location:
                setChild(TickMapFragment.newInstance(LOCATION_CUSTOM));
        }
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
        return mCallback;
    }

    public void onReset() {
        Log.d(TAG, "On reset");
        AnimalSelectionFragment fragment = (AnimalSelectionFragment) getParentFragment();
        fragment.onReset();
    }
}
