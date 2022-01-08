package com.shuoxd.camera.module.pictrue;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.gyf.immersionbar.ImmersionBar;
import com.ortiz.touchview.TouchImageView;
import com.shuoxd.camera.R;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.utils.GlideUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BigImageActivty extends BaseActivity<BigImagePresenter> implements BigImageView {


    @BindView(R.id.iv_touch)
    TouchImageView ivTouch;

    @Override
    protected BigImagePresenter createPresenter() {
        return new BigImagePresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_big_pictrue;
    }

    @Override
    protected void initViews() {

    }


    /**
     * 初始化沉浸式
     * Init immersion bar.
     */
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(true, 0.2f)//设置状态栏图片为深色，(如果android 6.0以下就是半透明)
                .statusBarColor(R.color.nocolor)//这里的颜色，你可以自定义。
                .init();
    }


    @Override
    protected void initData() {
        String path = getIntent().getStringExtra("path");
            if (!TextUtils.isEmpty(path)){
                GlideUtils.getInstance().showImageContext(mContext, R.drawable.deer, R.drawable.deer, path, ivTouch);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

}
