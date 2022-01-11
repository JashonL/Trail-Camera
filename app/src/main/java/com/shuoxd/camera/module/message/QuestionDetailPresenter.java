package com.shuoxd.camera.module.message;

import android.content.Context;

import com.google.gson.Gson;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.bean.QuestionBean;
import com.shuoxd.camera.bean.ReplyBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuestionDetailPresenter extends BasePresenter<QuestionDetailView> {


    public QuestionDetailPresenter(Context context, QuestionDetailView baseView) {
        super(context, baseView);
    }


    public void questionDetail(String id, String email) {
        //获取设备
        addDisposable(apiServer.question(id, email), new BaseObserver<String>(baseView, true) {

            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)) {//请求成功
                        JSONObject obj = jsonObject.optJSONObject("obj");
                        if (obj == null) return;
                        JSONObject qsBean = obj.optJSONObject("question");
                        QuestionBean questionBean = new Gson().fromJson(qsBean.toString(), QuestionBean.class);
                        baseView.showQuestion(questionBean);

                        JSONArray jsonArray = obj.optJSONArray("questionReplyList");
                        List<ReplyBean> beans = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                            ReplyBean bean1 = new Gson().fromJson(jsonObject1.toString(), ReplyBean.class);
                            beans.add(bean1);
                        }
                        baseView.showReply(beans);

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
