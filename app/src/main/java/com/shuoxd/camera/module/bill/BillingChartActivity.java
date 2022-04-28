package com.shuoxd.camera.module.bill;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.BillingHistoryAdapter;
import com.shuoxd.camera.adapter.BillingYearAdapter;
import com.shuoxd.camera.adapter.CameraFiterAdapter;
import com.shuoxd.camera.adapter.PlansListAdapter;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.bean.YearBean;
import com.shuoxd.camera.customview.LinearDivider;
import com.shuoxd.camera.module.camera.chart_fragment.BarChartFrag;
import com.shuoxd.camera.module.camera.chart_fragment.BarChartMonthFrag;
import com.shuoxd.camera.module.camera.chart_fragment.PieChartFrag;
import com.shuoxd.camera.utils.CommentUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class BillingChartActivity extends BaseActivity<BillingChartPresenter> implements BillChartView,
        Toolbar.OnMenuItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener,
        BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemLongClickListener {

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_toolbar)
    LinearLayout appToolbar;
    @BindView(R.id.v_pop)
    View vPop;
    @BindView(R.id.tv_subtitle)
    TextView tvSubtitle;
    @BindView(R.id.rlv_year)
    RecyclerView rlvYear;


    private BillingYearAdapter mAdapter;

    private BarChartFrag monthFragment;

    private FragmentTransaction mTransaction;
    private FragmentManager mManager;


    @Override
    public boolean onMenuItemClick(MenuItem item) {

        if (item.getItemId() == R.id.right_action) {
            View contentView = LayoutInflater.from(this).inflate(
                    R.layout.pop_layout, null);
            List<CameraBean> cameraList = presenter.getCameraList();

            if (cameraList == null || cameraList.size() == 0) {
                presenter.getAlldevice();
            } else {
                int width = getResources().getDimensionPixelSize(R.dimen.dp_225);
                int hight = getResources().getDimensionPixelSize(R.dimen.dp_248);
                int itemHight = getResources().getDimensionPixelOffset(R.dimen.dp_40);
                if (itemHight * cameraList.size() > hight) {
                    hight = getResources().getDimensionPixelSize(R.dimen.dp_248);
                } else {
                    hight = LinearLayout.LayoutParams.WRAP_CONTENT;
                }
                final PopupWindow popupWindow = new PopupWindow(contentView, width, hight, true);
                popupWindow.setTouchable(true);
                RecyclerView rvCamera = contentView.findViewById(R.id.ry_camera);
                rvCamera.setLayoutManager(new LinearLayoutManager(this));
                CameraFiterAdapter camerAdapter = new CameraFiterAdapter(R.layout.item_camera_menu, cameraList);
                rvCamera.setAdapter(camerAdapter);
                camerAdapter.setOnItemClickListener((adapter, view, position) -> {
                    camerAdapter.setNowSelectPosition(position);
                    CameraBean cameraBean = cameraList.get(position);
                    String imei = cameraBean.getCamera().getImei();
                    String alias = cameraBean.getCamera().getAlias();
                    if (position == 0) {
                        //默认请求全部
                        presenter.setIsAllCamera("1");
                        presenter.setImei("-1");
                    } else {
                        presenter.setImei(imei);
                        presenter.setIsAllCamera(String.valueOf(-1));
                    }
                    if (TextUtils.isEmpty(alias)){
                        alias=imei;
                    }
                    tvSubtitle.setText(alias);
                    presenter.billLogChart();
                    popupWindow.dismiss();
                });

                popupWindow.setTouchInterceptor((v, event) -> false);
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(lp);
                popupWindow.setOnDismissListener(() -> {
                    WindowManager.LayoutParams lp1 = getWindow().getAttributes();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    getWindow().setAttributes(lp1);
                });
                popupWindow.setBackgroundDrawable(new ColorDrawable(0));
                popupWindow.setAnimationStyle(R.style.Popup_Anim);
                popupWindow.showAsDropDown(vPop);
            }


        }
        return true;
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        mAdapter.setNowSelectPosition(position);
        YearBean yearBean = mAdapter.getData().get(position);
        setChart(yearBean);
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        return false;
    }

    @Override
    protected BillingChartPresenter createPresenter() {
        return new BillingChartPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_biling_chart;
    }

    @Override
    protected void initViews() {
        initToobar(toolbar);
        //头部toolBar
        toolbar.inflateMenu(R.menu.menu_camera);
        toolbar.setOnMenuItemClickListener(this);
        tvTitle.setText(R.string.m80_billing_history);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rlvYear.setLayoutManager(layoutManager);
        mAdapter = new BillingYearAdapter(R.layout.item_bill_year, new ArrayList<>());
        rlvYear.setAdapter(mAdapter);
        rlvYear.addItemDecoration(new LinearDivider(this, LinearLayoutManager.HORIZONTAL,
                20, ContextCompat.getColor(this, R.color.nocolor)));
        mAdapter.setOnItemClickListener(this);
        mAdapter.disableLoadMoreIfNotFullPage(rlvYear);
        mAdapter.setOnItemLongClickListener(this);


        mManager = getSupportFragmentManager();
        //初始化饼状图
        //开启事务
        mTransaction = mManager.beginTransaction();
        monthFragment = new BarChartFrag();
        mTransaction.add(R.id.vp_chart, monthFragment);
        mTransaction.commit();
    }

    @Override
    protected void initData() {
        presenter.getAlldevice();
        String alias = getIntent().getStringExtra("alias");
        String imei = getIntent().getStringExtra("imei");
        presenter.setImei(imei);
        if ("-1".equals(imei)) {
            tvSubtitle.setText(R.string.m77_all_camera);
            presenter.setIsAllCamera("1");
        } else {
            presenter.setIsAllCamera("-1");
            if (TextUtils.isEmpty(alias)) {
                alias = imei;
            }
            tvSubtitle.setText(alias);
        }
        //请求图表数据
        presenter.billLogChart();

    }



    @OnClick({R.id.iv_previous,R.id.iv_next})
    public void onViewClicked(View view) {
        int selected_index = mAdapter.getSelected_index();

        switch (view.getId()) {
            case R.id.iv_previous:
                if (selected_index==0)return;
                int i = selected_index-1;
                mAdapter.setNowSelectPosition(i);
                YearBean yearBean = mAdapter.getData().get(i);
                setChart(yearBean);

                break;
            case R.id.iv_next:
                if (selected_index==mAdapter.getData().size()-1)return;
                int i1 = selected_index+1;
                mAdapter.setNowSelectPosition(i1);
                YearBean yearBean1 = mAdapter.getData().get(i1);
                setChart(yearBean1);
                break;
        }
    }




    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void showChartData(List<YearBean> beans) {
        Collections.sort(beans, (yearBean, t1) -> {
            String year = yearBean.getYear();
            String year1 = t1.getYear();
            return year.compareTo(year1);
        });
        YearBean yearBean = beans.get(beans.size()-1);
        yearBean.setIschecked(true);
        mAdapter.replaceData(beans);
        mAdapter.setSelected_index(beans.size()-1);

        setChart(yearBean);
    }

    private void setChart(YearBean yearBean) {
        //设置图表
        int[] data = yearBean.getData();
        List<Integer> datalist = new ArrayList<>();
        List<String> month = CommentUtils.getMonth();
        for (int ydata : data) {
            datalist.add(ydata);
        }

        upChart(month, datalist);
    }

    @Override
    public void upChart(List<String> weekList, List<Integer> readList) {
        if (monthFragment != null) {
            monthFragment.setBarChart(weekList, readList);
        }
    }
}
