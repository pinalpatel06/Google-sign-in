package tekkan.synappz.com.tekkan.fragment;

import android.content.Intent;
import android.graphics.Rect;
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
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.activity.FilterOptionActivity;

/**
 * Created by Tejas Sherdiwala on 4/20/2017.
 * &copy; Knoxpo
 */

public class HeatMapFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {
    private static final String TAG = HeatMapFragment.class.getSimpleName();
    SimpleDateFormat DATE_FORMAT
            = new SimpleDateFormat("MM dd yyyy", Locale.ENGLISH);

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.tv_title_filter)
    TextView mBottomSheetTitle;
    @BindView(R.id.gl_bottom_sheet)
    GridLayout mBottomSheetGL;
    @BindView(R.id.seek_bar)
    SeekBar mSeekBar;
    @BindView(R.id.tv_year_left)
    TextView mCurrentYearTV;
    @BindView(R.id.tv_year_right)
    TextView mNextYearTV;
    @BindView(R.id.tv_seekbar_value)
    TextView mSeekValueTV;
    @BindView(R.id.tv_month_1)
    TextView mMonth1TV;
    @BindView(R.id.tv_month_2)
    TextView mMonth2TV;
    @BindView(R.id.tv_month_3)
    TextView mMonth3TV;
    @BindView(R.id.tv_month_4)
    TextView mMonth4TV;
    @BindView(R.id.tv_month_5)
    TextView mMonth5TV;
    @BindView(R.id.tv_month_6)
    TextView mMonth6TV;
    @BindView(R.id.tv_month_7)
    TextView mMonth7TV;
    @BindView(R.id.tv_month_8)
    TextView mMonth8TV;
    @BindView(R.id.tv_month_9)
    TextView mMonth9TV;
    @BindView(R.id.tv_month_10)
    TextView mMonth10TV;
    @BindView(R.id.tv_month_11)
    TextView mMonth11TV;
    @BindView(R.id.tv_month_12)
    TextView mMonth12TV;
    private String[] mMonthName;
    private BottomSheetBehavior mBottomSheetBehavior;
    private int mScreenMinValue, mScreenMaxValue;

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
        mSeekBar.setOnSeekBarChangeListener(this);
        ViewTreeObserver vto = mBottomSheetTitle.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mBottomSheetTitle.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mBottomSheetBehavior.setPeekHeight(mBottomSheetTitle.getHeight());
                mScreenMaxValue =  mBottomSheetTitle.getWidth();
                mScreenMinValue = (int) mBottomSheetTitle.getX();
            }
        });
        updateUI();
        setHasOptionsMenu(true);
        return v;
    }

    private void init(View v) {
        ButterKnife.bind(this, v);
        mSeekBar.setMax(calcNoOfWeek());
        mMonthName = getResources().getStringArray(R.array.month_name);
    }

    private void updateUI() {
        Calendar c = Calendar.getInstance();
        mCurrentYearTV.setText(String.valueOf(c.get(Calendar.YEAR)));
        mSeekValueTV.setText(DATE_FORMAT.format(c.getTime()));
        mMonth1TV.setText(mMonthName[c.get(Calendar.MONTH)]);
        c.add(Calendar.MONTH, 1);
        mMonth2TV.setText(mMonthName[c.get(Calendar.MONTH)]);
        c.add(Calendar.MONTH, 1);
        mMonth3TV.setText(mMonthName[c.get(Calendar.MONTH)]);
        c.add(Calendar.MONTH, 1);
        mMonth4TV.setText(mMonthName[c.get(Calendar.MONTH)]);
        c.add(Calendar.MONTH, 1);
        mMonth5TV.setText(mMonthName[c.get(Calendar.MONTH)]);
        c.add(Calendar.MONTH, 1);
        mMonth6TV.setText(mMonthName[c.get(Calendar.MONTH)]);
        c.add(Calendar.MONTH, 1);
        mMonth7TV.setText(mMonthName[c.get(Calendar.MONTH)]);
        c.add(Calendar.MONTH, 1);
        mMonth8TV.setText(mMonthName[c.get(Calendar.MONTH)]);
        c.add(Calendar.MONTH, 1);
        mMonth9TV.setText(mMonthName[c.get(Calendar.MONTH)]);
        c.add(Calendar.MONTH, 1);
        mMonth10TV.setText(mMonthName[c.get(Calendar.MONTH)]);
        c.add(Calendar.MONTH, 1);
        mMonth11TV.setText(mMonthName[c.get(Calendar.MONTH)]);
        c.add(Calendar.MONTH, 1);
        mMonth12TV.setText(mMonthName[c.get(Calendar.MONTH)]);
        mNextYearTV.setText(String.valueOf(c.get(Calendar.YEAR)));
    }

    private int calcNoOfWeek() {

        Calendar cal = Calendar.getInstance();

        int weeksInYear = cal.getWeeksInWeekYear();
        int currentWeekNo = cal.get(Calendar.WEEK_OF_YEAR);
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
        int nextYearWeek = cal.get(Calendar.WEEK_OF_YEAR);
        return weeksInYear - currentWeekNo + nextYearWeek;
    }

    @OnClick(R.id.tv_title_filter)
    public void showBottomSheet() {
        if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_WEEK_IN_MONTH, progress);
        mSeekValueTV.setText(DATE_FORMAT.format(c.getTime()));
        Rect rect = mSeekBar.getThumb().getBounds();
        int x = rect.left - mSeekValueTV.getWidth() / 5;
        if (x >= mScreenMinValue && x < (mScreenMaxValue - (mSeekValueTV.getWidth()))) {
            mSeekValueTV.setX((x));
        }
        mSeekValueTV.invalidate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
