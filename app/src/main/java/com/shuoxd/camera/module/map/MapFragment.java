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
import com.shuoxd.camera.MainActivity;
import com.shuoxd.camera.R;
import com.shuoxd.camera.base.BaseFragment;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.bean.MapLoctionBean;
import com.shuoxd.camera.constants.GlobalConstant;
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

    @BindView(R.id.tv_battery)
    TextView tvBattery;

    @BindView(R.id.tv_ext)
    TextView tvExt;

    @BindView(R.id.tv_sdcard)
    TextView tvSdcard;


    @BindView(R.id.tv_address)
    TextView tvAddress;

    @BindView(R.id.tv_nogoogle_service)
    TextView tvNoGoogle;


    private MapView mMap;
    private GoogleMap googleMap;
    private LatLng mCenterLatlng;//??????????????????


    private List<CameraBean> cameraList;

    private List<Marker> mMarkerRainbow = new ArrayList<Marker>();
    private Geocoder geocoder;

    private MapLoctionBean centerBean;


    private CameraBean cameraBean;

    private final String GOOGLE_MAP_APP = "com.google.android.apps.maps";

    private LatLng markerLatLng;
    private final String GOOGLE_MAP_NAVI_URI = "google.navigation:q=";
    private boolean googleService;


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
        //???activity??????onCreate?????????mMapView.onCreate(savedInstanceState)???????????????
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

