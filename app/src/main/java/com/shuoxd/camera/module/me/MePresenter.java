package com.shuoxd.camera.module.me;

import android.content.Context;

import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.module.home.HomeView;

public class MePresenter extends BasePresenter<HomeView> {
    public MePresenter(HomeView baseView) {
        super(baseView);
    }

    public MePresenter(Context context, HomeView baseView) {
        super(context, baseView);
    }
}
