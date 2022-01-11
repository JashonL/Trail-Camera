package com.shuoxd.camera.module.message;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.ImageAdapter;
import com.shuoxd.camera.adapter.MessageAdapter;
import com.shuoxd.camera.adapter.QuestionReplyAdapter;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.bean.QuestionBean;
import com.shuoxd.camera.bean.ReplyBean;
import com.shuoxd.camera.customview.CustomLoadMoreView;
import com.shuoxd.camera.customview.LinearDivider;
import com.shuoxd.camera.utils.SoftHideKeyBoardUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class QuestionDetailActivity extends BaseActivity<QuestionDetailPresenter>
        implements QuestionDetailView ,Toolbar.OnMenuItemClickListener, BaseQuickAdapter.OnItemClickListener{

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_toolbar)
    LinearLayout appToolbar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_question_detail)
    TextView tvQuestionDetail;
    @BindView(R.id.rlv_pic)
    RecyclerView rlvPic;
    @BindView(R.id.rlv_content)
    RecyclerView rlvContent;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.rlv_reply_pic)
    RecyclerView rlvReplyPic;
    @BindView(R.id.cl_reply)
    ConstraintLayout clReply;
    @BindView(R.id.btn_demo)
    AppCompatButton btnDemo;



    private QuestionReplyAdapter mAdapter;

    private ImageAdapter imageAdapter;

    @Override
    protected QuestionDetailPresenter createPresenter() {
        return new QuestionDetailPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_quetion_detail;
    }

    @Override
    protected void initViews() {
        SoftHideKeyBoardUtil.assistActivity(this);
        //toolbar
        initToobar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        tvTitle.setText(R.string.m139_question);


        setQuestionPic();
        //recyclerview
        setReplyAdapter();
    }



    //回复的列表
    private void setQuestionPic() {
        rlvPic.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        imageAdapter = new ImageAdapter(R.layout.item_image, new ArrayList<>());
        rlvPic.setAdapter(imageAdapter);
        rlvPic.addItemDecoration(new LinearDivider(this, LinearLayoutManager.HORIZONTAL, 20, ContextCompat.getColor(this, R.color.nocolor)));
        imageAdapter.setOnItemClickListener(this);
    }



    //回复的列表
    private void setReplyAdapter() {
        rlvContent.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new QuestionReplyAdapter(R.layout.item_question_reply, new ArrayList<>());
        rlvContent.setAdapter(mAdapter);
        rlvContent.addItemDecoration(new LinearDivider(this, LinearLayoutManager.VERTICAL, 20, ContextCompat.getColor(this, R.color.nocolor)));
        mAdapter.setOnItemClickListener(this);
    }




    @Override
    protected void initData() {

        //请求单个问题详情
        String id = getIntent().getStringExtra("id");
        String accountName = App.getUserBean().getAccountName();
        presenter.questionDetail(id,accountName);
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
    public void showQuestion(QuestionBean questionBean) {
        String title = questionBean.getTitle();
        if (!TextUtils.isEmpty(title)){
            tvName.setText(title);
        }

        String content = questionBean.getContent();
        if (!TextUtils.isEmpty(content)){
            tvQuestionDetail.setText(content);
        }

        String time = questionBean.getCreateTime();
        if (!TextUtils.isEmpty(time)){
            tvDate.setText(time);
        }


        List<String>paths=new ArrayList<>();
        String path1 = questionBean.getAttachmentOnePath();
        String path2 = questionBean.getAttachmentTwoPath();
        String path3 = questionBean.getAttachmentThreePath();
        if (!TextUtils.isEmpty(path1)){
            paths.add(path1);
        }
        if (!TextUtils.isEmpty(path2)){
            paths.add(path2);
        }

        if (!TextUtils.isEmpty(path3)){
            paths.add(path3);
        }
        imageAdapter.replaceData(paths);

    }

    @Override
    public void showReply(List<ReplyBean> beans) {
    }
}
