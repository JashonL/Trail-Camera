package com.shuoxd.camera.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shuoxd.camera.R;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.bean.ReplyBean;
import com.shuoxd.camera.customview.LinearDivider;
import com.shuoxd.camera.module.pictrue.BigImageActivty;

import java.util.ArrayList;
import java.util.List;

public class QuestionReplyAdapter extends BaseQuickAdapter<ReplyBean, BaseViewHolder> implements BaseQuickAdapter.OnItemClickListener {
    public QuestionReplyAdapter(int layoutResId, @Nullable List<ReplyBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, ReplyBean item) {
        helper.setText(R.id.tv_username, item.getReplyUsername());
        helper.setText(R.id.tv_date, item.getReplyTime());
        helper.setText(R.id.tv_question_detail, item.getContent());


        RecyclerView rlvPic = helper.getView(R.id.rlv_pic);

        List<String> paths = new ArrayList<>();
        String path1 = item.getAttachmentOnePath();
        String path2 = item.getAttachmentTwoPath();
        String path3 = item.getAttachmentThreePath();
        if (!TextUtils.isEmpty(path1)) {
            paths.add(path1);
        }
        if (!TextUtils.isEmpty(path2)) {
            paths.add(path2);
        }

        if (!TextUtils.isEmpty(path3)) {
            paths.add(path3);
        }

        rlvPic.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        ImageAdapter imageAdapter = new ImageAdapter(R.layout.item_image, paths);
        rlvPic.setAdapter(imageAdapter);
        rlvPic.addItemDecoration(new LinearDivider(mContext, LinearLayoutManager.HORIZONTAL, 20, ContextCompat.getColor(mContext, R.color.nocolor)));
        imageAdapter.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        String s = ((ImageAdapter) adapter).getData().get(position);
        Intent intent = new Intent(mContext, BigImageActivty.class);
        intent.putExtra("path", s);
        mContext.startActivity(intent);
    }
}
