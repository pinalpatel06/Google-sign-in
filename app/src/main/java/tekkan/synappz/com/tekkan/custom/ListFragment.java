package tekkan.synappz.com.tekkan.custom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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

        View emptyView = getEmptyView(LayoutInflater.from(getActivity()), mEmptyFL);
        if (emptyView == null) {
            TextView tv = new TextView(getActivity());
            mEmptyFL.addView(tv);
            ViewGroup.LayoutParams params = tv.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            tv.setLayoutParams(params);
            tv.setText(getEmptyString());
        } else {
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
                (mItems == null || mItems.size() == 0)
                        ? View.VISIBLE
                        : View.GONE
        );
    }

    public void updateEmptyView() {
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

    protected int getViewLayoutId() {
        return R.layout.fragment_list;
    }

    protected RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }

    protected int getItemViewType(int position) {
        return 0;
    }

    protected View getEmptyView(LayoutInflater inflater, ViewGroup parent) {
        return null;
    }

    protected int getEmptyString() {
        return R.string.no_items;
    }

    protected ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public final void loadNewItems(List<T> items) {
        mItems.clear();
        if (items != null) {
            mItems.addAll(items);
        }
        mAdapter.notifyDataSetChanged();
        updateUI();
    }

    public void notifyDatasetChanged() {
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

    public final void removeItem(int position) {
        mItems.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    public final int getItemCount() {
        return mAdapter.getItemCount();
    }

    public final List<T> getItems() {
        return mItems;
    }

    protected void onSwipeCompleted(int position, int direction, T item){

    }

    protected boolean canSwipe(int viewType){
        return true;
    }

    protected final void setSwipe(
            final int defaultColor,
            final int leftColorRes,
            final int leftIconRes,
            final int rightColorRes,
            final int rightIconRes) {
        ItemTouchHelper helper
                = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(
                        ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
                ) {

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        onSwipeCompleted(position, direction, mItems.get(position));
                    }

                    @Override
                    public boolean onMove(RecyclerView arg0, RecyclerView.ViewHolder arg1, RecyclerView.ViewHolder arg2) {
                        // TODO Auto-generated method stub
                        return false;
                    }

                    @Override
                    public void onChildDraw(Canvas c,
                                            RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                            float dX, float dY, int actionState,
                                            boolean isCurrentlyActive) {

                        boolean canSwipe = canSwipe(getItemViewType(viewHolder.getAdapterPosition()));

                        if(!canSwipe){
                            super.onChildDraw(c, recyclerView, viewHolder, 0, dY, actionState, false);
                            return;
                        }

                        // Get RecyclerView item from the ViewHolder
                        View itemView = viewHolder.itemView;
                        Paint p = new Paint();
                        Bitmap icon;
                        Resources resources = getActivity().getResources();

                        int recyclerViewWidth = recyclerView.getWidth();


                        if (!isCurrentlyActive && (Math.abs(dX) == recyclerViewWidth || dX == 0)) {
                            c.drawColor(ContextCompat.getColor(getActivity(),defaultColor));
                            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                            return;
                        }

                        if (dX > 0) {
                            //swipe to right
                            try {
                                p.setColor(ContextCompat.getColor(getActivity(), rightColorRes));
                            } catch (Resources.NotFoundException e) {
                                p.setARGB(255, 255, 0, 0);
                            }
                            c.drawRect(
                                    (float) itemView.getLeft(),
                                    (float) itemView.getTop(),
                                    dX,
                                    (float) itemView.getBottom(),
                                    p);

                            if (rightIconRes != 0) {
                                icon = BitmapFactory.decodeResource(
                                        resources, rightIconRes);
                                c.drawBitmap(icon,
                                        (float) itemView.getLeft() + 16,
                                        (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2,
                                        p);
                            }
                        } else {
                            //swipe to left
                            try {
                                p.setColor(ContextCompat.getColor(getActivity(), leftColorRes));
                            } catch (Resources.NotFoundException e) {
                                p.setARGB(255, 0, 0, 255);
                            }
                            c.drawRect(
                                    (float) itemView.getRight() + dX,
                                    (float) itemView.getTop(),
                                    (float) itemView.getRight(),
                                    (float) itemView.getBottom(),
                                    p);

                            if (leftIconRes != 0) {
                                icon = BitmapFactory.decodeResource(
                                        resources, leftIconRes);
                                c.drawBitmap(icon,
                                        (float) itemView.getRight() - 72,
                                        (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2,
                                        p);
                            }
                        }

                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState,
                                isCurrentlyActive);
                    }
                });
        helper.attachToRecyclerView(getRecyclerView());
    }

    private class Adapter extends RecyclerView.Adapter<VH> {

        private LayoutInflater mLayoutInflater;

        public Adapter() {
            mLayoutInflater = LayoutInflater.from(getActivity());
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = mLayoutInflater.inflate(getItemLayoutId(viewType), parent, false);
            return ListFragment.this.onCreateViewHolder(v, viewType);
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
