package com.shuoxd.camera.module.gallery;

import android.content.Context;

import com.google.gson.Gson;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.bean.PictureBean;
import com.shuoxd.camera.module.home.HomeView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PhotoPresenter extends BasePresenter<PhotoView> {

    private int pageNow = 0;

    private int totalPage = 1;


    //请求参数
    private String imeis;
    private String isAllCamera;
    private String startDate;
    private String endDate;
    private String amPm;
    private String photoType;
    private String favorites;
    private String moonPhase;
    private String startTemperature;
    private String endTemperature;
    private String temperatureUnit;


    public PhotoPresenter(Context context, PhotoView baseView) {
        super(context, baseView);
        defautParams();

    }

    public void defautParams() {
        pageNow = 0;
        totalPage = 1;
        imeis="-1";
        isAllCamera="1";
        startDate="-1";
        endDate="-1";
        amPm="-1";
        photoType="-1";
        favorites="-1";
        moonPhase="-1";
        startTemperature="0";
        endTemperature="0";
        temperatureUnit="0";
    }


    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public String getImeis() {
        return imeis;
    }

    public void setImeis(String imeis) {
        this.imeis = imeis;
    }

    public String getIsAllCamera() {
        return isAllCamera;
    }

    public void setIsAllCamera(String isAllCamera) {
        this.isAllCamera = isAllCamera;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getAmPm() {
        return amPm;
    }

    public void setAmPm(String amPm) {
        this.amPm = amPm;
    }

    public String getPhotoType() {
        return photoType;
    }

    public void setPhotoType(String photoType) {
        this.photoType = photoType;
    }

    public String getFavorites() {
        return favorites;
    }

    public void setFavorites(String favorites) {
        this.favorites = favorites;
    }

    public String getMoonPhase() {
        return moonPhase;
    }

    public void setMoonPhase(String moonPhase) {
        this.moonPhase = moonPhase;
    }

    public String getStartTemperature() {
        return startTemperature;
    }

    public void setStartTemperature(String startTemperature) {
        this.startTemperature = startTemperature;
    }

    public String getEndTemperature() {
        return endTemperature;
    }

    public void setEndTemperature(String endTemperature) {
        this.endTemperature = endTemperature;
    }

    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(String temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }

    /**
     * 获取相机相片
     */
    public void getCameraPic() {

        if (pageNow >= totalPage) {
            //没有更多的数据
            baseView.showNoMoreData();
        } else {
            pageNow++;
            //正式登录
            addDisposable(apiServer.photoList(pageNow, imeis, isAllCamera,
                    startDate, endDate,
                    amPm, photoType,
                    favorites, moonPhase,
                    startTemperature, endTemperature, temperatureUnit),
                    new BaseObserver<String>(baseView, true) {

                        @Override
                        public void onSuccess(String bean) {
                            try {
                                JSONObject jsonObject = new JSONObject(bean);
                                String result = jsonObject.optString("result");
                                if ("0".equals(result)) {//请求成功
                                    JSONObject obj = jsonObject.optJSONObject("obj");
                                    if (obj == null) return;
                                    totalPage = obj.optInt("pageTotal");
                                    pageNow = obj.optInt("pageNow");
                                    int totalNum = obj.optInt("totalNum");
                                    baseView.showTotalNum(totalNum);
                                    JSONArray jsonArray = obj.optJSONArray("dataList");
                                    List<PictureBean> beans = new ArrayList<>();
                                    assert jsonArray != null;
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                                        PictureBean pictureBean = new Gson().fromJson(jsonObject1.toString(), PictureBean.class);
                                        beans.add(pictureBean);
                                    }
                                    baseView.showCameraPic(beans);

                                } else {
                                    String msg = jsonObject.optString("msg");
                                    baseView.showResultError(msg);
                                    refreshErrPage();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(String msg) {
                            refreshErrPage();
                            baseView.showServerError(msg);
                        }
                    });

        }


    }

    private void refreshErrPage() {
        baseView.showMoreFail();
        if (pageNow > 1) {
            pageNow--;
        }
    }

    public void setPageNow(int pageNow) {
        this.pageNow = pageNow;
    }

    public int getPageNow() {
        return pageNow;
    }

    public int getTotalPage() {
        return totalPage;
    }

}
