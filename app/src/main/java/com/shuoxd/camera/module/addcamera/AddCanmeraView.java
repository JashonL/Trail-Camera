package com.shuoxd.camera.module.addcamera;

import com.shuoxd.camera.base.BaseBean;
import com.shuoxd.camera.base.BaseView;
import com.shuoxd.camera.module.login.User;

public interface AddCanmeraView extends BaseView {

    void showImei(String imei);

    void showUserInfo(User user);

    void showLoginError(String errorMsg);

}
