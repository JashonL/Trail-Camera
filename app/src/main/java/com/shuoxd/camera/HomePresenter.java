package com.shuoxd.camera;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;


import com.google.gson.Gson;
import com.mylhyl.circledialog.res.drawable.CircleDrawable;
import com.mylhyl.circledialog.res.values.CircleColor;
import com.mylhyl.circledialog.res.values.CircleDimen;
import com.mylhyl.circledialog.view.listener.OnCreateBodyViewListener;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseBean;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.module.home.HomeView;
import com.shuoxd.camera.module.login.User;


import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;


public class HomePresenter extends BasePresenter<HomeView> {


    private int pageNow = 0;

    private int totalPage = 1;

    public HomePresenter(Context context, HomeView baseView) {
        super(context, baseView);
    }


    public void getAlldevice() throws Exception {
        if (pageNow>=totalPage){
            //没有更多的数据
            baseView.showNoMoreData();
        }else {
            pageNow++;
            String accountName = App.getUserBean().getAccountName();
            //获取设备
            addDisposable(apiServer.cameraList(pageNow, accountName), new BaseObserver<String>(baseView, true) {

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
                            List<CameraBean> cameraList = new ArrayList<>();
                            for (int i = 0; i < dataList.length(); i++) {
                                JSONObject jsonObject1 = dataList.getJSONObject(i);
                                CameraBean cameraBean = new Gson().fromJson(jsonObject1.toString(), CameraBean.class);
                                cameraList.add(cameraBean);
                            }
                            baseView.setDeviceList(cameraList);
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
