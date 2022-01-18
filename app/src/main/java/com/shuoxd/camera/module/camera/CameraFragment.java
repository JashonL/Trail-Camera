package com.shuoxd.camera.module.camera;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.navigation.NavigationView;
import com.gyf.immersionbar.ImmersionBar;
import com.shuoxd.camera.MainActivity;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.CameraFiterAdapter;
import com.shuoxd.camera.adapter.CameraInfoAdapter;
import com.shuoxd.camera.adapter.CameraPicAdapter;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseBean;
import com.shuoxd.camera.base.BaseFragment;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.bean.InfoHeadBean;
import com.shuoxd.camera.bean.PictureBean;
import com.shuoxd.camera.customview.CustomLoadMoreView;
import com.shuoxd.camera.customview.GridDivider;
import com.shuoxd.camera.customview.MySwipeRefreshLayout;
import com.shuoxd.camera.eventbus.FreshCameraName;
import com.shuoxd.camera.eventbus.FreshPhoto;
import com.shuoxd.camera.module.leftmenu.CameraNavigationViewFragment;
import com.shuoxd.camera.module.leftmenu.HomeNavigationViewFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CameraFragment extends BaseFragment<CameraPresenter> implements CameraView, Toolbar.OnMenuItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener, CameraNavigationViewFragment.IMenuListeners {


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
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.rv_menu)
    RecyclerView rvMenu;
    @BindView(R.id.iv_style)
    ImageView ivStyle;
    @BindView(R.id.tv_pic_num)
    TextView tvPicNum;
    @BindView(R.id.iv_switch)
    ImageView ivSwitch;

    @BindView(R.id.navigationview1)
    NavigationView navigationview;
    @BindView(R.id.drawer_layout1)
    DrawerLayout drawerLayout;
    @BindView(R.id.v_pop)
    View vPop;

    private CameraPicAdapter mAdapter;

    private CameraInfoAdapter mCameraInfoAdapter;

    private List<PictureBean> picList = new ArrayList<>();


    private int spanCount = 1;
    public String cameraId;

    public String alias;

    @Override
    protected CameraPresenter createPresenter() {
        return new CameraPresenter(getContext(), this);
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


        //设备图片列表
        setAdapter(spanCount);

        //设备相片列表

        ivStyle.setImageResource(R.drawable.list_pic_row);
        ivSwitch.setImageResource(R.drawable.list_style_menu);

        rvMenu.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mCameraInfoAdapter = new CameraInfoAdapter(R.layout.item_camera_info, new ArrayList<>());
        rvMenu.setAdapter(mCameraInfoAdapter);
        mCameraInfoAdapter.setOnItemClickListener(this);


        ivSwitch.setOnClickListener(view12 -> {
            int itemDecorationCount = rlvDevice.getItemDecorationCount();
            for (int i = 0; i < itemDecorationCount; i++) {
                rlvDevice.removeItemDecorationAt(i);
            }
            if (spanCount == 1) {
                spanCount = 2;
                ivSwitch.setImageResource(R.drawable.camera_arrang);
            } else if (spanCount == 2) {
                spanCount = 3;
                ivSwitch.setImageResource(R.drawable.spancount);
            } else {
                spanCount = 1;
                ivSwitch.setImageResource(R.drawable.list_style_menu);
            }
            setAdapter(spanCount);
        });


        srlPull.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.color_theme_green));


        //添加两个头布局
   /*     View adHeader = LayoutInflater.from(getContext()).inflate(R.layout.header_of_carden, rlvDevice, false);
        RecyclerView rvMenu = adHeader.findViewById(R.id.rv_menu);
        mCameraInfoAdapter = new CameraInfoAdapter(R.layout.item_camera_info, new ArrayList<>());
        rvMenu.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvMenu.setAdapter(mCameraInfoAdapter);

        mAdapter.addHeaderView(adHeader);
        View menuHeader = LayoutInflater.from(getContext()).inflate(R.layout.home_top2_listmenu, rlvDevice, false);
        ImageView ivStyle = menuHeader.findViewById(R.id.iv_style);
        ivStyle.setImageResource(R.drawable.list_style_menu);
        ivStyle.setOnClickListener(view1 -> {
        });

        ImageView ivAdd = menuHeader.findViewById(R.id.iv_add);
        ivAdd.setOnClickListener(view12 -> {
            Intent intent = new Intent(getContext(), CustomScanActivity.class);
            startActivity(intent);
        });
        mAdapter.addHeaderView(menuHeader);*/
        /*---------------------------自定义侧边栏布局-----------------------------*/
        ivStyle.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.navigationview1, new CameraNavigationViewFragment(this)).commit();

    }


    //小图片布局
    private void setAdapter(int span) {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), span);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlvDevice.setLayoutManager(layoutManager);
        mAdapter = new CameraPicAdapter(R.layout.item_camera_pic, picList);
        rlvDevice.setAdapter(mAdapter);
        rlvDevice.addItemDecoration(new GridDivider(ContextCompat.getColor(getActivity(), R.color.nocolor), 10, 10));
        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_empty_view, rlvDevice, false);
        mAdapter.setEmptyView(view);
        mAdapter.setHeaderAndEmpty(true);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.disableLoadMoreIfNotFullPage(rlvDevice);
        mAdapter.setOnLoadMoreListener(() -> {
            try {
                presenter.getCameraPic();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, rlvDevice);

        mAdapter.setOnItemClickListener(this);
    }


    @Override
    protected void initData() {
        EventBus.getDefault().register(this);

        cameraId = getArguments().getString("cameraId");
        alias = getArguments().getString("alias");

        if (TextUtils.isEmpty(alias)) {
            alias = cameraId;
        }
        if (TextUtils.isEmpty(alias)) {
            alias = "";
        }
        tvTitle.setText(alias);


        srlPull.setOnRefreshListener(() -> {
            try {
                presenter.setTotalPage(1);
                presenter.setPageNow(0);
                String accountName = App.getUserBean().getAccountName();
                presenter.cameraInfo(cameraId, accountName);
                presenter.getCameraPic();
                presenter.getAlldevice();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        //获取列表设备列表
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    //从其他页面跳转进来
    public void refresh() {
        String accountName = App.getUserBean().getAccountName();
        presenter.setTotalPage(1);
        presenter.setPageNow(0);
        presenter.cameraInfo(cameraId, accountName);
        presenter.setImeis(cameraId);
        presenter.setIsAllCamera("-1");
        presenter.getCameraPic();
        presenter.getAlldevice();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.right_action) {
            // 一锟斤拷锟皆讹拷锟斤拷牟锟斤拷郑锟斤拷锟轿拷锟绞撅拷锟斤拷锟斤拷锟�
            View contentView = LayoutInflater.from(getActivity()).inflate(
                    R.layout.pop_layout, null);

            List<CameraBean> cameraList = presenter.getCameraList();
            RecyclerView rvCamera = contentView.findViewById(R.id.ry_camera);
            rvCamera.setLayoutManager(new LinearLayoutManager(getContext()));
            CameraFiterAdapter camerAdapter = new CameraFiterAdapter(R.layout.item_camera_menu, cameraList);
            rvCamera.setAdapter(camerAdapter);
            camerAdapter.setOnItemClickListener((adapter, view, position) -> {
                camerAdapter.setNowSelectPosition(position);
                CameraBean cameraBean = cameraList.get(position);
                cameraId = cameraBean.getCamera().getImei();
                presenter.setImeis(cameraId);
                presenter.setTotalPage(1);
                presenter.setPageNow(0);
                String accountName = App.getUserBean().getAccountName();
                presenter.cameraInfo(cameraId, accountName);
                presenter.getCameraPic();
            });


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


            popupWindow.setTouchInterceptor((v, event) -> false);
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 0.4f; //设置透明度
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            getActivity().getWindow().setAttributes(lp);
            popupWindow.setOnDismissListener(() -> {
                WindowManager.LayoutParams lp1 = getActivity().getWindow().getAttributes();
                lp1.alpha = 1f;
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getActivity().getWindow().setAttributes(lp1);
            });
            popupWindow.setBackgroundDrawable(new ColorDrawable(0));
            popupWindow.setAnimationStyle(R.style.Popup_Anim);
            popupWindow.showAsDropDown(vPop);
        }
        return true;
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter == mCameraInfoAdapter) {
            switch (position) {
                case 4:
                    Intent intent = new Intent(getContext(), ChartActivity.class);
                    intent.putExtra("imei", cameraId);
                    startActivity(intent);
                    break;
                case 5:
                    Intent intent5 = new Intent(getContext(), CameraStepUpActivity.class);
                    intent5.putExtra("imei", cameraId);
                    startActivity(intent5);
                    break;
            }

        }

        if (adapter == mAdapter) {
            CameraShowListManerge.getInstance().setPicList(picList);
            Intent intent = new Intent(getContext(), CameraDetailActivity.class);
            intent.putExtra("position", position);
            if (!TextUtils.isEmpty(alias)) {
                intent.putExtra("alias", alias);
            } else {
                intent.putExtra("alias", cameraId);
            }
            startActivity(intent);
        }

    }


    @Override
    public void showCameraInfo(CameraBean.CameraInfo cameraBean) {

        String[] title = {
                getString(R.string.m70_Signal), getString(R.string.m75_steup), getString(R.string.m72_sd),
                getString(R.string.m73_map), getString(R.string.m74_tracker), getString(R.string.m75_steup),
        };

        List<InfoHeadBean> beans = new ArrayList<>();
        for (int i = 0; i < title.length; i++) {
            InfoHeadBean bean = new InfoHeadBean();
            bean.setTitle(title[i]);
            switch (i) {
                case 0:
                    String signalStrength = cameraBean.getSignalStrength();
                    int wifiStrength = Integer.parseInt(signalStrength);
                    if (wifiStrength == 0) {
                        bean.setIconRes(R.drawable.signal1);
                    } else if (wifiStrength <= 25) {
                        bean.setIconRes(R.drawable.signal1);
                    } else if (wifiStrength <= 50) {
                        bean.setIconRes(R.drawable.signal2);
                    } else if (wifiStrength <= 75) {
                        bean.setIconRes(R.drawable.signal3);
                    } else {
                        bean.setIconRes(R.drawable.signal4);
                    }
                    break;
                case 1:
                    String batteryLevel = cameraBean.getBatteryLevel();
                    int batteryL = Integer.parseInt(batteryLevel);
                    String extDcLevel = cameraBean.getExtDcLevel();
                    if ("100".equals(extDcLevel)) {
                        bean.setIconRes(R.drawable.chadian);
                        bean.setTitle(extDcLevel + "%");
                    } else {
                        if (batteryL == 0) {
                            bean.setIconRes(R.drawable.bat1);
                        } else if (batteryL <= 25) {
                            bean.setIconRes(R.drawable.bat1);
                        } else if (batteryL <= 50) {
                            bean.setIconRes(R.drawable.bat2);
                        } else if (batteryL <= 75) {
                            bean.setIconRes(R.drawable.bat3);
                        } else {
                            bean.setIconRes(R.drawable.bat4);
                        }
                        bean.setTitle(batteryLevel + "%");
                    }


                    break;
                case 2:
                    String cardSpace = cameraBean.getCardSpace();
                    int sSpace = Integer.parseInt(cardSpace);
                    if (sSpace == 0) {
                        bean.setIconRes(R.drawable.sdcard1);
                    } else if (sSpace <= 25) {
                        bean.setIconRes(R.drawable.sdcard1);
                    } else if (sSpace <= 50) {
                        bean.setIconRes(R.drawable.sdcard2);
                    } else if (sSpace <= 75) {
                        bean.setIconRes(R.drawable.sdcard3);
                    } else {
                        bean.setIconRes(R.drawable.sdcard4);
                    }
                    bean.setTitle(cardSpace + "%");

                    break;
                case 3:
                    bean.setIconRes(R.drawable.map);
                    break;
                case 4:
                    bean.setIconRes(R.drawable.chart);
                    break;
                case 5:
                    bean.setIconRes(R.drawable.setting);
                    break;
            }
            beans.add(bean);
        }
        mCameraInfoAdapter.replaceData(beans);

    }

    @Override
    public void showCameraPic(List<PictureBean> beans) {
        srlPull.setRefreshing(false);
        int pageNow = presenter.getPageNow();
        if (pageNow == 1) {
            picList.clear();
        }
        picList.addAll(beans);

        if (pageNow == 1) {
            mAdapter.setNewData(picList);
        } else {
            mAdapter.addData(beans);
            mAdapter.loadMoreComplete();
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


    @Override
    public void showNoMoreData() {
        //数据已加载完
        mAdapter.loadMoreEnd();
    }

    @Override
    public void showMoreFail() {
        //数据加载完毕
        mAdapter.loadMoreFail();
    }

    @Override
    public void showTotalNum(int totalNum) {
        String s = totalNum +" "+ getString(R.string.m76_photos);
        tvPicNum.setText(s);
    }

    @Override
    public void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(false, 0.2f)//设置状态栏图片为深色，(如果android 6.0以下就是半透明)
                .statusBarColor(R.color.color_app_main)//这里的颜色，你可以自定义。
                .statusBarView(statusBarView)
                .init();

    }

    @Override
    public void reset() {
        presenter.setTotalPage(1);
        presenter.setPageNow(0);
        presenter.defautParams();
        presenter.getCameraPic();
        drawerLayout.closeDrawers();
    }

    @Override
    public void apply(String startDate, String endDate, String amPm,
                      String photoType, String favorites, String moonPhase,
                      String startTemperature, String endTemperature, String temperatureUnit) {

        presenter.setStartDate(startDate);
        presenter.setEndDate(endDate);
        presenter.setAmPm(amPm);
        presenter.setPhotoType(photoType);
        presenter.setFavorites(favorites);
        presenter.setMoonPhase(moonPhase);
        presenter.setStartTemperature(startTemperature);
        presenter.setEndTemperature(endTemperature);
        presenter.setTemperatureUnit(temperatureUnit);
        presenter.setTotalPage(1);
        presenter.setPageNow(0);
        presenter.getCameraPic();
        drawerLayout.closeDrawers();

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdata(FreshPhoto bean) {
        //刷新图片列表
        try {
            presenter.setTotalPage(1);
            presenter.setPageNow(0);
            presenter.getCameraPic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdata(FreshCameraName bean) {
        //刷新图片列表
        try {
            String name = bean.getName();
            if (!TextUtils.isEmpty(name)){
                tvTitle.setText(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
