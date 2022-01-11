package com.shuoxd.camera.bean;

public class ReplyBean {


    private String id;
    private String replyUserId;
    private String replyUsername;
    private String questionId;
    private String content;
    private String attachmentOnePath;
    private String attachmentTwoPath;
    private String attachmentThreePath;
    private String replyTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(String replyUserId) {
        this.replyUserId = replyUserId;
    }

    public String getReplyUsername() {
        return replyUsername;
    }

    public void setReplyUsername(String replyUsername) {
        this.replyUsername = replyUsername;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttachmentOnePath() {
        return attachmentOnePath;
    }

    public void setAttachmentOnePath(String attachmentOnePath) {
        this.attachmentOnePath = attachmentOnePath;
    }

    public String getAttachmentTwoPath() {
        return attachmentTwoPath;
    }

    public void setAttachmentTwoPath(String attachmentTwoPath) {
        this.attachmentTwoPath = attachmentTwoPath;
    }

    public String getAttachmentThreePath() {
        return attachmentThreePath;
    }

    public void setAttachmentThreePath(String attachmentThreePath) {
        this.attachmentThreePath = attachmentThreePath;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }
}
