package com.shuoxd.camera.module.camera;

import android.content.Context;

import com.google.gson.Gson;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.bean.PictureBean;
import com.shuoxd.camera.module.home.HomeView;
import com.shuoxd.camera.module.login.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Field;

public class CameraPresenter extends BasePresenter<CameraView> {


    private int pageNow = 0;

    private int totalPage = 1;


    public CameraPresenter(Context context, CameraView baseView) {
        super(context, baseView);
    }


    /**
     * 登录
     */
    public void cameraInfo(String imei, String email) {
        //正式登录
        addDisposable(apiServer.cameraInfo(imei, email), new BaseObserver<String>(baseView, true) {

            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)) {//请求成功
                        JSONObject obj = jsonObject.optJSONObject("obj");
                        if (obj == null) return;
                        CameraBean.CameraInfo cameraInfo = new Gson().fromJson(obj.toString(), CameraBean.CameraInfo.class);
                        baseView.showCameraInfo(cameraInfo);
                    } else {
                        String msg = jsonObject.optString("msg");
                        baseView.showResultError(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {
                baseView.showServerError(msg);
            }
        });

    }


    /**
     * 获取相机相片
     */
    public void getCameraPic(String imeis, String isAllCamera,
                             String startDate, String endDate,
                             String amPm, String photoType,
                             String favorites, String moonPhase,
                             String startTemperature, String endTemperature,
                             String temperatureUnit) {


        if (pageNow>=totalPage){
            //没有更多的数据
            baseView.showNoMoreData();
        }else {
            pageNow++;
            //正式登录
            addDisposable(apiServer.photoList(pageNow,imeis, isAllCamera,
                    startDate, endDate,
                    amPm, photoType,
                    favorites, moonPhase,
                    startTemperature, endTemperature,temperatureUnit),
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
                                    JSONArray jsonArray = jsonObject.optJSONArray("dataList");
                                    List<PictureBean>beans=new ArrayList<>();
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
                                refreshErrPage();
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
