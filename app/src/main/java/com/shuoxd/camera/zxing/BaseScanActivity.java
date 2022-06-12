package com.shuoxd.camera.zxing;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gyf.immersionbar.ImmersionBar;
import com.king.zxing.CameraScan;
import com.shuoxd.camera.R;

import butterknife.ButterKnife;

public abstract class BaseScanActivity extends AppCompatActivity implements CameraScan.OnScanResultCallback {

    protected abstract int getContentView();


    protected abstract void initViews();

    protected abstract void initData();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        //初始化沉浸式
        initImmersionBar();
        initViews();
        initData();
    }


    /**
     * 初始化沉浸式
     * Init immersion bar.
     */
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).
                statusBarColor(R.color.color_app_main).fitsSystemWindows(true).
                navigationBarColor(R.color.white).init();
    }


    public void initToobar(Toolbar toolbar){
        if (toolbar!=null){
            toolbar.setNavigationIcon(R.drawable.icon_return_w);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }





}
