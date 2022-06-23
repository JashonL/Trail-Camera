package com.shuoxd.camera.module.me;

import android.content.Context;

import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.constants.SharePreferenConstants;
import com.shuoxd.camera.module.login.User;
import com.shuoxd.camera.utils.SharedPreferencesUnit;

import org.json.JSONObject;

public class DestroyPresenter extends BasePresenter<DestroyView> {


    public DestroyPresenter(Context context, DestroyView baseView) {
        super(context, baseView);
    }

    /**
     * 登录
     */
    public void deleteUser(String operationType) {

        //正式登录
        addDisposable(apiServer.deleteUser(operationType), new BaseObserver<String>(baseView, true) {

            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    String msg = jsonObject.optString("msg");

                    if ("0".equals(result)) {//请求成功
                        baseView.destroysuccess(msg);
                    } else if ("10000".equals(result)) {
                        userReLogin(context, () -> {
                            deleteUser(operationType);
                        });
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


    public void getUserInfo() {

        userReLogin(context, () -> {
            baseView.updataUser();
        });


    /*    String accountName = App.getUserBean().getAccountName();
        addDisposable(apiServer.getUserInfo(accountName), new BaseObserver<String>(baseView,
                true) {
            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)) {//请求成功
                        //通知刷新列表
//                        EventBus.getDefault().post(new FreshCameraList());
                        JSONObject object = jsonObject.optJSONObject("obj");
                        User user = new Gson().fromJson(object.toString(), User.class);
                        App.setUserBean(user);
                        baseView.updataUser();
                    } else if ("10000".equals(result)) {
                        userReLogin(context, () -> {
                            getUserInfo();
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
        });*/
    }



}
