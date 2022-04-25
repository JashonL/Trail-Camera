package com.shuoxd.camera.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shuoxd.camera.R;
import com.shuoxd.camera.bean.PictureBean;
import com.shuoxd.camera.bean.PlansBean;
import com.shuoxd.camera.utils.GlideUtils;

import java.util.List;

public class PlansListAdapter extends BaseQuickAdapter<PlansBean, BaseViewHolder> {
    public PlansListAdapter(int layoutResId, @Nullable List<PlansBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, PlansBean item) {
    }
}
