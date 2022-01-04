package com.shuoxd.camera.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shuoxd.camera.R;
import com.shuoxd.camera.bean.PhaseBean;
import com.shuoxd.camera.bean.SetBean;

import java.util.Collection;
import java.util.List;

public class PhaseAdapter extends BaseQuickAdapter<PhaseBean, BaseViewHolder> {

    private int nowSelectPosition=-1;

    public PhaseAdapter(int layoutResId, @Nullable List<PhaseBean> data) {
        super(layoutResId, data);
    }



    @Override
    protected void convert(@NonNull BaseViewHolder helper, PhaseBean item) {
        int index = item.getIndex();
        boolean selected = item.isSelected();
        helper.setImageResource(R.id.iv_icon,selected?item.getResSelected():item.getResNormal());
    }


    public int getNowSelectPosition() {
        return nowSelectPosition;
    }



    public void setNowSelectPosition(int position) {
        if (position >= getItemCount()) return;
        //去除其他item选择
        try {
            //不相等时才去除之前选中item以及赋值，防止重复操作
            if (this.nowSelectPosition != position) {
                if (this.nowSelectPosition >=0 && this.nowSelectPosition < getItemCount()) {
                    PhaseBean itemPre = getItem(nowSelectPosition);
                    assert itemPre != null;
                    itemPre.setSelected(false);
                }
                this.nowSelectPosition = position;
                PhaseBean itemNow = getItem(nowSelectPosition);
                //只有没被选中才刷新数据
                if (itemNow==null)return;
                if (!itemNow.isSelected()) {
                    itemNow.setSelected(true);
                    notifyDataSetChanged();
                }
            }else {
                PhaseBean itemNow = getItem(position);
                if (itemNow==null)return;
                itemNow.setSelected(false);
                nowSelectPosition=-1;
                notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void replaceData(@NonNull Collection<? extends PhaseBean> data) {
        super.replaceData(data);
        int nowPos = -1;
        if (nowSelectPosition >= 0 && nowSelectPosition < data.size()){
            nowPos = nowSelectPosition;
        }
        setNowSelectPosition(nowPos);
    }

}
