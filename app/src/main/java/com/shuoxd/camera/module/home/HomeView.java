package com.shuoxd.camera.module.home;

import com.shuoxd.camera.base.BaseView;
import com.shuoxd.camera.bean.CameraBean;

import java.util.List;

public interface HomeView extends BaseView {

    void setDeviceList(List<CameraBean> cameraBeanList);


}
