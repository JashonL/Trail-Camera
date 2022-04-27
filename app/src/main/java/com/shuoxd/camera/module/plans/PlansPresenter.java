package com.shuoxd.camera.module.plans;

import android.content.Context;

import com.google.gson.Gson;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.module.pictrue.BigImageView;
import com.shuoxd.camera.utils.Mydialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlansPresenter extends BasePresenter<PlansView> {

    private List<CameraBean> cameraList = new ArrayList<>();



    public PlansPresenter(Context context, PlansView baseView) {
        super(context, baseView);
    }



    public void getAlldevice() {
        String accountName = App.getUserBean().getAccountName();
        Mydialog.show(context);
        //获取设备
        addDisposable(apiServer.allCameraList(accountName), new BaseObserver<String>(baseView, false) {

            @Override
            public void onSuccess(String bean) {
                Mydialog.dissmiss();
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

                        baseView.showList(cameraList);

                   /*     CameraBean cameraBean =new CameraBean();
                        cameraBean.setTotalPhotoNum(String.valueOf(totalNum));
                        CameraBean.CameraInfo info=new CameraBean.CameraInfo();
                        info.setAlias(context.getString(R.string.m77_all_camera));
                        cameraBean.setCamera(info);
                        cameraBean.setSelected(true);
                        cameraList.add(0,cameraBean);*/

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

}
