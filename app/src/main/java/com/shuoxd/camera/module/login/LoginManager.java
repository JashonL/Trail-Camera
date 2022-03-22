package com.shuoxd.camera.module.login;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Message;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.shuoxd.camera.MainActivity;
import com.shuoxd.camera.MainActivity2;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BaseView;
import com.shuoxd.camera.constants.SharePreferenConstants;
import com.shuoxd.camera.http.API;
import com.shuoxd.camera.http.RetrofitService;
import com.shuoxd.camera.noleakHandler.NoLeakHandler;
import com.shuoxd.camera.utils.SharedPreferencesUnit;

import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LoginManager {

    private CompositeDisposable compositeDisposable;
    public Context context;
    public LoginView baseView;
    /**
     * 这个后面可以直接用   Example：apiServer.login(username, password)；
     */
    protected API.WAZApi apiServer = RetrofitService.getInstance().getApiService();

    public LoginManager(Context context) {
        this.context=context;
    }



    /**
     * 解除绑定
     */
    public void detachView() {
        removeDisposable();
    }



    public void addDisposable(Observable<?> observable, BaseObserver observer) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable
                .add(observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(observer));
    }

    private void removeDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }


    public void activityFinish(){
        ((Activity)context).finish();
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

    }
}
