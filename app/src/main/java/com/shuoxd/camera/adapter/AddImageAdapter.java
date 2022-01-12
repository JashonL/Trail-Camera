package com.shuoxd.camera.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.MultipleItemRvAdapter;
import com.shuoxd.camera.R;
import com.shuoxd.camera.bean.AddImageBean;
import com.shuoxd.camera.utils.GlideUtils;

import java.util.List;

public class AddImageAdapter extends BaseMultiItemQuickAdapter<AddImageBean, BaseViewHolder> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public AddImageAdapter(List<AddImageBean> data) {
        super(data);
        addItemType(0, R.layout.item_image_subqs);//正常图片
        addItemType(1, R.layout.item_image_add);//添加图片
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, AddImageBean item) {
        int itemType = item.getItemType();
        if (itemType==0){
            ImageView icPic = helper.getView(R.id.iv_pic);
            String path = item.getPath();
            GlideUtils.getInstance().showImageContext(mContext, R.drawable.kaola, R.drawable.kaola, path, icPic);
            helper.addOnClickListener(R.id.iv_delete);
        }else {

        }
    }
}
