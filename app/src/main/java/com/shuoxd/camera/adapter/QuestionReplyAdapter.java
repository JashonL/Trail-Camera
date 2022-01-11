package com.shuoxd.camera.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.bean.ReplyBean;

import java.util.List;

public class QuestionReplyAdapter extends BaseQuickAdapter<ReplyBean, BaseViewHolder> {
    public QuestionReplyAdapter(int layoutResId, @Nullable List<ReplyBean> data) {
        super(layoutResId, data);
    }

    public QuestionReplyAdapter(@Nullable List<ReplyBean> data) {
        super(data);
    }

    public QuestionReplyAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ReplyBean item) {

    }
}
