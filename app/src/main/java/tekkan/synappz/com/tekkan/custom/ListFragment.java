package tekkan.synappz.com.tekkan.custom;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import tekkan.synappz.com.tekkan.R;

/**
 * Created by Tejas Sherdiwala on 4/24/2017.
 * &copy; Knoxpo
 */

public abstract class ListFragment<T, VH extends RecyclerView.ViewHolder> extends Fragment {

    public abstract List<T> onCreateItems(Bundle savedInstanceState);

    public abstract int getItemLayoutId(int viewType);

    public abstract VH onCreateViewHolder(View v, int viewType);

    public abstract void onBindViewHolder(VH holder, T item);

    private RecyclerView mRecyclerView;
    private FrameLayout mEmptyFL;
    private Adapter mAdapter;
    private List<T> mItems;
    ProgressBar mProgressBar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(getViewLayoutId(), container, false);
        init(v);

        mItems = onCreateItems(savedInstanceState);

        mRecyclerView.setLayoutManager(getLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        View emptyView = getEmptyView(LayoutInflater.from(getActivity()),mEmptyFL);
        if(emptyView == null){
            TextView tv = new TextView(getActivity());
            mEmptyFL.addView(tv);
            ViewGroup.LayoutParams params = tv.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            tv.setLayoutParams(params);
            tv.setText(getEmptyString());
        }else{
            mEmptyFL.addView(emptyView);
        }

        updateUI();

        return v;
    }

    private void init(View v) {
        mRecyclerView = (RecyclerView) v.findViewById(getRecyclerViewId());
        mEmptyFL = (FrameLayout) v.findViewById(R.id.fl_empty);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progress);
        mAdapter = new Adapter();
    }

    private void updateUI() {
        mRecyclerView.setVisibility(
                (mItems == null || mItems.size() == 0)
                        ? View.GONE
                        : View.VISIBLE
        );

        mEmptyFL.setVisibility(
                (mItems == null || mItems.size() == 0)
                        ? View.VISIBLE
                        : View.GONE
        );

        mProgressBar.setVisibility(
                (mItems == null || mItems.size() == 0 )
                ? View.VISIBLE
                : View.GONE
        );
    }

    public void updateEmptyView(){
        mEmptyFL.setVisibility(
                (mItems == null || mItems.size() == 0)
                        ? View.VISIBLE
                        : View.GONE
        );
    }

    private int getRecyclerViewId() {
        return R.id.rv_list;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public Adapter getAdapter() {
        return mAdapter;
    }

    protected int getViewLayoutId(){
        return R.layout.fragment_list;
    }

    protected RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }

    protected int getItemViewType(int position) {
        return 0;
    }

    protected View getEmptyView(LayoutInflater inflater,ViewGroup parent){
        return null;
    }

    protected int getEmptyString(){
        return R.string.no_items;
    }

    protected ProgressBar getProgressBar(){
        return mProgressBar;
    }

    public final void loadNewItems(List<T> items) {
        mItems.clear();
        if(items != null){
            mItems.addAll(items);
        }
        mAdapter.notifyDataSetChanged();
        updateUI();
    }

    public void notifyDatasetChanged(){
        mAdapter.notifyDataSetChanged();
    }

    public final void appendNewItems(List<T> items) {
        mItems.addAll(items);
        mAdapter.notifyItemRangeInserted(
                mItems.size() - items.size(),
                items.size()
        );
        updateUI();
    }

    public final void removeItem(int position){
        mItems.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    public final int getItemCount(){
        return mAdapter.getItemCount();
    }

    public final List<T> getItems(){
        return mItems;
    }

    private class Adapter extends RecyclerView.Adapter<VH> {

        private LayoutInflater mLayoutInflater;

        public Adapter() {
            mLayoutInflater = LayoutInflater.from(getActivity());
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = mLayoutInflater.inflate(getItemLayoutId(viewType), parent, false);
            return ListFragment.this.onCreateViewHolder(v , viewType);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            ListFragment.this.onBindViewHolder(holder, mItems.get(position));
        }

        @Override
        public int getItemViewType(int position) {
            return ListFragment.this.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }
}
