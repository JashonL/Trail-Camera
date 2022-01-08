package com.shuoxd.camera.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shuoxd.camera.R;
import com.shuoxd.camera.bean.InfoHeadBean;

import java.util.List;


public class CameraInfoAdapter extends BaseQuickAdapter<InfoHeadBean, BaseViewHolder> {
    public CameraInfoAdapter(int layoutResId, @Nullable List<InfoHeadBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, InfoHeadBean item) {
        helper.setText(R.id.tv_info,item.getTitle());
       helper.setImageResource(R.id.iv_icon,item.getIconRes());
    }




}
