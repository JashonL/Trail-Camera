package com.shuoxd.camera.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shuoxd.camera.bean.CameraBean;

import java.util.List;

public class HomeDeviceBigAdapter extends BaseQuickAdapter<CameraBean, BaseViewHolder> {
    public HomeDeviceBigAdapter(int layoutResId, @Nullable List<CameraBean> data) {
        super(layoutResId, data);
    }

    public HomeDeviceBigAdapter(@Nullable List<CameraBean> data) {
        super(data);
    }

    public HomeDeviceBigAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CameraBean item) {

    }
}
