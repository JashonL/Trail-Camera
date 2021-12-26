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
                .statusBarColor(R.color.nocolor)//这里的颜色，你可以自定义。
                .init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initListener();
    }

    protected void initToolbar() {
        if (mToolBar == null) {
            mToolBar = findViewById(R.id.toolbar);
            if (mToolBar == null) {
            } else {
                mToolBar.setTitleTextColor(ContextCompat.getColor(this, R.color.color_text_00));
            }
        }
    }

    public Toolbar getToolBar() {
        return mToolBar;
    }


    protected void setTitle(String title) {
        if (mToolBar != null) {
            mToolBar.setTitle(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        if (mToolBar != null) {
            mToolBar.setTitle(titleId);
        }
    }

    protected void setSubTitle(String title) {
        if (mToolBar != null) {
            mToolBar.setSubtitle(title);
        }
    }

    protected void setLogo(Drawable logo) {
        if (mToolBar != null) {
            mToolBar.setLogo(logo);
        }
    }


    protected void setNavigationIcon(Drawable logo) {
        if (mToolBar != null) {
            mToolBar.setNavigationIcon(logo);
        }
    }

    protected void setMenu(int resId, Toolbar.OnMenuItemClickListener listener) {
        if (mToolBar != null) {
            mToolBar.inflateMenu(resId);
            mToolBar.setOnMenuItemClickListener(listener);
        }
    }

    protected void setDisplayHomeAsUpEnabled(int iconResId, final View.OnClickListener listener) {
        if (mToolBar != null) {
            mToolBar.setNavigationIcon(iconResId);
            if (listener != null) {
                mToolBar.setNavigationOnClickListener(listener);
            } else {
                mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
        }
    }

    protected void setDisplayHomeAsUpEnabled() {
        setDisplayHomeAsUpEnabled(R.drawable.icon_return, null);
    }

    protected void setDisplayHomeAsUpEnabled(final View.OnClickListener listener) {
        setDisplayHomeAsUpEnabled(R.drawable.icon_return, listener);
    }

    protected void hideToolBarView() {
        if (mToolBar != null) {
            mToolBar.setVisibility(View.GONE);
        }
    }

    protected void showToolBarView() {
        if (mToolBar != null) {
            mToolBar.setVisibility(View.VISIBLE);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        //将权限交给EasyPermissions处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }




}
