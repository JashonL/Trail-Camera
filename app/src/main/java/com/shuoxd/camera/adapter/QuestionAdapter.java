package com.shuoxd.camera.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shuoxd.camera.R;
import com.shuoxd.camera.bean.MessageBean;
import com.shuoxd.camera.bean.QuestionBean;

import java.util.List;


public class QuestionAdapter extends BaseQuickAdapter<QuestionBean, BaseViewHolder> {
    public QuestionAdapter(int layoutResId, @Nullable List<QuestionBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, QuestionBean item) {

        helper.setText(R.id.tv_title, item.getTitle());

        helper.setText(R.id.tv_content, item.getCreateTime());


        setQuestionStatus(helper,Integer.parseInt(item.getStatus()));

    }

    /**
     * 显示状态
     */
    private void setQuestionStatus(BaseViewHolder helper,int status) {
        switch (status) {
            case 0://待回复
                helper.setText(R.id.tv_status,R.string.m131_question_wait);
                helper.setTextColor(R.id.tv_status, ContextCompat.getColor(mContext,R.color.red));
                break;

            case 1://已回复
                helper.setText(R.id.tv_status,R.string.m132_question_aleady);
                helper.setTextColor(R.id.tv_status, ContextCompat.getColor(mContext,R.color.orangle));
                break;

            case 2://已解决
                helper.setText(R.id.tv_status,R.string.m133_question_solved);
                helper.setTextColor(R.id.tv_status, ContextCompat.getColor(mContext,R.color.color_text_66));
                break;

        }
    }


}
