package com.shuoxd.camera.module.camera;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shuoxd.camera.HomePresenter;
import com.shuoxd.camera.MainActivity;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.CameraPicAdapter;
import com.shuoxd.camera.adapter.HomeDeviceSmallAdapter;
import com.shuoxd.camera.base.BaseFragment;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.customview.LinearDivider;
import com.shuoxd.camera.customview.MySwipeRefreshLayout;
import com.shuoxd.camera.module.home.HomeView;
import com.shuoxd.camera.zxing.CustomScanActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CameraFragment extends BaseFragment<HomePresenter> implements HomeView,Toolbar.OnMenuItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener {


    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_toolbar)
    LinearLayout appToolbar;
    @BindView(R.id.rlv_device)
    RecyclerView rlvDevice;
    @BindView(R.id.srl_pull)
    MySwipeRefreshLayout srlPull;


    public String cameraId;

    private CameraPicAdapter mAdapter;


    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(getContext(), this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frament_camera_info;
    }

    @Override
    protected void initView() {
        //头部toolBar
        toolbar.inflateMenu(R.menu.menu_camera);
        toolbar.setOnMenuItemClickListener(this);
        tvTitle.setText(R.string.m65_camera_of_garden);
        toolbar.setNavigationIcon(R.drawable.icon_return_w);
        toolbar.setNavigationOnClickListener(view -> {
            //电站列表
            MainActivity main = (MainActivity) getActivity();
            main.showHome();
        });

        //

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlvDevice.setLayoutManager(layoutManager);
        mAdapter = new CameraPicAdapter(R.layout.item_camera_pic, new ArrayList<>());
        rlvDevice.setAdapter(mAdapter);
        rlvDevice.addItemDecoration(new LinearDivider(getActivity(), LinearLayoutManager.VERTICAL, ContextCompat.getColor(getActivity(), R.color.nocolor), 32));
        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_empty_view, rlvDevice);
        mAdapter.setEmptyView(view);
        //添加两个头布局
        View adHeader = LayoutInflater.from(getContext()).inflate(R.layout.header_of_carden, rlvDevice);
        mAdapter.addHeaderView(adHeader);
        View menuHeader = LayoutInflater.from(getContext()).inflate(R.layout.home_top2_listmenu, rlvDevice);
        ImageView ivStyle = menuHeader.findViewById(R.id.iv_style);
        ivStyle.setImageResource(R.drawable.list_style_menu);
        ivStyle.setOnClickListener(view1 -> {
        });

        ImageView ivAdd = menuHeader.findViewById(R.id.iv_add);
        ivAdd.setOnClickListener(view12 -> {
            Intent intent = new Intent(getContext(), CustomScanActivity.class);
            startActivity(intent);
        });
        mAdapter.addHeaderView(menuHeader);


    }

    @Override
    protected void initData() {

    }

    @Override
    public void setDeviceList(List<CameraBean> cameraBeanList) {

    }

    public void refresh() {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }





}
