package com.shuoxd.camera.module.camera;

import com.shuoxd.camera.base.BaseView;

public interface CameraDetailView extends BaseView {

    //收藏结果
    void showCollecMsg(String collection);
    //删除
    void delete(String photoId);
    //请求下载 或取消下载
    void dowload(String photoId,String msg,String resolution);
}
