package com.shuoxd.camera.module.camera;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.shuoxd.camera.R;
import com.shuoxd.camera.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartActivity extends BaseActivity<ChartPresenter> implements ChartView {


    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_toolbar)
    LinearLayout appToolbar;
    @BindView(R.id.tv_subtitle)
    TextView tvSubtitle;
    @BindView(R.id.rb_day)
    RadioButton rbDay;
    @BindView(R.id.rb_week)
    RadioButton rbWeek;
    @BindView(R.id.rb_month)
    RadioButton rbMonth;
    @BindView(R.id.rb_year)
    RadioButton rbYear;
    @BindView(R.id.rb_total)
    RadioButton rbTotal;
    @BindView(R.id.rg_date)
    RadioGroup rgDate;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.chart)
    BarChart chart;
    @BindView(R.id.ll_chart)
    LinearLayout llChart;
    @BindView(R.id.tv_wheater)
    TextView tvWheater;
    @BindView(R.id.cl_chart)
    ConstraintLayout clChart;
    @BindView(R.id.btn_register)
    Button btnRegister;

    @Override
    protected ChartPresenter createPresenter() {
        return new ChartPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.acivity_photo_tracker;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
