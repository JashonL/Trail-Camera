package com.shuoxd.camera.module.me;

import android.content.Context;
import android.content.Intent;

import com.shuoxd.camera.R;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.bean.AppSystemDto;
import com.shuoxd.camera.constants.GlobalConstant;
import com.shuoxd.camera.module.webview.WebViewActivity;
import com.shuoxd.camera.utils.CommentUtils;

import org.json.JSONObject;

public class SettingPresenter extends BasePresenter<SettingView> {

    public SettingPresenter(Context context, SettingView baseView) {
        super(context, baseView);
    }


    public void getSystemConfig() {
        String verSionName = CommentUtils.getVerSionName(context);
        //获取设备
        addDisposable(apiServer.getSystemConfig("1",verSionName), new BaseObserver<String>(baseView, true) {

            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)) {//请求成功
                        JSONObject obj = jsonObject.optJSONObject("obj");
                        String phoneOs = obj.optString("phoneOs");
                        String needUpgrade = obj.optString("needUpgrade");
                        String forcedUpgrade = obj.optString("forcedUpgrade");
                        String nowVersion = obj.optString("nowVersion");
                        String nowVersionIsBeta = obj.optString("nowVersionIsBeta");
                        String lastVersion = obj.optString("lastVersion");
                        String lastVersionUpgradeUrl = obj.optString("lastVersionUpgradeUrl");
                        String lastVersionIsBeta = obj.optString("lastVersionIsBeta");
                        String lastVersionUpgradeDescription = obj.optString("lastVersionUpgradeDescription");

                        AppSystemDto appSystemDto=new AppSystemDto();
                        appSystemDto.setNeedUpgrade(needUpgrade);
                        appSystemDto.setForcedUpgrade(forcedUpgrade);
                        appSystemDto.setNowVersion(nowVersion);
                        appSystemDto.setNowVersionIsBeta(nowVersionIsBeta);
                        appSystemDto.setLastVersion(lastVersion);
                        appSystemDto.setLastVersionUpgradeUrl(lastVersionUpgradeUrl);
                        appSystemDto.setLastVersionIsBeta(lastVersionIsBeta);
                        appSystemDto.setLastVersionUpgradeDescription(lastVersionUpgradeDescription);
                        App.setSystemDto(appSystemDto);

                    } else if ("10000".equals(result)) {
                        userReLogin(context, () -> {
                            getSystemConfig();
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


    public void toWebView(){
        Intent intent=new Intent(context, WebViewActivity.class);
        intent.putExtra(GlobalConstant.WEB_URL,GlobalConstant.PRICY_URL);
        String s=context.getString(R.string.m281_privacy_policy);
        intent.putExtra("title",s);
        context.startActivity(intent);
    }
}
