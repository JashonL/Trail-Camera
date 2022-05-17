package com.shuoxd.camera.module.bill;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.BillingHistoryAdapter;
import com.shuoxd.camera.adapter.CameraFiterAdapter;
import com.shuoxd.camera.adapter.SupportAdapter;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.base.BaseBean;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.bean.HistoryBean;
import com.shuoxd.camera.customview.CustomLoadMoreView;
import com.shuoxd.camera.customview.LinearDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class BillingHistoryActivity extends BaseActivity<BillingHistoryPresenter> implements BilingHistoryView,
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

    @BindView(R.id.billing_history)
    RecyclerView billHistory;

    @BindView(R.id.srl_pull)
    SwipeRefreshLayout srlPull;
    @BindView(R.id.v_pop)
    View vPop;


    private BillingHistoryAdapter mAdapter;

    @Override
    protected BillingHistoryPresenter createPresenter() {
        return new BillingHistoryPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_billing_history;
    }

    @Override
    protected void initViews() {

        initToobar(toolbar);
        //头部toolBar
        toolbar.inflateMenu(R.menu.menu_camera);
        toolbar.setOnMenuItemClickListener(this);
        tvTitle.setText(R.string.m80_billing_history);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        billHistory.setLayoutManager(layoutManager);
        mAdapter = new BillingHistoryAdapter(R.layout.item_billing_history, new ArrayList<>());
        billHistory.setAdapter(mAdapter);
        billHistory.addItemDecoration(new LinearDivider(this, LinearLayoutManager.VERTICAL,
                1, ContextCompat.getColor(this, R.color.nocolor)));

        View view = LayoutInflater.from(this).inflate(R.layout.list_empty_view, null);
        mAdapter.setEmptyView(view);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.setOnItemClickListener(this);
        mAdapter.disableLoadMoreIfNotFullPage(billHistory);
        mAdapter.setOnItemLongClickListener(this);
        mAdapter.setOnLoadMoreListener(() -> {
            try {
                presenter.getBillLogList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, billHistory);

    }

    @Override
    protected void initData() {
        presenter.getAlldevice();
        srlPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    presenter.setPageNow(0);
                    presenter.setTotalPage(1);
                    presenter.getBillLogList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // 获取列表设备列表
        try {
            //默认请求全部
            presenter.setIsAllCamera("1");
            presenter.setImei("-1");
            presenter.setPageNow(0);
            presenter.setTotalPage(1);
            presenter.getBillLogList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.bt_statistical_graph})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_statistical_graph:
                String imei = presenter.getImei();
                String alias = presenter.getAlias();
                Intent intent = new Intent(this, BillingChartActivity.class);
                intent.putExtra("imei",imei);
                intent.putExtra("alias",alias);
                startActivity(intent);
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
                        presenter.setAlias(alias);
                    }

                    presenter.setPageNow(0);
                    presenter.setTotalPage(1);
                    presenter.getBillLogList();
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

    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        return false;
    }

    @Override
    public void showNoMoreData() {
        srlPull.setRefreshing(false);
        //数据已加载完
        mAdapter.loadMoreEnd();
    }

    @Override
    public void showMoreFail() {
        mAdapter.loadMoreFail();
    }


    @Override
    public void showResultError(String msg) {
        super.showResultError(msg);
        srlPull.setRefreshing(false);
    }

    @Override
    public void showServerError(String msg) {
        super.showServerError(msg);
        srlPull.setRefreshing(false);
    }

    @Override
    public void onErrorCode(BaseBean bean) {
        super.onErrorCode(bean);

        srlPull.setRefreshing(false);
    }


    @Override
    public void deleteSuccess() {
        // 获取列表设备列表
        try {
            presenter.setPageNow(0);
            presenter.setTotalPage(1);
            presenter.getBillLogList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showDatalist(List<HistoryBean> list) {
        srlPull.setRefreshing(false);
        int pageNow = presenter.getPageNow();
        if (pageNow == 1) {
            List<HistoryBean> adapterList = new ArrayList<>(list);
            mAdapter.setNewData(adapterList);
        } else {
            mAdapter.addData(list);
            mAdapter.loadMoreComplete();
        }
    }
}
