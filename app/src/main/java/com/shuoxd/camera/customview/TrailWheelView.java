package com.shuoxd.camera.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.contrarywind.view.WheelView;

public class TrailWheelView extends WheelView {
    public TrailWheelView(Context context) {
        super(context);
    }

    public TrailWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onTouchEvent(event);
    }
}
