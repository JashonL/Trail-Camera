package com.shuoxd.camera.module.message;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.shuoxd.camera.R;
import com.shuoxd.camera.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageDetailActivity extends BaseActivity<MessageDetailPresenter> implements MessageDetailView {
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_toolbar)
    LinearLayout appToolbar;
    @BindView(R.id.tv_content)
    TextView tvContent;

    @Override
    protected MessageDetailPresenter createPresenter() {
        return new MessageDetailPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_detail;
    }

    @Override
    protected void initViews() {
        initToobar(toolbar);
        tvTitle.setText(R.string.m158_message);
    }

    @Override
    protected void initData() {
        String title = getIntent().getStringExtra("title");

        if (!TextUtils.isEmpty(title)){
            tvContent.setText(title);
        }

        String message = getIntent().getStringExtra("message");
        if (!TextUtils.isEmpty(message)){
            tvContent.setText(message);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

}
