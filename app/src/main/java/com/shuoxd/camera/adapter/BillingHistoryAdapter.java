package com.shuoxd.camera.adapter;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shuoxd.camera.R;
import com.shuoxd.camera.bean.HistoryBean;

import java.util.List;


public class BillingHistoryAdapter extends BaseQuickAdapter<HistoryBean, BaseViewHolder> {
    public BillingHistoryAdapter(int layoutResId, @Nullable List<HistoryBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, HistoryBean item) {

        helper.setText(R.id.tv_content,item.getDescription());
        helper.setText(R.id.tv_date,item.getTime());
        helper.setText(R.id.tv_cost,"$"+item.getAmount());


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
