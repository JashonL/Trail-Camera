package com.shuoxd.camera.module.message;

import android.content.Context;

import com.google.gson.Gson;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.bean.MessageBean;
import com.shuoxd.camera.bean.QuestionBean;
import com.shuoxd.camera.bean.SetBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessagePresenter  extends BasePresenter<MessageView> {


    private int pageNow = 0;
    private int totalPage = 1;




    private int qsPageNow = 0;
    private int qsTotalPage = 1;


    public MessagePresenter(Context context, MessageView baseView) {
        super(context, baseView);
    }



    public void getMessage() {
        if (pageNow>=totalPage){
            //没有更多的数据
            baseView.showNoMoreData();
        }else {
            pageNow++;
            String accountName = App.getUserBean().getAccountName();
            //获取设备
            addDisposable(apiServer.messageList(pageNow,accountName), new BaseObserver<String>(baseView,true) {

                @Override
                public void onSuccess(String bean) {
                    try {
                        JSONObject jsonObject = new JSONObject(bean);
                        String result = jsonObject.optString("result");
                        if ("0".equals(result)){//请求成功
                            JSONObject obj = jsonObject.optJSONObject("obj");
                            if (obj==null)return;
                            totalPage = obj.optInt("pageTotal");
                            pageNow = obj.optInt("pageNow");
                            JSONArray dataList = obj.getJSONArray("dataList");
                            //解析相机数据
                            List<MessageBean> messageBeans=new ArrayList<>();
                            for (int i = 0; i < dataList.length(); i++) {
                                JSONObject jsonObject1 = dataList.getJSONObject(i);
                                MessageBean msgBean=new MessageBean();
                                MessageBean.MessageInfo info=new Gson().fromJson(jsonObject1.optJSONObject("message").toString(),MessageBean.MessageInfo.class);
                                MessageBean.UserInfo userInfo=new Gson().fromJson(jsonObject1.optJSONObject("message2User").toString(),MessageBean.UserInfo.class);
                                msgBean.setInfo(info);
                                msgBean.setUser(userInfo);
                                messageBeans.add(msgBean);
                            }
                            baseView.showMessage(messageBeans);
                        }else {
                            String msg = jsonObject.optString("msg");
                            baseView.showResultError(msg);
                            refreshErrPage();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        refreshErrPage();
                    }
                }

                @Override
                public void onError(String msg) {
                    refreshErrPage();
                    baseView.showServerError(msg);
                }
            });
        }




    }



    private void refreshErrPage() {
        baseView.showMoreFail();
        if (pageNow > 1) {
            pageNow--;
        }
    }

    public void setPageNow(int pageNow) {
        this.pageNow = pageNow;
    }


    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public void setQsPageNow(int qsPageNow) {
        this.qsPageNow = qsPageNow;
    }

    public void setQsTotalPage(int qsTotalPage) {
        this.qsTotalPage = qsTotalPage;
    }

    public int getPageNow() {
        return pageNow;
    }

    public int getTotalPage() {
        return totalPage;
    }



    public void getQuestion() {
        if (qsPageNow>=qsTotalPage){
            //没有更多的数据
            baseView.showNoQuestion();
        }else {
            qsPageNow++;
            //获取设备
            String accountName = App.getUserBean().getAccountName();
            //获取设备
            addDisposable(apiServer.questionList(qsPageNow,accountName), new BaseObserver<String>(baseView,true) {

                @Override
                public void onSuccess(String bean) {
                    try {
                        JSONObject jsonObject = new JSONObject(bean);
                        String result = jsonObject.optString("result");
                        if ("0".equals(result)){//请求成功
                            JSONObject obj = jsonObject.optJSONObject("obj");
                            if (obj==null)return;
                            qsPageNow = obj.optInt("pageTotal");
                            qsPageNow = obj.optInt("pageNow");
                            JSONArray dataList = obj.getJSONArray("dataList");
                            //解析相机数据
                            List<QuestionBean> questionBeans=new ArrayList<>();
                            for (int i = 0; i < dataList.length(); i++) {
                                JSONObject jsonObject1 = dataList.getJSONObject(i);
                                QuestionBean info=new Gson().fromJson(jsonObject1.toString(),QuestionBean.class);
                                questionBeans.add(info);
                            }
                            baseView.showQuestion(questionBeans);
                        }else {
                            String msg = jsonObject.optString("msg");
                            baseView.showResultError(msg);
                            refreshErrPage();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        refreshErrPage();
                    }
                }

                @Override
                public void onError(String msg) {
                    refreshErrPage();
                    baseView.showServerError(msg);
                }
            });


        }




    }


}
