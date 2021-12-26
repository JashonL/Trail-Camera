package com.shuoxd.camera.module.camera;

import com.shuoxd.camera.HomePresenter;
import com.shuoxd.camera.R;
import com.shuoxd.camera.base.BaseFragment;
import com.shuoxd.camera.module.home.HomeView;

public class CameraFragment extends BaseFragment<HomePresenter> implements HomeView {




    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(getContext(),this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_frament;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
