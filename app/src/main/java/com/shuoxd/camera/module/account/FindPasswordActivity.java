package com.shuoxd.camera.module.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.mylhyl.circledialog.CircleDialog;
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

public class FindPasswordActivity extends BaseActivity<FindPassWordPresenter> implements FindPassWordView {
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_toolbar)
    LinearLayout appToolbar;
    @BindView(R.id.et_value)
    EditText etValue;
    @BindView(R.id.btn_ok)
    AppCompatButton btnOk;
    @BindView(R.id.et_olb_password)
    EditText etOlbPassword;
    @BindView(R.id.btn_getcode)
    AppCompatTextView btnGetcode;
    @BindView(R.id.ll_code)
    LinearLayout llCode;


    @Override
    protected FindPassWordPresenter createPresenter() {
        return new FindPassWordPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_find_password;
    }

    @Override
    protected void initViews() {
        initToobar(toolbar);
        tvTitle.setText(R.string.m178_find_password);
    }

    @Override
    protected void initData() {
        String type = presenter.getType();
        if ("1".equals(type)) {
            etValue.setHint(R.string.m60_email_address);
        } else {
            etValue.setHint(R.string.m9_imei_num);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }


    @OnClick({R.id.btn_ok,R.id.btn_getcode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                String s = etValue.getText().toString();
                String s1 = etOlbPassword.getText().toString();
                if (TextUtils.isEmpty(s1)) {
                    MyToastUtils.toast(R.string.m226_input_code);
                    return;
                }

                if (TextUtils.isEmpty(s)) {
                    MyToastUtils.toast(R.string.m145_content_cannot_empty);
                    return;
                }
                presenter.findPassword(s,s1);
                break;
            case R.id.btn_getcode:
                String type = presenter.getType();
                String email=etValue.getText().toString();
                if (TextUtils.isEmpty(email)){
                    if ("1".equals(type)){
                        MyToastUtils.toast(R.string.m228_input_email);
                    }else {
                        MyToastUtils.toast(R.string.m229_input_imei);

                    }
                    return;
                }


                //将获取按钮设置成灰色
                btnGetcode.setEnabled(false);
                CommentUtils.hideKeyboard(view);
                //请求获取验证码
                if ("1".equals(type)) {
                    presenter.sendEmailCode("1",email);
                } else {
                    presenter.sendEmailCode("2",email);

                }
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
    public void showSuccess(String success) {
        CircleDialog.Builder builder = new CircleDialog.Builder();
        builder.setTitle(getString(R.string.m150_tips));
        builder.setGravity(Gravity.CENTER);
        builder.setText(success);
        builder.setCanceledOnTouchOutside(true);
        builder.setCancelable(true);
        builder.setWidth(0.75f);
        builder.setNegative(getString(R.string.m127_cancel), view -> {

        });
        builder.setPositive(getString(R.string.m152_ok), view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        builder.show(getSupportFragmentManager());
    }

    @Override
    public void showError(String error) {
        MyToastUtils.toast(error);
    }

    @Override
    public void getCodeSuccess(String code) {

    }

}
