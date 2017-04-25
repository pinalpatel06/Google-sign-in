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
import tekkan.synappz.com.tekkan.fragment.ProfileFragment;
import tekkan.synappz.com.tekkan.fragment.Step1Fragment;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

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

        mAdapter.addFragment(Step1Fragment.newInstance("Takenscanner"));
        mAdapter.addFragment(new ProfileFragment());
        mAdapter.addFragment(Step1Fragment.newInstance("Teek melden"));
        mAdapter.addFragment(Step1Fragment.newInstance("Onderzoek"));
        mAdapter.addFragment(Step1Fragment.newInstance("Product"));

        mViewPager.setAdapter(mAdapter);
        mTabLayout.addOnTabSelectedListener(this);
        updateUI();
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
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

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
