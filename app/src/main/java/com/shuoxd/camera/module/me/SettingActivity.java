package com.shuoxd.camera.module.me;

import android.content.Intent;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.shuoxd.camera.R;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.customview.MySwipeRefreshLayout;
import com.shuoxd.camera.module.camera.CameraDetailActivity;
import com.shuoxd.camera.utils.AppUtils;
import com.shuoxd.camera.utils.CircleDialogUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity<SettingPresenter> implements SettingView,
        Toolbar.OnMenuItemClickListener{

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_toolbar)
    LinearLayout appToolbar;

    @BindView(R.id.tv_version)
    TextView tvVersion;




    @Override
    protected SettingPresenter createPresenter() {
        return new SettingPresenter(this,this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initViews() {
        //toolbar
        initToobar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        tvTitle.setText(R.string.m288_setting);




        String versionName = "Version: "+ AppUtils.getVersionName(this);
        tvVersion.setText(versionName);


    }


    @OnClick({R.id.ll_agreement, R.id.ll_check, R.id.ll_delete_account})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_agreement:
                presenter.toWebView();
                break;

            case R.id.ll_check:
                presenter.getSystemConfig();
                AppUtils.isUpgradeApp(this,true);
                break;

            case R.id.ll_delete_account:
                //判断升级
                CircleDialogUtils.showCommentDialog(this, "", getString(R.string.m292_destroy_tips),
                        getString(R.string.m152_ok), getString(R.string.m127_cancel), Gravity.CENTER, view22 -> {
                            //插入到图库
                            startActivity(new Intent(this,DestroyActivity.class));

                        }, view2 -> {
                        });

                break;
        }
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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
