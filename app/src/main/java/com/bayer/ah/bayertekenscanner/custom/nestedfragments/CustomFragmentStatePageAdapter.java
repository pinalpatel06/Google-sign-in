package com.bayer.ah.bayertekenscanner.custom.nestedfragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

/**
 * Created by Tejas Sherdiwala on 26/04/17.
 */

public abstract class CustomFragmentStatePageAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = CustomFragmentStatePageAdapter.class.getSimpleName();

    private Fragment mCurrentFragment;
    private SparseArray<Fragment> mFragments = new SparseArray<>();
    private FragmentManager mFragmentManager;

    public CustomFragmentStatePageAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        mCurrentFragment = (Fragment) object;
    }

    public Fragment getFragment(int position) {
        return mFragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mFragments.remove(position);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        super.restoreState(state, loader);

        if (state != null) {
            Bundle bundle = (Bundle) state;
            bundle.setClassLoader(loader);
            mFragments.clear();
            Iterable<String> keys = bundle.keySet();
            for (String key : keys) {
                if (key.startsWith("f")) {
                    int index = Integer.parseInt(key.substring(1));
                    Fragment f = mFragmentManager.getFragment(bundle, key);
                    if (f != null) {
                        mFragments.put(index, f);
                    } else {
                        Log.w(TAG, "Bad fragment at key " + key);
                    }
                }
            }
        }
    }
}
