package com.shuoxd.camera.module.gallery;

import com.shuoxd.camera.base.BaseView;
import com.shuoxd.camera.bean.PictureBean;

import java.util.List;

public interface PhotoView extends BaseView {

    void showCameraPic(List<PictureBean> beans);

    void showNoMoreData();

    void showMoreFail();

    void showTotalNum(int totalNum);



    //收藏结果
    void showCollecMsg();
    //删除
    void delete();
    //请求下载 或取消下载
    void dowload();
}
