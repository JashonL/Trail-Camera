package com.shuoxd.camera.adapter;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shuoxd.camera.R;
import com.shuoxd.camera.bean.HistoryBean;
import com.shuoxd.camera.bean.ProgramBean;
import com.shuoxd.camera.bean.YearBean;

import org.w3c.dom.Text;

import java.util.List;


public class BillingYearAdapter extends BaseQuickAdapter<YearBean, BaseViewHolder> {

    private int selected_index;

    public BillingYearAdapter(int layoutResId, @Nullable List<YearBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, YearBean item) {

        TextView tvYear = helper.getView(R.id.tv_year);
        tvYear.setText(item.getYear());
        boolean ischecked = item.isIschecked();
        setQuestionStatus(tvYear,ischecked);

    }

    /**
     * 显示状态
     */
    private void setQuestionStatus(TextView tvYear,boolean ischecked) {
        if (ischecked){
            tvYear.setTextColor(ContextCompat.getColor(mContext,R.color.color_bill_chart));
            tvYear.setBackgroundResource(R.drawable.shape_blue_billchart);
        }else {
            tvYear.setTextColor(ContextCompat.getColor(mContext,R.color.gray_d2d2d));
            tvYear.setBackgroundResource(R.drawable.shape_white_stroke);
        }

    }



    public void setNowSelectPosition(int position) {
        if (position >= getItemCount()) return;
        try {
            YearBean item = getItem(position);
            if (item != null) {
                int itemCount = getItemCount();
                List<YearBean> data = getData();
                for (int i = 0; i < itemCount; i++) {
                    YearBean cameraBean = data.get(i);
                    cameraBean.setIschecked(position == i);
                }
                selected_index=position;
                notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public int getSelected_index() {
        return selected_index;
    }

    public void setSelected_index(int selected_index) {
        this.selected_index = selected_index;
    }
}
