package com.shuoxd.camera.module.map;

import android.content.Context;

import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.module.home.HomeView;

public class MapPresenter extends BasePresenter<MapView> {
    public MapPresenter(MapView baseView) {
        super(baseView);
    }

    public MapPresenter(Context context, MapView baseView) {
        super(context, baseView);
    }
}
