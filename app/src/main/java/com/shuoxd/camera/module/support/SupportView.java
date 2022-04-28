package com.shuoxd.camera.module.support;

import com.shuoxd.camera.base.BaseView;
import com.shuoxd.camera.bean.SupportBean;

import java.util.List;

public interface SupportView extends BaseView {

    void showSupportList(List<SupportBean>list);
}
