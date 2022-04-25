package com.shuoxd.camera.module.plans;

import androidx.annotation.NonNull;

import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.PlansListAdapter;
import com.shuoxd.camera.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class PlansActivity extends BaseActivity<PlansPresenter> implements PlansView{

    private PlansListAdapter mAdapter;


    @Override
    protected PlansPresenter createPresenter() {
        return new PlansPresenter(this,this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_plans;
    }

    @Override
    protected void initViews() {
        mAdapter=new PlansListAdapter(R.layout.item_plans,new ArrayList<>());
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
}
