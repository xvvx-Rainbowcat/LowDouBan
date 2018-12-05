package com.example.administrator.myapplication.Util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MyScrollView extends NestedScrollView {
    private boolean isIntercept = false;
    private int downX;
    private int downY;

    public MyScrollView (@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent (MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                int dx = Math.abs(x - downX);
                int dy = Math.abs(y - downY);
                if (dx < dy) {    //如果竖直
                    int childHeight = this.getChildAt(0).getHeight();
                    if (childHeight != this.getHeight() + this.getScrollY()) {  //如果未至底部
                        //全拦截
                        return true;
                    }
                }
            }
            break;
        }
        downX = x;
        downY = y;
        return super.onInterceptTouchEvent(ev);
    }
}
