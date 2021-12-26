package com.shuoxd.camera.module.me;

import com.shuoxd.camera.base.BaseFragment;
import com.shuoxd.camera.module.gallery.PhotoPresenter;
import com.shuoxd.camera.module.gallery.PhotoView;

public class MeFragment extends BaseFragment<PhotoPresenter> implements PhotoView {
    @Override
    protected PhotoPresenter createPresenter() {
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
}
