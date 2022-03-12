package com.shuoxd.camera.module.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.hjq.toast.ToastUtils;
import com.shuoxd.camera.R;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.module.login.LoginActivity;
import com.shuoxd.camera.utils.CommentUtils;
import com.shuoxd.camera.utils.MyToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePassWordActivity extends BaseActivity<ChangePassWordPresenter> implements ChangePassWordView {


    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_toolbar)
    LinearLayout appToolbar;
    @BindView(R.id.tv_custom_service)
    TextView tvCustomService;
    @BindView(R.id.et_olb_password)
    EditText etOlbPassword;
    @BindView(R.id.btn_getcode)
    AppCompatTextView btnGetcode;
    @BindView(R.id.ll_code)
    LinearLayout llCode;
    @BindView(R.id.et_new_password)
    EditText etNewPassword;
    @BindView(R.id.et_newpassword_comfir)
    EditText etNewpasswordComfir;
    @BindView(R.id.btn_save)
    Button btnSave;

    @Override
    protected ChangePassWordPresenter createPresenter() {
        return new ChangePassWordPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initViews() {
        initToobar(toolbar);
        tvTitle.setText(R.string.m54_change_password);


        //获取用户邮箱
        String email = App.getUserBean().getEmail();
        if (!TextUtils.isEmpty(email)) {
            String s = CommentUtils.hideString(email);
            String enterCode = getString(R.string.m227_enter_code);
            String format = String.format(enterCode, s);
            etOlbPassword.setHint(format);
        }


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


    @OnClick({R.id.btn_save, R.id.btn_getcode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                String newPwd = etNewPassword.getText().toString();
                String oldPwd = etOlbPassword.getText().toString();
                String comfirPwd = etNewpasswordComfir.getText().toString();

                if (TextUtils.isEmpty(oldPwd)) {
                    MyToastUtils.toast(R.string.m226_input_code);
                    return;
                }


                if (TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(comfirPwd)) {
                    MyToastUtils.toast(R.string.m63_password_cannot_empty);
                    return;
                }

                if (!comfirPwd.equals(newPwd)) {
                    MyToastUtils.toast(R.string.m197_password_error);
                    return;
                }


                presenter.modifyUserInfo(oldPwd, newPwd, comfirPwd);

                break;

            case R.id.btn_getcode:
                //将获取按钮设置成灰色
                btnGetcode.setEnabled(false);
                CommentUtils.hideKeyboard(view);
                //请求获取验证码
                String email = App.getUserBean().getEmail();
                presenter.sendEmailCode("1",email);
                //开始倒计时
                showAfterButton();
                break;

        }
    }


    //显示灰色button
    private void showAfterButton() {
        if (btnGetcode.isEnabled()) {
            btnGetcode.setEnabled(false);
        }
        //显示文本
        btnGetcode.setText(TIME_COUNT +"s");
        //发送消息
        handler.sendEmptyMessageDelayed(102, 1000);
    }



    //显示正常button
    private void showBeforeButton() {
        btnGetcode.setEnabled(true);
        TIME_COUNT = TOTAL_TIME;
        //显示文本
        btnGetcode.setText(R.string.m225_code);
    }


    private final int TOTAL_TIME = 60;
    private int TIME_COUNT = TOTAL_TIME;

    Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            TIME_COUNT--;
            if (TIME_COUNT <= 0) {
                showBeforeButton();
            } else {
                showAfterButton();
            }

        }
    };



    @Override
    public void changePasswordSuccess() {
        String accountName = App.getUserBean().accountName;
        presenter.userLogout(accountName);
    }

    @Override
    public void logout() {
        //跳转到登录界面
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void showLoginError(String errorMsg) {
        ToastUtils.show(errorMsg);
    }

    @Override
    public void getCodeSuccess(String code) {

    }


}
