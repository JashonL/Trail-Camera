package com.shuoxd.camera.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shuoxd.camera.R;
import com.shuoxd.camera.bean.InfoHeadBean;

import java.util.List;


public class CameraInfoAdapter extends BaseMultiItemQuickAdapter<InfoHeadBean, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public CameraInfoAdapter(List<InfoHeadBean> data) {
        super(data);
        addItemType(0, R.layout.item_camera_info);
        addItemType(1, R.layout.item_camera_sync_time);


    }
 /*   public CameraInfoAdapter(int layoutResId, @Nullable List<InfoHeadBean> data) {
        super(layoutResId, data);
    }*/


    @Override
    protected void convert(@NonNull BaseViewHolder helper, InfoHeadBean item) {

        int itemType = item.getItemType();
        int index = item.getIndex();
        if (itemType == 0) {
            helper.setText(R.id.tv_info, item.getTitle());
            helper.setImageResource(R.id.iv_icon, item.getIconRes());
            if (index<4){
                helper.setBackgroundColor(R.id.ll_group, ContextCompat.getColor(mContext,R.color.camera_info_color1));
            }else  {
                helper.setBackgroundColor(R.id.ll_group, ContextCompat.getColor(mContext,R.color.camera_info_color2));
            }

        } else {
            String sync=item.getTitle();
            helper.setText(R.id.tv_last_sync,sync);
        }

    }


}
