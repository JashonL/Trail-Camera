package com.shuoxd.camera.module.init;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.gyf.immersionbar.ImmersionBar;
import com.shuoxd.camera.R;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.module.login.LoginActivity;

import java.util.List;

import butterknife.BindView;

public class SplashActivity extends BaseActivity<SplashPresenter> implements ISplashView {


    @BindView(R.id.iv_logo)
    ImageView ivLogo;

    protected ImmersionBar mImmersionBar;


    private Handler handler = new Handler();
    private Runnable runnableToLogin = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this, ivLogo,"share");
            startActivity(intent, options.toBundle());
            finish();

        }
    };


    @Override
    protected SplashPresenter createPresenter() {
        return new SplashPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initViews() {
        handler.postDelayed(runnableToLogin, 2000);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnableToLogin);
        handler = null;
    }

    /**
     * 初始化沉浸式
     * Init immersion bar.
     */
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(true, 0.2f)//设置状态栏图片为深色，(如果android 6.0以下就是半透明)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.nocolor)//这里的颜色，你可以自定义。
                .init();
    }

    @Override
    public void loginSuccess(String user) {
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

}
