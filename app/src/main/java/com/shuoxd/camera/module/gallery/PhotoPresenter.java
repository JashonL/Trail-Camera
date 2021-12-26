package com.shuoxd.camera.module.gallery;

import android.content.Context;

import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.module.home.HomeView;

public class PhotoPresenter extends BasePresenter<PhotoView> {
    public PhotoPresenter(PhotoView baseView) {
        super(baseView);
    }

    public PhotoPresenter(Context context, PhotoView baseView) {
        super(context, baseView);
    }
}
