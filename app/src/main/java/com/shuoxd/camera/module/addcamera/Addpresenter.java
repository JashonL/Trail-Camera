package com.shuoxd.camera.module.addcamera;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.shuoxd.camera.MainActivity;
import com.shuoxd.camera.MainActivity2;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.base.ReLogListener;
import com.shuoxd.camera.constants.GlobalConstant;
import com.shuoxd.camera.constants.SharePreferenConstants;
import com.shuoxd.camera.eventbus.FreshCameraList;
import com.shuoxd.camera.module.login.User;
import com.shuoxd.camera.utils.SharedPreferencesUnit;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

public class Addpresenter extends BasePresenter<AddCanmeraView> {

    String imei;


    public Addpresenter(Context context, AddCanmeraView baseView) {
        super(context, baseView);
    }

    public void initData(){
        imei = ((Activity) context).getIntent().getStringExtra(GlobalConstant.SCAN_RESULT);
        if (!TextUtils.isEmpty(imei)){
            baseView.showImei(imei);
        }
    }


    public void addCamera(String imei,String name){
        addDisposable(apiServer.addCamera(imei,name),  new BaseObserver<String>(baseView,
                true) {
            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)){//请求成功
                        //通知刷新列表
                        EventBus.getDefault().post(new FreshCameraList());


                        String accountName = App.getUserBean().getAccountName();
                        String password = App.getUserBean().getPassword();
                        if (App.IS_LOGIN){
                            userLogin(accountName,password);

                        }else {
                            ((Activity) context).finish();

                        }
                    }
                    else if ("10000".equals(result)){
                        userReLogin(context, () -> {
                            addCamera( imei, name);
                        });
                    }
                    else {
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



    /**
     * 登录/注册 成功，保存用户信息
     */
    public void savaUserInfo(String username, String password, User user) {
        SharedPreferencesUnit.getInstance(context).put(SharePreferenConstants.SP_USER_NAME, username);
        SharedPreferencesUnit.getInstance(context).put(SharePreferenConstants.SP_USER_PASSWORD, password);
        App.setUserBean(user);
    }





    private void loginSuccess(){
        Intent intent = new Intent(context, MainActivity2.class);
        context. startActivity(intent,
                ActivityOptions.makeSceneTransitionAnimation((Activity) context).toBundle());
        ((Activity) context).finish();
    }

}
