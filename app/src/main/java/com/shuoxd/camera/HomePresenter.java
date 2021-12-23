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


import com.mylhyl.circledialog.res.drawable.CircleDrawable;
import com.mylhyl.circledialog.res.values.CircleColor;
import com.mylhyl.circledialog.res.values.CircleDimen;
import com.mylhyl.circledialog.view.listener.OnCreateBodyViewListener;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseBean;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.module.home.HomeView;


import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;



public class HomePresenter  extends BasePresenter<HomeView> {

    public HomePresenter(HomeView baseView) {
        super(baseView);
    }


    public HomePresenter(Context context, HomeView baseView) {
        super(context, baseView);
    }



}
