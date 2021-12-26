package com.shuoxd.camera.module.map;

import android.content.Context;

import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.module.home.HomeView;

public class MapPresenter extends BasePresenter<HomeView> {
    public MapPresenter(HomeView baseView) {
        super(baseView);
    }

    public MapPresenter(Context context, HomeView baseView) {
        super(context, baseView);
    }
}
