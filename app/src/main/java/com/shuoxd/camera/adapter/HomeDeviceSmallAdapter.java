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

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.MultipleItemRvAdapter;
import com.shuoxd.camera.R;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.utils.GlideUtils;

import org.w3c.dom.Text;

import java.util.List;

public class HomeDeviceSmallAdapter extends BaseMultiItemQuickAdapter<CameraBean, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public HomeDeviceSmallAdapter(List<CameraBean> data) {
        super(data);
        addItemType(0,R.layout.item_camera);
        addItemType(1,R.layout.item_camera_gray);


    }
 /*   public HomeDeviceSmallAdapter(int layoutResId, @Nullable List<CameraBean> data) {
        super(layoutResId, data);
    }*/


    @Override
    protected void convert(@NonNull BaseViewHolder helper, CameraBean item) {

        int itemType = item.getItemType();


        //相机实体类
        CameraBean.CameraInfo camera = item.getCamera();
        //别名
        String alias = camera.getAlias();
        String imei = camera.getImei();
        String signalStrength = camera.getSignalStrength();
        String batteryLevel = camera.getBatteryLevel();
        String cardSpace = camera.getCardSpace();
        String extDcLevel = camera.getExtDcLevel();



        TextView tvName = helper.getView(R.id.tv_name);
        if (TextUtils.isEmpty(alias)) {
            alias = imei;
        }
        if (TextUtils.isEmpty(alias)) {
            alias = "";
        }
        tvName.setText(alias);


        if (itemType==0){
            TextView tvWifi = helper.getView(R.id.tv_wifi);
            int wifiStrength = Integer.parseInt(signalStrength);
            String sWiff = mContext.getString(R.string.m70_Signal);



            ImageView ivWifi = helper.getView(R.id.iv_wifi);
            if (wifiStrength == 0) {
//            setTextViewDrawableTop(mContext, tvWifi, R.drawable.signal1);
                ivWifi.setImageResource(R.drawable.signal1);
            } else if (wifiStrength <= 25) {
//            setTextViewDrawableTop(mContext, tvWifi, R.drawable.signal1);
                ivWifi.setImageResource(R.drawable.signal1);
            } else if (wifiStrength <= 50) {
//            setTextViewDrawableTop(mContext, tvWifi, R.drawable.signal2);
                ivWifi.setImageResource(R.drawable.signal2);
            } else if (wifiStrength <= 75) {
//            setTextViewDrawableTop(mContext, tvWifi, R.drawable.signal3);
                ivWifi.setImageResource(R.drawable.signal3);
            } else {
//            setTextViewDrawableTop(mContext, tvWifi, R.drawable.signal4);
                ivWifi.setImageResource(R.drawable.signal4);
            }

            tvWifi.setText(sWiff);


            TextView tvExt = helper.getView(R.id.tv_ext);
            ImageView ivExt = helper.getView(R.id.iv_ext);
            int extDcl = Integer.parseInt(extDcLevel);
            if (extDcl == 0) {
//            setTextViewDrawableTop(mContext, tvExt, R.drawable.ext1);
                ivExt.setImageResource(R.drawable.ext0);
            } else if (extDcl <= 25) {
//            setTextViewDrawableTop(mContext, tvExt, R.drawable.ext1);
                ivExt.setImageResource(R.drawable.ext1);
            } else if (extDcl <= 50) {
//            setTextViewDrawableTop(mContext, tvExt, R.drawable.ext2);
                ivExt.setImageResource(R.drawable.ext2);
            } else if (extDcl <= 75) {
//            setTextViewDrawableTop(mContext, tvExt, R.drawable.ext3);
                ivExt.setImageResource(R.drawable.ext3);
            } else {
//            setTextViewDrawableTop(mContext, tvExt, R.drawable.ext4);
                ivExt.setImageResource(R.drawable.ext4);
            }

            tvExt.setText(extDcl + "%");







            TextView tvBattery = helper.getView(R.id.tv_battery);
            ImageView ivBattery = helper.getView(R.id.iv_battery);
            int batteryL = Integer.parseInt(batteryLevel);
            if (batteryL == 0) {
//            setTextViewDrawableTop(mContext, tvBattery, R.drawable.bat1);
                ivBattery.setImageResource(R.drawable.bat0);
            } else if (batteryL <= 25) {
//            setTextViewDrawableTop(mContext, tvBattery, R.drawable.bat1);
                ivBattery.setImageResource(R.drawable.bat1);
            } else if (batteryL <= 50) {
//            setTextViewDrawableTop(mContext, tvBattery, R.drawable.bat2);
                ivBattery.setImageResource(R.drawable.bat2);
            } else if (batteryL <= 75) {
//            setTextViewDrawableTop(mContext, tvBattery, R.drawable.bat3);
                ivBattery.setImageResource(R.drawable.bat3);
            } else {
//            setTextViewDrawableTop(mContext, tvBattery, R.drawable.bat4);
                ivBattery.setImageResource(R.drawable.bat4);
            }

            tvBattery.setText(batteryL + "%");


            TextView tvSdcard = helper.getView(R.id.tv_sdcard);
            ImageView ivsdcard = helper.getView(R.id.iv_sdcard);
            int sSpace = Integer.parseInt(cardSpace);
            if (sSpace == 0) {
//            setTextViewDrawableTop(mContext, tvSdcard, R.drawable.sdcard1);
                ivsdcard.setImageResource(R.drawable.sdcard0);
            } else if (sSpace <= 19) {
//            setTextViewDrawableTop(mContext, tvSdcard, R.drawable.sdcard1);
                ivsdcard.setImageResource(R.drawable.sdcard1);
            } else if (sSpace <= 49) {
//            setTextViewDrawableTop(mContext, tvSdcard, R.drawable.sdcard2);
                ivsdcard.setImageResource(R.drawable.sdcard2);
            } else if (sSpace <= 69) {
//            setTextViewDrawableTop(mContext, tvSdcard, R.drawable.sdcard3);
                ivsdcard.setImageResource(R.drawable.sdcard3);
            } else if (sSpace <= 94){
                ivsdcard.setImageResource(R.drawable.sdcard3);
            }else {
//            setTextViewDrawableTop(mContext, tvSdcard, R.drawable.sdcard4);
                ivsdcard.setImageResource(R.drawable.sdcard5);
            }

            tvSdcard.setText(sSpace + "%");


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
            String path = lastPhoto.getFullPath();
            String fullVideoImgPath = lastPhoto.getFullVideoImgPath();




            //类型 图片类型(0:缩略图;1:高清图;2:视频)
            TextView tvHD = helper.getView(R.id.tv_hd);
            String type = lastPhoto.getType();
            if (!"1".equals(type)) {
                tvHD.setVisibility(View.GONE);
            } else {
                tvHD.setVisibility(View.VISIBLE);
            }



            ImageView ivPic = helper.getView(R.id.iv_camera);
            ImageView ivVideoPlay = helper.getView(R.id.iv_video_play);
            if (!"2".equals(type)) {
                ivVideoPlay.setVisibility(View.GONE);

                if (!TextUtils.isEmpty(path)) {
                    GlideUtils.getInstance().showImageContext(mContext, R.drawable.default_pic, R.drawable.default_pic, path, ivPic);
                }

            } else {
                ivVideoPlay.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(fullVideoImgPath)) {
                    fullVideoImgPath=path;
                }
                GlideUtils.getInstance().showImageContext(mContext, R.drawable.default_pic, R.drawable.default_pic, fullVideoImgPath, ivPic);
            }



            String amPm = lastPhoto.getAmPm();
            String uploadTime = lastPhoto.getUploadTime();
            String uploadDate = lastPhoto.getUploadDate();
            TextView tvTime = helper.getView(R.id.tv_time);
            TextView tvDate = helper.getView(R.id.tv_date);
            TextView tvTemp = helper.getView(R.id.tv_temp);

            if ("0".equals(amPm)) {
                tvTime.setText(uploadTime + "AM");
            } else {
                tvTime.setText(uploadTime + "PM");
            }

            tvDate.setText(uploadDate);
        }else {
            String s=mContext.getString(R.string.m298_please_activate)+"\n"+
                    mContext.getString(R.string.m285_electrify_instructions)+"\n"+
                    mContext.getString(R.string.m286_manual_uploading);
            helper.setText(R.id.tv_mask,s);
        }

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
