package com.shuoxd.camera.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shuoxd.camera.R;
import com.shuoxd.camera.bean.CameraBean;

import java.util.List;

public class CameraNameAdapter extends BaseQuickAdapter<CameraBean, BaseViewHolder> {
    public CameraNameAdapter(int layoutResId, @Nullable List<CameraBean> data) {
        super(layoutResId, data);
    }



    @Override
    protected void convert(@NonNull BaseViewHolder helper, CameraBean item) {
        CameraBean.CameraInfo camera = item.getCamera();
        String imei = camera.getImei();
        helper.setText(R.id.tv_name,imei);
    }
}
