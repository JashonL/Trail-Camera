package com.shuoxd.camera.module.presenter;

import android.content.Context;

import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.module.view.LoginView;

public class LoginPresenter extends BasePresenter<LoginView> {
    public LoginPresenter(LoginView baseView) {
        super(baseView);
    }

    public LoginPresenter(Context context, LoginView baseView) {
        super(context, baseView);
    }
}
