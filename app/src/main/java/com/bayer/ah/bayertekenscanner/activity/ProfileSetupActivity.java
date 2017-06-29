package com.bayer.ah.bayertekenscanner.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.bayer.ah.bayertekenscanner.R;
import com.bayer.ah.bayertekenscanner.fragment.HelpStepFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;


/**
 * Created by Tejas Sherdiwala on 4/20/2017.
 * &copy; Knoxpo
 */

public class ProfileSetupActivity extends AppCompatActivity {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.tv_back)
    TextView mBackTV;
    @BindView(R.id.tv_next)
    TextView mNextTV;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private Adapter mAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile_setup, menu);
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        init();
        setSupportActionBar(mToolbar);
        setTitle(null);

        mAdapter.addFragment(new HelpStepFragment());
        mAdapter.addFragment(new HelpStepFragment());
        mAdapter.addFragment(new HelpStepFragment());
        mAdapter.addFragment(new HelpStepFragment());
        mAdapter.addFragment(new HelpStepFragment());
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        updateUI();
    }

    private void init() {
        ButterKnife.bind(this);
        mAdapter = new Adapter(getSupportFragmentManager());
    }

    private void updateUI() {
        ((TextView) mToolbar.findViewById(android.R.id.text1)).setText(getString(R.string.profile_setup_title));

        int currentItem = mViewPager.getCurrentItem();
        mBackTV.setVisibility(currentItem == 0 ? View.INVISIBLE : View.VISIBLE);
        mNextTV.setVisibility(currentItem < mAdapter.getCount() - 1 ? View.VISIBLE : View.INVISIBLE);
    }

    @OnClick(R.id.tv_next)
    public void onClickNext() {
        int currentItem = mViewPager.getCurrentItem();
        if (currentItem < mAdapter.getCount() - 1) {
            mViewPager.setCurrentItem(currentItem + 1, true);
        }
    }

    @OnClick(R.id.tv_back)
    public void onClickBack() {
        int currentItem = mViewPager.getCurrentItem();
        if (currentItem > 0) {
            mViewPager.setCurrentItem(currentItem - 1, true);
        } else {
            super.onBackPressed();
        }
    }

    @OnPageChange(R.id.view_pager)
    public void onPageChanged() {
        updateUI();
    }

    private class Adapter extends FragmentStatePagerAdapter {

        private ArrayList<Fragment> mFragments;

        Adapter(FragmentManager fm) {
            super(fm);
            mFragments = new ArrayList<>();

        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        void addFragment(Fragment fragment) {
            mFragments.add(fragment);
        }
    }
}
