package com.shuoxd.camera.utils;

import android.app.Activity;

import androidx.core.content.ContextCompat;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.shuoxd.camera.R;
import com.shuoxd.camera.app.App;

import java.util.List;

public class PickViewUtils {


    /**
     * 弹出滚动选择器
     *
     * @param data     数据源
     * @param title    选择器标题
     */
    public static void showPickView(final Activity context,
                                    final List<String> data,
                                    final List<String> data1,
                                    final List<String> data2,
                                    int option1,
                                    int option2,
                                    int option3,
                                    OnOptionsSelectListener listener,
                                    String title) {
        OptionsPickerView<String> pvOptions = new OptionsPickerBuilder(context,listener)
                .setTitleText(title)
                .setCancelText(App.getInstance().getString(R.string.m127_cancel))//取消按钮文字
                .setSubmitText(App.getInstance().getString(R.string.m152_ok))//确认按钮文字
                .setTitleBgColor(ContextCompat.getColor(context,R.color.white))
                .setTitleColor(ContextCompat.getColor(context,R.color.color_app_main))
                .setSubmitColor(ContextCompat.getColor(context,R.color.color_app_main))
                .setCancelColor(ContextCompat.getColor(context,R.color.color_app_main))
                .setBgColor(ContextCompat.getColor(context,R.color.white))
                .setTitleSize(18)
                .setTextXOffset(5,5,5)
                .setLabels("hour","min","sec")
                .isCenterLabel(true)
                .setTextColorCenter(ContextCompat.getColor(context,R.color.color_app_main))
                .setSelectOptions(option1,option2,option3)
                .build();
        pvOptions.setNPicker(data,data1,data2);
        pvOptions.show();
    }


}
