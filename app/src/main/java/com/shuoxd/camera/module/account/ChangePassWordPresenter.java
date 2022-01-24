package com.shuoxd.camera.module.account;

import android.content.Context;

import com.hjq.toast.ToastUtils;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.constants.SharePreferenConstants;
import com.shuoxd.camera.utils.SharedPreferencesUnit;

import org.json.JSONObject;

public class ChangePassWordPresenter extends BasePresenter<ChangePassWordView> {
    public ChangePassWordPresenter(Context context, ChangePassWordView baseView) {
        super(context, baseView);
    }



    public void modifyUserInfo(String oldPassword, String newPassword,
                               String confirmPassword) {
        addDisposable(apiServer.modifyPassword(oldPassword, newPassword,confirmPassword), new BaseObserver<String>(baseView,
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
                        baseView.changePasswordSuccess();

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
