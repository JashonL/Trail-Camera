package com.shuoxd.camera.module.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.shuoxd.camera.R;
import com.shuoxd.camera.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPassWordActivity extends BaseActivity<ForgetPassWordPresenter> implements ForgetPassWordView {

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_toolbar)
    LinearLayout appToolbar;
    @BindView(R.id.btn_email)
    AppCompatButton btnEmail;
    @BindView(R.id.btn_imei)
    AppCompatButton btnImei;

    @Override
    protected ForgetPassWordPresenter createPresenter() {
        return new ForgetPassWordPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected void initViews() {

        initToobar(toolbar);
        tvTitle.setText(R.string.m178_find_password);

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



    @OnClick({R.id.btn_email, R.id.btn_imei})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_email:
                Intent intent=new Intent(this,FindPasswordActivity.class);
                intent.putExtra("type","1");
                startActivity(intent);
                break;
            case R.id.btn_imei:
                Intent intent1=new Intent(this,FindPasswordActivity.class);
                intent1.putExtra("type","2");
                startActivity(intent1);
                break;
        }
    }
}
