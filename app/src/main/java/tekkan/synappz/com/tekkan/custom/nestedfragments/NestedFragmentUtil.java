package tekkan.synappz.com.tekkan.custom.nestedfragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by Tejas Sherdiwala on 26/04/17.
 */

public final class NestedFragmentUtil {


    public static boolean onBackPressed(int containerId, FragmentManager fragmentManager, FragmentChangeCallback callback) {
        Fragment fragment = fragmentManager.findFragmentById(containerId);

        boolean isHandled = false;
        if (fragment instanceof ContainerNodeInterface) {
            isHandled = ((ContainerNodeInterface) fragment).onBackPressed();
        }

        if (isHandled) {
            return true;
        }

        if (fragment != null) {
            fragmentManager
                    .beginTransaction()
                    .remove(fragment)
                    .commitNow();

            if (callback != null) {
                callback.onFragmentChanged();
            }
            return true;
        }
        return false;
    }

    public static void setChild(CommonNodeInterface fragment, int containerId, FragmentManager fragmentManager, FragmentChangeCallback callback) {
        fragmentManager
                .beginTransaction()
                .replace(containerId, (Fragment) fragment)
                .commitNow();
        if (callback != null) {
            callback.onFragmentChanged();
        }
    }

    public static String getTitle(FragmentManager fragmentManager, String defaultTitle, int containerId) {
        Fragment fragment = fragmentManager.findFragmentById(containerId);
        String title = null;
        if (fragment != null && fragment instanceof CommonNodeInterface) {
            title = ((CommonNodeInterface) fragment).getTitle();
        }
        if (title == null || title.isEmpty()) {
            return defaultTitle;
        } else {
            return title;
        }
    }

    public static boolean hasChild(ContainerNodeInterface fragment, int containerId) {
        FragmentManager fm = ((Fragment) fragment).getChildFragmentManager();
        Fragment childFragment = fm.findFragmentById(containerId);
        return childFragment != null;
    }

}
