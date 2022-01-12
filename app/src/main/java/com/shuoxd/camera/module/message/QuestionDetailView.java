package com.shuoxd.camera.module.message;

import com.shuoxd.camera.base.BaseView;
import com.shuoxd.camera.bean.QuestionBean;
import com.shuoxd.camera.bean.ReplyBean;

import java.util.List;

public interface QuestionDetailView extends BaseView {

    void showQuestion(QuestionBean questionBean);


    void showStatus(String status);

    void showReply( List<ReplyBean> beans );


    void solved();

    void replySuccess();


}
