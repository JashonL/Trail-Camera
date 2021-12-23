package com.shuoxd.camera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.base.BaseBean;
import com.shuoxd.camera.module.LoginActivity;
import com.shuoxd.camera.module.home.HomeFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<HomePresenter> implements IMainActivityView, BaseQuickAdapter.OnItemChildClickListener,
        BottomNavigationBar.OnTabSelectedListener {

    @BindView(R.id.bootom_navigation_bar)
    BottomNavigationBar bottomNavigationView;
    @BindView(R.id.fl_content)
    FrameLayout flContent;


    /**
     * 包括四个fragment
     */
    private HomeFragment homeFragment;

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
                        .setInactiveIconResource(R.drawable.tab_photo ).setInActiveColorResource(R.color.gray))
                .addItem(new BottomNavigationItem(R.drawable.tab_map_selected, getString(R.string.m57_nearby)).setActiveColorResource(R.color.color_app_main)
                        .setInactiveIconResource(R.drawable.tab_map ).setInActiveColorResource(R.color.gray))
                .addItem(new BottomNavigationItem(R.drawable.tab_user_selected, getString(R.string.m58_user)).setActiveColorResource(R.color.color_app_main)
                        .setInactiveIconResource(R.drawable.tab_user).setInActiveColorResource(R.color.gray))
                .initialise();
        bottomNavigationView.setTabSelectedListener(this);
        bottomNavigationView.selectTab(0);

        registerMessageReceiver();  // used for receive msg
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
            } catch (Exception e){
            }
        }
    }

    private void setCostomMsg(String msg){

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
                }else {
                    mTransaction.show(homeFragment);
                }
                break;

        }
        mTransaction.commit();
    }




    /**
     * 隐藏fragment
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
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
    public void showTuyaLoginError() {

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
}
