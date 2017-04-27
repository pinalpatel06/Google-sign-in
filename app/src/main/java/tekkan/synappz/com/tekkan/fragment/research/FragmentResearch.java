package tekkan.synappz.com.tekkan.fragment.research;

import android.content.Context;
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
import tekkan.synappz.com.tekkan.custom.nestedfragments.ContainerNodeFragment;
import tekkan.synappz.com.tekkan.custom.nestedfragments.FragmentChangeCallback;
import tekkan.synappz.com.tekkan.custom.nestedfragments.NestedFragmentUtil;

/**
 * Created by Tejas Sherdiwala on 4/22/2017.
 * &copy; Knoxpo
 *
 * This fragment will be show on tab click
 * This contains the tab and viewpager.
 */

public class FragmentResearch extends ContainerNodeFragment implements FragmentAnimalTips.Callback {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    private Adapter mAdapter;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_onderzoek, container, false);
        init(v);

        mAdapter.addFragment(
                FragmentAnimalTips.newInstance(FragmentAnimalTips.ANIMAL_DOG),
                getString(R.string.text_onderzoek_tab1)
        );
        mAdapter.addFragment(
                FragmentAnimalTips.newInstance(FragmentAnimalTips.ANIMAL_CAT),
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


    @Override
    public String getTitle() {
        return NestedFragmentUtil.getTitle(getChildFragmentManager(), getString(R.string.title_research), getContainerId());
    }

    @Override
    public int getContainerId() {
        return R.id.fragment_container;
    }

    @Override
    public FragmentChangeCallback getChangeCallback() {
        return mCallback;
    }

    @Override
    public void onListItemClicked(int type, Bundle details) {
        switch (type){
            case FragmentAnimalTips.TYPE_PET:
                setChild(new FragmentResearchOutcome());
                break;
            case FragmentAnimalTips.TYPE_TIPS:
                setChild(new FragmentAnimalTipsDetails());
                break;
        }
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
