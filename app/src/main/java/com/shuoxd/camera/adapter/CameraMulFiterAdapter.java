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

public class CameraMulFiterAdapter extends BaseQuickAdapter<CameraBean, BaseViewHolder> {


    public CameraMulFiterAdapter(int layoutResId, @Nullable List<CameraBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, CameraBean item) {

        CameraBean.CameraInfo camera = item.getCamera();
        String alias = camera.getAlias();
        String imei = camera.getImei();
        if (TextUtils.isEmpty(alias)) {
            alias = imei;
        }
        helper.setText(R.id.tv_name, alias);

        String totalPhotoNum = item.getTotalPhotoNum() + "p";
        helper.setText(R.id.tv_num, totalPhotoNum);

        boolean selected = item.isSelected();
        if (selected) {
            helper.setBackgroundColor(R.id.background, ContextCompat.getColor(mContext, R.color.color_app_main));
            helper.setTextColor(R.id.tv_name, ContextCompat.getColor(mContext, R.color.white));
            helper.setTextColor(R.id.tv_num, ContextCompat.getColor(mContext, R.color.white));

        } else {
            helper.setBackgroundColor(R.id.background, ContextCompat.getColor(mContext, R.color.white));
            helper.setTextColor(R.id.tv_name, ContextCompat.getColor(mContext, R.color.color_text_33));
            helper.setTextColor(R.id.tv_num, ContextCompat.getColor(mContext, R.color.color_text_99));
        }


    }


    public void setNowSelectPosition(int position) {
        if (position >= getItemCount()) return;
        try {
            CameraBean item = getItem(position);
            if (item != null) {
//                item.setSelected(true);
                if (position == 0) {
                    item.setSelected(true);
                    //??????????????????????????????
                    int itemCount = getItemCount();
                    List<CameraBean> data = getData();
                    if (itemCount > 0) {
                        for (int i = 1; i < itemCount; i++) {
                            CameraBean cameraBean = data.get(i);
                            cameraBean.setSelected(false);
                        }
                    }
                } else {
                    boolean selected = item.isSelected();
                    getData().get(0).setSelected(false);
                    if (selected){
                        int count=0;
                        int itemCount = getItemCount();
                        List<CameraBean> data = getData();
                        for (int i = 0; i < itemCount; i++) {
                            CameraBean cameraBean = data.get(i);
                            boolean selected1 = cameraBean.isSelected();
                            if (selected1)count++;
                        }
                        if (count>1) item.setSelected(false);
                    }else {
                        item.setSelected(true);
                    }

                }

                notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
