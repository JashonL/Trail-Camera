package com.shuoxd.camera.module.me;

import com.shuoxd.camera.base.BaseView;

public interface DestroyView extends BaseView {

    void destroysuccess(String msg);

    void showLoginError(String errorMsg);

    void updataUser();

    void logout();
}
