package com.shuoxd.camera.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shuoxd.camera.R;
import com.shuoxd.camera.bean.AddImageBean;
import com.shuoxd.camera.bean.PlansInfoBean;
import com.shuoxd.camera.utils.GlideUtils;

import java.util.List;

public class PlansInfoAdapter extends BaseMultiItemQuickAdapter<PlansInfoBean, BaseViewHolder> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public PlansInfoAdapter(List<PlansInfoBean> data) {
        super(data);
        addItemType(0, R.layout.item_plan_info);//正常图片
        addItemType(1, R.layout.item_table_layout);//添加图片
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PlansInfoBean item) {
        int itemType = item.getItemType();
        if (itemType==0){
            helper.setText(R.id.tv_title,item.getTitle());
            helper.setText(R.id.tv_value,item.getValue());
        }else {
            helper.setText(R.id.tv_used_photo,item.getUsedPhoto());
            helper.setText(R.id.tv_used_hd,item.getUsedHDPhoto());
            helper.setText(R.id.tv_used_video,item.getUsedVideo());
            helper.setText(R.id.tv_package_photo,item.getPackagePhoto());
            helper.setText(R.id.tv_package_hd,item.getPackageHDPhoto());
            helper.setText(R.id.tv_package_video,item.getPackageVideo());
        }
    }
}
