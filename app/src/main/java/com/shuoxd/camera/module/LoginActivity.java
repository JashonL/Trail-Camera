package com.shuoxd.camera.module;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.gyf.immersionbar.ImmersionBar;
import com.shuoxd.camera.R;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.module.presenter.LoginPresenter;
import com.shuoxd.camera.module.view.LoginView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginView {

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }


    @Override
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.keyboardEnable(true).init();
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }



    @OnClick({R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                Intent intent = new Intent(this,LoginActivity.class);
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, logo,
//                        getString(R.string.transition_logo_splash));
//                startActivity(intent, options.toBundle());
                break;
        }
    }
}
