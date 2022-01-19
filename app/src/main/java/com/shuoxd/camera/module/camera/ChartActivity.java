package com.shuoxd.camera.module.camera;

import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.CameraMulFiterAdapter;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.module.camera.chart_fragment.BarChartFrag;
import com.shuoxd.camera.module.camera.chart_fragment.BarChartMonthFrag;
import com.shuoxd.camera.module.camera.chart_fragment.BarChartTotalFrag;
import com.shuoxd.camera.module.camera.chart_fragment.BarChartWeekFrag;
import com.shuoxd.camera.module.camera.chart_fragment.BarChartYearFrag;
import com.shuoxd.camera.module.camera.chart_fragment.LineChartFrag;
import com.shuoxd.camera.module.camera.chart_fragment.PieChartFrag;
import com.shuoxd.camera.utils.CommentUtils;
import com.shuoxd.camera.utils.DateUtils;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ChartActivity extends BaseActivity<ChartPresenter> implements ChartView, TabLayout.OnTabSelectedListener, Toolbar.OnMenuItemClickListener {


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
    @BindView(R.id.tab_title)
    TabLayout tabTitle;
    @BindView(R.id.v_chart_background)
    View vChartBackground;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_wheater)
    TextView tvWheater;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.v_top_padding)
    View vTopPadding;
    @BindView(R.id.v_bottom_padding)
    View vBottomPadding;
    @BindView(R.id.v_pop)
    View vPop;

    private String imei;


    /**
     * 包括五个fragment
     */
    private PieChartFrag dayFragment;
    private BarChartWeekFrag weekFragment;
    private BarChartMonthFrag monthFragment;
    private BarChartYearFrag yearFragment;
    private BarChartTotalFrag totalFragment;


    private FragmentTransaction mTransaction;
    private FragmentManager mManager;


    /**
     * 隐藏fragment
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (dayFragment != null) {
            transaction.hide(dayFragment);
        }

        if (weekFragment != null) {
            transaction.hide(weekFragment);
        }

        if (monthFragment != null) {
            transaction.hide(monthFragment);
        }

        if (yearFragment != null) {
            transaction.hide(yearFragment);
        }


        if (totalFragment != null) {
            transaction.hide(totalFragment);
        }

    }


    @Override
    protected ChartPresenter createPresenter() {
        return new ChartPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo_tracker;
    }

    @Override
    protected void initViews() {
        mManager = getSupportFragmentManager();


        //toolbar
        initToobar(toolbar);
        tvTitle.setText(R.string.m101_photo_tracker);
        toolbar.inflateMenu(R.menu.menu_camera);
        toolbar.setOnMenuItemClickListener(this);

        //tablayout设置标题
        String[] titles = new String[]{
                getString(R.string.m25_day),
                getString(R.string.m26_week),
                getString(R.string.m27_month),
                getString(R.string.m28_year),
                getString(R.string.m100_total),
        };
        tabTitle.removeAllTabs();
        for (String title : titles) {
            TabLayout.Tab tab = tabTitle.newTab();
            tab.setText(title);
            tabTitle.addTab(tab);
        }

        tabTitle.addOnTabSelectedListener(this);


        //默认设置为当天日期
        Calendar calendar = Calendar.getInstance();
        String yearStr = calendar.get(Calendar.YEAR) + "";//获取年份
        int month = calendar.get(Calendar.MONTH);//获取月份
        String monthStr = month < 10 ? "0" + month : month + "";
        int day = calendar.get(Calendar.DATE);//获取日
        String dayStr = day < 10 ? "0" + day : day + "";

        List<String> month1 = CommentUtils.getMonth();
        String date = month1.get(month) + " " + yearStr;
        tvSubtitle.setText(date);


        String dayS = dayStr + " " + month1.get(month) + " " + yearStr;
        tvDate.setText(dayS);

        //初始化饼状图
        //开启事务
        mTransaction = mManager.beginTransaction();
        dayFragment = new PieChartFrag();
        mTransaction.add(R.id.vp_chart, dayFragment);
        mTransaction.commit();

    }

    @Override
    protected void initData() {
        imei = getIntent().getStringExtra("imei");
        presenter.setImeis(imei);
        //获取图表数据
        presenter.getChartData();

        presenter.getAlldevice();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }


    @OnClick({R.id.tv_title, R.id.tv_subtitle, R.id.iv_left, R.id.iv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_title:
                break;
            case R.id.tv_subtitle:
                try {
                    DateUtils.showTimepickViews(this, presenter.getDate(), new DateUtils.ImplSelectTimeListener() {
                        @Override
                        public void seletedListener(String date) {
                            presenter.setDate(date);
                        }

                        @Override
                        public void result(int year, int month, int day) {
                            List<String> month1 = CommentUtils.getMonth();
                            String date = month1.get(month) + " " + year;
                            tvSubtitle.setText(date);
                            presenter.getChartData();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_right:
                presenter.dateNext();
                break;

            case R.id.iv_left:
                presenter.datePrevious();
                break;
        }
    }

    @Override
    public void upDate(String date) {
        tvDate.setText(date);
    }

    @Override
    public void upDateYear(String date) {
        tvSubtitle.setText(date);
    }

    @Override
    public void upPieChart(int total, int am, int pm) {
        if (dayFragment != null) {
            dayFragment.setChartData(total, am, pm);
        }
    }

    @Override
    public void upWeekChart(List<String> weekList, List<Integer> readList, List<Integer> unreadlist) {
        if (weekFragment != null) {
            weekFragment.setBarChart(weekList, readList, unreadlist);
        }
    }

    @Override
    public void upMonthChart(List<String> monthList, List<Integer> readList, List<Integer> unreadlist) {
        if (monthFragment != null) {
            monthFragment.setBarChart(monthList, readList, unreadlist);
        }
    }

    @Override
    public void upYearChart(List<String> yearList, List<Integer> readList, List<Integer> unreadlist) {
        if (yearFragment != null) {
            yearFragment.setBarChart(yearList, readList, unreadlist);
        }
    }

    @Override
    public void upTotalChart(List<String> totalList, List<Integer> readList, List<Integer> unreadlist) {
        if (totalFragment != null) {
            totalFragment.setBarChart(totalList, readList, unreadlist);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        presenter.setDateType(presenter.dataTypes[position]);
        presenter.getChartData();


        //开启事务
        mTransaction = mManager.beginTransaction();
        hideFragment(mTransaction);
        switch (position) {
            case 0:
                if (dayFragment == null) {
                    dayFragment = new PieChartFrag();
                    mTransaction.add(R.id.vp_chart, dayFragment);
                } else {
                    mTransaction.show(dayFragment);
                }
                break;

            case 1:
                if (weekFragment == null) {
                    weekFragment = new BarChartWeekFrag();
                    mTransaction.add(R.id.vp_chart, weekFragment);
                } else {
                    mTransaction.show(weekFragment);
                }
                break;

            case 2:
                if (monthFragment == null) {
                    monthFragment = new BarChartMonthFrag();
                    mTransaction.add(R.id.vp_chart, monthFragment);
                } else {
                    mTransaction.show(monthFragment);
                }
                break;

            case 3:
                if (yearFragment == null) {
                    yearFragment = new BarChartYearFrag();
                    mTransaction.add(R.id.vp_chart, yearFragment);
                } else {
                    mTransaction.show(yearFragment);
                }
                break;
            case 4:
                if (totalFragment == null) {
                    totalFragment = new BarChartTotalFrag();
                    mTransaction.add(R.id.vp_chart, totalFragment);
                } else {
                    mTransaction.show(totalFragment);
                }
                break;

        }
        mTransaction.commit();


    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.right_action) {
            // 一锟斤拷锟皆讹拷锟斤拷牟锟斤拷郑锟斤拷锟轿拷锟绞撅拷锟斤拷锟斤拷锟�
            View contentView = LayoutInflater.from(this).inflate(
                    R.layout.pop_layout, null);

            List<CameraBean> cameraList = presenter.getCameraList();
            RecyclerView rvCamera = contentView.findViewById(R.id.ry_camera);
            rvCamera.setLayoutManager(new LinearLayoutManager(this));
            CameraMulFiterAdapter camerAdapter = new CameraMulFiterAdapter(R.layout.item_camera_menu, cameraList);
            rvCamera.setAdapter(camerAdapter);
            camerAdapter.setOnItemClickListener((adapter, view, position) -> {
                camerAdapter.setNowSelectPosition(position);
                StringBuilder imeis = new StringBuilder();
                List<CameraBean> data = camerAdapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    CameraBean cameraBean = data.get(i);
                    boolean selected = cameraBean.isSelected();
                    if (selected) {
                        if (i == 0) {
                            imeis = new StringBuilder("-1");
                            break;
                        } else {
                            String imei = cameraBean.getCamera().getImei();
                            imeis.append(imei).append("_");
                        }
                    }
                }
                if (imeis.toString().endsWith("_")) {
                    imeis = new StringBuilder(imeis.substring(0, imeis.length() - 1));
                    presenter.setImeis(imeis.toString());
                    presenter.setIsAllCamera("-1");
                } else {
                    presenter.setImeis("-1");
                    presenter.setIsAllCamera("1");
                }
                presenter.getChartData();
            });


            int width = getResources().getDimensionPixelSize(R.dimen.dp_225);
            int weight = getResources().getDimensionPixelSize(R.dimen.dp_248);

            final PopupWindow popupWindow = new PopupWindow(contentView, width, weight, true);
            popupWindow.setTouchable(true);


            popupWindow.setTouchInterceptor((v, event) -> false);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.4f; //设置透明度
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            getWindow().setAttributes(lp);
            popupWindow.setOnDismissListener(() -> {
                WindowManager.LayoutParams lp1 = getWindow().getAttributes();
                lp1.alpha = 1f;
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(lp1);
            });
            popupWindow.setBackgroundDrawable(new ColorDrawable(0));
            popupWindow.setAnimationStyle(R.style.Popup_Anim);
            popupWindow.showAsDropDown(vPop);
        }

        return true;
    }

}
