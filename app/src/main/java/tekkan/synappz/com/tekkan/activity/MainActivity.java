package tekkan.synappz.com.tekkan.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.fragment.FragmentAnimalTips;
import tekkan.synappz.com.tekkan.fragment.FragmentOnderzoek;
import tekkan.synappz.com.tekkan.fragment.FragmentTekenScanner;
import tekkan.synappz.com.tekkan.fragment.OnderzoekCallback;
import tekkan.synappz.com.tekkan.fragment.Step1Fragment;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, OnderzoekCallback {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setSupportActionBar(mToolbar);
        setTitle(null);

        mAdapter.addFragment(new FragmentTekenScanner(), "Takenscanner");
        mAdapter.addFragment(Step1Fragment.newInstance("Profiel"), "Profiel");
        mAdapter.addFragment(Step1Fragment.newInstance("Teek melden"), "Teek melden");
        mAdapter.addFragment(new FragmentOnderzoek(), "Advies");
        mAdapter.addFragment(Step1Fragment.newInstance("Product"), "Product");

        mViewPager.setAdapter(mAdapter);
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
    public void onChildFragmentDisplayed(String title) {
        setTitle(null);
        setAppTitle(title);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == POSITION_ONDERZOEK) {
            FragmentOnderzoek fragment = (FragmentOnderzoek) mAdapter.getItem(POSITION_ONDERZOEK);

            FragmentAnimalTips fragmentAnimalTips;
            Fragment fragment1;

            int i = 0;
            do{
                fragmentAnimalTips = (FragmentAnimalTips) fragment.getChildFragmentManager().getFragments().get(i++);
                fragment1 = fragmentAnimalTips.getChildFragmentManager().findFragmentById(R.id.fragment_container);
            }while(fragment1 == null && i < fragment.getChildFragmentManager().getFragments().size());

            popupFragment(fragment1);
            setAppTitle(mAdapter.getPageTitle(mViewPager.getCurrentItem()));

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(fragment.hasChild());
            }
        } else {
            super.onBackPressed();
        }
    }

    private void popupFragment(Fragment fragment) {
        List<Fragment> childFragment = getChildFragments(fragment);
        if (childFragment == null || childFragment.size() == 0) {
            fragment.getFragmentManager().beginTransaction().remove(fragment).commitNow();
        } else {
            popupFragment(childFragment.get(childFragment.size() - 1));
        }
    }


    public static List<Fragment> getChildFragments(Fragment fragment) {
        FragmentManager fm = fragment.getChildFragmentManager();
        @SuppressWarnings("RestrictedApi")
        List<Fragment> childFragments = fm.getFragments();

        List<Fragment> nonNullFragments = new ArrayList<Fragment>();

        if (childFragments == null) {
            return nonNullFragments;
        }

        for (int i = 0; i < childFragments.size(); i++) {
            Fragment childFragment = childFragments.get(i);
            if (childFragment != null) {
                nonNullFragments.add(childFragment);
            }
        }

        return nonNullFragments;
    }

    private class Adapter extends FragmentStatePagerAdapter {

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
