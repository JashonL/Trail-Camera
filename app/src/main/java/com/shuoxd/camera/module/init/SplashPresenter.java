package com.shuoxd.camera.module.init;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.http.API;


import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SplashPresenter extends BasePresenter<ISplashView> {

    private String username;
    private String password;

    private String userUrl;//用户所属服务器
    public boolean isRemmenberPassword = false;

    public SplashPresenter(ISplashView baseView) {
        super(baseView);
    }

    public SplashPresenter(Context context, ISplashView baseView) {
        super(context, baseView);
        getUserInfo();
    }


    public void getUserInfo() {
    }


    public void autologin() {
        if (!isRemmenberPassword && TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            toLogin();
        } else {
            getUserType();
        }

    }


    public void toLogin() {
    }

    /**
     * 获取用户类型
     */

    public void getUserType() {
    }


    /**
     * 登录
     */
    public void userLogin(String url, String username, String password) {
    }


    /**
     * 根据国家获取国家码
     */
    public void getCountryCode() throws JSONException {
    }




}
