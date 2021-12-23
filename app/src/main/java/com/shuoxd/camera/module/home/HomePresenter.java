package com.shuoxd.camera.module.home;

import android.content.Context;

import com.shuoxd.camera.base.BasePresenter;

public class HomePresenter extends BasePresenter<HomeView> {
    public HomePresenter(HomeView baseView) {
        super(baseView);
    }

    public HomePresenter(Context context, HomeView baseView) {
        super(context, baseView);
    }
}
