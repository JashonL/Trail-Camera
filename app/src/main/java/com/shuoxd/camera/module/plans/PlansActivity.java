package com.shuoxd.camera.module.plans;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.PlansListAdapter;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.customview.LinearDivider;
import com.shuoxd.camera.customview.MySwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PlansActivity extends BaseActivity<PlansPresenter> implements PlansView,Toolbar.OnMenuItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener,
        BaseQuickAdapter.OnItemClickListener ,
        BaseQuickAdapter.OnItemLongClickListener{

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_toolbar)
    LinearLayout appToolbar;
    @BindView(R.id.rlv_allcamera)
    RecyclerView rlvAllcamera;




    private PlansListAdapter mAdapter;


    @Override
    protected PlansPresenter createPresenter() {
        return new PlansPresenter(this,this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_plans;
    }

    @Override
    protected void initViews() {

        initToobar(toolbar);
        tvTitle.setText(R.string.m79_plans);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlvAllcamera.setLayoutManager(layoutManager);
        mAdapter=new PlansListAdapter(R.layout.item_plans,new ArrayList<>());
        rlvAllcamera.setAdapter(mAdapter);
        rlvAllcamera.addItemDecoration(new LinearDivider(this, LinearLayoutManager.VERTICAL,
                1, ContextCompat.getColor(this, R.color.gray_d2d2d)));
        View view = LayoutInflater.from(this).inflate(R.layout.list_empty_view, null);
        mAdapter.setEmptyView(view);
        mAdapter.setOnItemClickListener(this);
        mAdapter.disableLoadMoreIfNotFullPage(rlvAllcamera);
        mAdapter.setOnItemLongClickListener(this);

    }

    @Override
    protected void initData() {
        presenter.getAlldevice();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }


    @Override
    public void showList(List<CameraBean> list) {
        mAdapter.replaceData(list);
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
        CameraBean cameraBean = mAdapter.getData().get(position);
        String imei = cameraBean.getCamera().getImei();
        String alias = cameraBean.getCamera().getAlias();
        Intent intent = new Intent(this, PlansDetailActivity.class);
        intent.putExtra("imei",imei);
        intent.putExtra("alias",alias);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        return false;
    }
}
