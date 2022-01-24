package com.shuoxd.camera.module.account;

import com.shuoxd.camera.base.BaseView;

public interface ChangePassWordView extends BaseView {

    void changePasswordSuccess();

    void logout();

    void showLoginError(String errorMsg);
}
