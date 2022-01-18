package com.shuoxd.camera.utils;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.view.WheelView;
import com.mylhyl.circledialog.BaseCircleDialog;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.res.drawable.CircleDrawable;
import com.mylhyl.circledialog.res.values.CircleColor;
import com.mylhyl.circledialog.res.values.CircleDimen;
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
                                                          String negative, View.OnClickListener negativeListner, String positive, View.OnClickListener positiveListner) {

        CircleDialog.Builder builder = new CircleDialog.Builder();

        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setGravity(Gravity.CENTER);
        builder.setBodyView(bodyView, listener);
        if (negativeListner != null) {
            builder.setNegative(negative, negativeListner);
        }
        if (positiveListner != null) {
            builder.setPositive(positive, positiveListner);
        }

        return builder.show(fragmentManager);
    }


    /**
     * 公共自定义框
     *
     * @return
     */
    public static DialogFragment showCostomBodyViewDialog(Context context, View bodyView, String title, FragmentManager fragmentManager, OnCreateBodyViewListener listener,
                                                          String negative, View.OnClickListener negativeListner, String positive, View.OnClickListener positiveListner, boolean isCancel) {

        CircleDialog.Builder builder = new CircleDialog.Builder();

        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setGravity(Gravity.CENTER);
        builder.setBodyView(bodyView, listener);
        if (negativeListner != null) {
            builder.setNegative(negative, negativeListner);
        }
        if (positiveListner != null) {
            builder.setPositive(positive, positiveListner);
        }

        builder.setCanceledOnTouchOutside(isCancel);
        builder.setCancelable(isCancel);
        return builder.show(fragmentManager);
    }


    public static void showInputValueDialog(FragmentActivity context, String title, String subTitle,
                                            String hint, String unit, OndialogComfirListener listener) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_input_custom, null, false);
        TextView tvTitle = contentView.findViewById(R.id.tv_title);
        TextView tvSubTtile = contentView.findViewById(R.id.tv_sub_title);
        TextView tvUnit = contentView.findViewById(R.id.tv_unit);
        TextView tvCancel = contentView.findViewById(R.id.tv_button_cancel);
        TextView tvConfirm = contentView.findViewById(R.id.tv_button_confirm);
        TextView etInput = contentView.findViewById(R.id.et_input);
        tvCancel.setText(R.string.m127_cancel);
        tvConfirm.setText(R.string.m152_ok);

        if (!TextUtils.isEmpty(hint)) {
            etInput.setText(hint);
        }

        CircleDialog.Builder builder = new CircleDialog.Builder();
        builder.setWidth(0.75f);
        builder.setMaxHeight(0.8f);
        builder.setBodyView(contentView, view -> {
            CircleDrawable bgCircleDrawable = new CircleDrawable(CircleColor.DIALOG_BACKGROUND
                    , CircleDimen.DIALOG_RADIUS, CircleDimen.DIALOG_RADIUS, CircleDimen.DIALOG_RADIUS, CircleDimen.DIALOG_RADIUS);
            view.setBackground(bgCircleDrawable);
        });
        builder.setGravity(Gravity.CENTER);
        builder.setCancelable(true);
        BaseCircleDialog show = builder.show(context.getSupportFragmentManager());
        tvSubTtile.setText(subTitle);
        tvUnit.setText(unit);
        tvTitle.setText(title);

        tvCancel.setOnClickListener(view1 -> {
            show.dialogDismiss();
        });


        tvConfirm.setOnClickListener(view -> {
            String value = etInput.getText().toString();
            if (TextUtils.isEmpty(value)) {
                MyToastUtils.toast(R.string.m145_content_cannot_empty);
                return;
            }
            show.dialogDismiss();
            listener.comfir(value);
        });
    }


    public static void showTimeValueDialog(FragmentActivity context, String title,
                                           List<String> mins, int select1,
                                           List<String> seconds, int select2,
                                           TimeSelectedListener listener) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_shot_lag, null, false);
        TextView tvTitle = contentView.findViewById(R.id.tv_title);
        TextView tvCancel = contentView.findViewById(R.id.tv_cancel);
        TextView tvSure = contentView.findViewById(R.id.tv_sure);


        WheelView minWheel = contentView.findViewById(R.id.np_choose1);
//        WheelView minWhee2 = contentView.findViewById(R.id.np_choose2);


        //初始化时间选择器
        minWheel.setCyclic(true);
//        minWheel.isCenterLabel(true);
        minWheel.setAdapter(new ArrayWheelAdapter<>(mins));
        minWheel.setCurrentItem(select1);
        minWheel.setTextColorCenter(ContextCompat.getColor(context, R.color.color_text_00));
        minWheel.setItemsVisibleCount(6);
//        minWheel.setLabel("min");

//        minWhee2.setCyclic(true);
//        minWhee2.isCenterLabel(true);
//        minWhee2.setAdapter(new ArrayWheelAdapter<>(seconds));
//        minWhee2.setCurrentItem(select2);
//        minWhee2.setTextColorCenter(ContextCompat.getColor(context, R.color.color_text_00));
//        minWhee2.setItemsVisibleCount(6);
//        minWhee2.setLabel("sec");

        CircleDialog.Builder builder = new CircleDialog.Builder();
        builder.setWidth(0.8f);
        builder.setBodyView(contentView, view -> {
            CircleDrawable bgCircleDrawable = new CircleDrawable(CircleColor.DIALOG_BACKGROUND
                    , CircleDimen.DIALOG_RADIUS, CircleDimen.DIALOG_RADIUS, CircleDimen.DIALOG_RADIUS, CircleDimen.DIALOG_RADIUS);
            view.setBackground(bgCircleDrawable);
        });
        builder.setGravity(Gravity.CENTER);
        builder.setCancelable(true);
        BaseCircleDialog show = builder.show(context.getSupportFragmentManager());

        tvTitle.setText(title);

        tvCancel.setOnClickListener(view1 -> {
            show.dialogDismiss();
        });


        tvSure.setOnClickListener(view -> {
            int currentItem = minWheel.getCurrentItem();
//            int currentItem1 = minWhee2.getCurrentItem();
            listener.selected(currentItem,0);
            show.dialogDismiss();
        });
    }


    public interface OndialogComfirListener {
        void comfir(String value);
    }

    public interface TimeSelectedListener {
       void selected(int min,int second);
    }

}
