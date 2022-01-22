package com.shuoxd.camera.module.account;

import androidx.annotation.NonNull;

import com.shuoxd.camera.R;
import com.shuoxd.camera.base.BaseActivity;

import java.util.List;

public class ForgetPassWordActivity extends BaseActivity<ForgetPassWordPresenter> implements ForgetPassWordView {

    @Override
    protected ForgetPassWordPresenter createPresenter() {
        return new ForgetPassWordPresenter(this,this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_password;
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
}
