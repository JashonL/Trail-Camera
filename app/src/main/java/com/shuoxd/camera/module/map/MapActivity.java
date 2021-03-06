package com.shuoxd.camera.module.map;


import static com.shuoxd.camera.constants.PermissionConstant.RC_LOCATION;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shuoxd.camera.R;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.bean.MapLoctionBean;
import com.shuoxd.camera.eventbus.FreshCameraLocation;
import com.shuoxd.camera.utils.CommentUtils;
import com.shuoxd.camera.utils.MyToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import pub.devrel.easypermissions.EasyPermissions;

public class MapActivity extends BaseActivity<MapPresenter> implements IMapView,
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnCameraIdleListener, Toolbar.OnMenuItemClickListener {

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_toolbar)
    LinearLayout appToolbar;

    @BindView(R.id.tvMarker)
    TextView mTvMarker;
    @BindView(R.id.tv_nogoogle_service)
    TextView tvNoGoogle;
    @BindView(R.id.map)
    FrameLayout map;

    private String mLng = "0";
    private String mLat = "0";
    private LatLng mCenterLatlng;//??????????????????


    private MapLoctionBean centerBean;


    private GoogleMap mMap;
    private Geocoder geocoder;


    private boolean permissionDenied = false;

    private String alias;


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

        //toolbar
        initToobar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        tvTitle.setText(R.string.m270_Location);


        boolean googleService = CommentUtils.isGoogleService(this);
        if (!googleService) {
            tvNoGoogle.setVisibility(View.VISIBLE);
            map.setVisibility(View.GONE);
        }


        //???????????????
        mLat = getIntent().getStringExtra("lat");
        mLat = mLat.substring(0, mLat.length()-1);
        mLng = getIntent().getStringExtra("lng");
        mLng = mLng.substring(0, mLng.length()-1);

        alias=getIntent().getStringExtra("alias");
        //????????????
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
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    @Override
    protected void initImmersionBar() {

    }


    @Override
    protected void initData() {
        //?????????????????????
        if (!TextUtils.isEmpty(mLat) && !TextUtils.isEmpty(mLng)) {
            double vLat = Double.parseDouble(mLat);
            double vLng = Double.parseDouble(mLng);
            LatLng location = new LatLng(vLat, vLng);
            getAddress(location);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        enableMyLocation();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        permissionDenied = true;
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        //???????????????????????????marker
        mMap = googleMap;
        mMap.setTrafficEnabled(true);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setRotateGesturesEnabled(false);
        //??????????????????
        try {
            MarkerOptions markerOption = new MarkerOptions();
            LatLng plantLg = new LatLng(Double.parseDouble(mLat), Double.parseDouble(mLng));
            markerOption.position(plantLg);
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(), R.drawable.camera_marker))).title(alias);
            mMap.addMarker(markerOption);
            moveCenter(plantLg);

            enableMyLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }


        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

    }


    private void moveCenter(LatLng location) {
        if (location != null) {
            mCenterLatlng = location;
            mMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(mCenterLatlng, 10f));
        }
    }


    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        MyToastUtils.toast("?????????marker");
        return true;
    }


    /**
     * ???????????????
     *
     * @param location ????????????
     */
    private void getAddress(LatLng location) {
        try {
            List<Address> addresses;
            if (geocoder == null) {
                geocoder = new Geocoder(this, Locale.getDefault());
            }
            String addStr = null; //??????
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
            Address adds = addresses.get(0);
            String address = addresses.get(0).getAddressLine(0);
            if (centerBean == null) {
                centerBean = new MapLoctionBean();
            }
            String city = adds.getLocality();
            if (TextUtils.isEmpty(city)) city = "";
            centerBean.setCity(city);
            centerBean.setCountry(adds.getCountryName());
            centerBean.setDetail(address);
            String text = String.format("%s\n\n%s%s | %s:%f %s:%f", address, adds.getCountryName(), city
                    , getString(R.string.m202_longitude), location.longitude, getString(R.string.m203_Latitude), location.latitude);
            mTvMarker.setText(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public void onCameraIdle() {
        CameraPosition cameraPosition = mMap.getCameraPosition();
        mCenterLatlng = cameraPosition.target;
        getAddress(mCenterLatlng);
    }


    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            // Do not have permissions, request them now
            String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
            EasyPermissions.requestPermissions(this, getString(R.string.m201_location),
                    RC_LOCATION, perms);
        }
    }


    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {

    }

    @Override
    public void showCameraList(List<CameraBean> cameraList) {

    }

    @Override
    public void showLocationSuccess(String lat, String lng) {
        FreshCameraLocation freshCameraLocation = new FreshCameraLocation();
        freshCameraLocation.setLat(lat);
        freshCameraLocation.setLng(lng);
        EventBus.getDefault().post(freshCameraLocation);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {

        return true;
    }
}
