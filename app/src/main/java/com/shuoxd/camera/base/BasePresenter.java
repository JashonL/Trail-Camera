package com.shuoxd.camera.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;


import com.google.gson.Gson;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.http.API;
import com.shuoxd.camera.http.RetrofitService;
import com.shuoxd.camera.module.login.LoginActivity;
import com.shuoxd.camera.module.login.User;
import com.shuoxd.camera.noleakHandler.NoLeakHandler;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Description : BasePresenter
 *
 * @author XuCanyou666
 * @date 2020/2/7
 */


public class BasePresenter<V extends BaseView> implements Handler.Callback {

    private CompositeDisposable compositeDisposable;
    public V baseView;
    public NoLeakHandler handler;
    public Context context;

    /**
     * 这个后面可以直接用   Example：apiServer.login(username, password)；
     */
    protected API.WAZApi apiServer = RetrofitService.getInstance().getApiService();

    //    public BasePresenter(V baseView) {
//        this.baseView = baseView;
//    }
    public BasePresenter(Context context, V baseView) {
        this.baseView = baseView;
        this.context = context;
    }

    public void initHandler(Context context) {
        handler = new NoLeakHandler(context, this);
    }

    /**
     * 解除绑定
     */
    public void detachView() {
        baseView = null;
        removeDisposable();
    }

    /**
     * 返回 view
     */
    public V getBaseView() {
        return baseView;
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


    public void activityFinish() {
        ((Activity) context).finish();
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        return false;
    }

    public void onDestroy() {
        if (handler != null) {
            this.handler.destroy();
        }
    }

    /**
     * 重新登录
     */
    public void userReLogin(Context context,ReLogListener logListener) {
        String username = App.getUserBean().getAccountName();
        String password = App.getUserBean().getPassword();
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
                        logListener.relogSuccess();
                    }else {
                       //失败跳转到登录界面
                        Intent intent =new Intent(context, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {
                //失败跳转到登录界面
                Intent intent =new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });

    }


}
