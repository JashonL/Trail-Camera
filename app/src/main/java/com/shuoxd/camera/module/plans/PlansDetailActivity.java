package com.shuoxd.camera.module.plans;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.CameraFiterAdapter;
import com.shuoxd.camera.adapter.CameraPicVedeoAdapter;
import com.shuoxd.camera.adapter.PlansInfoAdapter;
import com.shuoxd.camera.adapter.PlansListAdapter;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.bean.PictureBean;
import com.shuoxd.camera.bean.PlansInfoBean;
import com.shuoxd.camera.customview.LinearDivider;
import com.shuoxd.camera.download.CheckDownloadUtils;
import com.shuoxd.camera.download.FileDownLoadManager;
import com.shuoxd.camera.module.camera.CameraDetailActivity;
import com.shuoxd.camera.utils.CircleDialogUtils;
import com.shuoxd.camera.utils.FileUtils;
import com.shuoxd.camera.utils.MyToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PlansDetailActivity extends BaseActivity<PlansDetailPresenter>
        implements PlansDetailView, BaseQuickAdapter.OnItemChildClickListener,
        BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemLongClickListener,
        Toolbar.OnMenuItemClickListener {

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_toolbar)
    LinearLayout appToolbar;
    @BindView(R.id.rlv_plans_info)
    RecyclerView rlvPlansInfo;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.v_pop)
    View vPop;
    @BindView(R.id.suspend)
    TextView suspend;
    @BindView(R.id.iv_status)
    ImageView ivStatus;

    private PlansInfoAdapter mAdapter;
    private String status;

    @Override
    protected PlansDetailPresenter createPresenter() {
        return new PlansDetailPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_plans_detail;
    }

    @Override
    protected void initViews() {
        initToobar(toolbar);
        tvTitle.setText(R.string.m79_plans);

        toolbar.inflateMenu(R.menu.menu_camera);
        toolbar.setOnMenuItemClickListener(this);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlvPlansInfo.setLayoutManager(layoutManager);
        mAdapter = new PlansInfoAdapter(new ArrayList<>());
        rlvPlansInfo.setAdapter(mAdapter);
        rlvPlansInfo.addItemDecoration(new LinearDivider(this, LinearLayoutManager.VERTICAL,
                1, ContextCompat.getColor(this, R.color.gray_d2d2d)));
        View view = LayoutInflater.from(this).inflate(R.layout.list_empty_view, null);
        mAdapter.setEmptyView(view);
        mAdapter.setOnItemClickListener(this);
        mAdapter.disableLoadMoreIfNotFullPage(rlvPlansInfo);
        mAdapter.setOnItemLongClickListener(this);

    }


    @OnClick({R.id.ll_change, R.id.ll_suspend, R.id.ll_remove, R.id.ll_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_change:
                String imeis = presenter.getImeis();
                Intent intent = new Intent(this, PlansChangeActivity.class);
                intent.putExtra("imei", imeis);
                startActivity(intent);
                break;
            case R.id.ll_suspend:
                String imei = presenter.getImeis();
                String s;
                String content;
                if ("suspend".equalsIgnoreCase(status)) {
                    s = getString(R.string.m256_ative_plan);
                    content = getString(R.string.m254_plan_ative);
                } else {
                    s = getString(R.string.m255_suspend_plan);
                    content = getString(R.string.m253_plan_suspend);
                }
                CircleDialogUtils.showCommentDialog(PlansDetailActivity.this, s,
                        content,
                        getString(R.string.m152_ok),
                        getString(R.string.m127_cancel), Gravity.CENTER, view1 -> {
                            presenter.changePlanStatus(imei);
                        }, view12 -> {
                        });
                break;
            case R.id.ll_remove:
                break;
            case R.id.ll_back:
                finish();
                break;
        }
    }


    @Override
    protected void initData() {
        String imei = getIntent().getStringExtra("imei");
        String alias = getIntent().getStringExtra("alias");

        if (TextUtils.isEmpty(alias)) {
            alias = imei;
        }


//        tvName.setText(alias);
        presenter.getAlldevice();
        presenter.setImeis(imei);
        presenter.getPlansInfo(imei);


    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        return false;
    }

    @Override
    public void showPlansInfo(List<PlansInfoBean> list) {
        mAdapter.replaceData(list);
    }

    @Override
    public void status(String status) {
        this.status = status;
        if (mAdapter.getData().size() > 0) {
            PlansInfoBean plansInfoBean = mAdapter.getData().get(1);
            plansInfoBean.setStatus(status);
            plansInfoBean.setValue(status);
            mAdapter.notifyDataSetChanged();
        }

        if ("suspend".equalsIgnoreCase(status)) {
            suspend.setText("Ative");
            ivStatus.setImageResource(R.drawable.plan_active);
        } else {
            suspend.setText("suspend");
            ivStatus.setImageResource(R.drawable.plan_suspend);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.right_action) {
            View contentView = LayoutInflater.from(this).inflate(
                    R.layout.pop_layout, null);
            List<CameraBean> cameraList = presenter.getCameraList();

            if (cameraList == null || cameraList.size() == 0) {
                presenter.getAlldevice();
            } else {
                int width = getResources().getDimensionPixelSize(R.dimen.dp_225);
                int hight = getResources().getDimensionPixelSize(R.dimen.dp_248);
                int itemHight = getResources().getDimensionPixelOffset(R.dimen.dp_40);
                if (itemHight * cameraList.size() > hight) {
                    hight = getResources().getDimensionPixelSize(R.dimen.dp_248);
                } else {
                    hight = LinearLayout.LayoutParams.WRAP_CONTENT;
                }
                final PopupWindow popupWindow = new PopupWindow(contentView, width, hight, true);
                popupWindow.setTouchable(true);
                RecyclerView rvCamera = contentView.findViewById(R.id.ry_camera);
                rvCamera.setLayoutManager(new LinearLayoutManager(this));
                CameraFiterAdapter camerAdapter = new CameraFiterAdapter(R.layout.item_camera_menu, cameraList);
                rvCamera.setAdapter(camerAdapter);
                camerAdapter.setOnItemClickListener((adapter, view, position) -> {
                    camerAdapter.setNowSelectPosition(position);
                    CameraBean cameraBean = cameraList.get(position);
                    String imei = cameraBean.getCamera().getImei();
                    presenter.setImeis(imei);
                    presenter.getPlansInfo(imei);
                    popupWindow.dismiss();
                });

                popupWindow.setTouchInterceptor((v, event) -> false);
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(lp);
                popupWindow.setOnDismissListener(() -> {
                    WindowManager.LayoutParams lp1 = getWindow().getAttributes();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    getWindow().setAttributes(lp1);
                });
                popupWindow.setBackgroundDrawable(new ColorDrawable(0));
                popupWindow.setAnimationStyle(R.style.Popup_Anim);
                popupWindow.showAsDropDown(vPop);
            }


        }
        return true;
    }
}
