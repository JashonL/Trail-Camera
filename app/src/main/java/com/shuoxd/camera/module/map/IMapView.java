package com.shuoxd.camera.module.map;

import com.shuoxd.camera.base.BaseView;
import com.shuoxd.camera.bean.CameraBean;

import java.util.List;

public interface IMapView extends BaseView {

    void showCameraList(List<CameraBean> cameraList);


    void showLocationSuccess(String lat,String lng);
}
