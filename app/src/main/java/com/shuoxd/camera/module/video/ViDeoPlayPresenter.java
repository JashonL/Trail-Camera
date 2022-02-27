package com.shuoxd.camera.module.video;

import android.content.Context;

import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.module.pictrue.BigImageView;

import org.json.JSONObject;

public class ViDeoPlayPresenter extends BasePresenter<VideoPlayView> {
    public ViDeoPlayPresenter(Context context, VideoPlayView baseView) {
        super(context, baseView);
    }



    /**
     * 登录
     */
    public void operation(String photoId,String operationType,String operationValue) {
        //正式登录
        addDisposable(apiServer.operation(photoId,operationType,operationValue), new BaseObserver<String>(baseView, true) {

            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)) {//请求成功
                        switch (operationType){
                            case "favorites":
                                baseView.showCollecMsg(operationValue);
                                break;
                            case "remove":
                                baseView.delete(photoId);
                                break;
                            case "resolution":
                                String msg = jsonObject.optString("msg");
                                baseView.dowload(photoId,msg,operationValue);
                                break;
                        }

                    }
                    else if ("10000".equals(result)) {
                        userReLogin(context, () -> {
                            operation( photoId, operationType, operationValue);
                        });
                    }
                    else {
                        String msg = jsonObject.optString("msg");

                        baseView.showResultError(msg);
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
