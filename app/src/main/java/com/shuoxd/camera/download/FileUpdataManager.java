package com.shuoxd.camera.download;

import android.app.Activity;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;


import org.json.JSONObject;

import java.io.File;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

public class FileUpdataManager {
    private Map<String, String> mParams;
    // 是否忽略默认参数，解决
    private boolean mIgnoreDefParams = false;
    private Activity mActivity;
    private HttpManager mHttpManager;
    private String mUpdateUrl;
    private int mThemeColor;
    private @DrawableRes
    int mTopPic;
    private String mAppKey;
    private FileDownBean mUpdateApp;
    private String mTargetPath;
    private boolean isPost;
    private boolean mHideDialog;
    private boolean mShowIgnoreVersion;
    private boolean mDismissNotificationProgress;
    private boolean mOnlyWifi;
    private String datalogSn;
    //文件下载保存父目录
    private String fileDir;

    private DialogFragment dialogFragment;

    private DialogFragment progressDialog;


    public FileUpdataManager(Builder builder) {
        mActivity = builder.getActivity();
        mHttpManager = builder.getHttpManager();
        mUpdateUrl = builder.getUpdateUrl();

        mThemeColor = builder.getThemeColor();
        mTopPic = builder.getTopPic();

        mIgnoreDefParams = builder.isIgnoreDefParams();
        if (!mIgnoreDefParams) {
            mAppKey = builder.getAppKey();
        }
        mTargetPath = builder.getTargetPath();
        isPost = builder.isPost();
        mParams = builder.getParams();
        mHideDialog = builder.isHideDialog();
        mShowIgnoreVersion = builder.isShowIgnoreVersion();
        mDismissNotificationProgress = builder.isDismissNotificationProgress();
        mOnlyWifi = builder.isOnlyWifi();
        fileDir = builder.getmFileDir();
        datalogSn = builder.getDatalogSn();
    }







    /**
     * 隐藏更新提示框
     */
    public void dissmissDialog() {
        if (dialogFragment != null) {
            dialogFragment.dismiss();
            dialogFragment = null;
        }
    }


    /**
     * 跳转到更新页面
     */
    TextView tvLoadTitle;
    ProgressBar pbProgress;
    TextView tvProgress;


    public void setProgress(float progress, int total, int current) {
        int current_progress = Math.round(progress * 100);

        if (tvLoadTitle != null) {
//            String title = mActivity.getString(R.string.init_install_pack) + "(" + current + "/" + total + ")";
//            tvLoadTitle.setText(title);
        }

        if (pbProgress != null) {
            pbProgress.setProgress(current_progress);
        }


        if (tvProgress != null) {
            tvProgress.setText(current_progress + "%");
        }

    }

    /**
     * 显示下载提示
     */
    public void dissmissDownDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    public void setUpdateApp(FileDownBean mUpdateApp) {
        this.mUpdateApp = mUpdateApp;
    }

    /**
     * 去下载升级包
     */
    public void startDownLoad(FileDownLoadManager.DownloadCallback downloadCallback) {

        if (mUpdateApp == null) {
            throw new NullPointerException("updateApp 不能为空");
        }
        mUpdateApp.setTargetPath(mTargetPath);
        mUpdateApp.setHttpManager(mHttpManager);
        FileDownLoadManager fileDownLoadManager = new FileDownLoadManager(mUpdateApp, mActivity);
        fileDownLoadManager.startDownload(downloadCallback, 0);
        //使用service下载
   /*     DatalogDownloadService.bindService(mActivity.getApplicationContext(), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ((DatalogDownloadService.DownloadBinder) service).start(mUpdateApp, downloadCallback);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        });*/
    }


    public static class Builder {
        //必须有
        private Activity mActivity;
        //必须有
        private HttpManager mHttpManager;
        //必须有
        private String mUpdateUrl;

        private String mFileDir;


        //1，设置按钮，进度条的颜色
        private int mThemeColor = 0;
        //2，顶部的图片
        private
        @DrawableRes
        int mTopPic = 0;
        //3,唯一的appkey
        private String mAppKey;
        //4,apk的下载路径
        private String mTargetPath;
        //5,是否是post请求，默认是get
        private boolean isPost;
        //6,自定义参数
        private Map<String, String> params;
        // 是否忽略默认参数，解决
        private boolean mIgnoreDefParams = false;
        //7,是否隐藏对话框下载进度条
        private boolean mHideDialog = false;
        private boolean mShowIgnoreVersion;
        private boolean dismissNotificationProgress;
        private boolean mOnlyWifi;


        private String datalogSn;

        public Map<String, String> getParams() {
            return params;
        }

        /**
         * 自定义请求参数
         *
         * @param params 自定义请求参数
         * @return Builder
         */
        public Builder setParams(Map<String, String> params) {
            this.params = params;
            return this;
        }

