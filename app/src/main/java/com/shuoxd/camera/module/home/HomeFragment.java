package com.shuoxd.camera.module.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shuoxd.camera.HomePresenter;
import com.shuoxd.camera.MainActivity;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.HomeDeviceBigAdapter;
import com.shuoxd.camera.adapter.HomeDeviceSmallAdapter;
import com.shuoxd.camera.base.BaseBean;
import com.shuoxd.camera.base.BaseFragment;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.customview.LinearDivider;
import com.shuoxd.camera.customview.MySwipeRefreshLayout;
import com.shuoxd.camera.zxing.CustomScanActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeFragment extends BaseFragment<HomePresenter> implements HomeView,
        Toolbar.OnMenuItemClickListener, BaseQuickAdapter.OnItemChildClickListener,
        BaseQuickAdapter.OnItemClickListener {


    @BindView(R.id.rlv_device)
    RecyclerView rlvDevice;
    @BindView(R.id.srl_pull)
    MySwipeRefreshLayout srlPull;


    /*设备部分*/
    private HomeDeviceSmallAdapter mSmallAdapter;
    private HomeDeviceBigAdapter mBigAdapter;


    private List<CameraBean> deviceList = new ArrayList<>();


    //切换布局
    public int TYPE_SMALL = 1;
    public int TYPE_BIG = 2;

    private int mLayoutType = TYPE_SMALL;


    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(getContext(), this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_frament;
    }

    @Override
    protected void initView() {
        srlPull.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.color_theme_green));
        //设备列表初始化
        setSmallAdapter();
    }


    @Override
    protected void initImmersionBar() {
    }


    private void changeLayout() {
        int itemDecorationCount = rlvDevice.getItemDecorationCount();
        for (int i = 0; i < itemDecorationCount; i++) {
            rlvDevice.removeItemDecorationAt(i);
        }
        //布局切换方法
        if (mLayoutType == TYPE_SMALL) {
            setSmallAdapter();
            mLayoutType = TYPE_BIG;
        } else {
            //列表模式
            setBigAdapter();
            mLayoutType = TYPE_SMALL;
        }

    }

    //小图片布局
    private void setSmallAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlvDevice.setLayoutManager(layoutManager);
        mSmallAdapter = new HomeDeviceSmallAdapter(R.layout.item_camera, deviceList);
        rlvDevice.setAdapter(mSmallAdapter);
        rlvDevice.addItemDecoration(new LinearDivider(getActivity(), LinearLayoutManager.VERTICAL, ContextCompat.getColor(getActivity(), R.color.nocolor), 32));
        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_empty_view, null);
        mSmallAdapter.setEmptyView(view);
        mSmallAdapter.setHeaderAndEmpty(true);
        //添加两个头布局
        View adHeader = LayoutInflater.from(getContext()).inflate(R.layout.home_top_bigfic, null);
        mSmallAdapter.addHeaderView(adHeader);
        View menuHeader = LayoutInflater.from(getContext()).inflate(R.layout.home_header_menu, null);
        ImageView ivStyle = menuHeader.findViewById(R.id.iv_style);
        ivStyle.setImageResource(R.drawable.list_style_menu);
        ivStyle.setOnClickListener(view1 -> {
            changeLayout();
        });

        ImageView ivAdd = menuHeader.findViewById(R.id.iv_add);
        ivAdd.setOnClickListener(view12 -> {
            Intent intent = new Intent(getContext(), CustomScanActivity.class);
            startActivity(intent);
        });
        mSmallAdapter.addHeaderView(menuHeader);

    }

    //大图片布局
    private void setBigAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlvDevice.setLayoutManager(layoutManager);
        mBigAdapter = new HomeDeviceBigAdapter(R.layout.item_camera_big, deviceList);
        rlvDevice.setAdapter(mBigAdapter);
        rlvDevice.addItemDecoration(new LinearDivider(getActivity(), LinearLayoutManager.VERTICAL, ContextCompat.getColor(getActivity(), R.color.nocolor), 32));
        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_empty_view, null);
        mBigAdapter.setEmptyView(view);
        //添加两个头布局
        View adHeader = LayoutInflater.from(getContext()).inflate(R.layout.home_top_bigfic, null);
        mBigAdapter.addHeaderView(adHeader);
        View menuHeader = LayoutInflater.from(getContext()).inflate(R.layout.home_header_menu, null);
        ImageView ivStyle = menuHeader.findViewById(R.id.iv_style);
        ivStyle.setImageResource(R.drawable.list_style_menu2);
        ivStyle.setOnClickListener(view1 -> {
            changeLayout();
        });
        ImageView ivAdd = menuHeader.findViewById(R.id.iv_add);
        ivAdd.setOnClickListener(view12 -> {
            Intent intent = new Intent(getContext(), CustomScanActivity.class);
            startActivity(intent);
        });
        mBigAdapter.addHeaderView(menuHeader);

    }


    @Override
    protected void initData() {
        srlPull.setOnRefreshListener(() -> {
            try {
                presenter.getAlldevice();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        //获取列表设备列表
        try {
            presenter.getAlldevice();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        CameraBean cameraBean;
        if (mLayoutType==TYPE_SMALL){
             cameraBean = mBigAdapter.getData().get(position);
        }else {
             cameraBean = mBigAdapter.getData().get(position);
        }
        String id = cameraBean.getCamera().getId();
        showCameraInfo(id);

    }



    /**
     * 点击item跳转到Fragment1V2
     *
     */
    private void showCameraInfo(String id) {
        MainActivity main = (MainActivity) getActivity();
        main.cameraId = id;
        main.showCameraInfo();
    }



    @Override
    public void setDeviceList(List<CameraBean> cameraBeanList) {
        srlPull.setRefreshing(false);
        deviceList.clear();
        deviceList.addAll(cameraBeanList);
        //布局切换方法
        if (mLayoutType == TYPE_BIG) {
            mBigAdapter.replaceData(deviceList);
            mLayoutType = TYPE_BIG;
        } else {
            //列表模式
            mSmallAdapter.replaceData(deviceList);
            mLayoutType = TYPE_SMALL;
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

    /**
     * 总览跳转刷新
     *
     */
    public void jumpRefresh() {

    }

}
