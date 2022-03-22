package com.shuoxd.camera.module.map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.MapSearchAdapter;
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
import pub.devrel.easypermissions.EasyPermissions;

import static com.shuoxd.camera.constants.PermissionConstant.RC_LOCATION;

public class LocationActivity
       extends BaseActivity<MapPresenter> implements IMapView,
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,GoogleMap.OnCameraIdleListener ,Toolbar.OnMenuItemClickListener, GoogleMap.OnMyLocationChangeListener{



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

    private String mLng = "0";
    private String mLat = "0";

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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected MapPresenter createPresenter() {
        return new MapPresenter(this,this);
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

        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        ButterKnife.bind(this);


        toolbar.inflateMenu(R.menu.menu_text);
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
            moveCenter(new LatLng(pt.latitude,pt.longitude));
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


    @Override
    protected void initData() {
        imei = getIntent().getStringExtra("imei");
        //显示经纬度信息
        if (!TextUtils.isEmpty(mLat)&&!TextUtils.isEmpty(mLng)){
            double vLat = Double.parseDouble(mLat);
            double vLng = Double.parseDouble(mLng);
            LatLng location=new LatLng(vLat,vLng);
            getAddress(location);
        }
    }

    @Override
    public void showCameraList(List<CameraBean> cameraList) {

    }

    @Override
    public void showLocationSuccess(String lat,String lng) {

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
    }

    @Override
    public void onMyLocationChange(Location location) {
        if (location != null) {
            mLocation = location;
            if (isFirst) {
                isFirst = false;
                //移动屏幕到中心点
                moveMyLocation(location);
                //添加标记到指定经纬度
//                mMap.addMarker(new MarkerOptions().position(new LatLng(doubles[0], doubles[1])).title("Marker")
//                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
            }
        }
    }



    /**
     * 移动到我的位置
     * @param location
     */
    public void moveMyLocation(Location location) {
        if (location == null) return;
        if (!GPSUtil.outOfChina(location.getLatitude(), location.getLongitude())) {
            double[] doubles = GPSUtil.gps84_To_Gcj02(location.getLatitude(), location.getLongitude());
            LatLng latLng = new LatLng(doubles[0], doubles[1]);
            moveCenter(latLng);
            placeMarkerOnMap(latLng);
        }else {
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
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCenterLatlng, 16f));
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
            if (geocoder == null){
                geocoder = new Geocoder(this, Locale.getDefault());
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
            mTvMarker.setText(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initImmersionBar() {
      /*  mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(false, 0.2f)//设置状态栏图片为深色，(如果android 6.0以下就是半透明)
                .statusBarColor(R.color.color_app_main)//这里的颜色，你可以自定义。
                .statusBarView(statusBarView)
                .init();*/

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (centerBean == null){
            centerBean = new MapLoctionBean();
        }
        if (mCenterLatlng !=null) {
            centerBean.setLatitude(mCenterLatlng.latitude);
            centerBean.setLongitude(mCenterLatlng.longitude);
        }


        String operationValue=mCenterLatlng.longitude+"_"+mCenterLatlng.latitude;
        presenter.control(imei, "longitude_latitude", operationValue);

        return true;
    }
}
