package com.shuoxd.camera.module.me;

import android.content.Context;

import com.google.gson.Gson;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.constants.SharePreferenConstants;
import com.shuoxd.camera.module.home.HomeView;
import com.shuoxd.camera.module.login.User;
import com.shuoxd.camera.utils.SharedPreferencesUnit;

import org.json.JSONObject;

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

}
