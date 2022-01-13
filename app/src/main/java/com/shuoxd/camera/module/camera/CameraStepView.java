package com.shuoxd.camera.module.camera;

import com.shuoxd.camera.base.BaseView;
import com.shuoxd.camera.bean.DeviceSettingBean;

import java.util.List;

public interface CameraStepView extends BaseView {

    void showSetting(List<DeviceSettingBean> list);

    void cameraSetSuccess(String operationType, String operationValue);

}
