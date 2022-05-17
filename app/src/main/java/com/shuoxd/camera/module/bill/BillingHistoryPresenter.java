package com.shuoxd.camera.module.bill;

import android.content.Context;

import com.google.gson.Gson;
import com.shuoxd.camera.R;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.bean.HistoryBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BillingHistoryPresenter extends BasePresenter<BilingHistoryView> {

    private int pageNow = 0;

    private int totalPage = 1;

    private String imei;
    private String isAllCamera;
    private String alias;


    private List<CameraBean> cameraList = new ArrayList<>();


    public BillingHistoryPresenter(Context context, BilingHistoryView baseView) {
        super(context, baseView);
    }


    public void getBillLogList() {
        if (pageNow >= totalPage) {
            //没有更多的数据
            baseView.showNoMoreData();
        } else {
            pageNow++;
            //获取设备
            addDisposable(apiServer.billLogList(pageNow, imei, isAllCamera), new BaseObserver<String>(baseView, true) {

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
                            JSONArray dataList = obj.getJSONArray("dataList");
                            //解析相机数据
                            List<HistoryBean> cameraList = new ArrayList<>();
                            for (int i = 0; i < dataList.length(); i++) {
                                JSONObject jsonObject1 = dataList.getJSONObject(i);
                                HistoryBean cameraBean = new Gson().fromJson(jsonObject1.toString(), HistoryBean.class);
                                cameraList.add(cameraBean);
                            }
                            baseView.showDatalist(cameraList);
                        } else if ("10000".equals(result)) {
                            userReLogin(context, () -> {
                                try {
                                    getBillLogList();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        } else {
                            String msg = jsonObject.optString("msg");
                            baseView.showResultError(msg);
                            refreshErrPage();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        refreshErrPage();
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



    public void getAlldevice() {
        String accountName = App.getUserBean().getAccountName();
        //获取设备
        addDisposable(apiServer.allCameraList(accountName), new BaseObserver<String>(baseView, true) {

            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)) {//请求成功
                        JSONArray obj = jsonObject.getJSONArray("obj");
                        //解析相机数据
                        int totalNum = 0;
                        cameraList.clear();
                        for (int i = 0; i < obj.length(); i++) {
                            JSONObject jsonObject1 = obj.getJSONObject(i);
                            CameraBean cameraBean = new Gson().fromJson(jsonObject1.toString(), CameraBean.class);
                            String _imei = cameraBean.getCamera().getImei();
                            cameraBean.setSelected(_imei.equals(imei));
                            String totalPhotoNum = cameraBean.getTotalPhotoNum();
                            totalNum += Integer.parseInt(totalPhotoNum);
                            cameraList.add(cameraBean);
                        }

                        CameraBean cameraBean =new CameraBean();
                        cameraBean.setTotalPhotoNum(String.valueOf(totalNum));
                        CameraBean.CameraInfo info=new CameraBean.CameraInfo();
                        info.setAlias(context.getString(R.string.m77_all_camera));
                        cameraBean.setCamera(info);
                        cameraBean.setSelected(true);
                        cameraList.add(0,cameraBean);

                    }
                    else if ("10000".equals(result)){
                        userReLogin(context, () -> {
                            getAlldevice();
                        });
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




    private void refreshErrPage() {
        baseView.showMoreFail();
        if (pageNow > 1) {
            pageNow--;
        }
    }


    public int getPageNow() {
        return pageNow;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public void setPageNow(int pageNow) {
        this.pageNow = pageNow;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getIsAllCamera() {
        return isAllCamera;
    }

    public void setIsAllCamera(String isAllCamera) {
        this.isAllCamera = isAllCamera;
    }



    public List<CameraBean> getCameraList() {
        return cameraList;
    }

    public void setCameraList(List<CameraBean> cameraList) {
        this.cameraList = cameraList;
    }


    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
