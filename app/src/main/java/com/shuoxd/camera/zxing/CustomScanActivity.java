package com.shuoxd.camera.zxing;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.king.zxing.CaptureHelper;
import com.king.zxing.OnCaptureCallback;
import com.king.zxing.ViewfinderView;
import com.mylhyl.circledialog.res.drawable.CircleDrawable;
import com.mylhyl.circledialog.res.values.CircleColor;
import com.mylhyl.circledialog.res.values.CircleDimen;
import com.mylhyl.circledialog.view.listener.OnCreateBodyViewListener;
import com.shuoxd.camera.R;
import com.shuoxd.camera.utils.MyToastUtils;

import butterknife.BindView;
import butterknife.OnClick;


public class CustomScanActivity extends BaseScanActivity implements OnCaptureCallback, Toolbar.OnMenuItemClickListener {
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

    @BindView(R.id.bottomLayout)
    LinearLayoutCompat bottomLayout;
    @BindView(R.id.tv_find_serialnum)
    AppCompatTextView tvFindSerialnum;


    private CaptureHelper mCaptureHelper;
    private int type;

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
        Intent intent = getIntent();
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

    }


    @Override
    public boolean onResultCallback(String result) {
//        MyToastUtils.toast(result);



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
    }




    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.right_action:
                break;
        }
        return true;
    }
}
