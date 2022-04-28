package com.shuoxd.camera.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shuoxd.camera.R;
import com.shuoxd.camera.bean.QuestionBean;
import com.shuoxd.camera.bean.SupportBean;

import java.util.List;


public class SupportAdapter extends BaseQuickAdapter<SupportBean, BaseViewHolder> {
    public SupportAdapter(int layoutResId, @Nullable List<SupportBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, SupportBean item) {

        helper.setText(R.id.tv_question, item.getSystemKey());

        helper.setText(R.id.tv_answer, item.getSystemValue());


        boolean expand = item.isExpand();
        View view = helper.getView(R.id.cl_answer);
        view.setVisibility(expand?View.VISIBLE:View.GONE);

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
