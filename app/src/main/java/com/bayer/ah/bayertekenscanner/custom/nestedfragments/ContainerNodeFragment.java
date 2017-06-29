package com.bayer.ah.bayertekenscanner.custom.nestedfragments;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by Tejas Sherdiwala on 26/04/17.
 */

public abstract class ContainerNodeFragment extends Fragment implements ContainerNodeInterface {

    private static final String TAG = ContainerNodeFragment.class.getSimpleName();
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

    @Override
    public boolean onBackPressed() {
        return NestedFragmentUtil.onBackPressed(getContainerId(),getChildFragmentManager(),getChangeCallback());
    }

    @Override
    public void setChild(CommonNodeInterface fragment) {
       NestedFragmentUtil.setChild(fragment,getContainerId(),getChildFragmentManager(),getChangeCallback());
    }

    @Override
    public FragmentChangeCallback getChangeCallback() {
        return mCallback;
    }

    @Override
    public boolean shouldDisplayHomeAsUpEnabled() {
        return NestedFragmentUtil.shouldDisplayHomeAsUpEnabled(getContainerId(),true,getChildFragmentManager());
    }

    protected Fragment getChild(){
        return getChildFragmentManager().findFragmentById(getContainerId());
    }
}
