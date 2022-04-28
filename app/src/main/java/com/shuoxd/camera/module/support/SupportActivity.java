package com.shuoxd.camera.module.support;

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
import com.shuoxd.camera.adapter.SupportAdapter;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.bean.SupportBean;
import com.shuoxd.camera.customview.LinearDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SupportActivity extends BaseActivity<SupportPresenter> implements SupportView, Toolbar.OnMenuItemClickListener,
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

    @BindView(R.id.rlv_support)
    RecyclerView rlvSupport;

    private SupportAdapter mAdapter;

    @Override
    protected SupportPresenter createPresenter() {
        return new SupportPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_support;
    }

    @Override
    protected void initViews() {
        initToobar(toolbar);
        tvTitle.setText(R.string.m83_Support);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlvSupport.setLayoutManager(layoutManager);
        mAdapter = new SupportAdapter(R.layout.item_support, new ArrayList<>());
        rlvSupport.setAdapter(mAdapter);
        rlvSupport.addItemDecoration(new LinearDivider(this, LinearLayoutManager.VERTICAL,
                20, ContextCompat.getColor(this, R.color.nocolor)));

        View view = LayoutInflater.from(this).inflate(R.layout.list_empty_view, null);
        mAdapter.setEmptyView(view);
        mAdapter.setOnItemClickListener(this);
        mAdapter.disableLoadMoreIfNotFullPage(rlvSupport);
        mAdapter.setOnItemLongClickListener(this);

    }

    @Override
    protected void initData() {
        String accountName = App.getUserBean().getAccountName();
        presenter.faqList(accountName);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

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
        SupportBean supportBean = mAdapter.getData().get(position);
        boolean expand = supportBean.isExpand();
        supportBean.setExpand(!expand);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        return false;
    }

    @Override
    public void showSupportList(List<SupportBean> list) {
        mAdapter.replaceData(list);
    }
}
