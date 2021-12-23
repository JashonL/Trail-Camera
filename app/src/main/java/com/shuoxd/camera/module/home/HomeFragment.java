package com.shuoxd.camera.module.home;

import androidx.annotation.NonNull;

import com.shuoxd.camera.HomePresenter;
import com.shuoxd.camera.R;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.base.BaseFragment;

import java.util.List;

public class HomeFragment extends BaseFragment<HomePresenter> implements HomeView {




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
