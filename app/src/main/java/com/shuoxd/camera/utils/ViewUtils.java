package com.shuoxd.camera.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.TextView;

public class ViewUtils {

    public static void setTextViewDrawableTop(Context context, TextView textView, int drawId) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = context.getResources().getDrawable(drawId, null);
        } else {
            drawable = context.getResources().getDrawable(drawId);
        }
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textView.setCompoundDrawables(null, drawable, null, null);
        }
    }
}
