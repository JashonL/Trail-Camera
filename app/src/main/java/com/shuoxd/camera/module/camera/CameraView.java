package com.shuoxd.camera.module.camera;

import com.shuoxd.camera.base.BaseView;
import com.shuoxd.camera.bean.CameraBean;

public interface CameraView extends BaseView {

    void showCameraInfo(CameraBean.CameraInfo cameraInfo);
}