/*
        boolean googleService = CommentUtils.isGoogleService(getContext());
        if (!googleService) {
            tvNoGoogle.setVisibility(View.VISIBLE);
            mMap.setVisibility(View.GONE);
        } else {
            mMap.getMapAsync(this);
        }*/


        return view;
    }


    @Override
    protected void initData() {
    }


    @Override
    public void onResume() {
        super.onResume();
        mMap.onResume();

        if (GlobalConstant.isMapFirst) {
            googleService = CommentUtils.isGoogleService(getContext());
            GlobalConstant.isMapFirst = false;
        }

        if (!googleService) {
            tvNoGoogle.setVisibility(View.VISIBLE);
            mMap.setVisibility(View.GONE);
        } else {
            mMap.getMapAsync(this);
        }
    }

    /**
     * 74    * ??????????????????
     * 75    * map?????????????????????
     * 76
     */
    @Override
    public void onPause() {
        super.onPause();
        mMap.onPause();
    }


    /**
     * 85    * ??????????????????
     * 86    * map?????????????????????
     * 87
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMap.onSaveInstanceState(outState);
    }

    /**
     * 96    * ??????????????????
     * 97    * map?????????????????????
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

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        //???????????????????????????marker
        googleMap.setTrafficEnabled(true);
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setRotateGesturesEnabled(false);
        //??????????????????
  /*      try {
            MarkerOptions markerOption = new MarkerOptions();
            LatLng plantLg = new LatLng(Double.parseDouble("22.006358"), Double.parseDouble("143.530354"));
            markerOption.position(plantLg);
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(), R.drawable.camera_marker))).title("????????????");
            googleMap.addMarker(markerOption);
            moveCenter(plantLg);

            enableMyLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMapClickListener(latLng -> {
            cameraNavigation.setVisibility(View.GONE);
        });
        //??????????????????
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
            googleMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(mCenterLatlng, 0f));
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

        //??????????????????????????????
        boolean infoWindowShown = marker.isInfoWindowShown();
        if (infoWindowShown) {
            cameraNavigation.setVisibility(View.GONE);
        } else {
            cameraNavigation.setVisibility(View.VISIBLE);
        }
        //??????????????????
        cameraBean = cameraList.get(index);
        CameraBean.CameraInfo camera = cameraBean.getCamera();
        String alias = camera.getAlias();
        if (TextUtils.isEmpty(alias)) {
            alias = camera.getImei();
        }
        tvName.setText(alias);

        String signalStrength = camera.getSignalStrength();
        int wifiStrength = Integer.parseInt(signalStrength);
        if (wifiStrength == 0) {
            setTextViewDrawableTop(tvWifi, R.drawable.signal1);
        } else if (wifiStrength <= 25) {
            setTextViewDrawableTop(tvWifi, R.drawable.signal2);
        } else if (wifiStrength <= 50) {
            setTextViewDrawableTop(tvWifi, R.drawable.signal3);
        } else if (wifiStrength <= 75) {
            setTextViewDrawableTop(tvWifi, R.drawable.signal3);
        } else {
            setTextViewDrawableTop(tvWifi, R.drawable.signal4);
        }

        String batteryLevel = camera.getBatteryLevel();
        String cardSpace = camera.getCardSpace();


        String extDcLevel = camera.getExtDcLevel();
        int batteryL = Integer.parseInt(batteryLevel);
        if (batteryL == 0) {
            setTextViewDrawableTop(tvBattery, R.drawable.bat0);
        } else if (batteryL <= 25) {
            setTextViewDrawableTop(tvBattery, R.drawable.bat1);
        } else if (batteryL <= 50) {
            setTextViewDrawableTop(tvBattery, R.drawable.bat2);
        } else if (batteryL <= 75) {
            setTextViewDrawableTop(tvBattery, R.drawable.bat3);
        } else {
            setTextViewDrawableTop(tvBattery, R.drawable.bat4);
        }

        tvBattery.setText(batteryL + "%");


        int extDcl = Integer.parseInt(extDcLevel);
        if (extDcl == 0) {
            setTextViewDrawableTop(tvExt, R.drawable.ext0);
        } else if (extDcl <= 25) {
            setTextViewDrawableTop(tvExt, R.drawable.ext1);
        } else if (extDcl <= 50) {
            setTextViewDrawableTop(tvExt, R.drawable.ext2);
        } else if (extDcl <= 75) {
            setTextViewDrawableTop(tvExt, R.drawable.ext3);
        } else {
            setTextViewDrawableTop(tvExt, R.drawable.ext4);
        }

        tvExt.setText(extDcl + "%");


        int sSpace = Integer.parseInt(cardSpace);

        if (sSpace == 0) {
            setTextViewDrawableTop(tvSdcard, R.drawable.sdcard0);
        } else if (sSpace <= 19) {
            setTextViewDrawableTop(tvSdcard, R.drawable.sdcard1);
        } else if (sSpace <= 49) {
            setTextViewDrawableTop(tvSdcard, R.drawable.sdcard2);
        } else if (sSpace <= 69) {
            setTextViewDrawableTop(tvSdcard, R.drawable.sdcard3);
        } else if (sSpace <= 94) {
            setTextViewDrawableTop(tvSdcard, R.drawable.sdcard4);
        } else {
            setTextViewDrawableTop(tvSdcard, R.drawable.sdcard5);
        }

        tvSdcard.setText(sSpace + "%");


        markerLatLng = marker.getPosition();
        getAddress(markerLatLng);


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
        //???????????????
        for (int i = 0; i < cameraList.size(); i++) {
            CameraBean cameraBean = cameraList.get(i);
            String latitude = cameraBean.getCamera().getLatitude();
            String longitude = cameraBean.getCamera().getLongitude();
            latitude = latitude.substring(0, latitude.length() - 1);
            longitude = longitude.substring(0, longitude.length() - 1);
            lat = Double.parseDouble(latitude);
            lng = Double.parseDouble(longitude);
            String alias = cameraBean.getCamera().getAlias();
            //??????marker
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(
                            Double.parseDouble(latitude),
                            Double.parseDouble(longitude)))
                    .title("Marker " + i).
                            icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                    .decodeResource(getResources(), R.drawable.camera_marker))).title(alias));
            mMarkerRainbow.add(marker);
        }

        moveCenter2(new LatLng(lat, lng));


    }


    @Override
    public void showLocationSuccess(String lat, String lng) {

    }


    @OnClick({R.id.tv_details, R.id.tv_address, R.id.iv_navigation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_details:

                cameraNavigation.setVisibility(View.GONE);

                CameraBean.CameraInfo camera = cameraBean.getCamera();
                String alias = camera.getAlias();
                String imei = camera.getImei();
                showCameraInfo(imei, alias);


                break;
            case R.id.iv_navigation:
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
     * ???????????????
     */
    private void jumpLocation() {
        double lat = markerLatLng.latitude;
        double lng = markerLatLng.longitude;

//        navigateForDestination();
        if (isApplicationInstall(GOOGLE_MAP_APP)) {
            goNaviByGoogleMap(String.valueOf(lat), String.valueOf(lng));
        } else {
            MyToastUtils.toast(R.string.m205_app_not_installed);
        }
    }


    /**
     * by moos on 2017/09/18
     * func:??????????????????
     *
     * @param lat
     * @param lon
     */
    private void goNaviByGoogleMap(String lat, String lon) {

        Uri uri = Uri.parse(GOOGLE_MAP_NAVI_URI + lat + "," + lon);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }


    /**
     * by moos on 2017/09/18
     * func:????????????????????????????????????
     *
     * @param packageName
     * @return
     */
    private boolean isApplicationInstall(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }


    //??????item?????????Fragment1V2

    private void showCameraInfo(String id, String alias) {
        MainActivity main = (MainActivity) getActivity();
        main.cameraId = id;
        main.cameraAlias = alias;
        main.showCameraInfo2();
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
                geocoder = new Geocoder(getContext(), Locale.getDefault());
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
            String text = String.format("%s\n%s%s|%s:%f %s:%f", address, adds.getCountryName(), city
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
