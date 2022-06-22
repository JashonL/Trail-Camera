package com.shuoxd.camera.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class InfoHeadBean implements MultiItemEntity {

    private int iconRes;
    private String title;


    private int index;
    private int itemType;


    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
