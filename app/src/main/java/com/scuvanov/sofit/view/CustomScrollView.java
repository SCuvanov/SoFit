package com.scuvanov.sofit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by Sean on 2/15/2015.
 */

/**
 * Triggers a event when scrolling reaches bottom.
 * <p/>
 * <p/>
 * Usage:
 * <p/>
 * scrollView.setOnBottomReachedListener(
 * new CustomScrollView.OnBottomReachedListener() {
 *
 * @Override public void onBottomReached() {
 * // do something
 * }
 * }
 * );
 * <p/>
 * <p/>
 * Include in layout:
 * <p/>
 * <com.scuvanov.sofit.view.CustomScrollView
 * android:layout_width="match_parent"
 * android:layout_height="match_parent" />
 */
public class CustomScrollView extends ScrollView {
    OnBottomReachedListener mListener;
    BottomNotReachedListener nListener;

    public CustomScrollView(Context context, AttributeSet attrs,
                            int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context) {
        super(context);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        View view = (View) getChildAt(getChildCount() - 1);
        int diff = (view.getBottom() - (getHeight() + getScrollY()));

        if (diff == 0 && mListener != null) {
            mListener.onBottomReached();

        } else {
            nListener.BottomNotReached();
        }

        super.onScrollChanged(l, t, oldl, oldt);
    }


    // Getters & Setters

    public OnBottomReachedListener getOnBottomReachedListener() {
        return mListener;
    }

    public void setOnBottomReachedListener(
            OnBottomReachedListener onBottomReachedListener) {
        mListener = onBottomReachedListener;

    }

    public BottomNotReachedListener getBottomNotReachedListener() {
        return nListener;
    }

    public void setBottomNotReachedListener(
            BottomNotReachedListener BottomNotReachedListener) {
        nListener = BottomNotReachedListener;

    }


    /**
     * Event listener.
     */
    public interface OnBottomReachedListener {
        public void onBottomReached();
    }

    public interface BottomNotReachedListener {
        public void BottomNotReached();
    }


}
