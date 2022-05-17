package com.shuoxd.camera.module.plans;

import com.shuoxd.camera.base.BaseView;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.bean.PlansInfoBean;

import java.util.List;

public interface PlansDetailView extends BaseView {

    void showPlansInfo(List<PlansInfoBean> list);

    void status(String status);
}