        public boolean isIgnoreDefParams() {
            return mIgnoreDefParams;
        }

        /**
         * @param ignoreDefParams 是否忽略默认的参数注入 appKey version
         * @return Builder
         */
        public Builder setIgnoreDefParams(boolean ignoreDefParams) {
            this.mIgnoreDefParams = ignoreDefParams;
            return this;
        }

        public boolean isPost() {
            return isPost;
        }

        /**
         * 是否是post请求，默认是get
         *
         * @param post 是否是post请求，默认是get
         * @return Builder
         */
        public Builder setPost(boolean post) {
            isPost = post;
            return this;
        }

        public String getTargetPath() {
            return mTargetPath;
        }

        /**
         * apk的下载路径，
         *
         * @param targetPath apk的下载路径，
         * @return Builder
         */
        public Builder setTargetPath(String targetPath) {
            mTargetPath = targetPath;
            return this;
        }

        public String getAppKey() {
            return mAppKey;
        }

        /**
         * 唯一的appkey
         *
         * @param appKey 唯一的appkey
         * @return Builder
         */
        public Builder setAppKey(String appKey) {
            mAppKey = appKey;
            return this;
        }

        public Activity getActivity() {
            return mActivity;
        }

        /**
         * 是否是post请求，默认是get
         *
         * @param activity 当前提示的Activity
         * @return Builder
         */
        public Builder setActivity(Activity activity) {
            mActivity = activity;
            return this;
        }

        public HttpManager getHttpManager() {
            return mHttpManager;
        }

        /**
         * 设置网络工具
         *
         * @param httpManager 自己实现的网络对象
         * @return Builder
         */
        public Builder setHttpManager(HttpManager httpManager) {
            mHttpManager = httpManager;
            return this;
        }

        public String getUpdateUrl() {
            return mUpdateUrl;
        }

        /**
         * 更新地址
         *
         * @param updateUrl 更新地址
         * @return Builder
         */
        public Builder setUpdateUrl(String updateUrl) {
            mUpdateUrl = updateUrl;
            return this;
        }

        public int getThemeColor() {
            return mThemeColor;
        }

        /**
         * 设置按钮，进度条的颜色
         *
         * @param themeColor 设置按钮，进度条的颜色
         * @return Builder
         */
        public Builder setThemeColor(int themeColor) {
            mThemeColor = themeColor;
            return this;
        }

        public int getTopPic() {
            return mTopPic;
        }

        /**
         * 顶部的图片
         *
         * @param topPic 顶部的图片
         * @return Builder
         */
        public Builder setTopPic(int topPic) {
            mTopPic = topPic;
            return this;
        }


        public String getmFileDir() {
            return mFileDir;
        }

        public Builder setmFileDir(String mFileDir) {
            this.mFileDir = mFileDir;
            return this;
        }

        public String getDatalogSn() {
            return datalogSn;
        }

        public Builder setDatalogSn(String datalogSn) {
            this.datalogSn = datalogSn;
            return this;
        }

        /**
         * @return 生成app管理器
         */
        public FileUpdataManager build() {
            //校验
            if (getActivity() == null || getHttpManager() == null) {
                throw new NullPointerException("必要参数不能为空");
            }
            if (TextUtils.isEmpty(getTargetPath())) {
                //sd卡是否存在
                String path = "";
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()) {
                    try {
                        path = getActivity().getExternalCacheDir().getAbsolutePath();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (TextUtils.isEmpty(path)) {
                        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                    }
                } else {
                    path = getActivity().getCacheDir().getAbsolutePath();
                }
                setTargetPath(path);
            }
            return new FileUpdataManager(this);
        }

        /**
         * 是否隐藏对话框下载进度条
         *
         * @return Builder
         */
        public Builder hideDialogOnDownloading() {
            mHideDialog = true;
            return this;
        }

        /**
         * @return 是否影藏对话框
         */
        public boolean isHideDialog() {
            return mHideDialog;
        }

        /**
         * 显示忽略版本
         *
         * @return 是否忽略版本
         */
        public Builder showIgnoreVersion() {
            mShowIgnoreVersion = true;
            return this;
        }

        public boolean isShowIgnoreVersion() {
            return mShowIgnoreVersion;
        }

        /**
         * 不显示通知栏进度条
         *
         * @return 是否显示进度条
         */
        public Builder dismissNotificationProgress() {
            dismissNotificationProgress = true;
            return this;
        }

        public boolean isDismissNotificationProgress() {
            return dismissNotificationProgress;
        }

        public Builder setOnlyWifi() {
            mOnlyWifi = true;
            return this;
        }

        public boolean isOnlyWifi() {
            return mOnlyWifi;
        }

    }


}
