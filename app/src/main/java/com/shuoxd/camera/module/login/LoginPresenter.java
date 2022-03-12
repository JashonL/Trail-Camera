package com.shuoxd.camera.module.login;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.shuoxd.camera.MainActivity;
import com.shuoxd.camera.R;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.constants.SharePreferenConstants;
import com.shuoxd.camera.utils.MyToastUtils;
import com.shuoxd.camera.utils.SharedPreferencesUnit;
import com.shuoxd.camera.zxing.ScanActivity;

import org.json.JSONObject;

public class LoginPresenter extends BasePresenter<LoginView> {

    public Context context;

    public LoginPresenter(Context context, LoginView baseView) {
        super(context, baseView);
        this.context=context;
    }


    /**
     * 登录/注册 成功，保存用户信息
     */
    public void savaUserInfo(String username, String password, User user) {
        SharedPreferencesUnit.getInstance(context).put(SharePreferenConstants.SP_USER_NAME, username);
        SharedPreferencesUnit.getInstance(context).put(SharePreferenConstants.SP_USER_PASSWORD, password);
        SharedPreferencesUnit.getInstance(context).put(SharePreferenConstants.SP_AUTO_LOGIN, "1");
        App.setUserBean(user);
    }


    public void getUserInfo() {
        User user=new User();
        String username = SharedPreferencesUnit.getInstance(context).get(SharePreferenConstants.SP_USER_NAME);
        String password = SharedPreferencesUnit.getInstance(context).get(SharePreferenConstants.SP_USER_PASSWORD);
        String auto = SharedPreferencesUnit.getInstance(context).get(SharePreferenConstants.SP_AUTO_LOGIN);
        user.setAccountName(username);
        user.setPassword(password);
        baseView.showUserInfo(user,auto);
    }





    public void register(String timeZone,String email, String regPassword){
        if (TextUtils.isEmpty(email)){
            MyToastUtils.toast(R.string.m63_username_cannot_empty);
            return;
        }
        if (TextUtils.isEmpty(regPassword)){
            MyToastUtils.toast(R.string.m63_password_cannot_empty);
            return;
        }

        addDisposable(apiServer.register(timeZone,email, regPassword), new BaseObserver<String>(baseView,
                true) {
            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)){//请求成功
                        JSONObject obj = jsonObject.optJSONObject("obj");
                        if (obj==null)return;
                        JSONObject user1 = obj.optJSONObject("user");
                        if (user1==null)return;
                        //注册成功
                        //用户解析
                        User userInfo = new Gson().fromJson(user1.toString(), User.class);
                        userInfo.setAccountName(email);
                        userInfo.setPassword(regPassword);
                        savaUserInfo(email, regPassword, userInfo);
                        baseView.registerSuccess();
                    }else if ("10000".equals(result)){//调用登录接口

                    } else {
                        String msg = jsonObject.optString("msg");
                        baseView.showLoginError(msg);
                    }

//                    registerSuccess();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {

            }
        });
    }



    /**
     * 登录
     */
    public void userLogin(String username, String password) {
        //正式登录
        addDisposable(apiServer.login(username, password), new BaseObserver<String>(baseView,true) {

            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)){//请求成功
                        JSONObject obj = jsonObject.optJSONObject("obj");
                        if (obj==null)return;
                        JSONObject user1 = obj.optJSONObject("user");
                        if (user1==null)return;
                        //用户解析
                        User userInfo = new Gson().fromJson(user1.toString(), User.class);
                        userInfo.setAccountName(username);
                        userInfo.setPassword(password);
                        App.IS_LOGIN=true;
                        savaUserInfo(username, password, userInfo);
                        loginSuccess();
                    }else {
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



    private void loginSuccess(){
        Intent intent = new Intent(context, MainActivity.class);
        context. startActivity(intent,
                ActivityOptions.makeSceneTransitionAnimation((Activity) context).toBundle());

    }

    public void registerSuccess(){
        Intent intent = new Intent(context, ScanActivity.class);
        intent.putExtra("type","0");
        context.startActivity(intent);

    /*    context. startActivity(intent,
                ActivityOptions.makeSceneTransitionAnimation((Activity) context).toBundle());*/

    }

}
