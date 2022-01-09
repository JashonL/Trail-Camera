package com.shuoxd.camera.module.camera;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mylhyl.circledialog.CircleDialog;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.CameraPicAdapter;
import com.shuoxd.camera.adapter.CameraSettingAdapter;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.bean.DeviceSettingBean;
import com.shuoxd.camera.customview.CustomLoadMoreView;
import com.shuoxd.camera.customview.GridDivider;
import com.shuoxd.camera.utils.CameraSetUtils;
import com.shuoxd.camera.utils.CommentUtils;
import com.shuoxd.camera.utils.DateUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CameraStepUpActivity extends BaseActivity<CameraStepPresenter> implements CameraStepView,
        BaseQuickAdapter.OnItemClickListener,
        CameraSettingAdapter.OnChildCheckLiseners, Toolbar.OnMenuItemClickListener {
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_toolbar)
    LinearLayout appToolbar;
    @BindView(R.id.rl_setting)
    RecyclerView rlSetting;


    private CameraSettingAdapter mAdapter;

    private String imei;


    @Override
    protected CameraStepPresenter createPresenter() {
        return new CameraStepPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_camera_steup;
    }

    @Override
    protected void initViews() {
        //初始化toolbar
        initToobar(toolbar);
        tvTitle.setText(R.string.m102_camera_stepup);
        toolbar.inflateMenu(R.menu.menu_text);
        toolbar.setOnMenuItemClickListener(this);
        //初始化列表

        rlSetting.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CameraSettingAdapter(new ArrayList<>(), this);
        rlSetting.setAdapter(mAdapter);
        rlSetting.addItemDecoration(new GridDivider(ContextCompat.getColor(this, R.color.nocolor), 1, 1));
        mAdapter.setOnItemClickListener(this);

    }

    @Override
    protected void initData() {
        //系统设置项
        List<DeviceSettingBean> deviceBasicList = CameraSetUtils.getDeviceBasicList(this);
        mAdapter.replaceData(deviceBasicList);


        imei = getIntent().getStringExtra("imei");
        String accountName = App.getUserBean().getAccountName();
        presenter.cameraParamter(imei, accountName);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        DeviceSettingBean settingBean = mAdapter.getData().get(position);
        String key = settingBean.getKey();
        String title = settingBean.getTitle();
        int itemType = settingBean.getItemType();
        if (itemType == SettingConstants.SETTING_TYPE_SELECT) {
            setSelectItem(position, title);
        } else if (itemType == SettingConstants.SETTING_TYPE_NEXT) {
            if ("serverDate".equals(key)) {
                try {
                    String date = settingBean.getValueStr();
                    DateUtils.showTimepickViews(this, date, new DateUtils.ImplSelectTimeListener() {
                        @Override
                        public void seletedListener(String date) {
                            settingBean.setValueStr(date);
                            mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void result(int year, int month, int day) {
                            String time = year + ":" + month + ":" + day;
                            String value = year + month + day+"";
                            presenter.control(imei, key, value);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    DateUtils.showTimeDialogViews(this, new DateUtils.TimeSelectListener() {
                        @Override
                        public void seletedListener(String date) {

                        }

                        @Override
                        public void result(String hh, String mm, String ss) {
                            if ("timelapseStart".equals(key) || "timelapseStop".equals(key) || "dailySyncTime".equals(key)) {
                                String time = hh + ":" + mm;
                                String value = hh + mm;
                                settingBean.setValueStr(time);
                                presenter.control(imei, key, value);
                            }

                            if ("serverTime".equals(key)) {
                                String time = hh + ":" + mm + ":" + ss;
                                String value = hh + mm + ss;
                                settingBean.setValueStr(time);
                                presenter.control(imei, key, value);
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }


    }


    private void setSelectItem(int pos, String title) {
        List<DeviceSettingBean> data = mAdapter.getData();
        if (data.size() > pos) {
            DeviceSettingBean bean = data.get(pos);
            String[] items = bean.getItems();
            String key = bean.getKey();

            List<String> selects = new ArrayList<>(Arrays.asList(items));

            new CircleDialog.Builder()
                    .setTitle(title)
                    .setWidth(0.7f)
                    .setGravity(Gravity.CENTER)
                    .setMaxHeight(0.5f)
                    .setItems(selects, (parent, view1, position, id) -> {
                        mAdapter.getData().get(pos).setValueStr(selects.get(position));
                        mAdapter.notifyDataSetChanged();
                        presenter.control(imei, key, String.valueOf(position));
                        return true;
                    })
                    .setNegative(getString(R.string.m127_cancel), null)
                    .show(getSupportFragmentManager());
        }

    }


    @Override
    public void oncheck(boolean check, int position) {
        String key = mAdapter.getData().get(position).getKey();
        String value = check ? "1" : "0";
        presenter.control(imei, key, value);
    }

    @Override
    public void showSetting(List<DeviceSettingBean> list) {
        try {
            List<DeviceSettingBean> data = mAdapter.getData();
            for (int i = 0; i < list.size(); i++) {
                DeviceSettingBean settingBean1 = list.get(i);
                String key = list.get(i).getKey();
                for (int j = 0; j < data.size(); j++) {
                    DeviceSettingBean settingBean = data.get(j);
                    String key1 = settingBean.getKey();
                    String value = settingBean1.getValue();

                    if (key1.equals(key)) {
                        int itemType = settingBean.getItemType();
                        settingBean.setValue(value);

                        if (itemType == SettingConstants.SETTING_TYPE_SELECT) {
                            String[] items = settingBean.getItems();
                            int pos = Integer.parseInt(value);
                            String valueS = String.valueOf(pos);
                            if (pos < items.length) {
                                valueS = items[pos];
                            }
                            settingBean.setValueStr(valueS);

                        } else if (itemType == SettingConstants.SETTING_TYPE_NEXT) {
                            settingBean.setValueStr(value);
                            if ("timelapseStart".equals(key1) || "timelapseStop".equals(key1) || "dailySyncTime".equals(key1)) {//时间
                                if (!TextUtils.isEmpty(value)) {
                                    String startTime = value.substring(0, 2) + ":" + value.substring(2);
                                    settingBean.setValueStr(startTime);
                                }
                            }

                            if ( "serverTime".equals(key1)) {//日期
                                if (!TextUtils.isEmpty(value)) {
                                    String startTime = value.substring(0, 2) + ":" + value.substring(2, 4) + ":" + value.substring(4);
                                    settingBean.setValueStr(startTime);
                                }
                            }



                            if ("serverDate".equals(key1)) {//日期
                                if (!TextUtils.isEmpty(value)) {
                                    String startTime = value.substring(0, 2) + "-" + value.substring(2, 4) + "-" + value.substring(4);
                                    settingBean.setValueStr(startTime);
                                }
                            }

                        }
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        String accountName = App.getUserBean().getAccountName();
        presenter.cameraParamter(imei, accountName);
        return true;
    }
}
