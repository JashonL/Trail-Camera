package com.shuoxd.camera.module.gallery;

import com.shuoxd.camera.base.BaseView;
import com.shuoxd.camera.bean.PictureBean;

import java.util.List;

public interface PhotoView extends BaseView {

    void showCameraPic(List<PictureBean> beans);

    void showNoMoreData();

    void showMoreFail();

    void showTotalNum(int totalNum);
}
