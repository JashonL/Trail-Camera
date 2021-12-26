package com.shuoxd.camera;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;


import com.google.gson.Gson;
import com.mylhyl.circledialog.res.drawable.CircleDrawable;
import com.mylhyl.circledialog.res.values.CircleColor;
import com.mylhyl.circledialog.res.values.CircleDimen;
import com.mylhyl.circledialog.view.listener.OnCreateBodyViewListener;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseBean;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.module.home.HomeView;
import com.shuoxd.camera.module.login.User;


import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;


public class HomePresenter  extends BasePresenter<HomeView> {

    public HomePresenter(HomeView baseView) {
        super(baseView);
    }


    public HomePresenter(Context context, HomeView baseView) {
        super(context, baseView);
    }




    public void getAlldevice() throws Exception {
        String accountName = App.getUserBean().getAccountName();
        //获取设备
        addDisposable(apiServer.cameraList(accountName), new BaseObserver<String>(baseView,true) {

            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)){//请求成功
                        JSONObject obj = jsonObject.optJSONObject("obj");
                        if (obj==null)return;
                        JSONArray dataList = obj.getJSONArray("dataList");
                        //解析相机数据
                        List<CameraBean>cameraList=new ArrayList<>();
                        for (int i = 0; i < dataList.length(); i++) {
                            JSONObject jsonObject1 = dataList.getJSONObject(i);
                            CameraBean cameraBean = new Gson().fromJson(jsonObject1.toString(), CameraBean.class);
                            cameraList.add(cameraBean);
                        }
                        baseView.setDeviceList(cameraList);
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
