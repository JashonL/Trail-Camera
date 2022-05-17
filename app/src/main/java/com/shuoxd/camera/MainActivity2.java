package com.shuoxd.camera;

import android.content.Intent;
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
import com.shuoxd.camera.module.home.HomeFragment;
import com.shuoxd.camera.module.map.MapFragment;
import com.shuoxd.camera.module.me.MeFragment;

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
    private HomeFragment homeFragment;
    private PhotoFragment mPhotoFragment;
    private MapFragment mMapFragment;
    private MeFragment mMeFragment;


    private CameraFragment mCameraFragment;


    public String cameraId;
    public String cameraAlias;


    private FragmentTransaction mTransaction;
    private FragmentManager mManager;


    private ArrayList<Fragment> mFragments;


    @Override
    public void onTabSelected(int position) {

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
        homeFragment = new HomeFragment();
        mPhotoFragment = new PhotoFragment();
        mMapFragment = new MapFragment();
        mMeFragment = new MeFragment();

        mCameraFragment = new CameraFragment();


        mFragments.add(homeFragment);
        mFragments.add(mPhotoFragment);
        mFragments.add(mMapFragment);
        mFragments.add(mMeFragment);


        vp.setAdapter(new MyAdapter(getSupportFragmentManager()));
//        vp.setOffscreenPageLimit(4);
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
        public MyAdapter(@NonNull FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                //设置共同沉浸式样式
                ImmersionBar.with(this)
                        .statusBarDarkFont(true, 0.2f)//设置状态栏图片为深色，(如果android 6.0以下就是半透明)
                        .fitsSystemWindows(true)
                        .statusBarColor(R.color.white)//这里的颜色，你可以自定义。
                        .init();
                break;
            case 1:
                ImmersionBar.with(this).keyboardEnable(false).statusBarDarkFont(true, 0.2f).navigationBarColor(R.color.colorPrimary).init();
                break;
            case 2:
                ImmersionBar.with(this).keyboardEnable(false).statusBarDarkFont(false).navigationBarColor(R.color.colorPrimary).init();
                break;
            case 3:
                ImmersionBar.with(this).keyboardEnable(true).statusBarDarkFont(true).navigationBarColor(R.color.colorPrimary).init();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onBackPressed() {
        //实现Home键效果
        //super.onBackPressed();这句话一定要注掉,不然又去调用默认的back处理方式了
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
    }

}
