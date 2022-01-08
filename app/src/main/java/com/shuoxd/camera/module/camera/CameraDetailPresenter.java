package com.shuoxd.camera.module.camera;

import android.content.Context;

import com.shuoxd.camera.base.BaseBean;
import com.shuoxd.camera.base.BasePresenter;

public class CameraDetailPresenter extends BasePresenter<CameraDetailView> implements CameraDetailView {
    public CameraDetailPresenter(Context context, CameraDetailView baseView) {
        super(context, baseView);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showResultError(String msg) {

    }

    @Override
    public void onErrorCode(BaseBean bean) {

    }

    @Override
    public void showServerError(String msg) {

    }
}
