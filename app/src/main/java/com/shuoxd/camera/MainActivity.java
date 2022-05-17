package com.shuoxd.camera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.base.BaseBean;
import com.shuoxd.camera.module.camera.CameraFragment;
import com.shuoxd.camera.module.gallery.PhotoFragment;
import com.shuoxd.camera.module.home.HomeFragment;
import com.shuoxd.camera.module.map.MapFragment;
import com.shuoxd.camera.module.me.MeFragment;
import com.shuoxd.camera.utils.AppUtils;
import com.shuoxd.camera.utils.SharedPreferencesUnit;

import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity<HomePresenter> implements IMainActivityView, BaseQuickAdapter.OnItemChildClickListener,
        BottomNavigationBar.OnTabSelectedListener {

    @BindView(R.id.bootom_navigation_bar)
    BottomNavigationBar bottomNavigationView;
    @BindView(R.id.fl_content)
    FrameLayout flContent;


    /**
     * 包括五个fragment
     */
    private HomeFragment homeFragment;
    private PhotoFragment mPhotoFragment;
    private MapFragment mMapFragment;
    private MeFragment mMeFragment;


    private CameraFragment mCameraFragment;


    public String cameraId;
    public String cameraAlias;


    private FragmentTransaction mTransaction;
    private FragmentManager mManager;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
//        mHomeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        getWindow().setExitTransition(new Explode().setDuration(500));
//        getWindow().setEnterTransition(new Explode().setDuration(500));

        mManager = getSupportFragmentManager();


        //底部控件
        // TODO 设置模式
        bottomNavigationView.setMode(BottomNavigationBar.MODE_FIXED);
        // TODO 设置背景色样式
        bottomNavigationView.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationView.setBarBackgroundColor(R.color.white);
        /**
         *  new BottomNavigationItem(R.mipmap.tab_home_pressed,"首页") ,选中的图标，文字
         *  setActiveColorResource：选中的颜色
         *  setInactiveIconResource：未选中的图标
         *  setInActiveColorResource：未选中的颜色
         */
        bottomNavigationView.clearAll();
        bottomNavigationView
                .addItem(new BottomNavigationItem(R.drawable.tab_home_selected, getString(R.string.m56_home)).
                        setActiveColorResource(R.color.color_app_main).setInactiveIconResource(R.drawable.tab_home).
                        setInActiveColorResource(R.color.gray))
                .addItem(new BottomNavigationItem(R.drawable.tab_photo_selected, getString(R.string.m14_photo)).setActiveColorResource(R.color.color_app_main)
                        .setInactiveIconResource(R.drawable.tab_photo).setInActiveColorResource(R.color.gray))
                .addItem(new BottomNavigationItem(R.drawable.tab_map_selected, getString(R.string.m57_nearby)).setActiveColorResource(R.color.color_app_main)
                        .setInactiveIconResource(R.drawable.tab_map).setInActiveColorResource(R.color.gray))
                .addItem(new BottomNavigationItem(R.drawable.tab_user_selected, getString(R.string.m58_user)).setActiveColorResource(R.color.color_app_main)
                        .setInactiveIconResource(R.drawable.tab_user).setInActiveColorResource(R.color.gray))
                .initialise();
        bottomNavigationView.setTabSelectedListener(this);
        bottomNavigationView.selectTab(0);

        registerMessageReceiver();  // used for receive msg


        //判断升级
        AppUtils.isUpgradeApp(this);

    }


    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";


    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);

    }


    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");

                    setCostomMsg(showMsg.toString());
                }
            } catch (Exception e) {
            }
        }
    }

    private void setCostomMsg(String msg) {

    }


    @Override
    protected void initData() {

    }


    @Override
    public void onTabSelected(int position) {

        //开启事务
        mTransaction = mManager.beginTransaction();
        hideFragment(mTransaction);
        switch (position) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    mTransaction.add(R.id.fl_content, homeFragment);
                } else {
                    mTransaction.show(homeFragment);
                }
                break;

            case 1:
                if (mPhotoFragment == null) {
                    mPhotoFragment = new PhotoFragment();
                    mTransaction.add(R.id.fl_content, mPhotoFragment);
                } else {
                    mTransaction.show(mPhotoFragment);
                }
                break;

            case 2:
                if (mMapFragment == null) {
                    mMapFragment = new MapFragment();
                    mTransaction.add(R.id.fl_content, mMapFragment);
                } else {
                    mTransaction.show(mMapFragment);
                }
                break;

            case 3:
                if (mMeFragment == null) {
                    mMeFragment = new MeFragment();
                    mTransaction.add(R.id.fl_content, mMeFragment);
                } else {
                    mTransaction.show(mMeFragment);
                }
                break;

        }
        mTransaction.commit();
    }


    public void showCameraInfo() {
        mTransaction = mManager.beginTransaction();
        boolean isRefresh = false;
        if (mCameraFragment == null) {
            mCameraFragment = new CameraFragment();
            Bundle bundle = new Bundle();
            bundle.putString("cameraId", cameraId);//电站id传值
            bundle.putString("alias", cameraAlias);//电站id传值
            mCameraFragment.setArguments(bundle);
            mTransaction.add(R.id.fl_content, mCameraFragment);
        } else {
            //刷新界面
            mCameraFragment.cameraId = this.cameraId;
            mCameraFragment.alias = this.cameraAlias;

            isRefresh = true;
        }
        mTransaction.commit();
        hideFragment(mTransaction);
        mTransaction.show(mCameraFragment);
        if (isRefresh) {
            mCameraFragment.refresh();
        }
    }


    public void showHome() {
        mTransaction = mManager.beginTransaction();
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
            mTransaction.add(R.id.fl_content, homeFragment);
        } else {
            homeFragment.jumpRefresh();
        }
        mTransaction.commit();
        hideFragment(mTransaction);
        mTransaction.show(homeFragment);
    }


    /**
     * 隐藏fragment
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }

        if (mPhotoFragment != null) {
            transaction.hide(mPhotoFragment);
        }

        if (mMapFragment != null) {
            transaction.hide(mMapFragment);
        }

        if (mMeFragment != null) {
            transaction.hide(mMeFragment);
        }


        if (mCameraFragment != null) {
            transaction.hide(mCameraFragment);
        }

    }


    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void setArticleData(String list) {

    }

    @Override
    public void showArticleError(String errorMessage) {

    }

    @Override
    public void showCollectSuccess(String successMessage) {

    }

    @Override
    public void showCollectError(String errorMessage) {

    }

    @Override
    public void showUncollectSuccess(String successMessage) {

    }

    @Override
    public void showUncollectError(String errorMessage) {

    }


    @Override
    protected HomePresenter createPresenter() {
        return null;
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onErrorCode(BaseBean bean) {

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public  void  onBackPressed() {
        //实现Home键效果
        //super.onBackPressed();这句话一定要注掉,不然又去调用默认的back处理方式了
        Intent i=  new  Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
    }
}
