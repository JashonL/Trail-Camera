package com.shuoxd.camera.module.addcamera;

import static com.shuoxd.camera.constants.PermissionConstant.RC_LOCATION;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.shuoxd.camera.R;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.eventbus.GetAddressMsg;
import com.shuoxd.camera.module.login.User;
import com.shuoxd.camera.utils.LocationUtils;
import com.shuoxd.camera.utils.LogUtil;
import com.shuoxd.camera.utils.MyToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class ManulInputActivity extends BaseActivity<Addpresenter> implements AddCanmeraView {


    @BindView(R.id.et_imei)
    EditText etImei;
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_toolbar)
    LinearLayout appToolbar;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.tv_imei_num)
    TextView tvImeiNum;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.et_name)
    EditText etName;

    private String lat="000.00000";
    private String lng="000.00000";

    @Override
    protected Addpresenter createPresenter() {
        return new Addpresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_manul_add_camera;
    }

    @Override
    protected void initViews() {
        EventBus.getDefault().register(this);
        initToobar(toolbar);
        tvTitle.setText(R.string.m7_add_camera);
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getLocalCity(GetAddressMsg msg) {
       lat=msg.getLat();
       lng=msg.getLng();

        LogUtil.i("经纬度："+lat+"经纬度:"+lng);

    }


    @Override
    protected void initData() {
        presenter.initData();
        // 获取位置服务






        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationUtils.getAddress(this);

        } else {
            // Do not have permissions, request them now
            String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
            EasyPermissions.requestPermissions(this, getString(R.string.m201_location),
                    RC_LOCATION, perms);
        }


    }


    @Override
    public void showImei(String imei) {
        etImei.setText(imei);
    }

    @Override
    public void showUserInfo(User user) {

    }

    @Override
    public void showLoginError(String errorMsg) {

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationUtils.getAddress(this);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }


    @OnClick(R.id.btn_register)
    public void onViewClicked() {
        String s = etImei.getText().toString();
        String name = etName.getText().toString();
        boolean empty = TextUtils.isEmpty(s);
        boolean empty1 = TextUtils.isEmpty(name);
        if (empty||empty1) {
            MyToastUtils.toast(R.string.m64_imei_cannot_empty);
        } else {
            presenter.addCamera(s,name,String.valueOf(lng),String.valueOf(lat));
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
