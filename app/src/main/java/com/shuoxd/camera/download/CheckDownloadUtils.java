package com.shuoxd.camera.download;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;

import com.shuoxd.camera.app.App;

import java.util.ArrayList;
import java.util.List;


public class CheckDownloadUtils {

    public static FileUpdataManager downloadVideo(Activity act, List<String> urls, FileDownLoadManager.DownloadCallback downloadCallback) {
        FileUpdataManager build = new FileUpdataManager.Builder()
                .setActivity(act)
                .setPost(true)
                //实现httpManager接口的对象
                .setHttpManager(new DatalogUpDataHttpUtil())
                .setmFileDir(App.VIDEO_DOWNLOAD_FILE_DIR)
                .build();


        if (urls.size()<1){
            return build;
        }

        FileDownBean dowloadBean = new FileDownBean();
        List<FileDownBean.DownFileBean> downFileBeans = new ArrayList<>();
        for (String url:urls) {
            FileDownBean.DownFileBean downFileBean = new FileDownBean.DownFileBean();
            downFileBean.setDownUrl(url);
            String name = url.substring(url.lastIndexOf("/"));
            downFileBean.setFileName(name);
            downFileBean.setSavePath(App.VIDEO_DOWNLOAD_FILE_DIR);
            downFileBeans.add(downFileBean);
        }

        dowloadBean.setFile_urls(urls);
        dowloadBean.setDownFileBeans(downFileBeans);
        build.setUpdateApp(dowloadBean);
        build.startDownLoad(downloadCallback);
        return build;

    }




    public static FileUpdataManager downLoadImage(Activity act, List<String> urls, FileDownLoadManager.DownloadCallback downloadCallback) {
        FileUpdataManager build = new FileUpdataManager.Builder()
                .setActivity(act)
                .setPost(true)
                //实现httpManager接口的对象
                .setHttpManager(new DatalogUpDataHttpUtil())
                .setmFileDir(App.IMAGE_DOWNLOAD_FILE_DIR)
                .build();


        if (urls.size()<1){
            return build;
        }

        FileDownBean dowloadBean = new FileDownBean();
        List<FileDownBean.DownFileBean> downFileBeans = new ArrayList<>();
        for (String url:urls) {
            FileDownBean.DownFileBean downFileBean = new FileDownBean.DownFileBean();
            downFileBean.setDownUrl(url);
            String name = url.substring(url.lastIndexOf("/"));
            downFileBean.setFileName(name);
            downFileBean.setSavePath(App.IMAGE_DOWNLOAD_FILE_DIR);
            downFileBeans.add(downFileBean);
        }

        dowloadBean.setFile_urls(urls);
        dowloadBean.setDownFileBeans(downFileBeans);
        build.setUpdateApp(dowloadBean);
        build.startDownLoad(downloadCallback);
        return build;

    }


}
