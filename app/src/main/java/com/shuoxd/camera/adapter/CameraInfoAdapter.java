package com.shuoxd.camera.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.bean.InfoHeadBean;

import java.util.List;

public class CameraInfoAdapter extends BaseQuickAdapter<InfoHeadBean, BaseViewHolder> {
    public CameraInfoAdapter(int layoutResId, @Nullable List<InfoHeadBean> data) {
        super(layoutResId, data);
    }

    public CameraInfoAdapter(@Nullable List<InfoHeadBean> data) {
        super(data);
    }

    public CameraInfoAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, InfoHeadBean item) {

    }
}
