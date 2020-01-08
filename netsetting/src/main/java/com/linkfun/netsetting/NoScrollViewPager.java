package com.linkfun.netsetting;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;



/**
 * 不可以滑动，但是可以setCurrentItem的ViewPager。
 */
public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        getParent().getParent().requestDisallowInterceptTouchEvent(true);

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return false;
    }
    @Override
    public boolean canScrollHorizontally(int direction) {
        return false;
    }
}
