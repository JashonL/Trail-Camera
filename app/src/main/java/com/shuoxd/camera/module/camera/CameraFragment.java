package com.shuoxd.camera.module.camera;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.mylhyl.circledialog.BaseCircleDialog;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.view.listener.OnCreateBodyViewListener;
import com.shuoxd.camera.MainActivity;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.CameraFiterAdapter;
import com.shuoxd.camera.adapter.CameraInfoAdapter;
import com.shuoxd.camera.adapter.CameraPicAdapter;
import com.shuoxd.camera.adapter.CameraPicVedeoAdapter;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseBean;
import com.shuoxd.camera.base.BaseFragment;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.bean.InfoHeadBean;
import com.shuoxd.camera.bean.PictureBean;
import com.shuoxd.camera.constants.SharePreferenConstants;
import com.shuoxd.camera.customview.CustomLoadMoreView;
import com.shuoxd.camera.customview.GridDivider;
import com.shuoxd.camera.customview.MySwipeRefreshLayout;
import com.shuoxd.camera.eventbus.FreshCameraLocation;
import com.shuoxd.camera.eventbus.FreshCameraName;
import com.shuoxd.camera.eventbus.FreshPhoto;
import com.shuoxd.camera.module.leftmenu.CameraNavigationViewFragment;
import com.shuoxd.camera.module.leftmenu.HomeNavigationViewFragment;
import com.shuoxd.camera.module.map.MapActivity;
import com.shuoxd.camera.module.video.VideoPlayActivity;
import com.shuoxd.camera.utils.MyToastUtils;
import com.shuoxd.camera.utils.SharedPreferencesUnit;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CameraFragment extends BaseFragment<CameraPresenter> implements CameraView, Toolbar.OnMenuItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener,
        CameraNavigationViewFragment.IMenuListeners, CameraPicVedeoAdapter.SelectedListener
        , BaseQuickAdapter.OnItemLongClickListener {


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
    @BindView(R.id.content)
    LinearLayout content;


    @BindView(R.id.iv_edit)
    ImageView ivEdit;


    /*设备部分*/
//    private CameraPicAdapter mAdapter;
    private CameraPicVedeoAdapter mPicVideoAdapter;


    private CameraInfoAdapter mCameraInfoAdapter;

    private List<PictureBean> picList = new ArrayList<>();


    private int spanCount = 1;
    public String cameraId;

    public String alias;

    private CameraBean.CameraInfo info;


    private String upgrade="1";


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


        spanCount = SharedPreferencesUnit.getInstance(getContext()).getInt(SharePreferenConstants.SP_CAMERA_SHOW_STYLE);
        if (spanCount == 0) {
            spanCount = 1;
        }

        //设备列表初始化
        if (spanCount == 1) {
            ivSwitch.setImageResource(R.drawable.camera_arrang);
        } else if (spanCount == 2) {
            ivSwitch.setImageResource(R.drawable.spancount);
        } else {
            ivSwitch.setImageResource(R.drawable.list_style_menu);
        }
        setAdapter(spanCount);


        //设备相片列表

        ivStyle.setImageResource(R.drawable.list_pic_row);
        rvMenu.setLayoutManager(new GridLayoutManager(getContext(), 4));
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

            //保存到本地
            SharedPreferencesUnit.getInstance(getContext()).putInt(SharePreferenConstants.SP_CAMERA_SHOW_STYLE, spanCount);
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
        List<PictureBean> data = new ArrayList<>();
        if (mPicVideoAdapter != null) {
            data = mPicVideoAdapter.getData();
        }


        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), span);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlvDevice.setLayoutManager(layoutManager);
        mPicVideoAdapter = new CameraPicVedeoAdapter(data, this);
        rlvDevice.setAdapter(mPicVideoAdapter);
        rlvDevice.addItemDecoration(new GridDivider(ContextCompat.getColor(getActivity(), R.color.nocolor), 10, 10));
        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_empty_view, rlvDevice, false);
        mPicVideoAdapter.setEmptyView(view);
        mPicVideoAdapter.setHeaderAndEmpty(true);
        mPicVideoAdapter.setLoadMoreView(new CustomLoadMoreView());
        mPicVideoAdapter.disableLoadMoreIfNotFullPage(rlvDevice);
        mPicVideoAdapter.setOnLoadMoreListener(() -> {
            try {
                presenter.getCameraPic();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, rlvDevice);

        mPicVideoAdapter.setOnItemClickListener(this);
        mPicVideoAdapter.setOnItemLongClickListener(this);
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

        if (TextUtils.isEmpty(alias)) {
            alias = cameraId;
        }
        if (TextUtils.isEmpty(alias)) {
            alias = "";
        }
        tvTitle.setText(alias);

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
            View contentView = LayoutInflater.from(getActivity()).inflate(
                    R.layout.pop_layout, null);

            List<CameraBean> cameraList = presenter.getCameraList();

            if (cameraList == null || cameraList.size() == 0) {
                presenter.getAlldevice();
            } else {
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
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getActivity().getWindow().setAttributes(lp);
                popupWindow.setOnDismissListener(() -> {
                    WindowManager.LayoutParams lp1 = getActivity().getWindow().getAttributes();
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    getActivity().getWindow().setAttributes(lp1);
                });
                popupWindow.setBackgroundDrawable(new ColorDrawable(0));
                popupWindow.setAnimationStyle(R.style.Popup_Anim);
                popupWindow.showAsDropDown(vPop);
            }


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
                    View dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.camera_info, content, false);
                    TextView tvName = dialog_view.findViewById(R.id.tv_name_value);
                    TextView tvImei = dialog_view.findViewById(R.id.tv_imei_value);
                    TextView tvIccid = dialog_view.findViewById(R.id.tv_iccid_value);
                    TextView tvModel = dialog_view.findViewById(R.id.tv_model_value);
                    TextView tvVersion = dialog_view.findViewById(R.id.tv_version_value);
                    TextView tvFirewarePoint = dialog_view.findViewById(R.id.tv_version_point);
                    TextView tvModem = dialog_view.findViewById(R.id.tv_modem_value);
                    TextView tvFireware = dialog_view.findViewById(R.id.tv_fireware_value);
                    TextView tvFireModemPoint = dialog_view.findViewById(R.id.tv_fireware_point);
                    TextView tvUpnext=dialog_view.findViewById(R.id.tv_upgraded_next);

                    TextView tvLastUpdate = dialog_view.findViewById(R.id.tv_lastupdate_value);


                    TextView tvUpdate = dialog_view.findViewById(R.id.tv_to_update);
                    TextView tvLatest = dialog_view.findViewById(R.id.tv_last);

                    ImageView ivClose = dialog_view.findViewById(R.id.iv_close);

                    String alias = info.getAlias();
                    String imei = info.getImei();
                    String iccid = info.getIccid();
                    String deviceModel = info.getDeviceModel();
                    String fwVersion = info.getFwVersion();
                    String modemModel = info.getModemModel();
                    String modemFwVersion = info.getModemFwVersion();
                    String lastUpdateTime = info.getLastUpdateTime();
                    if (!TextUtils.isEmpty(alias)) {
                        tvName.setText(alias);
                    }
                    if (!TextUtils.isEmpty(imei)) {
                        tvImei.setText(imei);
                    }
                    if (!TextUtils.isEmpty(iccid)) {
                        tvIccid.setText(iccid);
                    }
                    if (!TextUtils.isEmpty(deviceModel)) {
                        tvModel.setText(deviceModel);
                    }
                    if (!TextUtils.isEmpty(fwVersion)) {
                        tvVersion.setText(fwVersion);
                    }
                    if (!TextUtils.isEmpty(modemModel)) {
                        tvModem.setText(modemModel);
                    }
                    if (!TextUtils.isEmpty(modemFwVersion)) {
                        tvFireware.setText(modemFwVersion);
                    }
                    if (!TextUtils.isEmpty(lastUpdateTime)) {
                        tvLastUpdate.setText(lastUpdateTime);
                    }


                    //判断是否有新版本
                    String newFwVersion = info.getNewFwVersion();
                    String fota = info.getCameraParamter().getFota();

                    String newModemFwVersion = info.getNewModemFwVersion();


                    if ("1".equals(newFwVersion)) {
                        tvFirewarePoint.setVisibility(View.VISIBLE);
                        tvUpdate.setVisibility(View.VISIBLE);
                        if ("0".equals(fota)){
                            tvUpdate.setText(R.string.m198_upgrade);
                            tvUpnext.setVisibility(View.GONE);
                            upgrade="1";//升级
                        }else {
                            tvUpdate.setText(R.string.m267_cancel_upgrade);
                            tvUpnext.setVisibility(View.VISIBLE);
                            upgrade="0";//取消升级
                        }

                    } else {
                        tvFirewarePoint.setVisibility(View.GONE);
                        tvUpdate.setVisibility(View.GONE);
                        tvUpnext.setVisibility(View.GONE);
                    }




                    if ("1".equals(newModemFwVersion)) {
                        tvLatest.setVisibility(View.VISIBLE);
                        tvFireModemPoint.setVisibility(View.VISIBLE);

                    } else {
                        tvLatest.setVisibility(View.GONE);
                        tvFireModemPoint.setVisibility(View.GONE);
                    }


                    BaseCircleDialog show = new CircleDialog.Builder().setBodyView(dialog_view, view1 -> {
                    })
                            .setGravity(Gravity.BOTTOM)
                            .setCancelable(true)
                            .setWidth(1)
                            .show(getActivity().getSupportFragmentManager());


                    tvUpdate.setOnClickListener(view12 -> {
                        if ("0".equals(upgrade)){
                            info.getCameraParamter().setFota("0");
                        }else {
                            info.getCameraParamter().setFota("1");
                        }
                        presenter.cameraOperation(imei, "upgradeFwVersion", upgrade);
                        show.dialogDismiss();
                    });


                    tvLatest.setOnClickListener(view13 -> {
                        presenter.cameraOperation(imei, "upgradeModemFwVersion", "1");
                        show.dialogDismiss();


                    });

                    ivClose.setOnClickListener(view14 -> {
                        show.dialogDismiss();
                    });

                    break;


                case 5:
                    Intent intent1 = new Intent(getContext(), MapActivity.class);
                    intent1.putExtra("imei", cameraId);
                    intent1.putExtra("lat", info.getLatitude());
                    intent1.putExtra("lng", info.getLongitude());
                    intent1.putExtra("alias", this.alias);
                    startActivity(intent1);
                    break;

                case 6:
                    Intent intent = new Intent(getContext(), ChartActivity.class);
                    intent.putExtra("imei", cameraId);
                    startActivity(intent);
                    break;
                case 7:
                    Intent intent5 = new Intent(getContext(), CameraStepUpActivity.class);
                    intent5.putExtra("imei", cameraId);
                    startActivity(intent5);
                    break;
            }

        }

        if (adapter == mPicVideoAdapter) {


            List<PictureBean> data = mPicVideoAdapter.getData();
            PictureBean pictureBean = data.get(position);
            boolean checked = pictureBean.isChecked();
            int itemType = pictureBean.getItemType();
            String id = pictureBean.getId();
            String fullPath = pictureBean.getFullPath();
            String collection = pictureBean.getCollection();

            if (itemType == CameraPicVedeoAdapter.HD_PIC_FLAG_EDIT || itemType == CameraPicVedeoAdapter.HD_PIC_FLAG_VIDEO_EDIT) {
                boolean b = !checked;
                pictureBean.setChecked(b);
                mPicVideoAdapter.toggle(id, b);
                mPicVideoAdapter.notifyDataSetChanged();


                List<String> selectedImeis = mPicVideoAdapter.getSelectedImeis();
                int size = selectedImeis.size();
                tv_selected_num.setText(size + "");

                if (selectedImeis.size() == mPicVideoAdapter.getData().size()) {
                    setAllSelected();
                } else {
                    setNotAllSelected();
                }

            } else {

               /* if (itemType==CameraPicVedeoAdapter.HD_PIC_FLAG_VIDEO){
                    Intent intent = new Intent(getContext(), VideoPlayActivity.class);
                    //当前选择的是哪一张
                    intent.putExtra("fullPath", fullPath);
                    intent.putExtra("id",id);
                    intent.putExtra("collection",collection);
                    intent.putExtra("alias", getString(R.string.m77_all_camera));
                    startActivity(intent);
                }else {
                    CameraShowListManerge.getInstance().setPicList(picList);
                    Intent intent = new Intent(getContext(), CameraDetailActivity.class);
                    intent.putExtra("position", position);
                    if (!TextUtils.isEmpty(alias)) {
                        intent.putExtra("alias", alias);
                    } else {
                        intent.putExtra("alias", cameraId);
                    }
                    startActivity(intent);
                }*/

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

    }


    @Override
    public void showCameraInfo(CameraBean.CameraInfo cameraBean) {

        this.info = cameraBean;


        String[] title = {
                getString(R.string.m70_Signal), getString(R.string.m75_steup), getString(R.string.m75_steup), getString(R.string.m72_sd),
                getString(R.string.m166_information), getString(R.string.m73_map), getString(R.string.m74_tracker), getString(R.string.m75_steup),
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

                    if (batteryL == 0) {
                        bean.setIconRes(R.drawable.bat0);
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


                    break;

                case 2:
                    String extDcLevel = cameraBean.getExtDcLevel();
                    int extDcl = Integer.parseInt(extDcLevel);
                    if (extDcl == 0) {
                        bean.setIconRes(R.drawable.ext0);
                    } else if (extDcl <= 25) {
                        bean.setIconRes(R.drawable.ext1);
                    } else if (extDcl <= 50) {
                        bean.setIconRes(R.drawable.ext2);
                    } else if (extDcl <= 75) {
                        bean.setIconRes(R.drawable.ext3);
                    } else {
                        bean.setIconRes(R.drawable.ext4);
                    }
                    bean.setTitle(extDcLevel + "%");

                    break;

                case 3:
                    String cardSpace = cameraBean.getCardSpace();
                    int sSpace = Integer.parseInt(cardSpace);
                    if (sSpace == 0) {
                        bean.setIconRes(R.drawable.sdcard0);
                    } else if (sSpace <= 19) {
                        bean.setIconRes(R.drawable.sdcard1);
                    } else if (sSpace <= 49) {
                        bean.setIconRes(R.drawable.sdcard2);
                    } else if (sSpace <= 69) {
                        bean.setIconRes(R.drawable.sdcard3);
                    } else if (sSpace <= 95) {
                        bean.setIconRes(R.drawable.sdcard4);
                    } else {
                        bean.setIconRes(R.drawable.sdcard5);
                    }
                    bean.setTitle(cardSpace + "%");

                    break;

                case 4:
                    bean.setIconRes(R.drawable.info);
                    break;


                case 5:
                    bean.setIconRes(R.drawable.map);
                    break;
                case 6:
                    bean.setIconRes(R.drawable.chart);
                    break;
                case 7:
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
            List<PictureBean> adapterList = new ArrayList<>(beans);
            mPicVideoAdapter.setNewData(adapterList);

        } else {
            mPicVideoAdapter.addData(beans);
            mPicVideoAdapter.loadMoreComplete();
        }
        picList.addAll(beans);


        int size = mPicVideoAdapter.getData().size();
        int size1 = mPicVideoAdapter.getSelectedImeis().size();

        if (size != size1) {
            setNotAllSelected();
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
        mPicVideoAdapter.loadMoreEnd();
    }

    @Override
    public void showMoreFail() {
        //数据加载完毕
        mPicVideoAdapter.loadMoreFail();
    }

    @Override
    public void showTotalNum(int totalNum) {
        String s = totalNum + " " + getString(R.string.m76_photos);
        tvPicNum.setText(s);
    }

    @Override
    public void showOperationSuccess() {
        MyToastUtils.toast(R.string.m148_success);
    }

    @Override
    public void showCollecMsg() {
        //批量操作，关闭pop刷新数据
        cloaseEdit(mPicVideoAdapter.getData());
        try {
            presenter.setTotalPage(1);
            presenter.setPageNow(0);
            presenter.getCameraPic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete() {
        //批量操作，关闭pop刷新数据
        cloaseEdit(mPicVideoAdapter.getData());
        try {
            presenter.setTotalPage(1);
            presenter.setPageNow(0);
            presenter.getCameraPic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dowload() {
        //批量操作，关闭pop刷新数据
        cloaseEdit(mPicVideoAdapter.getData());
        try {
            presenter.setTotalPage(1);
            presenter.setPageNow(0);
            presenter.getCameraPic();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            if (!TextUtils.isEmpty(name)) {
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


    @OnClick(R.id.iv_edit)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_edit:
                //点击修改布局
                List<PictureBean> data = mPicVideoAdapter.getData();
                if (presenter.isEditMode()) {
                    cloaseEdit(data);

                } else {

                    showEditPop();


                    presenter.setEditMode(true);
                    for (int i = 0; i < data.size(); i++) {
                        PictureBean pictureBean = data.get(i);
                        String type = pictureBean.getType();
                        //全部设置为未选中
                        pictureBean.setChecked(false);
                        if ("2".equals(type)) {
                            pictureBean.setItemType(CameraPicVedeoAdapter.HD_PIC_FLAG_VIDEO_EDIT);
                        } else {
                            pictureBean.setItemType(CameraPicVedeoAdapter.HD_PIC_FLAG_EDIT);
                        }
                    }
                }

                mPicVideoAdapter.notifyDataSetChanged();

                break;
        }
    }

    private void cloaseEdit(List<PictureBean> data) {
        presenter.setEditMode(false);
        for (int i = 0; i < data.size(); i++) {
            PictureBean pictureBean = data.get(i);
            String type = pictureBean.getType();
            //全部设置为未选中
            pictureBean.setChecked(false);
            //清除记录
            mPicVideoAdapter.getSelectedImeis().clear();

            if ("2".equals(type)) {
                pictureBean.setItemType(CameraPicVedeoAdapter.HD_PIC_FLAG_VIDEO);
            } else {
                pictureBean.setItemType(CameraPicVedeoAdapter.HD_PIC_FLAG);
            }
        }
        if (editPop != null) {
            editPop.dismiss();
        }
        srlPull.setEnabled(true);
    }


    private PopupWindow editPop;

    private TextView tv_selected_num;
    private TextView tv_select_all;
    private TextView tv_download;
    private TextView tv_collection;
    private TextView tv_selected_delete;
    private ImageView iv_camera;


    private void showEditPop() {
        srlPull.setEnabled(false);

        View myView = LayoutInflater.from(getActivity()).inflate(
                R.layout.pop_edit, null);

        iv_camera = myView.findViewById(R.id.iv_camera);
        tv_selected_num = myView.findViewById(R.id.tv_selected_num);
        tv_select_all = myView.findViewById(R.id.tv_select_all);
        tv_download = myView.findViewById(R.id.tv_download);
        tv_collection = myView.findViewById(R.id.tv_collection);
        tv_selected_delete = myView.findViewById(R.id.tv_selected_delete);


        tv_download.setOnClickListener(view -> {
            //批量下载
            List<String> selectedImeis = mPicVideoAdapter.getSelectedImeis();
            if (selectedImeis.size() == 0) {
                MyToastUtils.toast(R.string.m215_not_choose);
                return;
            }


            StringBuilder ids = new StringBuilder();
            for (int i = 0; i < selectedImeis.size(); i++) {
                String id = selectedImeis.get(i);
                ids.append(id).append("_");
            }
            if (ids.toString().endsWith("_")) {
                ids = new StringBuilder(ids.substring(0, ids.length() - 1));
            }
            presenter.operation(ids.toString(), "resolution", "1");
        });


        tv_collection.setOnClickListener(view -> {//批量收藏

            List<String> selectedImeis = mPicVideoAdapter.getSelectedImeis();
            if (selectedImeis.size() == 0) {
                MyToastUtils.toast(R.string.m215_not_choose);
                return;
            }

            StringBuilder ids = new StringBuilder();
            for (int i = 0; i < selectedImeis.size(); i++) {
                String id = selectedImeis.get(i);
                ids.append(id).append("_");
            }
            if (ids.toString().endsWith("_")) {
                ids = new StringBuilder(ids.substring(0, ids.length() - 1));
            }
            presenter.operation(ids.toString(), "favorites", "1");
        });


        //批量删除
        tv_selected_delete.setOnClickListener(view -> {
            List<String> selectedImeis = mPicVideoAdapter.getSelectedImeis();
            if (selectedImeis.size() == 0) {
                MyToastUtils.toast(R.string.m215_not_choose);
                return;
            }

            StringBuilder ids = new StringBuilder();
            for (int i = 0; i < selectedImeis.size(); i++) {
                String id = selectedImeis.get(i);
                ids.append(id).append("_");
            }
            if (ids.toString().endsWith("_")) {
                ids = new StringBuilder(ids.substring(0, ids.length() - 1));
            }
            presenter.operation(ids.toString(), "remove", "1");
        });


        iv_camera.setOnClickListener(view -> {
            //清除记录
            mPicVideoAdapter.getSelectedImeis().clear();

            List<PictureBean> data = mPicVideoAdapter.getData();
            presenter.setEditMode(false);
            for (int i = 0; i < data.size(); i++) {
                PictureBean pictureBean = data.get(i);
                String type = pictureBean.getType();
                //全部设置为未选中
                pictureBean.setChecked(false);
                //清除记录
                mPicVideoAdapter.getSelectedImeis().clear();

                if ("2".equals(type)) {
                    pictureBean.setItemType(CameraPicVedeoAdapter.HD_PIC_FLAG_VIDEO);
                } else {
                    pictureBean.setItemType(CameraPicVedeoAdapter.HD_PIC_FLAG);
                }
            }

            mPicVideoAdapter.notifyDataSetChanged();
            if (editPop != null) {
                editPop.dismiss();
            }

            srlPull.setEnabled(true);

        });

        tv_select_all.setOnClickListener(view -> {
            if (presenter.isAllSelected()) {

                setNotAllSelected();

                List<PictureBean> data = mPicVideoAdapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    PictureBean pictureBean = data.get(i);
                    pictureBean.setChecked(false);
                    mPicVideoAdapter.toggle(pictureBean.getId(), false);
                }

                tv_selected_num.setText(0 + "");

                mPicVideoAdapter.notifyDataSetChanged();
            } else {

                setAllSelected();

                List<PictureBean> data = mPicVideoAdapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    PictureBean pictureBean = data.get(i);
                    pictureBean.setChecked(true);
                    mPicVideoAdapter.toggle(pictureBean.getId(), true);
                }

                tv_selected_num.setText(data.size() + "");

                mPicVideoAdapter.notifyDataSetChanged();
            }
        });


        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int hight = LinearLayout.LayoutParams.WRAP_CONTENT;


        editPop = new PopupWindow(myView, width, hight, false);
        editPop.setTouchable(true);
        editPop.setFocusable(false); // 设置PopupWindow可获得焦点
        editPop.setTouchInterceptor((v, event) -> false);
        editPop.setBackgroundDrawable(new ColorDrawable(0));
        editPop.setAnimationStyle(R.style.Popup_Anim);
        editPop.setOutsideTouchable(false);
        editPop.showAtLocation(srlPull, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 200);

    }


    @Override
    public void selected() {
        List<String> selectedImeis = mPicVideoAdapter.getSelectedImeis();
        int size = selectedImeis.size();
        tv_selected_num.setText(size + "");
        if (selectedImeis.size() == mPicVideoAdapter.getData().size()) {
            setAllSelected();
        } else {
            setNotAllSelected();
        }
    }


    private void setAllSelected() {
        if (tv_select_all != null) {
            tv_select_all.setText(R.string.m214_deselect_all);
            tv_select_all.setTextColor(ContextCompat.getColor(getContext(), R.color.color_app_main));
            presenter.setAllSelected(true);
            setTextViewDrawableTop(R.drawable.allselected);
        }

    }


    private void setNotAllSelected() {
        if (tv_select_all != null) {
            tv_select_all.setText(R.string.m212_selected_all);
            tv_select_all.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            presenter.setAllSelected(false);
            setTextViewDrawableTop(R.drawable.selected_all);
        }

    }


    public void setTextViewDrawableTop(int drawId) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getResources().getDrawable(drawId, null);
        } else {
            drawable = getResources().getDrawable(drawId);
        }
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_select_all.setCompoundDrawables(null, drawable, null, null);
        }
    }


    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {


        showEditPop();

        List<PictureBean> data = mPicVideoAdapter.getData();
        presenter.setEditMode(true);
        for (int i = 0; i < data.size(); i++) {
            PictureBean pictureBean = data.get(i);
            String type = pictureBean.getType();
            //全部设置为未选中
            pictureBean.setChecked(false);
            if ("2".equals(type)) {
                pictureBean.setItemType(CameraPicVedeoAdapter.HD_PIC_FLAG_VIDEO_EDIT);
            } else {
                pictureBean.setItemType(CameraPicVedeoAdapter.HD_PIC_FLAG_EDIT);
            }
        }

        mPicVideoAdapter.notifyDataSetChanged();
        return false;
    }


    public void hideEdit() {
        if (presenter != null) {
            presenter.setEditMode(false);
        }
        List<PictureBean> data = mPicVideoAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            PictureBean pictureBean = data.get(i);
            String type = pictureBean.getType();
            //全部设置为未选中
            pictureBean.setChecked(false);
            //清除记录
            mPicVideoAdapter.getSelectedImeis().clear();

            if ("2".equals(type)) {
                pictureBean.setItemType(CameraPicVedeoAdapter.HD_PIC_FLAG_VIDEO);
            } else {
                pictureBean.setItemType(CameraPicVedeoAdapter.HD_PIC_FLAG);
            }
        }
        if (editPop != null) {
            editPop.dismiss();
        }
        srlPull.setEnabled(true);
        mPicVideoAdapter.notifyDataSetChanged();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);


        if (hidden) {
            hideEdit();
        }

    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {




    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdata(FreshCameraLocation bean) {
        String accountName = App.getUserBean().getAccountName();
        presenter.cameraInfo(cameraId, accountName);
    }

}
