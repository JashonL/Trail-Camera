package com.shuoxd.camera.module.video;


import android.view.View;

import androidx.annotation.NonNull;

import com.shuoxd.camera.R;
import com.shuoxd.camera.base.BaseActivity;

import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class VideoPlayActivity extends BaseActivity<ViDeoPlayPresenter> implements VideoPlayView {

    JzvdStd jzVideo;


    @Override
    protected ViDeoPlayPresenter createPresenter() {
        return new ViDeoPlayPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_play;
    }

    @Override
    protected void initViews() {
       /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("标题");*/

        jzVideo = findViewById(R.id.jz_video);



    }

    @Override
    protected void initData() {
        String fullPath = getIntent().getStringExtra("fullPath");
        jzVideo.setUp(fullPath, ""
                , JzvdStd.SCREEN_NORMAL);
      /*  Glide.with(this)
                .load(UrlsKt.getThumbnails()[26])
                .into(jzVideo.posterImageView);*/
        // The Point IS 或者这样写也可以
//        jzVideo.videoRotation = 180;
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }


    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_ADAPTER);
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }



    public void clickRotationTo0(View view) {
        Jzvd.setTextureViewRotation(0);
    }

    public void clickRotationTo90(View view) {
        Jzvd.setTextureViewRotation(90);
    }

    public void clickRotationTo180(View view) {
        Jzvd.setTextureViewRotation(180);
    }

    public void clickRotationTo270(View view) {
        Jzvd.setTextureViewRotation(270);
    }

    public void clickVideoImageDiaplayAdapter(View view) {
        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_ADAPTER);
    }

    public void clickVideoImageDisplayFillParent(View view) {
        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT);
    }

    public void clickVideoImageDisplayFillCrop(View view) {
        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);
    }

    public void clickVideoImageDiaplayOriginal(View view) {
        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_ORIGINAL);
    }


}
