package com.shuoxd.camera.module.login;

import com.shuoxd.camera.base.BaseBean;
import com.shuoxd.camera.base.BaseView;

public interface LoginView extends BaseView {

    void showUserInfo(User user,String isAuto);

    void showLoginError(String errorMsg);

    void registerSuccess();

}
