package com.shuoxd.camera.module.account;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.shuoxd.camera.R;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.module.login.User;
import com.shuoxd.camera.utils.MyToastUtils;

import org.json.JSONObject;

public class FindPassWordPresenter extends BasePresenter<FindPassWordView> {

    private String type;


    public FindPassWordPresenter(Context context, FindPassWordView baseView) {
        super(context, baseView);
         type = ((Activity) context).getIntent().getStringExtra("type");
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void findPassword(String value,String emailCode){
        addDisposable(apiServer.findPassword(type,value,emailCode), new BaseObserver<String>(baseView,
                true) {
            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    String msg = jsonObject.optString("msg");
                    if ("0".equals(result)){//请求成功
                        baseView.showSuccess(msg);
                    }else {
                        baseView.showError(msg);
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


    public void sendEmailCode(String type, String value) {
        addDisposable(apiServer.sendEmailCode(type, value), new BaseObserver<String>(baseView,
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
                        baseView.getCodeSuccess(msg);

                    } else if ("10000".equals(result)) {
                        userReLogin(context, () -> {
                            sendEmailCode(type, value);
                        });
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
