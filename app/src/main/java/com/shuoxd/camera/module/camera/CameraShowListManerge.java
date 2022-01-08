package com.shuoxd.camera.module.camera;

import com.shuoxd.camera.bean.PictureBean;

import java.util.List;

public class CameraShowListManerge {


    private List<PictureBean> picList;

    private static CameraShowListManerge mStance = null;


    private CameraShowListManerge() {
    }

    public static CameraShowListManerge getInstance() {
        if (mStance == null) {
            synchronized (CameraShowListManerge.class) {
                if (mStance == null) {
                    mStance = new CameraShowListManerge();
                }
            }
        }
        return mStance;
    }



    public void setPicList(List<PictureBean> picList) {
        if (null == picList) {
            return;
        }
        this.picList=picList;
    }




    public List<PictureBean> getPicList() {
        return picList;
    }




}
