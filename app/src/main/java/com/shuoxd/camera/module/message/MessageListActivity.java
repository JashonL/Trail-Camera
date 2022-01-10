package com.shuoxd.camera.module.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.MessageAdapter;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.bean.MessageBean;
import com.shuoxd.camera.customview.CustomLoadMoreView;
import com.shuoxd.camera.customview.LinearDivider;
import com.shuoxd.camera.customview.MySwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MessageListActivity extends BaseActivity<MessagePresenter> implements MessageView,
        Toolbar.OnMenuItemClickListener, BaseQuickAdapter.OnItemClickListener {


    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_message)
    RecyclerView rvMessage;
    @BindView(R.id.srl_pull)
    MySwipeRefreshLayout srlPull;
    @BindView(R.id.fab)
    FloatingActionButton fab;


    private MessageAdapter mAdapter;


    @Override
    protected MessagePresenter createPresenter() {
        return new MessagePresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_list;
    }

    @Override
    protected void initViews() {
        //toolbar
        initToobar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        tvTitle.setText(R.string.m81_Message);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        //问题
        setAdapter();
        //系统
        setAdapter();

    }


    //
    private void setAdapter() {
        rvMessage.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MessageAdapter(R.layout.item_message, new ArrayList<>());
        rvMessage.setAdapter(mAdapter);
        rvMessage.addItemDecoration(new LinearDivider(this, LinearLayoutManager.VERTICAL, 32, ContextCompat.getColor(this, R.color.nocolor)));
        View view = LayoutInflater.from(this).inflate(R.layout.message_empty_view, null);
        mAdapter.setEmptyView(view);
        mAdapter.setHeaderAndEmpty(true);
        mAdapter.setOnItemClickListener(this);

        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.disableLoadMoreIfNotFullPage(rvMessage);
        mAdapter.setOnLoadMoreListener(() -> {
            try {
                presenter.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, rvMessage);
    }


    @Override
    protected void initData() {

    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void showMessage(List<MessageBean> msgList) {

    }

    @Override
    public void showNoMoreData() {

    }

    @Override
    public void showMoreFail() {

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

}
