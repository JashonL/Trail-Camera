package com.shuoxd.camera.module.message;

import android.content.Context;


import com.shuoxd.camera.R;
import com.shuoxd.camera.bean.MessageBean;

import java.util.ArrayList;
import java.util.List;

public class MessageManager {

    //分销商用户
    public static final int OSS_USER_CLIETNT = 1;
    //Server用户
    public static final int SERVER_USER_CLIETNT = 2;
    //客服用户
    public static final int CUSTOMSERVER_USER_CLIETNT = 3;
    //消息类型
    public static final int MESSAGE_TYPE_ACOUNT = 0;//账号信息
    public static final int MESSAGE_TYPE_DEVICE = 1;//设备信息
    public static final int MESSAGE_TYPE_RECHARGE = 2;//充值信息
    public static final int MESSAGE_TYPE_MALFUNCTION = 3;//故障信息
    public static final int MESSAGE_TYPE_SERVICE = 4;//服务信息
    public static final int MESSAGE_TYPE_NOTICE = 5;//通知信息
    public static final int MESSAGE_TYPE_REPORT = 6;//报表信息
    public static final int MESSAGE_TYPE_NEWS = 7;//资讯信息


    public static volatile MessageManager stance = null;

    //server所有消息
    private List<MessageBean> allMessageBeans = new ArrayList<>();


    private MessageManager() {
    }


    //单例获取
    public static MessageManager getStance() {
        if (stance == null) {
            synchronized (MessageManager.class) {
                if (stance == null) {
                    stance = new MessageManager();
                }
            }
        }
        return stance;
    }


    private void initMessages(Context context) {

    }


    public List<MessageBean> getAllMessageBeans(Context context) {
        if (allMessageBeans == null || allMessageBeans.size() <= 0) {
            initMessages(context);
        }
        return allMessageBeans;
    }

    public void setAllMessageBeans(List<MessageBean> allMessageBeans) {
        this.allMessageBeans = allMessageBeans;
    }


    public void clearMessage() {
        allMessageBeans.clear();
    }


}
