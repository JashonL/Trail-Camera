package com.shuoxd.camera.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class ProgramBean implements MultiItemEntity {

    private int itemType;

    private String title;
    private String id;
    private String planType;
    private String cost;
    private String photoCount;
    private String videoCount;
    private String hqCount;
    private String cameraCount;
    private String selected;


    private String costs;
    private String photos;
    private String videcPreviews;
    private String datas;


    public String getCosts() {
        return costs;
    }

    public void setCosts(String costs) {
        this.costs = costs;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getVidecPreviews() {
        return videcPreviews;
    }

    public void setVidecPreviews(String videcPreviews) {
        this.videcPreviews = videcPreviews;
    }

    public String getDatas() {
        return datas;
    }

    public void setDatas(String datas) {
        this.datas = datas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getPhotoCount() {
        return photoCount;
    }

    public void setPhotoCount(String photoCount) {
        this.photoCount = photoCount;
    }

    public String getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(String videoCount) {
        this.videoCount = videoCount;
    }

    public String getHqCount() {
        return hqCount;
    }

    public void setHqCount(String hqCount) {
        this.hqCount = hqCount;
    }

    public String getCameraCount() {
        return cameraCount;
    }

    public void setCameraCount(String cameraCount) {
        this.cameraCount = cameraCount;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
