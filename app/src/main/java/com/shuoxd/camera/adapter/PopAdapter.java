package com.shuoxd.camera.adapter;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shuoxd.camera.R;
import com.shuoxd.camera.bean.CameraBean;

import java.util.Collection;
import java.util.List;

public class PopAdapter extends BaseQuickAdapter<String, BaseViewHolder> {



    public PopAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);

    }



    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {

        helper.setText(R.id.tv_name,item);


    }




}
