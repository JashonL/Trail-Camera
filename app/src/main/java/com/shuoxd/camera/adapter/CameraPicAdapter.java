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
import com.shuoxd.camera.bean.PictureBean;
import com.shuoxd.camera.utils.GlideUtils;

import java.util.List;

public class CameraPicAdapter extends BaseQuickAdapter<PictureBean, BaseViewHolder> {
    public CameraPicAdapter(int layoutResId, @Nullable List<PictureBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, PictureBean item) {
        //相机的最后一张图片
        String path = item.getFullPath();
        ImageView ivPic=helper.getView(R.id.iv_camera);
        if (!TextUtils.isEmpty(path)){
            GlideUtils.getInstance().showImageContext(mContext, R.drawable.default_pic, R.drawable.default_pic, path, ivPic);
        }

        //类型 图片类型(0:缩略图;1:高清图;2:视频)
        TextView tvHD = helper.getView(R.id.tv_hd);
        String type = item.getType();
        if (!"1".equals(type)){
            tvHD.setVisibility(View.GONE);
        }else {
            tvHD.setVisibility(View.VISIBLE);
        }

        String amPm = item.getAmPm();
        String uploadTime = item.getUploadTime();
        String uploadDate = item.getUploadDate();
        TextView tvTime=helper.getView(R.id.tv_time);
        TextView tvDate=helper.getView(R.id.tv_date);
        TextView tvTemp=helper.getView(R.id.tv_temp);

        if ("0".equals(amPm)){
            tvTime.setText(uploadTime+"AM");
        }else {
            tvTime.setText(uploadTime+"PM");
        }

        tvDate.setText(uploadDate);
    }
}
