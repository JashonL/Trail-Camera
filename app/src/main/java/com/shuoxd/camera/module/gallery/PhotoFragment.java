package com.shuoxd.camera.module.gallery;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.HomeDeviceSmallAdapter;
import com.shuoxd.camera.base.BaseFragment;
import com.shuoxd.camera.customview.LinearDivider;
import com.shuoxd.camera.customview.MySwipeRefreshLayout;
import com.shuoxd.camera.zxing.CustomScanActivity;

import java.util.ArrayList;

import butterknife.BindView;

public class PhotoFragment extends BaseFragment<PhotoPresenter> implements PhotoView , BaseQuickAdapter.OnItemClickListener {
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



    /*设备部分*/
    private HomeDeviceSmallAdapter mAdapter;


    @Override
    protected PhotoPresenter createPresenter() {
        return new PhotoPresenter(getContext(), this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.gallery_fragment;
    }

    @Override
    protected void initView() {
        srlPull.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.color_theme_green));

        //toolbar

        //头部toolBar

        //设备列表初始化
        setAdapter();
    }

    @Override
    protected void initData() {

    }

    //小图片布局
    private void setAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlvDevice.setLayoutManager(layoutManager);
        mAdapter = new HomeDeviceSmallAdapter(R.layout.item_camera, new ArrayList<>());
        rlvDevice.setAdapter(mAdapter);
        rlvDevice.addItemDecoration(new LinearDivider(getActivity(), LinearLayoutManager.VERTICAL, ContextCompat.getColor(getActivity(), R.color.nocolor), 32));
        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_empty_view, rlvDevice);
        mAdapter.setEmptyView(view);
        //添加两个头布局
        View menuHeader = LayoutInflater.from(getContext()).inflate(R.layout.header_of_carden, rlvDevice);
        mAdapter.addHeaderView(menuHeader);

        View switchHeader = LayoutInflater.from(getContext()).inflate(R.layout.home_top2_listmenu, rlvDevice);
        mAdapter.addHeaderView(switchHeader);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
