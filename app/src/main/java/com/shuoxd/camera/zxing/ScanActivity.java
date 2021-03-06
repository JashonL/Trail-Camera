package com.shuoxd.camera.zxing;

import android.content.Intent;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.view.PreviewView;

import com.google.zxing.Result;
import com.king.zxing.CameraScan;
import com.king.zxing.DefaultCameraScan;
import com.king.zxing.ViewfinderView;
import com.shuoxd.camera.R;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseBean;
import com.shuoxd.camera.constants.GlobalConstant;
import com.shuoxd.camera.module.addcamera.ManulInputActivity;
import com.shuoxd.camera.module.login.LoginActivity;
import com.shuoxd.camera.module.login.LoginManager;
import com.shuoxd.camera.module.login.LoginView;
import com.shuoxd.camera.module.login.User;
import com.shuoxd.camera.utils.DialogUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ScanActivity extends BaseScanActivity implements Toolbar.OnMenuItemClickListener, LoginView {
    @BindView(R.id.viewfinderView)
    ViewfinderView viewfinderView;
    @BindView(R.id.ivFlash)
    ImageView ivFlash;
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_toolbar)
    LinearLayout appToolbar;
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
    @BindView(R.id.bottomLayout)
    LinearLayoutCompat bottomLayout;
    @BindView(R.id.tv_find_serialnum)
    AppCompatTextView tvFindSerialnum;
    @BindView(R.id.previewView)
    PreviewView previewView;

    private CameraScan mCameraScan;
    private String type;
    private LoginManager manager;


    @Override
    protected int getContentView() {
        return R.layout.activity_custom_scan;
    }

    @Override
    protected void initViews() {
        initToobar(toolbar);
        tvTitle.setText(R.string.m7_add_camera);
    }

    @Override
    protected void initData() {
        type = getIntent().getStringExtra("type");
        initUI();
    }


    private void initUI() {
        toolbar.inflateMenu(R.menu.scan_menu);
        toolbar.setOnMenuItemClickListener(this);

        if ("0".equals(type)) {
            tvStep1.setVisibility(View.VISIBLE);
            vStepCenter.setVisibility(View.VISIBLE);
            tvStep2.setVisibility(View.VISIBLE);
            tvStep1Text.setVisibility(View.VISIBLE);
            tvStep2Text.setVisibility(View.VISIBLE);
        } else {
            tvStep1.setVisibility(View.GONE);
            vStepCenter.setVisibility(View.GONE);
            tvStep2.setVisibility(View.GONE);
            tvStep1Text.setVisibility(View.GONE);
            tvStep2Text.setVisibility(View.GONE);
        }

        mCameraScan = new DefaultCameraScan(this, previewView);
        mCameraScan.setOnScanResultCallback(this)
                .setVibrate(true)
                .startCamera();

        manager = new LoginManager(this);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.right_action:
                //?????????????????????????????????
                User userBean = App.getUserBean();
                String accountName = userBean.getAccountName();
                String password = userBean.getPassword();
                manager.userLogin(accountName, password);
                break;
        }
        return true;
    }


    @OnClick({R.id.tv_find_serialnum})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_find_serialnum:
                Intent intent = new Intent(this, ManulInputActivity.class);
                startActivity(intent);
                finish();
                break;
        }

    }

    @Override
    public void showLoading() {
        DialogUtils.getInstance().showLoadingDialog(this);
    }

    @Override
    public void hideLoading() {
        DialogUtils.getInstance().closeLoadingDialog();
    }

    @Override
    public void showResultError(String msg) {

    }

    @Override
    public void onErrorCode(BaseBean bean) {

    }

    @Override
    public void showServerError(String msg) {

    }

    @Override
    public void LoginException() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void showUserInfo(User user, String isAuto) {

    }

    @Override
    public void showLoginError(String errorMsg) {

    }

    @Override
    public void registerSuccess() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCameraScan.release();
        manager.detachView();
    }


    /**
     * ??????????????????
     *
     * @param result ????????????
     * @return
     */
    @Override
    public boolean onScanResultCallback(Result result) {
        //        MyToastUtils.toast(result);
        //???????????????????????????????????????
        Intent intent = new Intent(this, ManulInputActivity.class);
        intent.putExtra(GlobalConstant.SCAN_RESULT, result.getText());
        startActivity(intent);
        finish();
        return false;
    }

}
