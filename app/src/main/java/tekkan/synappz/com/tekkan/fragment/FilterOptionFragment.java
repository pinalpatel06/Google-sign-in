package tekkan.synappz.com.tekkan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tekkan.synappz.com.tekkan.R;
import tekkan.synappz.com.tekkan.activity.MoreInfoActivity;

/**
 * Created by Tejas Sherdiwala on 4/21/2017.
 * &copy; Knoxpo
 */

public class FilterOptionFragment extends Fragment {

    private static final String TAG = FilterOptionFragment.class.getSimpleName();

    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_more_info)
    TextView mMoreInfoTV;

    private Adapter mAdapter;
    private ArrayList<FilterOptionItem> mFilterOptionItems;


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile_setup,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_close:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        init(v);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        setHasOptionsMenu(true);
        return v;
    }

    private void init(View v){
        ButterKnife.bind(this,v);
        mAdapter = new Adapter();
        mFilterOptionItems = new ArrayList<>();
        onCreateItems();
    }

    private void onCreateItems(){
        mFilterOptionItems.add(new FilterOptionItem(
            R.drawable.bg_filter_option_1,
                getString(R.string.filter_option_1),
                false
        ));
        mFilterOptionItems.add(new FilterOptionItem(
                R.drawable.bg_filter_option_2,
                getString(R.string.filter_option_2),
                false
        ));

        mFilterOptionItems.add(new FilterOptionItem(
                R.drawable.bg_filter_option_3,
                getString(R.string.filter_option_3),
                false
        ));

        mFilterOptionItems.add(new FilterOptionItem(
                R.drawable.bg_filter_option_4,
                getString(R.string.filter_option_4),
                false
        ));
    }

    @OnClick(R.id.tv_more_info)
    public void onClickMoreInfo(){
        Intent intent = new Intent(getActivity(), MoreInfoActivity.class);
        startActivity(intent);
    }

    private class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private LayoutInflater mLayoutInflater;

        Adapter() {
            mLayoutInflater = LayoutInflater.from(getActivity());
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = mLayoutInflater.inflate(R.layout.item_filter, parent, false);
            return new FilterOptionVH(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((FilterOptionVH)holder).bind(mFilterOptionItems.get(position));
        }

        @Override
        public int getItemCount() {
            return mFilterOptionItems.size();
        }
    }

    public class FilterOptionVH extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_filter_color)
        ImageView mFilterColorIV;
        @BindView(R.id.tv_filter_text)
        TextView mFilterTitleTV;
        @BindView(R.id.sw_filter_status)
        Switch mIsFilterOn;

        FilterOptionVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(FilterOptionItem item) {
            mFilterColorIV.setImageDrawable(getActivity().getDrawable(item.getColorResId()));
            mFilterTitleTV.setText(item.getFilterText());
            mIsFilterOn.setChecked(item.isFilterOn());
        }
    }

    public class FilterOptionItem {
        int mColorResId;
        String mFilterText;
        boolean mIsFilterOn;


        public FilterOptionItem(int colorResId, String filterText, boolean isFilterOn) {
            mColorResId = colorResId;
            mFilterText = filterText;
            mIsFilterOn = isFilterOn;
        }

        int getColorResId() {
            return mColorResId;
        }

        String getFilterText() {
            return mFilterText;
        }

        boolean isFilterOn() {
            return mIsFilterOn;
        }
    }
}
