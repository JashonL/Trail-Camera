package com.shuoxd.camera.module.gallery;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.gyf.immersionbar.ImmersionBar;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.CameraFiterAdapter;
import com.shuoxd.camera.adapter.CameraMulFiterAdapter;
import com.shuoxd.camera.adapter.CameraPicAdapter;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseBean;
import com.shuoxd.camera.base.BaseFragment;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.bean.PictureBean;
import com.shuoxd.camera.customview.CustomLoadMoreView;
import com.shuoxd.camera.customview.GridDivider;
import com.shuoxd.camera.customview.MySwipeRefreshLayout;
import com.shuoxd.camera.eventbus.FreshPhoto;
import com.shuoxd.camera.eventbus.FreshQuestion;
import com.shuoxd.camera.module.camera.CameraDetailActivity;
import com.shuoxd.camera.module.camera.CameraShowListManerge;
import com.shuoxd.camera.module.leftmenu.HomeNavigationViewFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PhotoFragment extends BaseFragment<PhotoPresenter> implements PhotoView,
        BaseQuickAdapter.OnItemClickListener , Toolbar.OnMenuItemClickListener,HomeNavigationViewFragment.IMenuListeners{


    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_toolbar)
    LinearLayout appToolbar;
    @BindView(R.id.iv_style)
    ImageView ivStyle;
    @BindView(R.id.tv_pic_num)
    TextView tvPicNum;
    @BindView(R.id.iv_switch)
    ImageView ivSwitch;
    @BindView(R.id.rlv_device)
    RecyclerView rlvDevice;
    @BindView(R.id.srl_pull)
    MySwipeRefreshLayout srlPull;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.v_pop)
    View vPop;


    /*设备部分*/
    private CameraPicAdapter mAdapter;


    private List<PictureBean> picList = new ArrayList<>();


    private int spanCount = 1;




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
        toolbar.inflateMenu(R.menu.menu_camera);
        toolbar.setOnMenuItemClickListener(this);
        tvTitle.setText(R.string.m77_all_camera);

        ivStyle.setImageResource(R.drawable.list_pic_row);
        ivSwitch.setImageResource(R.drawable.list_style_menu);

        //设备图片列表
        setAdapter(spanCount);


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



        ivStyle.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        /*---------------------------自定义侧边栏布局-----------------------------*/
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.navigationview, new HomeNavigationViewFragment(this)).commit();
    }




    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
//        cameraId = getArguments().getString("cameraId");


        //默认请求全部
        presenter.setImeis("-1");
        presenter.setIsAllCamera("1");

        srlPull.setOnRefreshListener(() -> {
            try {
                presenter.setTotalPage(1);
                presenter.setPageNow(0);
                refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        //获取列表设备列表
        try {
            presenter.setTotalPage(1);
            presenter.setPageNow(0);
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public void refresh() {
        presenter.getAlldevice();
        presenter.getCameraPic();
    }


    //小图片布局
    private void setAdapter(int span) {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), span);
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
                refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, rlvDevice);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        CameraShowListManerge.getInstance().setPicList(picList);
        Intent intent =new Intent(getContext(), CameraDetailActivity.class);
        //当前选择的是哪一张
        intent.putExtra("position",position);
        intent.putExtra("alias",getString(R.string.m77_all_camera));
        startActivity(intent);
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
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId()==R.id.right_action){
            // 一锟斤拷锟皆讹拷锟斤拷牟锟斤拷郑锟斤拷锟轿拷锟绞撅拷锟斤拷锟斤拷锟�
            View contentView = LayoutInflater.from(getActivity()).inflate(
                    R.layout.pop_layout, null);

            List<CameraBean> cameraList = presenter.getCameraList();
            RecyclerView rvCamera = contentView.findViewById(R.id.ry_camera);
            rvCamera.setLayoutManager(new LinearLayoutManager(getContext()));
            CameraMulFiterAdapter camerAdapter=new CameraMulFiterAdapter(R.layout.item_camera_menu,cameraList);
            rvCamera.setAdapter(camerAdapter);
            camerAdapter.setOnItemClickListener((adapter, view, position) -> {
                camerAdapter.setNowSelectPosition(position);
                StringBuilder imeis= new StringBuilder();
                List<CameraBean> data = camerAdapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    CameraBean cameraBean = data.get(i);
                    boolean selected = cameraBean.isSelected();
                    if (selected){
                        if (i==0){
                            imeis = new StringBuilder("-1");
                            break;
                        }else {
                            String imei = cameraBean.getCamera().getImei();
                            imeis.append(imei).append("_");
                        }
                    }
                }
                if (imeis.toString().endsWith("_")){
                    imeis = new StringBuilder(imeis.substring(0, imeis.length() - 1));
                    presenter.setImeis(imeis.toString());
                    presenter.setIsAllCamera("-1");
                }else {
                    presenter.setImeis("-1");
                    presenter.setIsAllCamera("1");
                }
                presenter.setTotalPage(1);
                presenter.setPageNow(0);
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
    public void showResultError(String msg) {
        super.showResultError(msg);
        srlPull.setRefreshing(false);
    }



    @Override
    public void showCameraPic(List<PictureBean> beans) {
        srlPull.setRefreshing(false);
        int pageNow = presenter.getPageNow();
        if (pageNow==1){
            picList.clear();
        }
        picList.addAll(beans);

        if (pageNow==1) {
            mAdapter.setNewData(picList);
        }else {
            mAdapter.addData(beans);
            mAdapter.loadMoreComplete();
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
