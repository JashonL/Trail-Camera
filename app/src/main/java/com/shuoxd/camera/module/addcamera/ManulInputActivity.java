package com.shuoxd.camera.module.addcamera;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

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
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_toolbar)
    LinearLayout appToolbar;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.tv_imei_num)
    TextView tvImeiNum;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.et_name)
    EditText etName;

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
        initToobar(toolbar);
        tvTitle.setText(R.string.m7_add_camera);
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
        String name = etName.getText().toString();
        boolean empty = TextUtils.isEmpty(s);
        boolean empty1 = TextUtils.isEmpty(name);
        if (empty||empty1) {
            MyToastUtils.toast(R.string.m64_imei_cannot_empty);
        } else {
            presenter.addCamera(s,name);
        }
    }



}
