package com.shuoxd.camera.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shuoxd.camera.R;
import com.shuoxd.camera.bean.SetBean;

import java.util.List;

public class MySetAdapter extends BaseQuickAdapter<SetBean, BaseViewHolder> {
    public MySetAdapter(int layoutResId, @Nullable List<SetBean> data) {
        super(layoutResId, data);
    }



    @Override
    protected void convert(@NonNull BaseViewHolder helper, SetBean item) {

        helper.setImageResource(R.id.iv_icon,item.getIconRes());
        helper.setText(R.id.tv_title,item.getTitle());
    }
}
