package com.shuoxd.camera.module.login;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.gyf.immersionbar.BarParams;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.shuoxd.camera.R;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.constants.GlobalConstant;
import com.shuoxd.camera.module.account.ForgetPassWordActivity;
import com.shuoxd.camera.module.webview.WebViewActivity;
import com.shuoxd.camera.utils.CommentUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.shuoxd.camera.constants.PermissionConstant.RC_CAMERA;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginView,
        TabLayout.OnTabSelectedListener {


    @BindView(R.id.v_background)
    View vBackground;
    @BindView(R.id.v_center)
    View vCenter;
    @BindView(R.id.iv_login_logo)
    ImageView ivLoginLogo;
    @BindView(R.id.tab_title)
    TabLayout tabTitle;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.til_user)
    TextInputLayout tilUser;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.tv_forgot_pwd)
    TextView tvForgotPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.ctl_group_login)
    ConstraintLayout ctlGroupLogin;
    @BindView(R.id.et_register_username)
    EditText etRegisterUsername;
    @BindView(R.id.til_re_user)
    TextInputLayout tilReUser;
    @BindView(R.id.et_register_password)
    EditText etRegisterPassword;
    @BindView(R.id.til_re_password)
    TextInputLayout tilRePassword;
    @BindView(R.id.et_confirm_password)
    EditText etConfirmPassword;
    @BindView(R.id.til_re_comfir_password)
    TextInputLayout tilReComfirPassword;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.ctl_group_register)
    ConstraintLayout ctlGroupRegister;
    @BindView(R.id.card_login)
    CardView cardLogin;
    @BindView(R.id.btn_demo)
    AppCompatButton btnDemo;
    @BindView(R.id.tv_step_1)
    TextView tvStep1;
    @BindView(R.id.v_step_center)
    View vStepCenter;
    @BindView(R.id.tv_step_2)
    TextView tvStep2;
    @BindView(R.id.tv_step1_text)
    TextView tvStep1Text;
    @BindView(R.id.tv_step2_text)
    TextView tvStep2Text;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.gp_register_step)
    Group gpRestep;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;
    @BindView(R.id.cb_agreement)
    CheckBox cbAgreement;


    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initImmersionBar() {
//        mImmersionBar = ImmersionBar.with(this);
//        mImmersionBar.keyboardEnable(true).init();

        ImmersionBar immersionBar = ImmersionBar.with(this).statusBarDarkFont(false, 0.2f)//?????????????????????????????????(??????android 6.0?????????????????????)
                .transparentNavigationBar()
                .keyboardEnable(true);
        immersionBar.init();

    }

    @Override
    protected void initViews() {
        //?????????tablayout
        tabTitle.addOnTabSelectedListener(this);

        //???????????????
        String logHing = getString(R.string.m60_email_address);
        etUsername.setHint(logHing);
        etPassword.setHint(R.string.m61_password);

        //???????????????
        etRegisterUsername.setHint(logHing);
        etRegisterPassword.setHint(R.string.m61_password);
        etConfirmPassword.setHint(R.string.m62_confirm_password);

        String s="??? "+getString(R.string.m281_privacy_policy)+" ???";
        tvAgreement.setText(s);
    }

    @Override
    protected void initData() {
        presenter.getUserInfo();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }


    /**
     * ??????????????????
     */
    @AfterPermissionGranted(RC_CAMERA)
    private void checkCameraPermissions() {
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {//?????????
            presenter.registerSuccess();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.m162_requires_permission),
                    RC_CAMERA, perms);
        }
    }


    @OnClick({R.id.btn_login, R.id.btn_register, R.id.tv_forgot_pwd,R.id.tv_agreement,R.id.btn_demo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:

                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    ToastUtils.show(R.string.m63_username_cannot_empty);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    ToastUtils.show(R.string.m63_password_cannot_empty);
                    return;
                }
                presenter.userLogin(username, password);
                break;
            case R.id.btn_register:

                if (cbAgreement.isChecked()){
                    String reUserName = etRegisterUsername.getText().toString();
                    String rePassword = etRegisterPassword.getText().toString();

                    String timeZone = CommentUtils.getTimeZone();
                    presenter.register(timeZone,reUserName, rePassword);
                }else {
                    ToastUtils.show(R.string.m282_please_choose);
                }


                break;
            case R.id.tv_forgot_pwd:
                startActivity(new Intent(this, ForgetPassWordActivity.class));
                break;
            case R.id.tv_agreement:
                presenter.toWebView();
                break;
            case R.id.btn_demo:
                presenter.loginGuest();
                break;
        }
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        switch (position) {
            case 0:
                btnDemo.setVisibility(View.VISIBLE);
                gpRestep.setVisibility(View.GONE);
                ctlGroupLogin.setVisibility(View.VISIBLE);
                ctlGroupRegister.setVisibility(View.GONE);
                break;
            case 1:
                btnDemo.setVisibility(View.GONE);
                gpRestep.setVisibility(View.VISIBLE);
                ctlGroupLogin.setVisibility(View.GONE);
                ctlGroupRegister.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    @Override
    public void showUserInfo(User user, String isAuto) {
        String username = user.getAccountName();
        String password = user.getPassword();
        if (!TextUtils.isEmpty(username)) {
            etUsername.setText(username);
            etUsername.setSelection(username.length());
        }
        if (!TextUtils.isEmpty(password)) {
            etPassword.setText(password);
        }


        //????????????????????????
        if ("1".equals(isAuto)) {
            presenter.userLogin(username, password);
        }

    }

    @Override
    public void showLoginError(String errorMsg) {
        ToastUtils.show(errorMsg);
    }

    @Override
    public void registerSuccess() {
        checkCameraPermissions();
    }
}
