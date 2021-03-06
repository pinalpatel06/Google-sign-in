package com.bayer.ah.bayertekenscanner.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bayer.ah.bayertekenscanner.R;
import com.bayer.ah.bayertekenscanner.custom.nestedfragments.ContainerNodeInterface;
import com.bayer.ah.bayertekenscanner.custom.nestedfragments.CustomFragmentStatePageAdapter;
import com.bayer.ah.bayertekenscanner.custom.nestedfragments.FragmentChangeCallback;
import com.bayer.ah.bayertekenscanner.custom.nestedfragments.NestedFragmentUtil;
import com.bayer.ah.bayertekenscanner.fragment.HeatMapFragment;
import com.bayer.ah.bayertekenscanner.fragment.LoginFragment;
import com.bayer.ah.bayertekenscanner.fragment.advices.TipsTabFragment;
import com.bayer.ah.bayertekenscanner.fragment.product.ProductTabFragment;
import com.bayer.ah.bayertekenscanner.fragment.teekMelden.TickReportHelpFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, FragmentChangeCallback, TickReportHelpFragment.Callback {

    private static final int
            POSITION_TEKENSCANNER = 0,
            POSITION_PROFILE = 1,
            POSITION_TEEK_MELDEN = 2,
            POSITION_ONDERZOEK = 3,
            POSITION_PRODUCT = 4;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private Adapter mAdapter;

    private boolean mIsBackPressed = false;

    private static final int
            DEFAULT_DELAY = 2000;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mIsBackPressed = false;
            return true;
        }
    });

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setSupportActionBar(mToolbar);
        setTitle(null);

        mAdapter.addFragment(new HeatMapFragment(), getString(R.string.text_tab1));
        mAdapter.addFragment(new LoginFragment(), getString(R.string.text_tab2));
        mAdapter.addFragment(new TickReportHelpFragment(), getString(R.string.text_tab3));
        mAdapter.addFragment(new TipsTabFragment(), getString(R.string.title_research));
        mAdapter.addFragment(new ProductTabFragment(), getString(R.string.text_tab5));

        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setText("");
            switch (i) {
                case 0:
                    tab.setIcon(getDrawable(R.drawable.bg_tab1));
                    break;
                case 1:
                    tab.setIcon(getDrawable(R.drawable.bg_tab2));
                    break;
                case 2:
                    tab.setIcon(getDrawable(R.drawable.bg_tab3));
                    break;
                case 3:
                    tab.setIcon(getDrawable(R.drawable.bg_tab4));
                    break;
                case 4:
                    tab.setIcon(getDrawable(R.drawable.bg_tab5));
                    break;
            }
        }
        mTabLayout.addOnTabSelectedListener(this);

        setCurrentItem(POSITION_TEKENSCANNER);
    }

    private void init() {
        ButterKnife.bind(this);
        mAdapter = new Adapter(getSupportFragmentManager());
    }

    private void setAppTitle(CharSequence title) {
        ((TextView) mToolbar.findViewById(android.R.id.text1)).setText(title);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition(), false);
        setTitle(null);
        setAppTitle(mAdapter.getPageTitle(tab.getPosition()));
        onFragmentChanged();
    }

    private void invalidateFragmentMenus(int position) {
        for (int i = 0; i < mAdapter.getCount(); i++) {
            Fragment fragment = mAdapter.getFragment(i);
            if (fragment != null) {
                fragment.setHasOptionsMenu(i == position);
            }
        }
        invalidateOptionsMenu(); //or respectively its support method.
    }


    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    private void setCurrentItem(int position) {
        mViewPager.setCurrentItem(position, false);
        setAppTitle(mAdapter.getPageTitle(position));
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getCurrentFragment();
        boolean isHandled = false;
        if (fragment != null && fragment instanceof ContainerNodeInterface) {
            isHandled = ((ContainerNodeInterface) fragment).onBackPressed();
        }
        if (!isHandled) {
            if (mIsBackPressed) {
                mHandler.removeCallbacksAndMessages(this);
                super.onBackPressed();
            } else {
                mIsBackPressed = true;
                mHandler.sendEmptyMessageDelayed(0, DEFAULT_DELAY);
                Toast.makeText(
                        this,
                       R.string.exit_msg,
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    private Fragment getCurrentFragment() {
        return mAdapter.getFragment(mViewPager.getCurrentItem());
    }

    @Override
    public void onFragmentChanged() {
        Fragment fragment = getCurrentFragment();

        boolean hasToShowHomeIcon = false;

        if (fragment != null && fragment instanceof ContainerNodeInterface) {
            setAppTitle(((ContainerNodeInterface) fragment).getTitle());

            hasToShowHomeIcon = NestedFragmentUtil.hasChild((ContainerNodeInterface) fragment, ((ContainerNodeInterface) fragment).getContainerId()) &
                    ((ContainerNodeInterface) fragment).shouldDisplayHomeAsUpEnabled();
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(hasToShowHomeIcon);
        }

        invalidateFragmentMenus(mViewPager.getCurrentItem());
    }

    @Override
    public void resetFragment() {
        setCurrentItem(POSITION_TEKENSCANNER);
    }

    private class Adapter extends CustomFragmentStatePageAdapter {

        private ArrayList<Fragment> mFragments;
        private ArrayList<String> mTitles;

        Adapter(FragmentManager fm) {
            super(fm);
            mFragments = new ArrayList<>();
            mTitles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }

        void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mTitles.add(title);
        }
    }
}
