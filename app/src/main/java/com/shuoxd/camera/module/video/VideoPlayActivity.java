package com.shuoxd.camera.module.video;


import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import com.shuoxd.camera.R;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.bean.PictureBean;
import com.shuoxd.camera.utils.CommentUtils;
import com.shuoxd.camera.utils.DownLoadUtils;
import com.shuoxd.camera.utils.IntentUtils;
import com.shuoxd.camera.utils.ViewUtils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class VideoPlayActivity extends BaseActivity<ViDeoPlayPresenter> implements VideoPlayView {


    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_collec)
    TextView tvCollec;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.cl_menu)
    ConstraintLayout clMenu;
    @BindView(R.id.btn_download)
    Button btnDownLoad;


    private JzvdStd jzVideo;

    private String id;
    //是否收藏了
    private String collection;


    private DownloadManager downloadManager;
    private long reference;
    private String fullPath;
    private String filePath;


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

        //初始化toolbar
        initToobar(toolbar);
        tvTitle.setText(R.string.m219_video);

    }


    @Override
    protected void initData() {
        fullPath = getIntent().getStringExtra("fullPath");
        collection = getIntent().getStringExtra("collection");
        id = getIntent().getStringExtra("id");
        String proxyUrl = App.getProxy(this).getProxyUrl(fullPath);


        jzVideo.setUp(proxyUrl, ""
                , JzvdStd.SCREEN_NORMAL);
      /*  Glide.with(this)
                .load(UrlsKt.getThumbnails()[26])
                .into(jzVideo.posterImageView);*/
        // The Point IS 或者这样写也可以
//        jzVideo.videoRotation = 180;
        jzVideo.startPreloading(); //开始预加载，加载完等待播放
        jzVideo.startVideoAfterPreloading(); //如果预加载完会开始播放，如果未加载则开始加载


        //直接使用系统的下载管理器。是不是非常方便
        downloadManager = (DownloadManager) getBaseContext().getSystemService(Context.DOWNLOAD_SERVICE);


        if ("1".equals(collection)) {
            ViewUtils.setTextViewDrawableTop(this, tvCollec, R.drawable.collected);
        } else {
            ViewUtils.setTextViewDrawableTop(this, tvCollec, R.drawable.collection);
        }



        String name = fullPath.substring(fullPath.lastIndexOf("/")+1);
        tvTitle.setText(name);


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


    @OnClick({R.id.tv_collec, R.id.tv_delete, R.id.tv_share, R.id.btn_download})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_collec: {
             /*   List<PictureBean> viewLists = mAdapter.getViewLists();
                int position = vp.getCurrentItem();
                PictureBean pictureBean = viewLists.get(position);
                String id = pictureBean.getId();
                String collection = pictureBean.getCollection();*/

                String operationValue = "1".equals(collection) ? "0" : "1";
                presenter.operation(id, "favorites", operationValue);
            }
            break;
            case R.id.tv_delete: {
                presenter.operation(id, "remove", "1");
            }
            break;
            case R.id.tv_share:


                String parentPath = getExternalFilesDir(null).getPath()+"/video/";
                File fileDir=new File(parentPath);
                if (!fileDir.exists()){
                    //不存在
                    fileDir.mkdirs();
                }

                String name = fullPath.substring(fullPath.lastIndexOf("/")+1);
                String filePath=parentPath+"/"+name;
                File file=new File(filePath);
                if (file.exists()){//已经存在 直接分享
                    //获取File的Uri
                    IntentUtils.shareFile(VideoPlayActivity.this,file,IntentUtils.TYPE_VIDEO,getString(R.string.m217_video_sharing));

                }else {//不存在就下载


                    Uri videoContentUri =Uri.fromFile(file);

                    DownLoadUtils.builder()
                            .setContext(this)
                            .setUrl(fullPath)
                            .setFileName(name)
                            .setFileUri(videoContentUri)
                            .setFileUri(parentPath)
                            .setLister(uri -> {
                                IntentUtils.shareVideoByUri(VideoPlayActivity.this, uri, IntentUtils.TYPE_VIDEO,getString(R.string.m217_video_sharing));
                            })
                            .download();
                }





                /*

                //可以是视频也可以是图片，分享时要填写正确的type类型，在下面我会列出各种类型
                Uri uri = Uri.parse(fullPath);
                DownloadManager.Request request = new DownloadManager.Request(uri);
       *//*         //通知栏的标题
                request.setTitle("视频下载");
                //显示通知栏的说明
                request.setDescription("测试的广告");*//*
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);//不显示通知栏（若不显示就不需要写上面的内容）
                request.setVisibleInDownloadsUi(true);
                //下载到那个文件夹下，以及命名

                String parentPath = App.getInstance().getFilesDir().getPath();
                String name = fullPath.substring(fullPath.lastIndexOf("/")+1);

                filePath = parentPath + "/" + name;

                File file=new File(filePath);

                Uri videoContentUri = FileProvider.getUriForFile(this, CommentUtils.getPackageName(this) + ".fileProvider", file);

                request.setDestinationUri(videoContentUri);




                //下载的唯一标识，可以用这个标识来控制这个下载的任务enqueue（）开始执行这个任务
                reference = downloadManager.enqueue(request);*/


                break;

            case R.id.btn_download:
                break;

        }
    }


    @Override
    public void showCollecMsg(String collection) {
        if ("1".equals(collection)) {
            ViewUtils.setTextViewDrawableTop(this, tvCollec, R.drawable.collected);
        } else {
            ViewUtils.setTextViewDrawableTop(this, tvCollec, R.drawable.collection);
        }
    }

    @Override
    public void delete(String photoId) {
        finish();
    }

    @Override
    public void dowload(String photoId, String msg, String resolution) {

    }


}
