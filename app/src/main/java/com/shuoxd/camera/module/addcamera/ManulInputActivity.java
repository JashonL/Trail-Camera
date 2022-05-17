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
import com.shuoxd.camera.module.login.User;
import com.shuoxd.camera.utils.MyToastUtils;

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
    private LocationManager locationManager;
    private Location location;
    private String provider;
    private double lat;
    private double lng;

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
        initToobar(toolbar);
        tvTitle.setText(R.string.m7_add_camera);
    }

    @Override
    protected void initData() {
        presenter.initData();
        // 获取位置服务


// 调用getSystemService()方法来获取LocationManager对象

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager==null)return;

// 指定LocationManager的定位方法

        provider = LocationManager.GPS_PROVIDER;

// 调用getLastKnownLocation()方法获取当前的位置信息



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            location = locationManager.getLastKnownLocation(provider);

            //获取纬度
             lat = location.getLatitude();

            //获取经度
             lng = location.getLongitude();
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
        location = locationManager.getLastKnownLocation(provider);
        //获取纬度
         lat = location.getLatitude();

        //获取经度
         lng = location.getLongitude();
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



}
