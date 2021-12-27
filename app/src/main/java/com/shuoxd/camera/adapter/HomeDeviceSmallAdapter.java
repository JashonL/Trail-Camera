package com.shuoxd.camera.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import com.shuoxd.camera.utils.GlideUtils;

import org.w3c.dom.Text;

import java.util.List;

public class HomeDeviceSmallAdapter extends BaseQuickAdapter<CameraBean, BaseViewHolder> {
    public HomeDeviceSmallAdapter(int layoutResId, @Nullable List<CameraBean> data) {
        super(layoutResId, data);
    }

    public HomeDeviceSmallAdapter(@Nullable List<CameraBean> data) {
        super(data);
    }

    public HomeDeviceSmallAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CameraBean item) {
        //相机实体类
        CameraBean.CameraInfo camera = item.getCamera();
        //别名
        String alias = camera.getAlias();
        String imei = camera.getImei();
        String signalStrength = camera.getSignalStrength();
        String batteryLevel = camera.getBatteryLevel();
        String cardSpace = camera.getCardSpace();

        TextView tvName = helper.getView(R.id.tv_name);
        if (TextUtils.isEmpty(alias)) {
            alias = imei;
        }
        if (TextUtils.isEmpty(alias)) {
            alias = "";
        }
        tvName.setText(alias);


        TextView tvWifi = helper.getView(R.id.tv_wifi);
        int wifiStrength = Integer.parseInt(signalStrength);
        if (wifiStrength == 0) {
            setTextViewDrawableTop(mContext, tvWifi, R.drawable.wifi0);
        } else if (wifiStrength <= 25) {
            setTextViewDrawableTop(mContext, tvWifi, R.drawable.wifi1);
        } else if (wifiStrength <= 50) {
            setTextViewDrawableTop(mContext, tvWifi, R.drawable.wifi1);
        } else if (wifiStrength <= 75) {
            setTextViewDrawableTop(mContext, tvWifi, R.drawable.wifi3);
        } else {
            setTextViewDrawableTop(mContext, tvWifi, R.drawable.wifi4);
        }


        TextView tvBattery = helper.getView(R.id.tv_battery);
        int batteryL = Integer.parseInt(batteryLevel);
        if (batteryL == 0) {
            setTextViewDrawableTop(mContext, tvBattery, R.drawable.battery1);
        } else if (wifiStrength <= 25) {
            setTextViewDrawableTop(mContext, tvBattery, R.drawable.battery2);
        } else if (wifiStrength <= 50) {
            setTextViewDrawableTop(mContext, tvBattery, R.drawable.battery2);
        } else if (wifiStrength <= 75) {
            setTextViewDrawableTop(mContext, tvBattery, R.drawable.battery4);
        } else {
            setTextViewDrawableTop(mContext, tvBattery, R.drawable.battery_4);
        }


        TextView tvSdcard = helper.getView(R.id.tv_sdcard);
        int sSpace = Integer.parseInt(cardSpace);
        if (sSpace == 0) {
            setTextViewDrawableTop(mContext, tvSdcard, R.drawable.sdcard1);
        } else if (wifiStrength <= 25) {
            setTextViewDrawableTop(mContext, tvSdcard, R.drawable.sdcard1);
        } else if (wifiStrength <= 50) {
            setTextViewDrawableTop(mContext, tvSdcard, R.drawable.sdcard3);
        } else if (wifiStrength <= 75) {
            setTextViewDrawableTop(mContext, tvSdcard, R.drawable.sdcard4);
        } else {
            setTextViewDrawableTop(mContext, tvSdcard, R.drawable.sdcard4);
        }


        String noReadPhotoNum = item.getNoReadPhotoNum();
        TextView navDot = helper.getView(R.id.nav_tv_dot);

        if (!TextUtils.isEmpty(noReadPhotoNum)) {
            if ("0".equals(noReadPhotoNum)) {

                navDot.setVisibility(View.GONE);
            } else {
                navDot.setText(noReadPhotoNum);
            }
        }


        //相机的最后一张图片
        CameraBean.LastPhoto lastPhoto = item.getLastPhoto();
        String path = lastPhoto.getPath();
        ImageView ivPic=helper.getView(R.id.iv_camera);
        if (!TextUtils.isEmpty(path)){
            GlideUtils.getInstance().showImageContext(mContext, R.drawable.kaola, R.drawable.kaola, path, ivPic);
        }

        //类型 图片类型(0:缩略图;1:高清图;2:视频)
        TextView tvHD = helper.getView(R.id.tv_hd);
        String type = lastPhoto.getType();
        if (!"1".equals(type)){
            tvHD.setVisibility(View.GONE);
        }else {
            tvHD.setVisibility(View.VISIBLE);
        }

        String amPm = lastPhoto.getAmPm();
        String uploadTime = lastPhoto.getUploadTime();
        String uploadDate = lastPhoto.getUploadDate();
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


    public void setTextViewDrawableTop(Context context, TextView textView, int drawId) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = context.getResources().getDrawable(drawId, null);
        } else {
            drawable = context.getResources().getDrawable(drawId);
        }
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textView.setCompoundDrawables(null, drawable, null, null);
        }
    }
}