package com.shuoxd.camera.module.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.shuoxd.camera.R;
import com.shuoxd.camera.base.BaseFragment;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.module.camera.CameraFragment;

import java.util.List;


public class HomeComFragment extends BaseFragment<HomePresenter> implements HomeView {


    private HomeFragment mHomefragment;
    private CameraFragment mCameraFragment;


    public FragmentTransaction mTransaction;
    public FragmentManager mManager;
    public String cameraId;
    public String cameraAlias;

    //当前显示的页面
    public int pager = 1;


    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(getContext(), this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment;
    }

    @Override
    protected void initView() {
        mManager = getChildFragmentManager();
        showHome();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void showHome() {
        mTransaction = mManager.beginTransaction();
   /*     if (mHomefragment == null) {
            mHomefragment = new HomeFragment();
            mTransaction.add(R.id.fl_content, mHomefragment);
        }else {
            mTransaction.show(mHomefragment);

        }*/

        if (mHomefragment == null) {
            mHomefragment = new HomeFragment();
        }
        pager = 1;
        mTransaction.replace(R.id.fl_content, mHomefragment);
        mTransaction.commit();
    }

    public void showCamera() {
        mTransaction = mManager.beginTransaction();
        if (mCameraFragment == null) {
            mCameraFragment = new CameraFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putString("cameraId", cameraId);//电站id传值
        bundle.putString("alias", cameraAlias);//电站id传值
        mCameraFragment.setArguments(bundle);
        pager = 2;
        mTransaction.replace(R.id.fl_content, mCameraFragment);
        mTransaction.commit();
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

    @Override
    public void setDeviceList(List<CameraBean> cameraBeanList) {

    }

    @Override
    public void showNoMoreData() {

    }

    @Override
    public void showMoreFail() {

    }

    @Override
    public void deleteSuccess() {

    }
}
