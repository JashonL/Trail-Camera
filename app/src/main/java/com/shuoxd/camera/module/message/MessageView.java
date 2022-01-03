package com.shuoxd.camera.module.message;

import com.shuoxd.camera.base.BaseView;
import com.shuoxd.camera.bean.MessageBean;

import java.util.List;

public interface MessageView extends BaseView {

    void showMessage(List<MessageBean> msgList);

    void showNoMoreData();

    void showMoreFail();
}
