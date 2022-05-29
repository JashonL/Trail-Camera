package com.shuoxd.camera.module.webview;

import android.app.Activity;
import android.content.Context;

import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.constants.GlobalConstant;
import com.shuoxd.camera.module.webview.IWebViewView;


public class WebViewPresenter extends BasePresenter<IWebViewView> {

    private String url;


    public WebViewPresenter(Context context, IWebViewView baseView) {
        super(context, baseView);
        url=((Activity)context).getIntent().getStringExtra(GlobalConstant.WEB_URL);
    }

    public void showWeb(){
        baseView.openWebView(url);
    }
}
