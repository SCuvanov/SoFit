package com.scuvanov.sofit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Sean on 3/8/2015.
 */
public class CustomListView extends ListView {
    OnBottomReachedListener mListener;
    BottomNotReachedListener nListener;


    public CustomListView(Context context) {
        super(context);
    }

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        ListView view = (ListView) getChildAt(getChildCount() - 1);
        int diff = (view.getBottom() - (getHeight() + getScrollY()));

        if (diff == 0 && mListener != null) {
            mListener.onBottomReached();

        } else {
            nListener.BottomNotReached();
        }

        super.onScrollChanged(l, t, oldl, oldt);
    }


    // Getters & Setters

    public void setOnBottomReachedListener(
            OnBottomReachedListener onBottomReachedListener) {
        mListener = onBottomReachedListener;

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
