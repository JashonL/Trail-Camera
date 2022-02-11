package com.shuoxd.camera.module.map;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shuoxd.camera.R;
import com.shuoxd.camera.base.BaseActivity;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapActivity extends BaseActivity<MapPresenter> implements IMapView, OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {


    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_toolbar)
    LinearLayout appToolbar;
    @BindView(R.id.map)
    SupportMapFragment map;
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.ll_share)
    LinearLayout llShare;
    @BindView(R.id.tv_navigation)
    TextView tvNavigation;
    @BindView(R.id.ll_navigation)
    LinearLayout llNavigation;

    private final String GOOGLE_MAP_NAVI_URI = "google.navigation:q=";


    @Override
    protected MapPresenter createPresenter() {
        return new MapPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    protected void initViews() {
        //初始化toolbar
        initToobar(toolbar);
        tvTitle.setText(R.string.m200_camera_map);

        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        double lat = 40.73;
        double lng = -73.99;
        LatLng appointLoc = new LatLng(lat, lng);

        // 移动地图到指定经度的位置
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(appointLoc));

        //添加标记到指定经纬度
        googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Marker")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.camera_maker)));
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        //跳转到谷歌地图导航
        double lat = 40.73;
        double lng = -73.99;
        goNaviByGoogleMap(String.valueOf(lat),String.valueOf(lng));
        return false;
    }




    /**
     * by moos on 2017/09/18
     * func:调起谷歌导航
     * @param lat
     * @param lon
     */
    private void goNaviByGoogleMap(String lat,String lon){
        Uri uri = Uri.parse(GOOGLE_MAP_NAVI_URI+lat+","+lon);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

}
