package com.shuoxd.camera.module.bill;

import android.content.Context;

import com.google.gson.Gson;
import com.shuoxd.camera.R;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.bean.YearBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

public class BillingChartPresenter extends BasePresenter<BillChartView> {

    private String imei;
    private String isAllCamera;


    private List<CameraBean> cameraList = new ArrayList<>();


    public BillingChartPresenter(Context context, BillChartView baseView) {
        super(context, baseView);
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

                        CameraBean cameraBean = new CameraBean();
                        cameraBean.setTotalPhotoNum(String.valueOf(totalNum));
                        CameraBean.CameraInfo info = new CameraBean.CameraInfo();
                        info.setAlias(context.getString(R.string.m77_all_camera));
                        cameraBean.setCamera(info);
                        cameraBean.setSelected(true);
                        cameraList.add(0, cameraBean);

                    } else if ("10000".equals(result)) {
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


    public void billLogChart() {
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
         String nowDate = sdf.format(new Date());
        //获取设备
        addDisposable(apiServer.billLogChart(imei, isAllCamera, nowDate), new BaseObserver<String>(baseView, true) {

            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)) {//请求成功
                        JSONObject obj = jsonObject.optJSONObject("obj");
                        List<YearBean> list = new ArrayList<>();
                        Iterator<String> keys = obj.keys();
                        while (keys.hasNext()) {
                            YearBean bean1 = new YearBean();
                            String year = keys.next();
                            bean1.setYear(year);
                            JSONArray jsonArray = obj.optJSONArray(year);
                            if (jsonArray == null) return;
                            int[] data = new int[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                int i1 = jsonArray.optInt(i);
                                data[i] = i1;
                            }
                            bean1.setData(data);
                            list.add(bean1);
                        }

                        baseView.showChartData(list);
                    } else if ("10000".equals(result)) {
                        userReLogin(context, () -> {
                            billLogChart();
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
}
