package com.shuoxd.camera.module.map;

import static com.shuoxd.camera.constants.PermissionConstant.RC_LOCATION;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.view.listener.OnLvItemClickListener;
import com.shuoxd.camera.MainActivity;
import com.shuoxd.camera.R;
import com.shuoxd.camera.base.BaseFragment;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.bean.MapLoctionBean;
import com.shuoxd.camera.utils.CommentUtils;
import com.shuoxd.camera.utils.MyToastUtils;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class MapFragment extends BaseFragment<MapPresenter> implements IMapView,
        OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnCameraIdleListener {

    @BindView(R.id.camera_navigation)
    CardView cameraNavigation;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_wifi)
    TextView tvWifi;
    @BindView(R.id.tv_address)
    TextView tvAddress;

    @BindView(R.id.tv_nogoogle_service)
    TextView tvNoGoogle;



    private MapView mMap;
    private GoogleMap googleMap;
    private LatLng mCenterLatlng;//中心坐标位置


    private List<CameraBean> cameraList;

    private List<Marker> mMarkerRainbow = new ArrayList<Marker>();
    private Geocoder geocoder;

    private MapLoctionBean centerBean;


   private CameraBean cameraBean;

    private final String GOOGLE_MAP_APP = "com.google.android.apps.maps";

    private  LatLng markerLatLng;
    private final String GOOGLE_MAP_NAVI_URI = "google.navigation:q=";


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
        cameraNavigation.setVisibility(View.GONE);
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

   /*     int errorCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this.getActivity());

        if (ConnectionResult.SUCCESS != errorCode) {
            GooglePlayServicesUtil.getErrorDialog(errorCode,
                    this.getActivity(), 0).show();
        } else {
            mMap.getMapAsync(this);
        }*/


        boolean googleService = CommentUtils.isGoogleService(getContext());
        if (!googleService){
            tvNoGoogle.setVisibility(View.VISIBLE);
            mMap.setVisibility(View.GONE);
        }else {
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



    private void moveCenter2(LatLng location) {
        if (location != null) {
            mCenterLatlng = location;
            googleMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(mCenterLatlng, 5f));
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
    public void onCameraIdle() {
        CameraPosition cameraPosition = googleMap.getCameraPosition();
        mCenterLatlng = cameraPosition.target;
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        int index = 0;
        for (int i = 0; i < mMarkerRainbow.size(); i++) {
            Marker marker1 = mMarkerRainbow.get(i);
            if (marker1.equals(marker)) {
                index = i;
                break;
            }
        }

        //如果已经显示的就隐藏
        boolean infoWindowShown = marker.isInfoWindowShown();
        if (infoWindowShown){
            cameraNavigation.setVisibility(View.GONE);
        }else {
            cameraNavigation.setVisibility(View.VISIBLE);
        }
        //获取相机信息
        cameraBean = cameraList.get(index);
        CameraBean.CameraInfo camera = cameraBean.getCamera();
        String alias = camera.getAlias();
        if (TextUtils.isEmpty(alias)){
            alias=camera.getImei();
        }
        tvName.setText(alias);

        String signalStrength = camera.getSignalStrength();
        int wifiStrength = Integer.parseInt(signalStrength);
        if (wifiStrength == 0) {
            setTextViewDrawableTop( tvWifi, R.drawable.signal1);
        } else if (wifiStrength <= 25) {
            setTextViewDrawableTop( tvWifi, R.drawable.signal2);
        } else if (wifiStrength <= 50) {
            setTextViewDrawableTop( tvWifi, R.drawable.signal3);
        } else if (wifiStrength <= 75) {
            setTextViewDrawableTop( tvWifi, R.drawable.signal3);
        } else {
            setTextViewDrawableTop( tvWifi, R.drawable.signal4);
        }


        LatLng position = marker.getPosition();
        getAddress(position);


        return false;
    }





    public void setTextViewDrawableTop(TextView textView, int drawId) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getResources().getDrawable(drawId, null);
        } else {
            drawable = getResources().getDrawable(drawId);
        }
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textView.setCompoundDrawables(drawable, null, null, null);
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
    public void showCameraList(List<CameraBean> cameraList) {
        this.cameraList = cameraList;


        double lat = 0;
        double lng = 0;
        //解析经纬度
        for (int i = 0; i < cameraList.size(); i++) {
            CameraBean cameraBean = cameraList.get(i);
            String latitude = cameraBean.getCamera().getLatitude();
            String longitude = cameraBean.getCamera().getLongitude();
            latitude = latitude.substring(0, latitude.length() - 1);
            longitude = longitude.substring(0, longitude.length() - 1);
            lat=Double.parseDouble(latitude);
            lng=Double.parseDouble(longitude);
            //封装marker
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(
                            Double.parseDouble(latitude),
                            Double.parseDouble(longitude)))
                    .title("Marker " + i).
                            icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                    .decodeResource(getResources(), R.drawable.camera_marker))).title(""));
            mMarkerRainbow.add(marker);

        }

        moveCenter2(new LatLng(lat,lng));


    }





    @Override
    public void showLocationSuccess(String lat,String lng) {

    }


    @OnClick({R.id.tv_details,R.id.tv_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_details:
                CameraBean.CameraInfo camera = cameraBean.getCamera();
                String alias = camera.getAlias();
                String imei = camera.getImei();
                showCameraInfo(imei,alias);
                break;
            case R.id.tv_address:
                try {
                    jumpLocation();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }



    /**
     * 跳转到导航
     */
    private void jumpLocation() {
        double lat = markerLatLng.latitude;
        double lng = markerLatLng.longitude;

//        navigateForDestination();
        if (isApplicationInstall(GOOGLE_MAP_APP)) {
            goNaviByGoogleMap(String.valueOf(lat),String.valueOf(lng));
        } else {
            MyToastUtils.toast(R.string.m205_app_not_installed);
        }
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





    /**
     * by moos on 2017/09/18
     * func:判断手机是否安装了该应用
     * @param packageName
     * @return
     */
    private boolean isApplicationInstall(String packageName){
        return new File("/data/data/" + packageName).exists();
    }



    //点击item跳转到Fragment1V2

    private void showCameraInfo(String id, String alias) {
        MainActivity main = (MainActivity) getActivity();
        main.cameraId = id;
        main.cameraAlias = alias;
        main.showCameraInfo();
    }



    /**
     * 逆地理编码
     *
     * @param location 定位结果
     */
    private void getAddress(LatLng location) {
        try {
            List<Address> addresses;
            if (geocoder == null){
                geocoder = new Geocoder(getContext(), Locale.getDefault());
            }
            String addStr = null; //结果
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
            Address adds = addresses.get(0);
            String address = addresses.get(0).getAddressLine(0);
            if (centerBean == null){
                centerBean = new MapLoctionBean();
            }
            String city = adds.getLocality();
            if (TextUtils.isEmpty(city)) city = "";
            centerBean.setCity(city);
            centerBean.setCountry(adds.getCountryName());
            centerBean.setDetail(address);
            String text = String.format("%s\n\n%s%s | %s:%f %s:%f", address, adds.getCountryName(), city
                    , getString(R.string.m202_longitude), location.longitude, getString(R.string.m203_Latitude), location.latitude);
            tvAddress.setText(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
