package com.shuoxd.camera.module.addcamera;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.shuoxd.camera.R;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.module.login.User;
import com.shuoxd.camera.utils.MyToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManulInputActivity extends BaseActivity<Addpresenter> implements AddCanmeraView {


    @BindView(R.id.et_imei)
    EditText etImei;

    @Override
    protected Addpresenter createPresenter() {
        return new Addpresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_manul_add_camera;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {
        presenter.initData();
    }


    @Override
    public void showImei(String imei) {
        etImei.setText(imei);
    }

    @Override
    public void showUserInfo(User user) {

    }

    @Override
    public void showLoginError(String errorMsg) {

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }


    @OnClick(R.id.btn_register)
    public void onViewClicked() {
        String s = etImei.getText().toString();
        boolean empty = TextUtils.isEmpty(s);
        if (empty){
            MyToastUtils.toast(R.string.m64_imei_cannot_empty);
        }else {
            presenter.addCamera(s);
        }
    }
}
