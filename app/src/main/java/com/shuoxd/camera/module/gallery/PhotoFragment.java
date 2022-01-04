package com.shuoxd.camera.module.gallery;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.ImmersionBar;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.CameraPicAdapter;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseBean;
import com.shuoxd.camera.base.BaseFragment;
import com.shuoxd.camera.bean.PictureBean;
import com.shuoxd.camera.customview.CustomLoadMoreView;
import com.shuoxd.camera.customview.GridDivider;
import com.shuoxd.camera.customview.MySwipeRefreshLayout;
import com.shuoxd.camera.module.leftmenu.HomeNavigationViewFragment;

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


    /*设备部分*/
    private CameraPicAdapter mAdapter;

    public String cameraId;

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
//        cameraId = getArguments().getString("cameraId");

        srlPull.setOnRefreshListener(() -> {
            try {
                presenter.setPageNow(0);
                refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        //获取列表设备列表
        try {
            presenter.setPageNow(0);
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public void refresh() {
        String accountName = App.getUserBean().getAccountName();
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
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

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
        return false;
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
        String s = totalNum + getString(R.string.m76_photos);
        tvPicNum.setText(s);
    }

    @Override
    public void reset() {
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

        presenter.setPageNow(0);
        presenter.getCameraPic();
        drawerLayout.closeDrawers();

    }


}
