package com.shuoxd.camera.module.camera;

import android.content.Context;

import com.google.gson.Gson;
import com.shuoxd.camera.base.BaseBean;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.bean.CameraBean;

import org.json.JSONObject;

public class CameraDetailPresenter extends BasePresenter<CameraDetailView>   {


    //当前的操作
    private String currentOperation;





    public CameraDetailPresenter(Context context, CameraDetailView baseView) {
        super(context, baseView);

    }


    public String getCurrentOperation() {
        return currentOperation;
    }

    public void setCurrentOperation(String currentOperation) {
        this.currentOperation = currentOperation;
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
                                baseView.dowload(photoId,msg);
                                break;
                        }

                    } else {
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
