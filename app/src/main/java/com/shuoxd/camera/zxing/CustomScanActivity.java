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

import com.king.zxing.CaptureHelper;
import com.king.zxing.OnCaptureCallback;
import com.king.zxing.ViewfinderView;
import com.shuoxd.camera.R;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseBean;
import com.shuoxd.camera.base.BaseView;
import com.shuoxd.camera.constants.GlobalConstant;
import com.shuoxd.camera.module.addcamera.ManulInputActivity;
import com.shuoxd.camera.module.login.LoginManager;
import com.shuoxd.camera.module.login.LoginView;
import com.shuoxd.camera.module.login.User;
import com.shuoxd.camera.utils.DialogUtils;

import butterknife.BindView;
import butterknife.OnClick;


public class CustomScanActivity extends BaseScanActivity implements OnCaptureCallback, Toolbar.OnMenuItemClickListener, LoginView {


    @BindView(R.id.surfaceView)
    SurfaceView surfaceView;
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
    private CaptureHelper mCaptureHelper;
    private int type;

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
        initUI();
    }


    private void initUI() {
        toolbar.inflateMenu(R.menu.scan_menu);
        toolbar.setOnMenuItemClickListener(this);
        mCaptureHelper = new CaptureHelper(this, surfaceView, viewfinderView, ivFlash);
        mCaptureHelper.setOnCaptureCallback(this);
        mCaptureHelper.onCreate();
        mCaptureHelper.vibrate(true)
                .fullScreenScan(true)//全屏扫码
                .supportVerticalCode(true)//支持扫垂直条码，建议有此需求时才使用。
                .supportLuminanceInvert(true)//是否支持识别反色码（黑白反色的码），增加识别率
                .continuousScan(true);
        manager = new LoginManager(this);


    }


    @Override
    public boolean onResultCallback(String result) {//扫码回调
//        MyToastUtils.toast(result);
          //扫码成功跳转到手动添加页面
        Intent intent = new Intent(this, ManulInputActivity.class);
        intent.putExtra(GlobalConstant.SCAN_RESULT, result);
        startActivity(intent);
        finish();
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        mCaptureHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCaptureHelper.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCaptureHelper.onDestroy();
        manager.detachView();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.right_action:
                //跳过直接登录进入主界面
                User userBean = App.getUserBean();
                String accountName = userBean.getAccountName();
                String password = userBean.getPassword();
                manager.userLogin(accountName,password);
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
    public void showUserInfo(User user) {

    }

    @Override
    public void showLoginError(String errorMsg) {

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
}
