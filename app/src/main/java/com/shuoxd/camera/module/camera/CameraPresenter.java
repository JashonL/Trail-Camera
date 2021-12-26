package com.shuoxd.camera.module.camera;

import android.content.Context;

import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.module.home.HomeView;

public class CameraPresenter extends BasePresenter<HomeView> {
    public CameraPresenter(HomeView baseView) {
        super(baseView);
    }

    public CameraPresenter(Context context, HomeView baseView) {
        super(context, baseView);
    }
}
