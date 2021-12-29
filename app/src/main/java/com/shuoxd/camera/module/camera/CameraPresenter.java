package com.shuoxd.camera.module.camera;

import android.content.Context;

import com.google.gson.Gson;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.module.home.HomeView;
import com.shuoxd.camera.module.login.User;

import org.json.JSONObject;

public class CameraPresenter extends BasePresenter<CameraView> {


    public CameraPresenter(Context context, CameraView baseView) {
        super(context, baseView);
    }


    /**
     * 登录
     */
    public void cameraInfo(String imei, String email) {
        //正式登录
        addDisposable(apiServer.login(imei, email), new BaseObserver<String>(baseView,true) {

            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)){//请求成功
                        JSONObject obj = jsonObject.optJSONObject("obj");
                        if (obj==null)return;
                        CameraBean.CameraInfo cameraInfo = new Gson().fromJson(obj.toString(), CameraBean.CameraInfo.class);
                        baseView.showCameraInfo(cameraInfo);
                    }else {
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
