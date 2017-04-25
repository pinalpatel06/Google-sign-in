package tekkan.synappz.com.tekkan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.activity.FilterOptionActivity;

/**
 * Created by Tejas Sherdiwala on 4/20/2017.
 * &copy; Knoxpo
 */

public class FragmentTekenScanner extends Fragment {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.tv_title_filter)
    TextView mBottomSheetTitle;
    @BindView(R.id.gl_bottom_sheet)
    GridLayout mBottomSheetGL;
    private BottomSheetBehavior mBottomSheetBehavior;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_teken_scanner, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                Intent intent = new Intent(getActivity(), FilterOptionActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_teken_scanner, container, false);
        init(v);

        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheetGL);

        ViewTreeObserver vto = mBottomSheetTitle.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mBottomSheetTitle.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mBottomSheetBehavior.setPeekHeight(mBottomSheetTitle.getHeight());
            }
        });

        setHasOptionsMenu(true);

        return v;
    }

    private void init(View v) {
        ButterKnife.bind(this, v);
    }

    @OnClick(R.id.tv_title_filter)
    public void showBottomSheet() {
        if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }
}
