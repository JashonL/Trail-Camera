package com.shuoxd.camera.adapter;

import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shuoxd.camera.R;
import com.shuoxd.camera.bean.AddImageBean;
import com.shuoxd.camera.bean.PictureBean;
import com.shuoxd.camera.utils.GlideUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CameraPicVedeoAdapter extends BaseMultiItemQuickAdapter<PictureBean, BaseViewHolder> {


    public static final int HD_PIC_FLAG = 1;
    public static final int HD_PIC_FLAG_EDIT = 2;
    public static final int HD_PIC_FLAG_VIDEO = 3;
    public static final int HD_PIC_FLAG_VIDEO_EDIT = 4;


    private SelectedListener listener;


    private List<String> selectedImeis = new ArrayList<>();


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public CameraPicVedeoAdapter(List<PictureBean> data, SelectedListener listener) {
        super(data);
        addItemType(HD_PIC_FLAG, R.layout.item_camera_pic);
        addItemType(HD_PIC_FLAG_EDIT, R.layout.item_camera_pic_edit);
        addItemType(HD_PIC_FLAG_VIDEO, R.layout.item_camera_video);
        addItemType(HD_PIC_FLAG_VIDEO_EDIT, R.layout.item_camera_video_edit);
        this.listener = listener;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PictureBean item) {
        //相机的最后一张图片
        String path = item.getFullPath();
        String type = item.getType();
        String amPm = item.getAmPm();
        String uploadTime = item.getUploadTime();
        String uploadDate = item.getUploadDate();
        boolean checked = item.isChecked();


        int itemType = item.getItemType();
        switch (itemType) {
            case HD_PIC_FLAG_VIDEO:
            case HD_PIC_FLAG:
                ImageView ivPic = helper.getView(R.id.iv_camera);
                if (!TextUtils.isEmpty(path)) {
                    GlideUtils.getInstance().showImageContext(mContext, R.drawable.kaola, R.drawable.kaola, path, ivPic);
                }
                //类型 图片类型(0:缩略图;1:高清图;2:视频)
                TextView tvHD = helper.getView(R.id.tv_hd);
                if (!"1".equals(type)) {
                    tvHD.setVisibility(View.GONE);
                } else {
                    tvHD.setVisibility(View.VISIBLE);
                }
                TextView tvTime = helper.getView(R.id.tv_time);
                TextView tvDate = helper.getView(R.id.tv_date);
                if ("0".equals(amPm)) {
                    tvTime.setText(uploadTime + "AM");
                } else {
                    tvTime.setText(uploadTime + "PM");
                }
                tvDate.setText(uploadDate);
                break;

            case HD_PIC_FLAG_VIDEO_EDIT:
            case HD_PIC_FLAG_EDIT:

                ImageView ivPic1 = helper.getView(R.id.iv_camera);
                if (!TextUtils.isEmpty(path)) {
                    GlideUtils.getInstance().showImageContext(mContext, R.drawable.kaola, R.drawable.kaola, path, ivPic1);
                }
                //类型 图片类型(0:缩略图;1:高清图;2:视频)
                TextView tvHD1 = helper.getView(R.id.tv_hd);
                if (!"1".equals(type)) {
                    tvHD1.setVisibility(View.GONE);
                } else {
                    tvHD1.setVisibility(View.VISIBLE);
                }
                TextView tvTime1 = helper.getView(R.id.tv_time);
                TextView tvDate1 = helper.getView(R.id.tv_date);
                if ("0".equals(amPm)) {
                    tvTime1.setText(uploadTime + "AM");
                } else {
                    tvTime1.setText(uploadTime + "PM");
                }
                tvDate1.setText(uploadDate);

                helper.setChecked(R.id.cb_checkbox, checked);


                CheckBox checkBox = helper.getView(R.id.cb_checkbox);
                checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
                    if (compoundButton.isPressed()) {
                        item.setChecked(b);
                        toggle(item.getId(), b);
                        listener.selected();

                    }
                });


                break;


        }


    }


    public void toggle(String imei, boolean b) {
        int i = selectedImeis.indexOf(imei);
        if (b) {
            if (i == -1) selectedImeis.add(imei);
        } else {
            if (i != -1) selectedImeis.remove(i);
        }
    }


    public List<String> getSelectedImeis() {
        return selectedImeis;
    }

    public void setSelectedImeis(List<String> selectedImeis) {
        this.selectedImeis = selectedImeis;
    }

    public interface SelectedListener {
        void selected();
    }

}
