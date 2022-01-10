package com.shuoxd.camera.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shuoxd.camera.R;
import com.shuoxd.camera.bean.MessageBean;

import java.util.List;


public class QuestionAdapter extends BaseQuickAdapter<MessageBean, BaseViewHolder> {
    public QuestionAdapter(int layoutResId, @Nullable List<MessageBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, MessageBean item) {
        MessageBean.MessageInfo info = item.getInfo();
        if (info!=null){
            String title = info.getTitle();
            String content = info.getContent();
            String createTime = info.getCreateTime();

            helper.setText(R.id.tv_title,title);
            helper.setText(R.id.tv_content,content);
            helper.setText(R.id.tv_time,createTime);

        }



        MessageBean.UserInfo user = item.getUser();
        if (user!=null){

        }

    }



}
