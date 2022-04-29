package com.shuoxd.camera.module.me;

import android.content.Context;

import com.google.gson.Gson;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.bean.YearBean;
import com.shuoxd.camera.constants.SharePreferenConstants;
import com.shuoxd.camera.module.home.HomeView;
import com.shuoxd.camera.module.login.User;
import com.shuoxd.camera.utils.SharedPreferencesUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class MePresenter extends BasePresenter<MeView> {

    public MePresenter(Context context, MeView baseView) {
        super(context, baseView);
    }


    /**
     * 登录
     */
    public void userLogout(String username) {
        //正式登录
        addDisposable(apiServer.loginOut(username), new BaseObserver<String>(baseView, true) {

            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)) {//请求成功
                        //用户解析
                        SharedPreferencesUnit.getInstance(context).put(SharePreferenConstants.SP_AUTO_LOGIN, "0");
                        baseView.logout();
                    } else {
                        String msg = jsonObject.optString("msg");
                        baseView.showLoginError(msg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {
                baseView.showLoginError(msg);
            }
        });

    }




    public void userCenter(String userName) {
        //获取设备
        addDisposable(apiServer.userCenter(userName), new BaseObserver<String>(baseView, true) {

            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)) {//请求成功
                        JSONObject obj = jsonObject.optJSONObject("obj");
                        String totalUploadCount = obj.optString("totalUploadCount");
                        String totalCamreaCount = obj.optString("totalCamreaCount");
                        String totalVideoCount = obj.optString("totalVideoCount");

                        baseView.photoCount(totalUploadCount);
                        baseView.cameraCount(totalCamreaCount);
                        baseView.videoCount(totalVideoCount);

                    } else if ("10000".equals(result)) {
                        userReLogin(context, () -> {
                            userCenter(userName);
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
