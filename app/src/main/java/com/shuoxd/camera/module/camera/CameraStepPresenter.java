package com.shuoxd.camera.module.camera;

import android.content.Context;
import android.graphics.Camera;

import com.google.gson.Gson;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.bean.DeviceSettingBean;
import com.shuoxd.camera.utils.MyToastUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.http.Field;

public class CameraStepPresenter extends BasePresenter<CameraStepView> {

    public CameraStepPresenter(Context context, CameraStepView baseView) {
        super(context, baseView);
    }


    public void cameraParamter(String imei, String email) {
        //正式登录
        addDisposable(apiServer.cameraParamter(imei, email), new BaseObserver<String>(baseView, true) {

            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)) {//请求成功
                        JSONObject obj = jsonObject.optJSONObject("obj");
                        if (obj == null) return;
                        Iterator<String> keys = obj.keys();
                        List<DeviceSettingBean> newList = new ArrayList<>();
                        while (keys.hasNext()) {
                            DeviceSettingBean settingBean = new DeviceSettingBean();
                            String next = keys.next();
                            String value = obj.optString(next);
                            settingBean.setKey(next);
                            settingBean.setValue(value);
                            newList.add(settingBean);
                        }
                        baseView.showSetting(newList);
                    } else {
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




    public void control(String imei, String operationType, String operationValue) {
        //正式登录
        addDisposable(apiServer.control(imei, operationType,operationValue), new BaseObserver<String>(baseView, true) {

            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)) {//请求成功
                        baseView.cameraSetSuccess(operationType,operationValue);
                        String msg = jsonObject.optString("msg");
                        MyToastUtils.toast(msg);
                    } else {
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



    public void cameraOperation(String imei, String operationType, String operationValue) {
        //修改别名
        addDisposable(apiServer.operation_camera(imei, operationType,operationValue), new BaseObserver<String>(baseView, true) {

            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)) {//请求成功
                        baseView.cameraSetSuccess(operationType,operationValue);
                        String msg = jsonObject.optString("msg");
                        MyToastUtils.toast(msg);
                    } else {
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
