package com.shuoxd.camera.utils;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

public class DownLoadUtils {

    private DownloadManager downloadManager;
    private long id;

    private static MyBroadcastReceiver receiver;


    public void download() {
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        //创建下载任务，url即任务链接
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //指定下载路径及文件名
//        request.setDestinationInExternalPublicDir(FILE_URI, fileName);
        //自定义下载目录
        request.setDestinationUri(fileUri);
        //获取下载管理器
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        //一些配置
        //允许移动网络与WIFI下载
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        //是否在通知栏显示下载进度
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);

        //设置可见及可管理
        /*注意，Android Q之后不推荐使用*/
        request.setVisibleInDownloadsUi(true);


        Mydialog.showNodissmiss(context);

        //将任务加入下载队列
        assert downloadManager != null;
        id = downloadManager.enqueue(request);
        receiver = new MyBroadcastReceiver();
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.registerReceiver(receiver, filter);
        }
    }


    //测试url，下载链接
    private String url;
    //保存地址
    private String FILE_URI;
    private IDownloadlister lister = null;
    //文件名
    private String fileName = "share.mp4";
    //Context
    private Context context;

    private Uri fileUri;


    public static DownLoadUtils builder() {
        return new DownLoadUtils();
    }

    public DownLoadUtils setUrl(String url) {
        this.url = url;
        return this;
    }


    public DownLoadUtils setFileUri(String fileUri) {
        this.FILE_URI = fileUri;
        return this;
    }


    public DownLoadUtils setFileUri(Uri fileUri) {
        this.fileUri = fileUri;
        return this;
    }

    public DownLoadUtils setLister(IDownloadlister lister) {
        this.lister = lister;
        return this;
    }


    public DownLoadUtils setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public DownLoadUtils setContext(Context context) {
        this.context = context;
        return this;
    }


    public interface IDownloadlister {
        void success(Uri uri);
    }



    public static void unRegister(Context context){

        if (receiver!=null){
            Activity activity = (Activity) context;
            activity.unregisterReceiver(receiver);
        }



    }



    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Mydialog.dissmiss();
            //获取下载id
            long myDwonloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (myDwonloadID == id) {
                //获取下载uri
                Uri uri = downloadManager.getUriForDownloadedFile(myDwonloadID);
                lister.success(uri);
            }
        }
    }


}
