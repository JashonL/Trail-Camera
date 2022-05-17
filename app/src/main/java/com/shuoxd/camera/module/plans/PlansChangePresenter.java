package com.shuoxd.camera.module.plans;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.shuoxd.camera.R;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.bean.PlansBean;
import com.shuoxd.camera.bean.PlansInfoBean;
import com.shuoxd.camera.bean.ProgramBean;
import com.shuoxd.camera.eventbus.FreshPlaninfo;
import com.shuoxd.camera.utils.MyToastUtils;
import com.shuoxd.camera.utils.Mydialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlansChangePresenter extends BasePresenter<PlansChangeView> {


    public PlansChangePresenter(Context context, PlansChangeView baseView) {
        super(context, baseView);
    }



    public void getPlanTemplateList(String imei) {
        Mydialog.show(context);
        //获取设备
        addDisposable(apiServer.getPlanTemplateList(imei), new BaseObserver<String>(baseView, false) {

            @Override
            public void onSuccess(String bean) {
                Mydialog.dissmiss();
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)) {//请求成功
                        JSONArray obj = jsonObject.getJSONArray("obj");
                        List<ProgramBean> monthlylist=new ArrayList<>();
                        List<ProgramBean> annualist=new ArrayList<>();
                        for (int i = 0; i < obj.length(); i++) {
                            JSONObject jsonObject1 = obj.getJSONObject(i);
                            //解析相机数据
                            ProgramBean programBean = new Gson().fromJson(jsonObject1.toString(), ProgramBean.class);
                            programBean.setItemType(1);
                            String planType = programBean.getPlanType();
                            if ("monthly".equals(planType)){
                                monthlylist.add(programBean);
                            }else {
                                annualist.add(programBean);
                            }
                        }

                        String[]titles={
                                context.getString(R.string.m247_cost),
                                context.getString(R.string.m248_photos),
                                context.getString(R.string.m219_video),
                                context.getString(R.string.m266_hq_photos)
                        };

                        ProgramBean programBean=new ProgramBean();
                        programBean.setItemType(0);
                        programBean.setCosts(titles[0]);
                        programBean.setPhotos(titles[1]);
                        programBean.setVidecPreviews(titles[2]);
                        programBean.setDatas(titles[3]);

                        monthlylist.add(0,programBean);
                        annualist.add(0,programBean);

                        baseView.showAnnual(annualist);
                        baseView.showMonthly(monthlylist);

                    }else if ("10000".equals(result)) {
                        userReLogin(context, () -> {
                            getPlanTemplateList(imei);
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




    public void modifyCameraPlan(String imei,String id) {
        Mydialog.show(context);
        //获取设备
        addDisposable(apiServer.modifyCameraPlan(imei,id), new BaseObserver<String>(baseView, false) {

            @Override
            public void onSuccess(String bean) {
                Mydialog.dissmiss();
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)) {//请求成功
                        MyToastUtils.toast(R.string.m252_plan_change);
                        EventBus.getDefault().post(new FreshPlaninfo());
                    }else if ("10000".equals(result)) {
                        userReLogin(context, () -> {
                            modifyCameraPlan(imei,id);
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


}
