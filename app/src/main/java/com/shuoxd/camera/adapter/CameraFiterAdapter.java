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

public class CameraFiterAdapter extends BaseQuickAdapter<CameraBean, BaseViewHolder> {

    private int nowSelectPosition=-1;

    public CameraFiterAdapter(int layoutResId, @Nullable List<CameraBean> data) {
        super(layoutResId, data);
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                boolean selected = data.get(i).isSelected();
                if (selected){
                    nowSelectPosition=i;
                    break;
                }
            }
        }
    }



    @Override
    protected void convert(@NonNull BaseViewHolder helper, CameraBean item) {

        CameraBean.CameraInfo camera = item.getCamera();
        String alias = camera.getAlias();
        String imei = camera.getImei();
        if (TextUtils.isEmpty(alias)){
            alias=imei;
        }
        helper.setText(R.id.tv_name,alias);

        String totalPhotoNum = item.getTotalPhotoNum()+"p";
        helper.setText(R.id.tv_num,totalPhotoNum);

        boolean selected = item.isSelected();
        if (selected){
            helper.setBackgroundColor(R.id.background, ContextCompat.getColor(mContext,R.color.color_app_main));
            helper.setTextColor(R.id.tv_name,ContextCompat.getColor(mContext,R.color.white));
            helper.setTextColor(R.id.tv_num,ContextCompat.getColor(mContext,R.color.white));

        }else {
            helper.setBackgroundColor(R.id.background, ContextCompat.getColor(mContext,R.color.white));
            helper.setTextColor(R.id.tv_name,ContextCompat.getColor(mContext,R.color.color_text_33));
            helper.setTextColor(R.id.tv_num,ContextCompat.getColor(mContext,R.color.color_text_99));
        }


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
                    CameraBean itemPre = getItem(nowSelectPosition);
                    assert itemPre != null;
                    itemPre.setSelected(false);
                }
                this.nowSelectPosition = position;
                CameraBean itemNow = getItem(nowSelectPosition);
                //只有没被选中才刷新数据
                if (itemNow==null)return;
                if (!itemNow.isSelected()) {
                    itemNow.setSelected(true);
                    notifyDataSetChanged();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void replaceData(@NonNull Collection<? extends CameraBean> data) {
        super.replaceData(data);
        int nowPos = -1;
        if (nowSelectPosition >= 0 && nowSelectPosition < data.size()){
            nowPos = nowSelectPosition;
        }
        setNowSelectPosition(nowPos);
    }


}
