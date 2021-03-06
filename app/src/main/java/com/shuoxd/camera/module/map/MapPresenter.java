package com.shuoxd.camera.module.map;

import android.content.Context;

import com.google.gson.Gson;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.eventbus.FreshCameraLocation;
import com.shuoxd.camera.utils.MyToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapPresenter extends BasePresenter<IMapView> {


    private List<CameraBean> cameraList = new ArrayList<>();


    public MapPresenter(Context context, IMapView baseView) {
        super(context, baseView);
    }


    public void getAlldevice() {
        String accountName = App.getUserBean().getAccountName();
        //获取设备
        addDisposable(apiServer.allCameraList(accountName), new BaseObserver<String>(baseView, true) {

            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)) {//请求成功
                        JSONArray obj = jsonObject.getJSONArray("obj");
                        //解析相机数据
                        cameraList.clear();
                        for (int i = 0; i < obj.length(); i++) {
                            JSONObject jsonObject1 = obj.getJSONObject(i);
                            CameraBean cameraBean = new Gson().fromJson(jsonObject1.toString(), CameraBean.class);
                            cameraList.add(cameraBean);
                        }
                        baseView.showCameraList(cameraList);
                    }else if ("10000".equals(result)) {
                        userReLogin(context, () -> {
                            getAlldevice();
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {
                baseView.showServerError(msg);
            }
        });


    }


    public List<CameraBean> getCameraList() {
        return cameraList;
    }




    public void control(String imei, String operationType, String operationValue) {
        //正式登录
        addDisposable(apiServer.control(imei, operationType,operationValue), new BaseObserver<String>(baseView, true) {

            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)) {//请求成功
                        String msg = jsonObject.optString("msg");
                        MyToastUtils.toast(msg);
                        String[] location = operationValue.split("_");

                        FreshCameraLocation freshCameraLocation = new FreshCameraLocation();
                        freshCameraLocation.setLat(location[1]);
                        freshCameraLocation.setLng(location[0]);
                        EventBus.getDefault().post(freshCameraLocation);


                    }


                    else if ("10000".equals(result)){
                        userReLogin(context, () -> {
                            control( imei,  operationType,  operationValue);
                        });
                    }

                    else {
                        String msg = jsonObject.optString("msg");
                        baseView.showResultError(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {
                baseView.showServerError(msg);
            }
        });

    }



}
