package com.shuoxd.camera.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.hjq.toast.ToastUtils;
import com.mylhyl.circledialog.res.values.CircleColor;
import com.shuoxd.camera.module.login.User;
import com.shuoxd.camera.okhttp.OkHttpUtils;
import com.shuoxd.camera.okhttp.https.HttpsUtils;
import com.shuoxd.camera.okhttp.log.LoggerInterceptor;
import com.shuoxd.camera.utils.LogUtil;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;


public class App extends Application {

//    final String[] sampleAssets = {"Smart Led Strip User Manua.pdf","US-Wall  Switch  User Manual.pdf"};

    /*
     * 单例模式获取Application:饿汉式
     */
    public static App app;


    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        App app = (App) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }


    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }


    public static App getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        /*吐司提示*/
        ToastUtils.init(this);


        //全局初始化弹框
        initCirclerDialog();


        LogUtil.setIsLog(true);

//        initSampleAssets();

//        Crasheye.init(this, "5665c120");

        ClearableCookieJar cookieJar1 = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);

//        CookieJarImpl cookieJar1 = new CookieJarImpl(new MemoryCookieStore());
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30000L, TimeUnit.MILLISECONDS)
                .readTimeout(30000, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor("TAG", true))
                .cookieJar(cookieJar1)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);


    }


    /**
     * 防止内存泄漏使用弱引用来存activity
     */

    private List<WeakReference<Activity>> activityList = new ArrayList<>();

    public List<WeakReference<Activity>> getActivityList() {
        return activityList;
    }

    public void addActivityList(WeakReference<Activity> activity) {
        this.activityList.add(activity);
    }

    public void exit() {
        try {
            for (WeakReference weakReference : activityList) {
                Activity activity = (Activity) weakReference.get();
                if (activity != null) {
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //注销涂鸦
            System.exit(0);
        }
    }

    /**
     * 设置弹框按钮颜色等
     */
    private void initCirclerDialog() {
        CircleColor.ITEM_CONTENT_TEXT = 0xFF2CDB7B;
        CircleColor.FOOTER_BUTTON_TEXT_POSITIVE = 0xFF2CDB7B;
        CircleColor.FOOTER_BUTTON_TEXT_NEGATIVE = 0xFF2CDB7B;
    }


    public static User userBean;

    public static User getUserBean() {
        return userBean;
    }

    public static void setUserBean(User userBean) {
        App.userBean = userBean;
    }


    //是否已登录
    public static boolean IS_LOGIN = false;


}
