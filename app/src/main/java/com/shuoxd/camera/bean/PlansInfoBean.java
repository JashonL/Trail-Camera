package com.shuoxd.camera.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class PlansInfoBean implements MultiItemEntity {

    private int itemType;

    private String title;
    private String value;
    private String usedPhoto;
    private String usedHDPhoto;
    private String usedVideo;
    private String packagePhoto;
    private String packageHDPhoto;
    private String packageVideo;


    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUsedPhoto() {
        return usedPhoto;
    }

    public void setUsedPhoto(String usedPhoto) {
        this.usedPhoto = usedPhoto;
    }

    public String getUsedHDPhoto() {
        return usedHDPhoto;
    }

    public void setUsedHDPhoto(String usedHDPhoto) {
        this.usedHDPhoto = usedHDPhoto;
    }

    public String getUsedVideo() {
        return usedVideo;
    }

    public void setUsedVideo(String usedVideo) {
        this.usedVideo = usedVideo;
    }

    public String getPackagePhoto() {
        return packagePhoto;
    }

    public void setPackagePhoto(String packagePhoto) {
        this.packagePhoto = packagePhoto;
    }

    public String getPackageHDPhoto() {
        return packageHDPhoto;
    }

    public void setPackageHDPhoto(String packageHDPhoto) {
        this.packageHDPhoto = packageHDPhoto;
    }

    public String getPackageVideo() {
        return packageVideo;
    }

    public void setPackageVideo(String packageVideo) {
        this.packageVideo = packageVideo;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
