package com.shuoxd.camera.module.account;

import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.shuoxd.camera.R;
import com.shuoxd.camera.base.BaseActivity;

import java.util.List;

import butterknife.BindView;

public class UserCenterActivity extends BaseActivity<UserCenterPresenter>implements UserCenterView {
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_title)
    LinearLayout headerTitle;
    @BindView(R.id.view_pager)
    RecyclerView mRecyclerView;



    @Override
    protected UserCenterPresenter createPresenter() {
        return new UserCenterPresenter(this,this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.user_center;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
