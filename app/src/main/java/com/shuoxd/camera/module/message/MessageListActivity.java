package com.shuoxd.camera.module.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
import com.shuoxd.camera.adapter.QuestionAdapter;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.bean.MessageBean;
import com.shuoxd.camera.customview.CustomLoadMoreView;
import com.shuoxd.camera.customview.LinearDivider;
import com.shuoxd.camera.customview.MySwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageListActivity extends BaseActivity<MessagePresenter> implements MessageView,
        Toolbar.OnMenuItemClickListener, BaseQuickAdapter.OnItemClickListener , RadioGroup.OnCheckedChangeListener {


    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rb_quetion)
    RadioButton rbQuetion;
    @BindView(R.id.rb_notifycation)
    RadioButton rbNotifycation;
    @BindView(R.id.rg_quetion)
    RadioGroup rgQuetion;
    @BindView(R.id.rv_quetion)
    RecyclerView rvQuetion;
    @BindView(R.id.rv_message)
    RecyclerView rvMessage;
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.srl_pull)
    MySwipeRefreshLayout srlPull;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private MessageAdapter mAdapter;

    private QuestionAdapter mQuestionAdapter;

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
        fab.setOnClickListener(view -> {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
        });


        rgQuetion.setOnCheckedChangeListener(this);
        rgQuetion.check(R.id.rb_quetion);

        //问题
        setQuetionAdapter();
        //系统
        setSystemAdapter();

    }



    //
    private void setQuetionAdapter() {
        rvQuetion.setLayoutManager(new LinearLayoutManager(this));
        mQuestionAdapter = new QuestionAdapter(R.layout.item_question, new ArrayList<>());
        rvQuetion.setAdapter(mQuestionAdapter);
        rvQuetion.addItemDecoration(new LinearDivider(this, LinearLayoutManager.VERTICAL, 20, ContextCompat.getColor(this, R.color.nocolor)));
        View view = LayoutInflater.from(this).inflate(R.layout.message_empty_view, null);
        mQuestionAdapter.setEmptyView(view);
        mQuestionAdapter.setHeaderAndEmpty(true);
        mQuestionAdapter.setOnItemClickListener(this);

        mQuestionAdapter.setLoadMoreView(new CustomLoadMoreView());
        mQuestionAdapter.disableLoadMoreIfNotFullPage(rvQuetion);
        mQuestionAdapter.setOnLoadMoreListener(() -> {
            try {
                presenter.getQuestion();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, rvQuetion);
    }



    //
    private void setSystemAdapter() {
        rvMessage.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MessageAdapter(R.layout.item_message, new ArrayList<>());
        rvMessage.setAdapter(mAdapter);
        rvMessage.addItemDecoration(new LinearDivider(this, LinearLayoutManager.VERTICAL, 20, ContextCompat.getColor(this, R.color.nocolor)));
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
        srlPull.setOnRefreshListener(() -> {
            try {
                presenter.setPageNow(0);
                presenter.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        //获取列表设备列表
        try {
            presenter.setPageNow(0);
            presenter.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        //数据已加载完
        mAdapter.loadMoreEnd();
    }

    @Override
    public void showNoQuestion() {
        //数据已加载完
        mQuestionAdapter.loadMoreEnd();
    }

    @Override
    public void showQuetionMoreFail() {
        //数据已加载完
        mQuestionAdapter.loadMoreEnd();
    }

    @Override
    public void showMoreFail() {
        //数据加载完毕
        mAdapter.loadMoreFail();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {

        if (id==R.id.rb_quetion){
            rvQuetion.setVisibility(View.VISIBLE);
            rvMessage.setVisibility(View.GONE);
        }else if (id==R.id.rb_notifycation){
            rvQuetion.setVisibility(View.GONE);
            rvMessage.setVisibility(View.VISIBLE);
        }

    }
}
