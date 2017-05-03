package tekkan.synappz.com.tekkan.custom.nestedfragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.custom.ListFragment;

/**
 * Created by Tejas Sherdiwala on 03/05/17.
 */

public abstract class ContainerNodeListFragment<T, VH extends RecyclerView.ViewHolder> extends ListFragment<T, VH> implements ContainerNodeInterface {


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
        return NestedFragmentUtil.onBackPressed(getContainerId(), getChildFragmentManager(), getChangeCallback());
    }

    @Override
    public void setChild(CommonNodeInterface fragment) {
        NestedFragmentUtil.setChild(fragment, getContainerId(), getChildFragmentManager(), getChangeCallback());
    }

    @Override
    public FragmentChangeCallback getChangeCallback() {
        return mCallback;
    }

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_container_node_list;
    }

    @Override
    public int getContainerId() {
        return R.id.fragment_container;
    }
}
