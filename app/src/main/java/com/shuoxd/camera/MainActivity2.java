package com.shuoxd.camera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.ImmersionBar;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.module.camera.CameraFragment;
import com.shuoxd.camera.module.gallery.PhotoFragment;
import com.shuoxd.camera.module.home.HomeComFragment;
import com.shuoxd.camera.module.map.MapFragment;
import com.shuoxd.camera.module.me.MeFragment;
import com.shuoxd.camera.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity2 extends BaseActivity<HomePresenter> implements IMainActivityView,
        BaseQuickAdapter.OnItemChildClickListener,
        BottomNavigationBar.OnTabSelectedListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.bootom_navigation_bar)
    BottomNavigationBar bottomNavigationView;
    @BindView(R.id.vp)
    ViewPager vp;


    /**
     * 包括五个fragment
     */
    private HomeComFragment homeFragment;
    private PhotoFragment mPhotoFragment;
    private MapFragment mMapFragment;
    private MeFragment mMeFragment;


    private CameraFragment mCameraFragment;


    public String cameraId;
    public String cameraAlias;


    private boolean fragmentsUpdateFlag[] = {false, false, false, false};


    private ArrayList<Fragment> mFragments;

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private MyAdapter myAdapter;


    @Override
    public void onTabSelected(int position) {
        vp.setCurrentItem(position,false);
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
    protected int getLayoutId() {
        return R.layout.activity_main2;
    }

    @Override
    protected void initViews() {

        mFragments = new ArrayList<>();
        homeFragment = new HomeComFragment();
        mPhotoFragment = new PhotoFragment();
        mMapFragment = new MapFragment();
        mMeFragment = new MeFragment();

        mCameraFragment = new CameraFragment();


        mFragments.add(homeFragment);
        mFragments.add(mPhotoFragment);
        mFragments.add(mMapFragment);
        mFragments.add(mMeFragment);


        myAdapter = new MyAdapter(getSupportFragmentManager(),mFragments);

        vp.setAdapter(myAdapter);
        vp.setOffscreenPageLimit(4);
        vp.addOnPageChangeListener(this);


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
        AppUtils.isUpgradeApp(this,false);

    }


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
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }


    private class MyAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;


        public MyAdapter(@NonNull FragmentManager fm,List<Fragment>fragments) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.fragments=fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        bottomNavigationView.selectTab(position);

        switch (position) {
            case 0:
                int pager = homeFragment.pager;
                int color;
                if (pager==1){
                    color= R.color.white;
                }else {
                    color= R.color.color_app_main;
                }

                ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).
                        statusBarColor(color).fitsSystemWindows(true).navigationBarColor(R.color.white)
                        .init();
                break;
            case 1:
                ImmersionBar.with(this).statusBarColor(R.color.color_app_main).navigationBarColor(R.color.white)
                        .statusBarDarkFont(false, 0.2f).keyboardEnable(false).
                        fitsSystemWindows(true).init();
                break;
            case 2:
                ImmersionBar.with(this).reset().keyboardEnable(true).navigationBarColor(R.color.white).statusBarDarkFont(true, 0.2f).init();
                break;
            case 3:
                ImmersionBar.with(this).reset().statusBarColor(R.color.color_app_main).
                        fitsSystemWindows(true).statusBarDarkFont(false).navigationBarColor(R.color.white).init();
                break;
            default:
                break;
        }
    }




    public void showCameraInfo() {


        mFragments.remove(0);

        myAdapter.notifyDataSetChanged();
        List<Fragment> newList = new ArrayList<>(mFragments);
        newList.add(0,mCameraFragment);
        myAdapter = new MyAdapter(getSupportFragmentManager(),newList);
        vp.setAdapter(myAdapter);



    }



    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onBackPressed() {
        if (vp.getCurrentItem()==0){
            int pager = homeFragment.pager;
            if (pager==1){
                hindAPP();
            }else {
                homeFragment.showHome();
            }
        }else {
            hindAPP();
        }
    }

    private void hindAPP() {
        //实现Home键效果
        //super.onBackPressed();这句话一定要注掉,不然又去调用默认的back处理方式了
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
    }

}
