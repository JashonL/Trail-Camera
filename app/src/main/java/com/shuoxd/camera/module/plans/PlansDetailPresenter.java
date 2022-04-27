package com.shuoxd.camera.module.plans;

import android.content.Context;

import com.google.gson.Gson;
import com.shuoxd.camera.R;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.bean.PlansBean;
import com.shuoxd.camera.bean.PlansInfoBean;
import com.shuoxd.camera.utils.Mydialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlansDetailPresenter extends BasePresenter<PlansDetailView> {

    private List<CameraBean> cameraList = new ArrayList<>();
    //请求参数
    private String imeis;

    public PlansDetailPresenter(Context context, PlansDetailView baseView) {
        super(context, baseView);
    }




    public void getPlansInfo(String imei) {
        Mydialog.show(context);
        //获取设备
        addDisposable(apiServer.getPlan(imei), new BaseObserver<String>(baseView, false) {

            @Override
            public void onSuccess(String bean) {
                Mydialog.dissmiss();
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)) {//请求成功
                        JSONObject obj = jsonObject.getJSONObject("obj");
                        //解析相机数据
                        PlansBean cameraBean = new Gson().fromJson(obj.toString(), PlansBean.class);
                        String[]titles={
                                context.getString(R.string.m161_camera_name),
                                context.getString(R.string.m240_status),
                                context.getString(R.string.m236_iccid),
                                context.getString(R.string.m235_imei),
                                context.getString(R.string.m241_current_plans),
                                context.getString(R.string.m242_days_remaining),
                                context.getString(R.string.m243_next_bill_date),
                                ""
                        };


                        String[]values={
                          cameraBean.getCameraName(),
                          cameraBean.getStatus(),
                          cameraBean.getIccid(),
                          cameraBean.getImei(),
                          cameraBean.getPlanDescription(),
                          cameraBean.getDayRemain(),
                          cameraBean.getBillDate(),
                                ""
                        };


                        baseView.status(cameraBean.getStatus());

                        List<PlansInfoBean>list=new ArrayList<>();

                        for (int i = 0; i < titles.length; i++) {
                            PlansInfoBean bean1=new PlansInfoBean();
                            bean1.setTitle(titles[i]);
                            bean1.setValue(values[i]);
                            if (i<titles.length-1){
                                bean1.setItemType(0);
                            }else {
                                bean1.setItemType(1);
                            }
                            bean1.setUsedPhoto(cameraBean.getUsedPhoto());
                            bean1.setUsedHDPhoto(cameraBean.getUsedHDPhoto());
                            bean1.setUsedVideo(cameraBean.getUsedVideo());
                            bean1.setPackagePhoto(cameraBean.getPackagePhoto());
                            bean1.setPackageHDPhoto(cameraBean.getPackageHDPhoto());
                            bean1.setPackageVideo(cameraBean.getPackageVideo());
                            list.add(bean1);
                        }

                        baseView.showPlansInfo(list);


                    }else if ("10000".equals(result)) {
                        userReLogin(context, () -> {
                            getPlansInfo(imei);
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
                            String imei = cameraBean.getCamera().getImei();
                            cameraBean.setSelected(imeis.equals(imei));
                            String totalPhotoNum = cameraBean.getTotalPhotoNum();
                            totalNum += Integer.parseInt(totalPhotoNum);
                            cameraList.add(cameraBean);
                        }

                   /*     CameraBean cameraBean =new CameraBean();
                        cameraBean.setTotalPhotoNum(String.valueOf(totalNum));
                        CameraBean.CameraInfo info=new CameraBean.CameraInfo();
                        info.setAlias(context.getString(R.string.m77_all_camera));
                        cameraBean.setCamera(info);
                        cameraBean.setSelected(true);
                        cameraList.add(0,cameraBean);*/

                    }
                    else if ("10000".equals(result)){
                        userReLogin(context, () -> {
                            getAlldevice();
                        });
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



    private void refreshErrPage() {
        baseView.showResultError("");

    }


    public String getImeis() {
        return imeis;
    }

    public void setImeis(String imeis) {
        this.imeis = imeis;
    }

    public List<CameraBean> getCameraList() {
        return cameraList;
    }

    public void setCameraList(List<CameraBean> cameraList) {
        this.cameraList = cameraList;
    }
}
