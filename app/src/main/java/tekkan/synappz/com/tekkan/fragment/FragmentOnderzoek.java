package tekkan.synappz.com.tekkan.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import tekkan.synappz.com.tekkan.R;

/**
 * Created by Tejas Sherdiwala on 4/22/2017.
 * &copy; Knoxpo
 */

public class FragmentOnderzoek extends Fragment {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    private Adapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_onderzoek, container, false);
        init(v);

        mAdapter.addFragment(
                FragmentAnimalTips.newInstance(getString(R.string.text_onderzoek_tab1)),
                getString(R.string.text_onderzoek_tab1)
        );
        mAdapter.addFragment(
                FragmentAnimalTips.newInstance(getString(R.string.text_onderzoek_tab2)),
                getString(R.string.text_onderzoek_tab2)
        );

        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            TextView customTabTextView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab_item, null);
            //noinspection ConstantConditions
            tab.setCustomView(customTabTextView);
        }

        setCurrentItem(0);
        return v;
    }

    private void init(View v) {
        ButterKnife.bind(this, v);
        mAdapter = new Adapter(getChildFragmentManager());
    }

    private void setCurrentItem(int position) {
        mViewPager.setCurrentItem(position, false);

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