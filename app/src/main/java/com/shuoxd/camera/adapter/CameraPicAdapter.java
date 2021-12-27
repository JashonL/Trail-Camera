package com.shuoxd.camera.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shuoxd.camera.bean.CameraBean;

import java.util.List;

public class CameraPicAdapter extends BaseQuickAdapter<CameraBean, BaseViewHolder> {
    public CameraPicAdapter(int layoutResId, @Nullable List<CameraBean> data) {
        super(layoutResId, data);
    }

    public CameraPicAdapter(@Nullable List<CameraBean> data) {
        super(data);
    }

    public CameraPicAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CameraBean item) {

    }
}
