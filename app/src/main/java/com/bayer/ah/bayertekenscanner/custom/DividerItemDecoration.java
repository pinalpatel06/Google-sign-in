package com.bayer.ah.bayertekenscanner.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.HashSet;

/**
 * Created by Tejas Sherdiwala on 6/29/2017.
 * &copy; Knoxpo
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private Drawable mDivider;
    private Context mContext;
    private HashSet<Integer> mEnabledViewTypes;

    /**
     * Default divider will be used
     */
    public DividerItemDecoration(Context context) {
        mContext = context;
        final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        mDivider = styledAttributes.getDrawable(0);
        styledAttributes.recycle();
    }

    /**
     * Custom divider will be used
     */
    public DividerItemDecoration(Context context, int resId) {
        mContext = context;
        mDivider = ContextCompat.getDrawable(mContext, resId);
    }

  /*  @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();


            mDivider.setBounds(left, top, right, bottom);

            //avoid the redrawing to increase the opacity
            //infact it is like resetting the pixel on canvas
            Paint p = new Paint();
            p.setARGB(255, 255, 255, 255);
            c.drawRect(mDivider.getBounds(), p);

            mDivider.draw(c);
        }
    }*/

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {

        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            if (isDecorated(child, parent)) {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();


                mDivider.setBounds(left, top, right, bottom);

                //avoid the redrawing to increase the opacity
                //infact it is like resetting the pixel on canvas
                Paint p = new Paint();
                p.setARGB(255, 255, 255, 255);
                c.drawRect(mDivider.getBounds(), p);

                mDivider.draw(c);
            }
        }
    }

    public boolean isDecorated(View view, RecyclerView parent) {
        if (mEnabledViewTypes == null || mEnabledViewTypes.isEmpty()) {
            return true;
        }

        RecyclerView.ViewHolder holder = parent.getChildViewHolder(view);
        int viewType = holder.getItemViewType();
        return mEnabledViewTypes.contains(viewType);
    }

    public int getDividerSize() {
        return mDivider.getIntrinsicHeight();
    }

    public void setEnabledViewTypes(int... types) {
        if (types == null || types.length <= 0) {
            return;
        }

        mEnabledViewTypes = new HashSet<>();
        for (int i = 0; i < types.length; i++) {
            mEnabledViewTypes.add(types[i]);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.bottom = getDividerSize();
    }
}
