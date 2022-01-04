package com.shuoxd.camera.module.camera;

import com.shuoxd.camera.base.BaseView;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.bean.PictureBean;

import java.util.List;

public interface CameraView extends BaseView {

    void showCameraInfo(CameraBean.CameraInfo cameraInfo);

    void showCameraPic(List<PictureBean>beans);

    void showNoMoreData();

    void showMoreFail();

    void showTotalNum(int totalNum);

}
