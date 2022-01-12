package com.shuoxd.camera.utils;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.view.WheelView;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.view.listener.OnCreateBodyViewListener;
import com.mylhyl.circledialog.view.listener.OnInputClickListener;
import com.mylhyl.circledialog.view.listener.OnLvItemClickListener;
import com.shuoxd.camera.R;

import java.util.List;

public class CircleDialogUtils {
    /**
     * 公共自定义弹框
     *
     * @return
     */
    public static DialogFragment showCommentBodyDialog(View bodyView, FragmentManager fragmentManager, OnCreateBodyViewListener listener) {
        DialogFragment bulbBodyDialog = new CircleDialog.Builder()
                .setBodyView(bodyView, listener)
                .setGravity(Gravity.BOTTOM)
                .setYoff(20)
                .show(fragmentManager);
        ;
        return bulbBodyDialog;
    }





    /**
     * 配网失败
     *
     * @return
     */
    public static DialogFragment showFailConfig(View bodyView, FragmentManager fragmentManager, OnCreateBodyViewListener listener) {
        DialogFragment failConfigDialog = new CircleDialog.Builder()
                .setBodyView(bodyView, listener)
                .setGravity(Gravity.CENTER)
                .show(fragmentManager);
        ;
        return failConfigDialog;
    }













    /**
     * 公共复选框
     *
     * @param activity
     * @return
     */
    public static DialogFragment showCommentItemDialog(FragmentActivity activity, String title, List<String> items, int gravity, OnLvItemClickListener listener) {
        DialogFragment itemsSelectDialog = new CircleDialog.Builder()
                .setTitle(title)
                .configTitle(params -> {
                    params.styleText = Typeface.BOLD;
                })
                .setItems(items, listener)
                .configItems(params -> {
                    params.dividerHeight = 0;
                    params.textColor = ContextCompat.getColor(activity, R.color.color_text_33);
                })
                .setGravity(gravity)
                .show(activity.getSupportFragmentManager());

        return itemsSelectDialog;
    }


    /**
     * 公共复选框,没有标题
     *
     * @param activity
     * @return
     */
    public static DialogFragment showCommentItemDialog(FragmentActivity activity, List<String> items, int gravity, OnLvItemClickListener listener) {
        DialogFragment itemsSelectDialog = new CircleDialog.Builder()
                .configTitle(params -> {
                    params.styleText = Typeface.BOLD;
                })
                .setItems(items, listener)
                .configItems(params -> {
                    params.dividerHeight = 0;
                    params.textColor = ContextCompat.getColor(activity, R.color.color_text_33);
                })
                .setGravity(gravity)
                .show(activity.getSupportFragmentManager());

        return itemsSelectDialog;
    }







    /**
     * 公共自定义框
     *
     * @return
     */
    public static DialogFragment showCostomBodyViewDialog(Context context, View bodyView, String title, FragmentManager fragmentManager, OnCreateBodyViewListener listener,
                                                          String negative, View.OnClickListener negativeListner, String positive , View.OnClickListener positiveListner) {

        CircleDialog.Builder builder = new CircleDialog.Builder();

        if (!TextUtils.isEmpty(title)){
            builder.setTitle(title);
        }
        builder.setGravity(Gravity.CENTER);
        builder.setBodyView(bodyView,listener);
        if (negativeListner!=null){
            builder.setNegative(negative,negativeListner);
        }
        if (positiveListner!=null){
            builder.setPositive(positive,positiveListner);
        }

        return builder.show(fragmentManager);
    }


    /**
     * 公共自定义框
     *
     * @return
     */
    public static DialogFragment showCostomBodyViewDialog(Context context, View bodyView, String title, FragmentManager fragmentManager, OnCreateBodyViewListener listener,
                                                          String negative, View.OnClickListener negativeListner, String positive , View.OnClickListener positiveListner, boolean isCancel) {

        CircleDialog.Builder builder = new CircleDialog.Builder();

        if (!TextUtils.isEmpty(title)){
            builder.setTitle(title);
        }
        builder.setGravity(Gravity.CENTER);
        builder.setBodyView(bodyView,listener);
        if (negativeListner!=null){
            builder.setNegative(negative,negativeListner);
        }
        if (positiveListner!=null){
            builder.setPositive(positive,positiveListner);
        }

        builder.setCanceledOnTouchOutside(isCancel);
        builder.setCancelable(isCancel);
        return builder.show(fragmentManager);
    }








    public interface timeSelectedListener {
        void cancle();
        void ok(boolean status, int hour, int min);
    }

}
