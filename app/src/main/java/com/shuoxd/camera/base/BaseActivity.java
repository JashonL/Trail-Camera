package com.shuoxd.camera.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;


import com.gyf.immersionbar.ImmersionBar;
import com.shuoxd.camera.R;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.module.login.LoginActivity;
import com.shuoxd.camera.utils.MyToastUtils;
import com.shuoxd.camera.utils.Mydialog;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.ButterKnife;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView, EasyPermissions.PermissionCallbacks {

    public Context mContext;

    protected P presenter;

    protected abstract P createPresenter();

    protected abstract int getLayoutId();

    protected abstract void initViews();

    protected abstract void initData();

    protected Toolbar mToolBar;

    protected ImmersionBar mImmersionBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LayoutInflater.from(this).inflate(getLayoutId(), null));
        ButterKnife.bind(this);
        App.getInstance().addActivityList(new WeakReference<>(this));
        mContext=this;
        presenter = createPresenter();
        initViews();
        //初始化沉浸式
        initImmersionBar();
        initData();
    }
    /**
     * 初始化沉浸式
     * Init immersion bar.
     */
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        mImmersionBar=  ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(true, 0.2f)//设置状态栏图片为深色，(如果android 6.0以下就是半透明)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.color_app_main)//这里的颜色，你可以自定义。
                .init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initListener();
    }



    public void initToobar(Toolbar toolbar){
        if (toolbar!=null){
            toolbar.setNavigationIcon(R.drawable.icon_return_w);
            toolbar.setNavigationOnClickListener(v -> finish());
        }
    }



    @Override
    public void setTitle(int titleId) {
        if (mToolBar != null) {
            mToolBar.setTitle(titleId);
        }
    }





    protected void initListener() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁时，解除绑定
        if (presenter != null) {
            presenter.detachView();
        }
    }

    @Override
    public void showLoading() {
        Mydialog.show(mContext);
    }

    @Override
    public void hideLoading() {
        Mydialog.dissmiss();
    }

    @Override
    public void onErrorCode(BaseBean bean) {
        Mydialog.dissmiss();
    }

    @Override
    public void showResultError(String msg) {
        MyToastUtils.toast(msg);
    }


    @Override
    public void showServerError(String msg) {
        MyToastUtils.toast(msg);
    }

    @Override
    public void LoginException() {
        Intent intent =new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        //将权限交给EasyPermissions处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }




}
