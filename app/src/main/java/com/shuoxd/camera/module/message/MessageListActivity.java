package com.shuoxd.camera.module.message;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mylhyl.circledialog.CircleDialog;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.MessageAdapter;
import com.shuoxd.camera.adapter.QuestionAdapter;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.bean.MessageBean;
import com.shuoxd.camera.bean.QuestionBean;
import com.shuoxd.camera.customview.CustomLoadMoreView;
import com.shuoxd.camera.customview.LinearDivider;
import com.shuoxd.camera.customview.MySwipeRefreshLayout;
import com.shuoxd.camera.eventbus.FreshQuestion;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageListActivity extends BaseActivity<MessagePresenter> implements MessageView,
        Toolbar.OnMenuItemClickListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener, RadioGroup.OnCheckedChangeListener {


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
    @BindView(R.id.srl_pull)
    MySwipeRefreshLayout srlPull;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.tv_all_read)
    TextView tvAllRead;


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
        EventBus.getDefault().register(this);
        //toolbar
        initToobar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        tvTitle.setText(R.string.m81_Message);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            //???????????????????????????
            Intent intent = new Intent(this, QuestionSubmitActivity.class);
            startActivity(intent);
        });


        rgQuetion.setOnCheckedChangeListener(this);
        rgQuetion.check(R.id.rb_quetion);

        rvQuetion.setVisibility(View.VISIBLE);
        rvMessage.setVisibility(View.GONE);
        tvAllRead.setVisibility(View.GONE);
        //??????
        setQuetionAdapter();
        //??????
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
        mQuestionAdapter.setOnItemLongClickListener(this);

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
                int checkedRadioButtonId = rgQuetion.getCheckedRadioButtonId();
                if (checkedRadioButtonId == R.id.rb_quetion) {
                    presenter.setQsPageNow(0);
                    presenter.setQsTotalPage(1);
                    presenter.getQuestion();

                } else {
                    presenter.setPageNow(0);
                    presenter.setTotalPage(1);
                    presenter.getMessage();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        //????????????????????????
        try {
            //????????????
            presenter.setPageNow(0);
            presenter.setTotalPage(1);
            presenter.getMessage();
            //??????????????????
            presenter.setQsPageNow(0);
            presenter.setQsTotalPage(1);
            presenter.getQuestion();

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
        if (mQuestionAdapter == adapter) {
            QuestionBean questionBean = mQuestionAdapter.getData().get(position);
            String id = questionBean.getId();
            Intent intent = new Intent(this, QuestionDetailActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }

        if (mAdapter == adapter) {
            MessageBean messageBean = mAdapter.getData().get(position);
            String readStatus = messageBean.getUser().getReadStatus();
            if ("0".equals(readStatus)) {
                messageBean.getUser().setReadStatus("1");
                mAdapter.notifyDataSetChanged();
                //????????????????????????
                String id = messageBean.getInfo().getId();
                presenter.operation_msg(id, "read");
            }

            String content = messageBean.getInfo().getContent();
            String title = messageBean.getInfo().getTitle();
            Intent intent = new Intent(this, MessageDetailActivity.class);
            intent.putExtra("message", content);
            intent.putExtra("title", title);
            startActivity(intent);
        }

    }

    @Override
    public void showMessage(List<MessageBean> msgList) {
        srlPull.setRefreshing(false);
        int pageNow = presenter.getPageNow();

        if (pageNow == 1) {
            mAdapter.setNewData(msgList);
        } else {
            mAdapter.addData(msgList);
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public void showNoMoreData() {
        //??????????????????
        mAdapter.loadMoreEnd();
    }

    @Override
    public void showNoQuestion() {
        //??????????????????
        mQuestionAdapter.loadMoreEnd();
    }

    @Override
    public void showQuetionMoreFail() {
        //??????????????????
        mQuestionAdapter.loadMoreEnd();
    }

    @Override
    public void delete() {
        //??????????????????
        presenter.setQsPageNow(0);
        presenter.setQsTotalPage(1);
        presenter.getQuestion();
    }

    @Override
    public void showMoreFail() {
        //??????????????????
        mAdapter.loadMoreFail();
    }

    @Override
    public void showQuestion(List<QuestionBean> msgList) {
        srlPull.setRefreshing(false);
        int pageNow = presenter.getPageNow();

        if (pageNow == 1) {
            mQuestionAdapter.setNewData(msgList);
        } else {
            mQuestionAdapter.addData(msgList);
            mQuestionAdapter.loadMoreComplete();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {

        if (id == R.id.rb_quetion) {
            rvQuetion.setVisibility(View.VISIBLE);
            rvMessage.setVisibility(View.GONE);
            tvAllRead.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);
        } else if (id == R.id.rb_notifycation) {
            rvQuetion.setVisibility(View.GONE);
            rvMessage.setVisibility(View.VISIBLE);
            tvAllRead.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdata(FreshQuestion bean) {
        //??????????????????
        presenter.setQsPageNow(0);
        presenter.setQsTotalPage(1);
        presenter.getQuestion();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter==mQuestionAdapter){
            QuestionBean questionBean = mQuestionAdapter.getData().get(position);
            String id = questionBean.getId();
            new CircleDialog.Builder().setWidth(0.7f)
                    .setTitle(getString(R.string.m150_tips))
                    .setText(getString(R.string.m151_delete_question))
                    .setNegative(getString(R.string.m127_cancel), view1 -> {

                    })
                    .setPositive(getString(R.string.m152_ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            presenter.operation(id, "remove");
                        }
                    }).show(getSupportFragmentManager());
        }


        if (mAdapter==adapter){
            MessageBean messageBean = mAdapter.getData().get(position);
            String id = messageBean.getInfo().getId();
            new CircleDialog.Builder().setWidth(0.7f)
                    .setTitle(getString(R.string.m150_tips))
                    .setText(getString(R.string.m160_message_delete))
                    .setNegative(getString(R.string.m127_cancel), view1 -> {

                    })
                    .setPositive(getString(R.string.m152_ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            presenter.operation_msg(id, "remove");
                        }
                    }).show(getSupportFragmentManager());
        }

        return true;
    }


    @OnClick({R.id.tv_all_read, R.id.rv_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_all_read:
                //????????????
                mAdapter.setALlRead();
                //????????????
                presenter.operation_msg("-1", "allRead");
                break;
            case R.id.rv_message:
                break;
        }
    }
}
