package com.shuoxd.camera.module.home;

import static com.shuoxd.camera.constants.PermissionConstant.RC_CAMERA;

import android.Manifest;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.ImmersionBar;
import com.mylhyl.circledialog.CircleDialog;
import com.shuoxd.camera.HomePresenter;
import com.shuoxd.camera.MainActivity;
import com.shuoxd.camera.MainActivity2;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.HomeDeviceBigAdapter;
import com.shuoxd.camera.adapter.HomeDeviceSmallAdapter;
import com.shuoxd.camera.base.BaseBean;
import com.shuoxd.camera.base.BaseFragment;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.constants.SharePreferenConstants;
import com.shuoxd.camera.customview.CustomLoadMoreView;
import com.shuoxd.camera.customview.LinearDivider;
import com.shuoxd.camera.customview.MySwipeRefreshLayout;
import com.shuoxd.camera.eventbus.FreshCameraList;
import com.shuoxd.camera.eventbus.FreshCameraLocation;
import com.shuoxd.camera.utils.LogUtil;
import com.shuoxd.camera.utils.SharedPreferencesUnit;
import com.shuoxd.camera.zxing.ScanActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class HomeFragment extends BaseFragment<HomePresenter> implements HomeView,
        Toolbar.OnMenuItemClickListener, BaseQuickAdapter.OnItemChildClickListener,
        BaseQuickAdapter.OnItemClickListener ,BaseQuickAdapter.OnItemLongClickListener {


    @BindView(R.id.rlv_device)
    RecyclerView rlvDevice;
    @BindView(R.id.srl_pull)
    MySwipeRefreshLayout srlPull;
    @BindView(R.id.iv_style)
    ImageView ivStyle;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.status_bar_view)
    View statusBarView;


    /*????????????*/
    private HomeDeviceSmallAdapter mSmallAdapter;
    private HomeDeviceBigAdapter mBigAdapter;




    //????????????
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
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).
                statusBarColor(R.color.white).fitsSystemWindows(true).navigationBarColor(R.color.white)
                .init();
        LogUtil.d("??????homefragment???????????????");
        //???????????????
        EventBus.getDefault().register(this);

        srlPull.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.color_theme_green));


        mLayoutType = SharedPreferencesUnit.getInstance(getContext()).getInt(SharePreferenConstants.SP_HOME_SHOW_STYLE);
        if (mLayoutType==0){
            mLayoutType=TYPE_SMALL;
        }

        //?????????????????????
        if (mLayoutType == TYPE_BIG) {
            setBigAdapter();
            mLayoutType = TYPE_BIG;
        } else {
            //????????????
            setSmallAdapter();
            mLayoutType = TYPE_SMALL;
        }
    }


    private void changeLayout() {
        int itemDecorationCount = rlvDevice.getItemDecorationCount();
        for (int i = 0; i  <itemDecorationCount; i++) {
            rlvDevice.removeItemDecorationAt(i);
        }
        //??????????????????
        if (mLayoutType == TYPE_SMALL) {
            setBigAdapter();
            mLayoutType = TYPE_BIG;
        } else {
            //????????????
            setSmallAdapter();
            mLayoutType = TYPE_SMALL;
        }

        //???????????????
        SharedPreferencesUnit.getInstance(getContext()).putInt(SharePreferenConstants.SP_HOME_SHOW_STYLE,mLayoutType);


    }

   // ???????????????
    private void setSmallAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlvDevice.setLayoutManager(layoutManager);

        List<CameraBean> data=new ArrayList();
        if (mBigAdapter!=null){
            data=mBigAdapter.getData();
        }


        mSmallAdapter = new HomeDeviceSmallAdapter(data);
        mSmallAdapter.setLoadMoreView(new CustomLoadMoreView());
        mSmallAdapter.setOnItemLongClickListener(this);
        rlvDevice.setAdapter(mSmallAdapter);
        rlvDevice.addItemDecoration(new LinearDivider(getActivity(), LinearLayoutManager.VERTICAL, 32, ContextCompat.getColor(getActivity(), R.color.nocolor)));
        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_empty_view, null);
        mSmallAdapter.setEmptyView(view);
        mSmallAdapter.setHeaderAndEmpty(true);
        ivStyle.setImageResource(R.drawable.list_style_menu);
        mSmallAdapter.setOnItemClickListener(this);
        mSmallAdapter.disableLoadMoreIfNotFullPage(rlvDevice);
        mSmallAdapter.setOnLoadMoreListener(() -> {
            try {
                presenter.getAlldevice();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },rlvDevice);



 /*       //?????????????????????
        View adHeader = LayoutInflater.from(getContext()).inflate(R.layout.home_top_bigfic, null);
        mSmallAdapter.addHeaderView(adHeader);
        View menuHeader = LayoutInflater.from(getContext()).inflate(R.layout.home_header_menu, null);
        ImageView ivStyle = menuHeader.findViewById(R.id.iv_style);
        ivStyle.setImageResource(R.drawable.list_style_menu);
        ivStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayout();

            }
        });


        ImageView ivAdd = menuHeader.findViewById(R.id.iv_add);
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CustomScanActivity.class);
                startActivity(intent);
            }
        });


        mSmallAdapter.addHeaderView(menuHeader);*/

    }

   // ???????????????
    private void setBigAdapter() {
        ivStyle.setImageResource(R.drawable.list_style_menu2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlvDevice.setLayoutManager(layoutManager);

        List<CameraBean> data=new ArrayList();
        if (mSmallAdapter!=null){
            data=mSmallAdapter.getData();
        }
        mBigAdapter = new HomeDeviceBigAdapter(data);
        mBigAdapter.setLoadMoreView(new CustomLoadMoreView());
        rlvDevice.setAdapter(mBigAdapter);
        rlvDevice.addItemDecoration(new LinearDivider(getActivity(), LinearLayoutManager.VERTICAL, 32, ContextCompat.getColor(getActivity(), R.color.nocolor)));
        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_empty_view, null);
        mBigAdapter.setEmptyView(view);
        mBigAdapter.setOnItemClickListener(this);
        mBigAdapter.disableLoadMoreIfNotFullPage(rlvDevice);
        mBigAdapter.setOnItemLongClickListener(this);


        mBigAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                try {
                    presenter.getAlldevice();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },rlvDevice);


