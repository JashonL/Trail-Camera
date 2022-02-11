package com.shuoxd.camera.module.map;

import static com.shuoxd.camera.constants.PermissionConstant.RC_LOCATION;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gyf.immersionbar.ImmersionBar;
import com.shuoxd.camera.R;
import com.shuoxd.camera.base.BaseFragment;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.bean.MapLoctionBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.EasyPermissions;

public class MapFragment extends BaseFragment<MapPresenter> implements IMapView,
        OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnCameraIdleListener {

    private MapView mMap;
    private GoogleMap googleMap;
    private LatLng mCenterLatlng;//中心坐标位置


    @Override
    protected MapPresenter createPresenter() {
        return new MapPresenter(getContext(), this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.map_fragment;
    }

    @Override
    protected void initView() {
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mMap = (MapView) view.findViewById(R.id.mapview);
        mMap.onCreate(savedInstanceState);
        mMap.onResume();

        try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        int errorCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this.getActivity());

        if (ConnectionResult.SUCCESS != errorCode) {
            GooglePlayServicesUtil.getErrorDialog(errorCode,
                    this.getActivity(), 0).show();
        } else {
            mMap.getMapAsync(this);
        }
        return view;
    }

    @Override
    protected void initData() {
    }


    @Override
    public void onResume() {
        super.onResume();
        mMap.onResume();
    }

    /**
     * 74    * 方法必须重写
     * 75    * map的生命周期方法
     * 76
     */
    @Override
    public void onPause() {
        super.onPause();
        mMap.onPause();
    }


    /**
     * 85    * 方法必须重写
     * 86    * map的生命周期方法
     * 87
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMap.onSaveInstanceState(outState);
    }

    /**
     * 96    * 方法必须重写
     * 97    * map的生命周期方法
     * 98
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMap != null) {
            mMap.onDestroy();
        }
    }


    @Override
    public void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(true, 0.2f)//设置状态栏图片为深色，(如果android 6.0以下就是半透明)
                .init();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        //添加相机的的坐标点marker
        googleMap.setTrafficEnabled(true);
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setRotateGesturesEnabled(false);
        //添加电站位置
  /*      try {
            MarkerOptions markerOption = new MarkerOptions();
            LatLng plantLg = new LatLng(Double.parseDouble("22.006358"), Double.parseDouble("143.530354"));
            markerOption.position(plantLg);
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(), R.drawable.camera_marker))).title("我的相机");
            googleMap.addMarker(markerOption);
            moveCenter(plantLg);

            enableMyLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);

        //获取相机列表
        presenter.getAlldevice();
    }

    private void moveCenter(LatLng location) {
        if (location != null) {
            mCenterLatlng = location;
            googleMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(mCenterLatlng, 16f));
        }
    }


    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (googleMap != null) {
                googleMap.setMyLocationEnabled(true);
            }
        } else {
            // Do not have permissions, request them now
            String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
            EasyPermissions.requestPermissions(this, getString(R.string.m199_location),
                    RC_LOCATION, perms);
        }
    }


    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {

    }


    @Override
    public void onCameraIdle() {
        CameraPosition cameraPosition = googleMap.getCameraPosition();
        mCenterLatlng = cameraPosition.target;
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public void showCameraList(List<CameraBean> cameraList) {

        List<Marker> mMarkerRainbow = new ArrayList<Marker>();
        //解析经纬度
        for (int i = 0; i < cameraList.size(); i++) {
            CameraBean cameraBean = cameraList.get(i);
            String latitude = cameraBean.getCamera().getLatitude();
            String longitude = cameraBean.getCamera().getLongitude();

            //封装marker
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(
                            Double.parseDouble(latitude),
                            Double.parseDouble(longitude)))
                    .title("Marker " + i).
                            icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                    .decodeResource(getResources(), R.drawable.camera_marker))).title("我的相机"));
            mMarkerRainbow.add(marker);

        }


    }


}
