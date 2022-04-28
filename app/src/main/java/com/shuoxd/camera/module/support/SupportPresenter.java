package com.shuoxd.camera.module.support;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.shuoxd.camera.R;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.bean.SupportBean;
import com.shuoxd.camera.utils.MyToastUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SupportPresenter extends BasePresenter<SupportView> {
    public SupportPresenter(Context context, SupportView baseView) {
        super(context, baseView);
    }



    public void faqList(String email) {
        //获取设备
        addDisposable(apiServer.faqList(email), new BaseObserver<String>(baseView, true) {

            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)) {//请求成功
                        JSONArray obj = jsonObject.getJSONArray("obj");
                        List<SupportBean>list=new ArrayList<>();
                        for (int i = 0; i < obj.length(); i++) {
                            JSONObject jsonObject1 = obj.optJSONObject(i);
                            SupportBean supportBean = new Gson().fromJson(jsonObject1.toString(), SupportBean.class);
                            supportBean.setExpand(false);
                            list.add(supportBean);
                        }
                        baseView.showSupportList(list);
                    }
                    else if ("10000".equals(result)){
                        userReLogin(context, () -> {
                            faqList(email);
                        });
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
