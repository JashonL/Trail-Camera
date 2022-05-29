package com.shuoxd.camera.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shuoxd.camera.R;
import com.shuoxd.camera.bean.CameraBean;


import java.util.List;

public class PlansListAdapter extends BaseQuickAdapter<CameraBean, BaseViewHolder> {
    public PlansListAdapter(int layoutResId, @Nullable List<CameraBean> data) {
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

        String iccid = camera.getIccid();
        String s = mContext.getString(R.string.m235_imei)+":"+imei;
        s += "\n"+mContext.getString(R.string.m236_iccid)+":";
        if (!TextUtils.isEmpty(iccid)){
            s+=iccid;
        }
        helper.setText(R.id.tv_content,s);

    }
}
