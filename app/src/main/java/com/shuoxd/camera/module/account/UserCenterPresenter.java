package com.shuoxd.camera.module.account;

import android.app.Activity;
import android.content.Context;

import com.hjq.toast.ToastUtils;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.eventbus.FreshCameraList;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import retrofit2.http.Field;

public class UserCenterPresenter extends BasePresenter<UserCenterView> {

    public UserCenterPresenter(Context context, UserCenterView baseView) {
        super(context, baseView);
    }


    public void modifyUserInfo(String firstName, String lastName,
                               String address, String addressDetail,
                               String country, String state,
                               String city, String zipCode, String mobileNum) {
        addDisposable(apiServer.modifyUserInfo(firstName, lastName,address,addressDetail,country,state,city,zipCode,mobileNum), new BaseObserver<String>(baseView,
                true) {
            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)) {//请求成功
                        //通知刷新列表
//                        EventBus.getDefault().post(new FreshCameraList());
                        String msg = jsonObject.getString("msg");
                        ToastUtils.show(msg);

                    } else {
                        String msg = jsonObject.getString("msg");
                        ToastUtils.show(msg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {

            }
        });
    }


}
