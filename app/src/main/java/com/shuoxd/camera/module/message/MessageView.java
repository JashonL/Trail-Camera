package com.shuoxd.camera.module.message;

import com.shuoxd.camera.base.BaseView;
import com.shuoxd.camera.bean.MessageBean;
import com.shuoxd.camera.bean.QuestionBean;

import java.util.List;

public interface MessageView extends BaseView {

    void showMessage(List<MessageBean> msgList);

    void showNoMoreData();


    void showMoreFail();


    void showQuestion(List<QuestionBean> msgList);

    void showNoQuestion();

    void showQuetionMoreFail();


    void delete();


}
