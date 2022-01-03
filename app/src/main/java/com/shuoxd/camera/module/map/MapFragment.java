package com.shuoxd.camera.module.map;

import com.shuoxd.camera.base.BaseFragment;
import com.shuoxd.camera.bean.PictureBean;
import com.shuoxd.camera.module.gallery.PhotoPresenter;
import com.shuoxd.camera.module.gallery.PhotoView;

import java.util.List;

public class MapFragment extends BaseFragment<MapPresenter> implements MapView {
    @Override
    protected MapPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void initImmersionBar() {

    }

}
