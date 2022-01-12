package com.shuoxd.camera.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;


public class AddImageBean implements MultiItemEntity {

    private int type;

    private String path;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
