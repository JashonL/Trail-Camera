package com.shuoxd.camera.module.message;

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
import com.shuoxd.camera.adapter.MessageAdapter;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.base.BaseBean;
import com.shuoxd.camera.bean.MessageBean;
import com.shuoxd.camera.customview.CustomLoadMoreView;
import com.shuoxd.camera.customview.LinearDivider;
import com.shuoxd.camera.customview.MySwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MessageActivity extends BaseActivity<MessagePresenter> implements MessageView,
        Toolbar.OnMenuItemClickListener, BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_toolbar)
    LinearLayout appToolbar;
    @BindView(R.id.rv_message)
    RecyclerView rvMessage;
    @BindView(R.id.srl_pull)
    MySwipeRefreshLayout srlPull;

    private MessageAdapter mAdapter;

    @Override
    protected MessagePresenter createPresenter() {
        return new MessagePresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected void initViews() {
        //toolbar
        initToobar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        tvTitle.setText(R.string.m81_Message);

        //recyclerview
        setAdapter();
    }


    //小图片布局
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
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void showMessage(List<MessageBean> msgList) {
        srlPull.setRefreshing(false);
        int pageNow = presenter.getPageNow();

        if (pageNow==1) {
            mAdapter.setNewData(msgList);
        }else {
            mAdapter.addData(msgList);
            mAdapter.loadMoreComplete();
        }

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
    public void showNoMoreData() {
        //数据已加载完
        mAdapter.loadMoreEnd();
    }

    @Override
    public void showNoQuestion() {

    }

    @Override
    public void showQuetionMoreFail() {

    }

    @Override
    public void showMoreFail() {
        //数据加载完毕
        mAdapter.loadMoreFail();
    }

}
