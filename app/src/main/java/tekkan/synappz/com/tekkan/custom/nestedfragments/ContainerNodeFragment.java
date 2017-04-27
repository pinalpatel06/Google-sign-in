package tekkan.synappz.com.tekkan.custom.nestedfragments;

import android.support.v4.app.Fragment;

/**
 * Created by Tejas Sherdiwala on 26/04/17.
 */

public abstract class ContainerNodeFragment extends Fragment implements ContainerNodeInterface {

    private static final String TAG = ContainerNodeFragment.class.getSimpleName();

    @Override
    public boolean onBackPressed() {
        return NestedFragmentUtil.onBackPressed(getContainerId(),getChildFragmentManager(),getChangeCallback());
    }

    @Override
    public void setChild(CommonNodeInterface fragment) {
       NestedFragmentUtil.setChild(fragment,getContainerId(),getChildFragmentManager(),getChangeCallback());
    }
}
