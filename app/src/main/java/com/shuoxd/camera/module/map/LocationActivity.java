package com.shuoxd.camera.module.map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.gyf.immersionbar.ImmersionBar;
import com.mylhyl.circledialog.CircleDialog;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.MapSearchAdapter;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.bean.MapLoctionBean;
import com.shuoxd.camera.bean.MapSearchBean;
import com.shuoxd.camera.constants.AllPermissionRequestCode;
import com.shuoxd.camera.utils.GPSUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

import static com.shuoxd.camera.constants.PermissionConstant.RC_LOCATION;

public class LocationActivity
        extends BaseActivity<MapPresenter> implements IMapView,
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnCameraIdleListener, Toolbar.OnMenuItemClickListener, GoogleMap.OnMyLocationChangeListener {


    @BindView(R.id.etContent)
    EditText mEtContent;
    @BindView(R.id.poiSugRV)
    RecyclerView mPoiSugRV;
    @BindView(R.id.tvMarker)
    TextView mTvMarker;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.status_bar_view)
    View statusBarView;


    private GoogleMap mMap;
    private Location mLocation;//当前地址
    private LatLng mCenterLatlng;//中心坐标位置
    private boolean isFirst = true;
    private MarkerOptions myLocationMark;
    private MapLoctionBean centerBean;
    private Geocoder geocoder;

    private MapSearchAdapter mAdapter;
    private List<MapSearchBean> mList;

    private String imei;

    public static final String MAP_LOCATION = "map_location";

    private String mLng = "-74.006363";
    private String mLat = "40.68399";


    @OnClick({R.id.flSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.flSearch:
                try {
                    String search2 = mEtContent.getText().toString().trim();
                    if (geocoder == null) {
                        geocoder = new Geocoder(this, Locale.getDefault());
                    }
                    List<Address> list = geocoder.getFromLocationName(search2, 10);
                    if (list != null && list.size() > 0) {
                        List<MapSearchBean> newList = new ArrayList<>();
                        for (Address info : list) {
                            MapSearchBean bean = new MapSearchBean();
                            bean.setAddress(info.getAddressLine(0));
                            String city = info.getLocality();
                            if (TextUtils.isEmpty(city)) city = "";
                            bean.setCity(city);
                            bean.setCountry(info.getCountryName());
                            bean.setDistrict(info.getAdminArea());
                            bean.setPt(new com.amap.api.maps.model.LatLng(info.getLatitude(), info.getLongitude()));
                            newList.add(bean);
                        }
                        mAdapter.replaceData(newList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ivReset:
                moveMyLocation(mLocation);
                break;
        }
    }


    @Override
    public void onCameraIdle() {

        CameraPosition cameraPosition = mMap.getCameraPosition();
        mCenterLatlng = cameraPosition.target;
        getAddress(mCenterLatlng);
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
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setTrafficEnabled(true);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setMapToolbarEnabled(true);
        mMap.setOnCameraIdleListener(this);

        //添加电站位置
        try {
            MarkerOptions markerOption = new MarkerOptions();
            LatLng plantLg = new LatLng(Double.parseDouble(mLat), Double.parseDouble(mLng));
            markerOption.position(plantLg);
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(), R.drawable.camera_marker))).title("");
            mMap.addMarker(markerOption);
            moveCenter(plantLg);
            getMyLocation();

//            getDeviceLocation();

        } catch (Exception e) {
            e.printStackTrace();
        }



    }



    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        try {
            LocationRequest request = new LocationRequest();
            request.setInterval(5000);
            request.setFastestInterval(5000);
            request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationCallback locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    Location lastLocation = locationResult.getLastLocation();
                    if (mMap != null) {
                        //获取到当前位置，将地图移至定位处并将地图放大14倍
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 13f));
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        //移除callBack,不然还会继续获取定位
                        fusedLocationProviderClient.removeLocationUpdates(this);
                        //清空之前添加的标记
                        mMap.clear();
                        //添加当前位置的标记
                        MarkerOptions markerOption = new MarkerOptions();
                        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                .decodeResource(getResources(), R.drawable.camera_marker))).title("");
                        LatLng plantLg = new LatLng(Double.parseDouble(mLat), Double.parseDouble(mLng));
                        markerOption.position(plantLg);
                        mMap.addMarker(markerOption);
                    }
                }

                @Override
                public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                    super.onLocationAvailability(locationAvailability);

                    //当前定位不可用
                    if (mMap.isMyLocationEnabled() != true){

                    }
                    fusedLocationProviderClient.removeLocationUpdates(this);
                }
            };

            fusedLocationProviderClient.requestLocationUpdates(request,locationCallback, Looper.myLooper());

        } catch (Exception e) {

        }
    }


    @Override
    protected MapPresenter createPresenter() {
        return new MapPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_location;
    }

    @Override
    protected void initViews() {

        //获取经纬度
        mLat = getIntent().getStringExtra("lat");
        mLng = getIntent().getStringExtra("lng");


        mLat = mLat.substring(0, mLat.length() - 1);
        mLng = mLng.substring(0, mLng.length() - 1);

        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        ButterKnife.bind(this);

        initToobar(toolbar);
        toolbar.inflateMenu(R.menu.menu_submit);
        toolbar.setOnMenuItemClickListener(this);
        tvTitle.setText(R.string.m207_modify_position);

        initRecyclerView();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            MapSearchBean item = mAdapter.getItem(position);
            mAdapter.replaceData(new ArrayList<>());
            //移动到中心位置
            com.amap.api.maps.model.LatLng pt = item.getPt();
            moveCenter(new LatLng(pt.latitude, pt.longitude));
            //设置数据
            mEtContent.setText(item.getAddress());
            String text = String.format("%s\n\n%s%s | %s:%f %s:%f", item.getAddress(), item.getCountry(), item.getCity()
                    , getString(R.string.m202_longitude), pt.longitude, getString(R.string.m203_Latitude), pt.latitude);
//            String text = String.format("%s\n\n%s%s%s | %s:%f %s:%f", item.getAddress(), item.getProvince(), item.getCity(), item.getDistrict(), getString(R.string.geographydata_longitude), pt.longitude, getString(R.string.geographydata_latitude), pt.latitude);
            mTvMarker.setText(text);

        });
    }


    private void initRecyclerView() {
        mList = new ArrayList<>();
        mAdapter = new MapSearchAdapter(mList);
        mPoiSugRV.setLayoutManager(new LinearLayoutManager(this));
        mPoiSugRV.setAdapter(mAdapter);
    }


    private void getMyLocation() {
        if (EasyPermissions.hasPermissions(this, AllPermissionRequestCode.PERMISSION_LOCATION)) {
            initNewLocation();
        } else {
            String[] perms = AllPermissionRequestCode.PERMISSION_LOCATION;
            EasyPermissions.requestPermissions(this, getString(R.string.m201_location),
                    RC_LOCATION, perms);
        }
    }

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void initData() {
        imei = getIntent().getStringExtra("imei");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //显示经纬度信息
        if (!TextUtils.isEmpty(mLat) && !TextUtils.isEmpty(mLng)) {
            double vLat = Double.parseDouble(mLat);
            double vLng = Double.parseDouble(mLng);
            LatLng location = new LatLng(vLat, vLng);
            getAddress(location);
        }
    }

    @Override
    public void showCameraList(List<CameraBean> cameraList) {

    }

    @Override
    public void showLocationSuccess(String lat, String lng) {

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        switch (requestCode) {
            case AllPermissionRequestCode.PERMISSION_LOCATION_CODE:
                if (EasyPermissions.hasPermissions(this, AllPermissionRequestCode.PERMISSION_LOCATION)) {
//                    getLocationReal();
                    initNewLocation();
                }
                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }



    @SuppressLint("MissingPermission")
    public void initNewLocation() {
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setOnMyLocationChangeListener(this);
//        mMap.setOnMyLocationButtonClickListener(this);

        try {
            Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        Location location = task.getResult();
                        //获取到当前的经纬度传入movecamera中就ok了
                     /*   if (location!=null){
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13.0f));
                        }*/
                    }
                }
            });
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }


    }

    @Override
    public void onMyLocationChange(Location location) {
        if (location != null) {
            mLocation = location;
            if (isFirst) {
                isFirst = false;
                //移动屏幕到中心点
//                moveMyLocation(location);
                //添加标记到指定经纬度
//                mMap.addMarker(new MarkerOptions().position(new LatLng(doubles[0], doubles[1])).title("Marker")
//                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
            }
        }
    }


    /**
     * 移动到我的位置
     *
     * @param location
     */
    public void moveMyLocation(Location location) {
        if (location == null) return;
        if (!GPSUtil.outOfChina(location.getLatitude(), location.getLongitude())) {
            double[] doubles = GPSUtil.gps84_To_Gcj02(location.getLatitude(), location.getLongitude());
            LatLng latLng = new LatLng(doubles[0], doubles[1]);
            moveCenter(latLng);
            placeMarkerOnMap(latLng);
        } else {
            moveCenter(location);
        }
    }

    protected void placeMarkerOnMap(LatLng location) {
        if (myLocationMark == null) {
            myLocationMark = new MarkerOptions().position(location);
            mMap.addMarker(myLocationMark);
        }
    }

    private void moveCenter(LatLng location) {
        if (location != null) {
            mCenterLatlng = location;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCenterLatlng, 13f));
        }
    }

    private void moveCenter(Location location) {
        if (location != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            moveCenter(latLng);
        }
    }

    /**
     * 逆地理编码
     *
     * @param location 定位结果
     */
    private void getAddress(LatLng location) {
        try {
            List<Address> addresses;
            if (geocoder == null) {
                geocoder = new Geocoder(this, Locale.getDefault());
            }
            String addStr = null; //结果
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
    public void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(false, 0.2f)//设置状态栏图片为深色，(如果android 6.0以下就是半透明)
                .statusBarColor(R.color.color_app_main)//这里的颜色，你可以自定义。
                .statusBarView(statusBarView)
                .init();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (centerBean == null) {
            centerBean = new MapLoctionBean();
        }
        if (mCenterLatlng == null) {
            return true;
        }


        centerBean.setLatitude(mCenterLatlng.latitude);
        centerBean.setLongitude(mCenterLatlng.longitude);
        String operationValue = mCenterLatlng.longitude + "_" + mCenterLatlng.latitude;
        presenter.control(imei, "longitude_latitude", operationValue);

        return true;
    }
}
