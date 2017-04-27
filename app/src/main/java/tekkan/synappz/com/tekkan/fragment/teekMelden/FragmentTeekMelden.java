package tekkan.synappz.com.tekkan.fragment.teekMelden;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.custom.nestedfragments.ContainerNodeFragment;
import tekkan.synappz.com.tekkan.custom.nestedfragments.FragmentChangeCallback;
import tekkan.synappz.com.tekkan.custom.nestedfragments.NestedFragmentUtil;

/**
 * Created by Tejas Sherdiwala on 4/26/2017.
 * &copy; Knoxpo
 */

public class FragmentTeekMelden extends ContainerNodeFragment {

    @BindView(R.id.btn_next)
    Button mNextBtn;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_teek_melden, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @OnClick(R.id.btn_next)
    public void onClickNext() {
        setChild(new FragmentAnimalSelection());
    }

    @Override
    public String getTitle() {
        return NestedFragmentUtil.getTitle(getChildFragmentManager(), getString(R.string.title_report_tick), getContainerId());
    }

    @Override
    public int getContainerId() {
        return R.id.fragment_container;
    }

    @Override
    public FragmentChangeCallback getChangeCallback() {
        return mCallback;
    }
}
