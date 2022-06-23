package com.shuoxd.camera.module.plans;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.tabs.TabLayout;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.PlansAdapter;
import com.shuoxd.camera.adapter.SceneViewPagerAdapter;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.bean.ProgramBean;
import com.shuoxd.camera.customview.LinearDivider;
import com.shuoxd.camera.customview.MySwipeRefreshLayout;
import com.shuoxd.camera.utils.MyToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PlansChangeActivity extends BaseActivity<PlansChangePresenter>
        implements PlansChangeView, BaseQuickAdapter.OnItemChildClickListener,
        BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemLongClickListener,
        Toolbar.OnMenuItemClickListener, ViewPager.OnPageChangeListener {


    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_toolbar)
    LinearLayout appToolbar;
    @BindView(R.id.tab_title)
    TabLayout tableTitle;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.srl_pull)
    MySwipeRefreshLayout srlPull;


    private RecyclerView mRlvAnnual;
    private RecyclerView mRlvMonthly;

    private PlansAdapter mAnnualAdapter;
    private PlansAdapter mMonthlyAdapter;

    private View annualEmpty;
    private View monthlyEmpty;

    private LinearLayout llAddLaunchView;
    private LinearLayout llAddLinkageView;
    private SceneViewPagerAdapter mSceneViewPagerAdapter;

    private String imei;

    private TextView tvAnnualTips1;
    private TextView tvMonthlyTips1;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_plans;
    }

    @Override
    protected PlansChangePresenter createPresenter() {
        return new PlansChangePresenter(this, this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (position == 0) return;
        if (adapter == mAnnualAdapter) {
            mAnnualAdapter.setNowSelectPosition(position);
//            mMonthlyAdapter.clearSelected();
            ProgramBean programBean = mAnnualAdapter.getData().get(position);
            String introduce = programBean.getIntroduce();
            if (!TextUtils.isEmpty(introduce)){
                introduce = introduce.replace("\\n", "\n");
                tvAnnualTips1.setText(introduce);
            }else {
                tvAnnualTips1.setText("");
            }

        } else {
            mMonthlyAdapter.setNowSelectPosition(position);
//            mAnnualAdapter.clearSelected();
            ProgramBean programBean = mMonthlyAdapter.getData().get(position);
            String introduce = programBean.getIntroduce();
            if (!TextUtils.isEmpty(introduce)){
                introduce = introduce.replace("\\n", "\n");
                tvMonthlyTips1.setText(introduce);
            }else {
                tvMonthlyTips1.setText("");
            }
        }
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        return false;
    }


    @Override
    protected void initViews() {
        //头部
        initToobar(toolbar);
        tvTitle.setText(R.string.m79_plans);
        //viewpager
        List<View> pagers = new ArrayList<>();
        View annualView = LayoutInflater.from(this).inflate(R.layout.layout_plans_annual, null, false);
        View monthView = LayoutInflater.from(this).inflate(R.layout.layout_plans_monthly, null, false);


        //一键执行
        tvAnnualTips1=annualView.findViewById(R.id.tv_tips);
        mRlvAnnual = annualView.findViewById(R.id.rlv_plans_annual);
        mRlvAnnual.setLayoutManager(new LinearLayoutManager(this));
        int div = getResources().getDimensionPixelSize(R.dimen.dp_10);
        mRlvAnnual.addItemDecoration(new LinearDivider(this, LinearLayoutManager.VERTICAL,
                1, ContextCompat.getColor(this, R.color.nocolor)));
        mAnnualAdapter = new PlansAdapter(new ArrayList<>());
        annualEmpty = LayoutInflater.from(this).inflate(R.layout.list_empty_view, mRlvAnnual, false);
        mAnnualAdapter.setEmptyView(annualEmpty);
        mRlvAnnual.setAdapter(mAnnualAdapter);
        mAnnualAdapter.setOnItemClickListener(this);

        //条件执行
        tvMonthlyTips1=monthView.findViewById(R.id.tv_tips);
        mRlvMonthly = monthView.findViewById(R.id.rlv_plans_monthly);
        mRlvMonthly.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRlvMonthly.addItemDecoration(new LinearDivider(this, LinearLayoutManager.VERTICAL,
                div, ContextCompat.getColor(this, R.color.nocolor)));
        mMonthlyAdapter = new PlansAdapter(new ArrayList<>());
        monthlyEmpty = LayoutInflater.from(this).inflate(R.layout.list_empty_view, mRlvMonthly, false);
        mMonthlyAdapter.setEmptyView(monthlyEmpty);
        mRlvMonthly.setAdapter(mMonthlyAdapter);
        mMonthlyAdapter.setOnItemClickListener(this);

        pagers.add(monthView );
        pagers.add(annualView);
        mSceneViewPagerAdapter = new SceneViewPagerAdapter(pagers);
        viewPager.setAdapter(mSceneViewPagerAdapter);
        viewPager.addOnPageChangeListener(this);
        //将tablayout和Viewpager绑定
        tableTitle.setupWithViewPager(viewPager);
        //tablayout设置标题
        String[] titles = new String[]{
                getString(R.string.m245_monthly),
                getString(R.string.m246_annual)};
//        String[] titles = new String[]{getString(R.string.m81_launch_tap_to_run), getString(R.string.m82_smart)};
        tableTitle.removeAllTabs();
        for (String title : titles) {
            TabLayout.Tab tab = tableTitle.newTab();
            tab.setText(title);
            tableTitle.addTab(tab);
        }


    }


    @OnClick({R.id.btn_save, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                String id="-1";
                List<ProgramBean> data;
                if (viewPager.getCurrentItem()==0){
                    data = mMonthlyAdapter.getData();
                }else {
                    data = mAnnualAdapter.getData();
                }
                for (int i = 0; i < data.size(); i++) {
                    ProgramBean programBean = data.get(i);
                    String selected = programBean.getSelected();
                    if ("1".equals(selected)){
                        id=programBean.getId();
                        break;
                    }
                }
                if ("-1".equals(id)){
                    MyToastUtils.toast(R.string.m251_choose_plans);
                    return;
                }
                presenter.modifyCameraPlan(imei,id);
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }


    @Override
    protected void initData() {

        imei = getIntent().getStringExtra("imei");

        try {
            presenter.getPlanTemplateList(imei);
        } catch (Exception e) {
            e.printStackTrace();
        }
        srlPull.setOnRefreshListener(() -> {
            presenter.getPlanTemplateList(imei);
        });
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
  /*      if (position==0){
            //清空
            mAnnualAdapter.clearSelected();
        }else {
            mMonthlyAdapter.clearSelected();
        }*/
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void showAnnual(List<ProgramBean> lists) {
        srlPull.setRefreshing(false);
        mAnnualAdapter.replaceData(lists);
    }

    @Override
    public void showMonthly(List<ProgramBean> lists) {
        srlPull.setRefreshing(false);
        mMonthlyAdapter.replaceData(lists);
    }

    @Override
    public void annualSelected(int selected) {
        ProgramBean programBean = mAnnualAdapter.getData().get(selected);
        String introduce = programBean.getIntroduce();
        if (!TextUtils.isEmpty(introduce)){
            introduce = introduce.replace("\\n", "\n");
            tvAnnualTips1.setText(introduce);
        }else {
            tvAnnualTips1.setText("");
        }
    }

    @Override
    public void monthlySelected(int selected) {
        ProgramBean programBean = mMonthlyAdapter.getData().get(selected);
        String introduce = programBean.getIntroduce();
        if (!TextUtils.isEmpty(introduce)){
            introduce = introduce.replace("\\n", "\n");
            tvMonthlyTips1.setText(introduce);
        }else {
            tvMonthlyTips1.setText("");
        }
    }
}
