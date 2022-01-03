package com.shuoxd.camera.module.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.shuoxd.camera.R;
import com.shuoxd.camera.base.BaseFragment;

import butterknife.BindView;

public class MapFragment extends BaseFragment<MapPresenter> implements IMapView {
    @BindView(R.id.map)
    MapView mapView;


    private AMap aMap;

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
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        return view;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void initImmersionBar() {

    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 74    * 方法必须重写
     * 75    * map的生命周期方法
     * 76
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }


    /**
     * 85    * 方法必须重写
     * 86    * map的生命周期方法
     * 87
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 96    * 方法必须重写
     * 97    * map的生命周期方法
     * 98
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

}
