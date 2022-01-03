package com.shuoxd.camera.module.leftmenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.contrarywind.view.WheelView;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.components.ImmersionFragment;
import com.shuoxd.camera.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeNavigationViewFragment extends ImmersionFragment {


    protected ImmersionBar mImmersionBar;
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_filter)
    TextView tvFilter;
    @BindView(R.id.tv_photo)
    TextView tvPhoto;
    @BindView(R.id.rb_all)
    RadioButton rbAll;
    @BindView(R.id.rb_hd)
    RadioButton rbHd;
    @BindView(R.id.rg_photo)
    RadioGroup rgPhoto;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.v_date_background)
    View vDateBackground;
    @BindView(R.id.tv_date_start)
    EditText tvDateStart;
    @BindView(R.id.tv_date_center)
    TextView tvDateCenter;
    @BindView(R.id.tv_date_end)
    EditText tvDateEnd;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.rb_am)
    RadioButton rbAm;
    @BindView(R.id.rb_pm)
    RadioButton rbPm;
    @BindView(R.id.rg_time)
    RadioGroup rgTime;
    @BindView(R.id.tv_favorites)
    TextView tvFavorites;
    @BindView(R.id.cb_favorites)
    CheckBox cbFavorites;
    @BindView(R.id.tv_temp)
    TextView tvTemp;
    @BindView(R.id.rb_f)
    RadioButton rbF;
    @BindView(R.id.rb_c)
    RadioButton rbC;
    @BindView(R.id.rg_temp)
    RadioGroup rgTemp;
    @BindView(R.id.wheel_start)
    WheelView wheelStart;
    @BindView(R.id.tv_to)
    TextView tvTo;
    @BindView(R.id.wheel_end)
    WheelView wheelEnd;
    @BindView(R.id.tv_lunar_phase)
    TextView tvLunarPhase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leftmenu_layout, null);
        ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(false, 0.2f)//设置状态栏图片为深色，(如果android 6.0以下就是半透明)
                .statusBarView(statusBarView)
                .statusBarColor(R.color.color_app_main)//这里的颜色，你可以自定义。
                .init();
    }
}
