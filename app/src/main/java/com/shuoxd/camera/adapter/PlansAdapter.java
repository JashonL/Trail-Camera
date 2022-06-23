package com.shuoxd.camera.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shuoxd.camera.R;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.bean.ProgramBean;

import java.util.List;

public class PlansAdapter extends BaseMultiItemQuickAdapter<ProgramBean, BaseViewHolder> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public PlansAdapter(List<ProgramBean> data) {
        super(data);
        addItemType(0, R.layout.item_plans_param_title);//标题
        addItemType(1, R.layout.item_plans_param_value);//添加图片
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ProgramBean item) {
        int itemType = item.getItemType();
        if (itemType == 0) {
            helper.setText(R.id.tv_cost, item.getCosts());
            helper.setText(R.id.tv_photos, item.getPhotos());
            helper.setText(R.id.tv_videcprview, item.getVidecPreviews());
            helper.setText(R.id.tv_data, item.getDatas());
        } else {
            helper.setText(R.id.tv_cost, "$"+item.getCost());

            String photoCount = item.getPhotoCount();
            String videoCount = item.getVideoCount();
            String hdCount = item.getHqCount();

            if ("-1".equals(photoCount)){
                photoCount="unlimit";
            }
            if ("-1".equals(videoCount)){
                videoCount="unlimit";
            }

            if ("-1".equals(hdCount)){
                hdCount="unlimit";
            }
            helper.setText(R.id.tv_photos, photoCount);
            helper.setText(R.id.tv_videcprview, videoCount);
            helper.setText(R.id.tv_data, hdCount);


            helper.setChecked(R.id.cb_select, "1".equals(item.getSelected()));


        }
    }


    public void setNowSelectPosition(int position) {
        if (position >= getItemCount()) return;
        try {
            ProgramBean item = getItem(position);
            if (item != null) {
                int itemCount = getItemCount();
                List<ProgramBean> data = getData();
                for (int i = 0; i < itemCount; i++) {
                    ProgramBean cameraBean = data.get(i);
                    if (position != i) {
                        cameraBean.setSelected("0");
                    } else {
                        cameraBean.setSelected("1");
                    }
                }
                notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void clearSelected() {
        List<ProgramBean> data = getData();
        for (ProgramBean bean :
                data) {
            bean.setSelected("0");
        }
        notifyDataSetChanged();
    }


}
