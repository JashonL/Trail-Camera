package com.shuoxd.camera.base;

public interface BaseView {

    void showLoading();

    void hideLoading();

    void showResultError(String msg);

    void onErrorCode(BaseBean bean);

    void showServerError(String msg);

    void LoginException();

}
