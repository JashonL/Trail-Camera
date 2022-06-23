package com.shuoxd.camera.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shuoxd.camera.R;
import com.shuoxd.camera.bean.AddImageBean;
import com.shuoxd.camera.bean.PlansInfoBean;
import com.shuoxd.camera.utils.GlideUtils;

import java.util.List;

public class PlansInfoAdapter extends BaseMultiItemQuickAdapter<PlansInfoBean, BaseViewHolder> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public PlansInfoAdapter(List<PlansInfoBean> data) {
        super(data);
        addItemType(0, R.layout.item_plan_info);//正常图片
        addItemType(1, R.layout.item_table_layout);//添加图片
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PlansInfoBean item) {
        int itemType = item.getItemType();
        if (itemType == 0) {
            helper.setText(R.id.tv_title, item.getTitle());
            helper.setText(R.id.tv_value, item.getValue());
            TextView tvValue = helper.getView(R.id.tv_value);

            String status = item.getStatus();
            if ("suspend".equalsIgnoreCase(status)) {
                tvValue.setTextColor(ContextCompat.getColor(mContext,R.color.red));
            } else if ("active".equalsIgnoreCase(status)) {
                tvValue.setTextColor(ContextCompat.getColor(mContext,R.color.orangle));
            }else if ("fixed".equalsIgnoreCase(status)){
                tvValue.setTextColor(ContextCompat.getColor(mContext,R.color.color_app_main));
            }
            else {
                tvValue.setTextColor(ContextCompat.getColor(mContext,R.color.color_text_33));
            }
        } else {
            String photo=item.getUsedPhoto()+"/"+item.getPackagePhoto();
            String hqPhoto=item.getUsedHDPhoto()+"/"+item.getPackageHDPhoto();
            String video=item.getUsedVideo()+"/"+item.getPackageVideo();
            helper.setText(R.id.photo, photo);
            helper.setText(R.id.hq_photo, hqPhoto);
            helper.setText(R.id.tv_video, video);
        }
    }
}
