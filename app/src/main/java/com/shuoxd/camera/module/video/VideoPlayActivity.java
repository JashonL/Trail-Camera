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


    private String fullPath;


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


        jzVideo.startPreloading(); //开始预加载，加载完等待播放
        jzVideo.startVideoAfterPreloading(); //如果预加载完会开始播放，如果未加载则开始加载


        //直接使用系统的下载管理器。是不是非常方便
        if ("1".equals(collection)) {
            ViewUtils.setTextViewDrawableTop(this, tvCollec, R.drawable.collected);
        } else {
            ViewUtils.setTextViewDrawableTop(this, tvCollec, R.drawable.collection);
        }


        String name = fullPath.substring(fullPath.lastIndexOf("/") + 1);
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


    @OnClick({R.id.tv_collec, R.id.tv_delete, R.id.tv_share, R.id.btn_download})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_collec: {
                String operationValue = "1".equals(collection) ? "0" : "1";
                presenter.operation(id, "favorites", operationValue);
            }
            break;
            case R.id.tv_delete: {
                presenter.operation(id, "remove", "1");
            }
            break;
            case R.id.tv_share:
                String parentPath = getExternalFilesDir(null).getPath() + "/video/";
                File fileDir = new File(parentPath);
                if (!fileDir.exists()) {
                    //不存在
                    fileDir.mkdirs();
                }

                String name = fullPath.substring(fullPath.lastIndexOf("/") + 1);
                String filePath = parentPath + "/" + name;
                File file = new File(filePath);
                if (file.exists()) {//已经存在 直接分享
                    //获取File的Uri
                    IntentUtils.shareFile(VideoPlayActivity.this, file, IntentUtils.TYPE_VIDEO, getString(R.string.m217_video_sharing));
                } else {//不存在就下载
                    Uri videoContentUri = Uri.fromFile(file);
                    DownLoadUtils.builder()
                            .setContext(this)
                            .setUrl(fullPath)
                            .setFileName(name)
                            .setFileUri(videoContentUri)
                            .setFileUri(parentPath)
                            .setLister(uri -> {
                                IntentUtils.shareVideoByUri(VideoPlayActivity.this, uri, IntentUtils.TYPE_VIDEO, getString(R.string.m217_video_sharing));
                            })
                            .download();
                }
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
