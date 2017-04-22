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

import butterknife.BindView;
import butterknife.ButterKnife;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.fragment.FragmentOnderzoek;
import tekkan.synappz.com.tekkan.fragment.FragmentTekenScanner;
import tekkan.synappz.com.tekkan.fragment.Step1Fragment;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

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

        mAdapter.addFragment(new FragmentTekenScanner(),"Takenscanner");
        mAdapter.addFragment(Step1Fragment.newInstance("Profiel"), "Profiel");
        mAdapter.addFragment(Step1Fragment.newInstance("Teek melden") , "Teek melden");
        mAdapter.addFragment(new FragmentOnderzoek(), "Advies");
        mAdapter.addFragment(Step1Fragment.newInstance("Product"), "Product");

        mViewPager.setAdapter(mAdapter);
        mTabLayout.addOnTabSelectedListener(this);
       // updateUI();
        setCurrentItem(POSITION_TEKENSCANNER);
    }

    private void init() {
        ButterKnife.bind(this);
        mAdapter = new Adapter(getSupportFragmentManager());
    }

    private void updateUI() {
        ((TextView) mToolbar.findViewById(android.R.id.text1)).setText(getString(R.string.main_activity_title));
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
       mViewPager.setCurrentItem(tab.getPosition(),false);
        setTitle(null);
        ((TextView) mToolbar.findViewById(android.R.id.text1)).setText(mAdapter.getPageTitle(tab.getPosition()));
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void setCurrentItem(int position) {
        mViewPager.setCurrentItem(position, false);
        ((TextView) mToolbar.findViewById(android.R.id.text1)).setText(mAdapter.getPageTitle(position));
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
