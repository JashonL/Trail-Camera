package com.shuoxd.camera.module.me;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.shuoxd.camera.R;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.utils.AppUtils;
import com.shuoxd.camera.utils.CircleDialogUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DestroyActivity extends BaseActivity<DestroyPresenter> implements DestroyView, Toolbar.OnMenuItemClickListener {

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_toolbar)
    LinearLayout appToolbar;

    @BindView(R.id.btn_delete)
    Button btnDelte;

    private boolean isDelete;


    @Override
    protected DestroyPresenter createPresenter() {
        return new DestroyPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_destroy;
    }

    @Override
    protected void initViews() {
        //toolbar
        initToobar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        tvTitle.setText(R.string.m291_delete_account);


        int userType = App.getUserBean().getUserType();
        if (userType >= 200 && userType < 300) {
            isDelete = true;
            btnDelte.setText(R.string.m293_recover_account);
        } else {
            isDelete = false;
            btnDelte.setText(R.string.m291_delete_account);
        }
    }


    @OnClick({R.id.btn_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_delete:
                if (isDelete) {
                    CircleDialogUtils.showCommentDialog(this, "", getString(R.string.m294_recover_tips),
                            getString(R.string.m152_ok), getString(R.string.m127_cancel), Gravity.CENTER, view22 -> {
                                presenter.deleteUser("recover");
                            }, view2 -> {
                            });
                } else {
                    presenter.deleteUser("remove");
                }
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

    @Override
    public void destroysuccess() {
        finish();
    }
}
