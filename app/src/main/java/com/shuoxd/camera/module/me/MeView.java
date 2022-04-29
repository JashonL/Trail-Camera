package com.shuoxd.camera.module.me;

import com.shuoxd.camera.base.BaseView;

public interface MeView extends BaseView {


    void showLoginError(String errorMsg);


    void logout();


    void photoCount(String photos);

    void cameraCount(String cameras);

    void videoCount(String videos);


}