/*
        //?????????????????????
        View adHeader = LayoutInflater.from(getContext()).inflate(R.layout.home_top_bigfic, null);
        mBigAdapter.addHeaderView(adHeader);
        View menuHeader = LayoutInflater.from(getContext()).inflate(R.layout.home_header_menu, null);
        ImageView ivStyle = menuHeader.findViewById(R.id.iv_style);
        ivStyle.setImageResource(R.drawable.list_style_menu2);

        ivStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayout();

            }
        });

        ImageView ivAdd = menuHeader.findViewById(R.id.iv_add);
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CustomScanActivity.class);
                startActivity(intent);
            }
        });



        mBigAdapter.addHeaderView(menuHeader);*/

    }


    @Override
    protected void initData() {
        srlPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    presenter.setPageNow(0);
                    presenter.setTotalPage(1);
                    presenter.getAlldevice();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

       // ????????????????????????
        try {
            presenter.setPageNow(0);
            presenter.setTotalPage(1);
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
        if (mLayoutType == TYPE_SMALL) {
            cameraBean = mSmallAdapter.getData().get(position);
        } else {
            cameraBean = mBigAdapter.getData().get(position);
        }

        CameraBean.CameraInfo camera = cameraBean.getCamera();

        String id = camera.getImei();
        String alias = camera.getAlias();

        //??????
        String signalStrength = camera.getSignalStrength();
        String batteryLevel = camera.getBatteryLevel();
        String cardSpace = camera.getCardSpace();
        String isNew = camera.getIsNew();
        String extDcLevel = camera.getExtDcLevel();


        showCameraInfo(id,alias,signalStrength,batteryLevel,cardSpace,isNew,extDcLevel);

    }



    //??????item?????????Fragment1V2

    private void showCameraInfo(String id, String alias,
                                String signalStrength,
                                String batteryLevel,
                                String cardSpace,String extDcLevel,String isNew) {
   /*     MainActivity2 main = (MainActivity2) getActivity();
        main.cameraId = id;
        main.cameraAlias = alias;*/
//        main.showCameraInfo();

        HomeComFragment parentFragment = (HomeComFragment)getParentFragment();
        assert parentFragment != null;
        parentFragment.cameraId = id;
        parentFragment.cameraAlias = alias;

        parentFragment.signalStrength = signalStrength;
        parentFragment.batteryLevel = batteryLevel;
        parentFragment.cardSpace = cardSpace;
        parentFragment.extDcLevel=extDcLevel;
        parentFragment.isNew = isNew;

        parentFragment.showCamera();
    }


    @Override
    public void setDeviceList(List<CameraBean> cameraBeanList) {
        srlPull.setRefreshing(false);
        int pageNow = presenter.getPageNow();
        if (mLayoutType == TYPE_BIG) {
            if (pageNow == 1) {
                List<CameraBean> adapterList = new ArrayList(cameraBeanList);
                mBigAdapter.setNewData(adapterList);

            } else {
                mBigAdapter.addData(cameraBeanList);
                mBigAdapter.loadMoreComplete();

            }



        } else {
            //????????????
            if (pageNow == 1) {
                List<CameraBean> adapterList = new ArrayList(cameraBeanList);
                mSmallAdapter.setNewData(adapterList);

            } else {
                mSmallAdapter.addData(cameraBeanList);
                mSmallAdapter.loadMoreComplete();

            }

        }

    }

    @Override
    public void showNoMoreData() {
        srlPull.setRefreshing(false);
        if (mLayoutType == TYPE_BIG) {
            //??????????????????
            mBigAdapter.loadMoreEnd();
        } else {
            //??????????????????
            mSmallAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showMoreFail() {
        if (mLayoutType == TYPE_BIG) {
           // ??????????????????
            mBigAdapter.loadMoreFail();
        } else {
            //??????????????????
            mSmallAdapter.loadMoreFail();
        }

    }

    @Override
    public void deleteSuccess() {
       // ????????????????????????
        try {
            presenter.setPageNow(0);
            presenter.setTotalPage(1);
            presenter.getAlldevice();
        } catch (Exception e) {
            e.printStackTrace();
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


    //??????????????????

    public void jumpRefresh() {

    }

    @OnClick({R.id.iv_style, R.id.iv_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_style:
                changeLayout();
                break;
            case R.id.iv_add:
                checkCameraPermissions();
                break;
        }
    }









    /**
     * ??????????????????
     */
    @AfterPermissionGranted(RC_CAMERA)
    private void checkCameraPermissions() {
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {//?????????
            presenter.addCamera();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.m162_requires_permission),
                    RC_CAMERA, perms);
        }
    }




    @Override
    public void initImmersionBar() {

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdata(FreshCameraList bean) {
        //??????????????????
        try {
            presenter.setPageNow(0);
            presenter.setTotalPage(1);
            presenter.getAlldevice();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdata(FreshCameraLocation bean) {
        //??????????????????
        try {
            presenter.setPageNow(0);
            presenter.setTotalPage(1);
            presenter.getAlldevice();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

        CameraBean cameraBean;
        if (mLayoutType == TYPE_BIG) {
            cameraBean = mBigAdapter.getData().get(position);
        } else {
            cameraBean = mSmallAdapter.getData().get(position);
        }
        String imei = cameraBean.getCamera().getImei();

        new CircleDialog.Builder().setWidth(0.7f)
                .setTitle(getString(R.string.m150_tips))
                .setText(getString(R.string.m167_delete_camera))
                .setNegative(getString(R.string.m127_cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })


                .setPositive(getString(R.string.m152_ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        presenter.cameraOperation(imei, "remove", "1");
                    }
                }).show(getActivity().getSupportFragmentManager());



        return true;
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
