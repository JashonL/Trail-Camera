package com.shuoxd.camera.bean;

public class MessageBean {

    private MessageInfo info;
    private UserInfo user;


    public MessageInfo getInfo() {
        return info;
    }

    public void setInfo(MessageInfo info) {
        this.info = info;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public static class MessageInfo {
        private String id;
        private String title;
        private String content;
        private String createTime;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }


    public static class UserInfo {
        private String id;
        private String sender;
        private String recever;
        private String msgId;
        private String sendStatus;
        private String receveStatus;
        private String readStatus;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public String getRecever() {
            return recever;
        }

        public void setRecever(String recever) {
            this.recever = recever;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public String getSendStatus() {
            return sendStatus;
        }

        public void setSendStatus(String sendStatus) {
            this.sendStatus = sendStatus;
        }

        public String getReceveStatus() {
            return receveStatus;
        }

        public void setReceveStatus(String receveStatus) {
            this.receveStatus = receveStatus;
        }

        public String getReadStatus() {
            return readStatus;
        }

        public void setReadStatus(String readStatus) {
            this.readStatus = readStatus;
        }
    }


}
