package com.shuoxd.camera.module.me;

import android.content.Context;

import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.constants.SharePreferenConstants;
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
                    if ("0".equals(result)) {//请求成功
                        baseView.destroysuccess();
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


}
