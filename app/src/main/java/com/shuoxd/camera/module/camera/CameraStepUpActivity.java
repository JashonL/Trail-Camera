package com.shuoxd.camera.module.camera;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mylhyl.circledialog.CircleDialog;
import com.shuoxd.camera.MainActivity;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.CameraPicAdapter;
import com.shuoxd.camera.adapter.CameraSettingAdapter;
import com.shuoxd.camera.adapter.CheckedAdapter;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.bean.DeviceSettingBean;
import com.shuoxd.camera.customview.CustomLoadMoreView;
import com.shuoxd.camera.customview.GridDivider;
import com.shuoxd.camera.utils.CameraSetUtils;
import com.shuoxd.camera.utils.CircleDialogUtils;
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
        String value1 = settingBean.getValue();


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
                            String value = year + month + day + "";
                            presenter.control(imei, key, value);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if ("operationDay".equals(key)) {
                String[] weeks = (String[]) CommentUtils.getWeeks().toArray();
                String[] weekValue = {"0", "0", "0", "0", "0", "0", "0"};
                final CheckedAdapter checkedAdapter = new CheckedAdapter(this, weeks);
                char[] chars = value1.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    if (String.valueOf(chars[i]).equals("1")) {
                        checkedAdapter.toggle(i, weeks[i]);
                    }
                }
                new CircleDialog.Builder()
                        .setGravity(Gravity.CENTER)
                        .setTitle(title)
                        .setWidth(0.75f)
                        .setItems(checkedAdapter, (parent, view12, position12, id) -> {
                                    checkedAdapter.toggle(position12, weeks[position12]);
                                    return false;
                                }
                        )
                        .setNegative(getString(R.string.m127_cancel), null)
                        .setPositive(getString(R.string.m152_ok), v -> {
                            SparseArray<String> saveChecked = checkedAdapter.getSaveChecked();
                            StringBuilder value = new StringBuilder();
                            for (int i = 0; i < saveChecked.size(); i++) {
                                int keyV = saveChecked.keyAt(i);
                                String s = saveChecked.get(keyV);
                                Log.e("key = " + keyV, s);
                                value.append(s).append(",");
                                weekValue[keyV] = "1";
                            }

                            String s = value.toString();
                            if (s.endsWith(",")) {
                                s = s.substring(0, s.length() - 1);
                            }
                            settingBean.setValueStr(s);

                            StringBuilder sb = new StringBuilder();
                            for (String item : weekValue) {
                                sb.append(item);
                            }
                            String s1 = sb.toString();
                            settingBean.setValue(s1);
                            presenter.control(imei, key, s1);
                            mAdapter.notifyDataSetChanged();
                        })
                        .show(getSupportFragmentManager());

            } else {
                try {
                    DateUtils.showTimeDialogViews(this, new DateUtils.TimeSelectListener() {
                        @Override
                        public void seletedListener(String date) {

                        }

                        @Override
                        public void result(String hh, String mm, String ss) {
                            if ("timelapseStart".equals(key) || "timelapseStop".equals(key)
                                    || "dailySyncTime".equals(key) || "operationStart".equals(key) || "operationStop".equals(key)) {
                                String time = hh + ":" + mm;
                                String value = hh + mm;
                                settingBean.setValueStr(time);
                                presenter.control(imei, key, value);
                            }

                            if ("serverTime".equals(key) || "timelapseInterval".equals(key)) {
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
            int[] items_value = bean.getItems_value();
            List<String> selects = new ArrayList<>(Arrays.asList(items));

            new CircleDialog.Builder()
                    .setTitle(title)
                    .setWidth(0.7f)
                    .setGravity(Gravity.CENTER)
                    .setMaxHeight(0.5f)
                    .setItems(selects, (parent, view1, position, id) -> {
                        mAdapter.getData().get(pos).setValueStr(selects.get(position));
                        mAdapter.getData().get(pos).setValue(String.valueOf(items_value[position]));
                        mAdapter.notifyDataSetChanged();
                        String operationValue = String.valueOf(items_value[position]);
                        presenter.control(imei, key, operationValue);
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
            int index_capture_mode = 0;
            int index_photo_resolution = 1;
            int index_burst_sshot = 2;
            int index_burst_interval = 3;
            int index_video_resolution = 4;
            int index_video_length = 5;
            int index_audio_recording = 6;

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
                            int[] items_value = settingBean.getItems_value();
                            int pos = Integer.parseInt(value);
                            String valueS = String.valueOf(pos);
                            for (int k = 0; k < items_value.length; k++) {
                                int i1 = items_value[k];
                                if (i1 == pos) {
                                    valueS = items[k];
                                    break;
                                }
                            }
                            settingBean.setValueStr(valueS);

                        } else if (itemType == SettingConstants.SETTING_TYPE_NEXT) {
                            settingBean.setValueStr(value);
                            if ("timelapseStart".equals(key1) || "timelapseStop".equals(key1)
                                    || "dailySyncTime".equals(key1) || "operationStart".equals(key1) || "operationStop".equals(key1)) {//时间
                                if (!TextUtils.isEmpty(value)) {
                                    String startTime = value.substring(0, 2) + ":" + value.substring(2);
                                    settingBean.setValueStr(startTime);
                                }
                            }

                            if ("serverTime".equals(key1) || "timelapseInterval".equals(key1)) {//日期
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

                            if ("operationDay".equals(key1)) {
                                //
                                if (!TextUtils.isEmpty(value)) {
                                    char[] chars = value.toCharArray();
                                    StringBuilder loopStyle = new StringBuilder();
                                    for (int k = 0; k < chars.length; k++) {
                                        if (String.valueOf(chars[k]).equals("1")) {
                                            String week = CommentUtils.getWeeks().get(k);
                                            loopStyle.append(week).append(",");
                                        }
                                    }
                                    String cycleDay = loopStyle.toString();
                                    if (cycleDay.endsWith(",")) {
                                        cycleDay = cycleDay.substring(0, cycleDay.length() - 1);
                                    }
                                    settingBean.setValueStr(cycleDay);

                                }
                            }


                        }
                        break;
                    }
                }

            }


            //判断capture mode是否为1
            String value = data.get(index_capture_mode).getValue();
            if ("1".equals(value)) {
                data.get(index_photo_resolution).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
                data.get(index_burst_sshot).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
                data.get(index_burst_interval).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
            } else {
                data.get(index_photo_resolution).setItemType(SettingConstants.SETTING_TYPE_SELECT);
                data.get(index_burst_sshot).setItemType(SettingConstants.SETTING_TYPE_SELECT);
                data.get(index_burst_interval).setItemType(SettingConstants.SETTING_TYPE_SELECT);
            }


            if ("0".equals(value) || "2".equals(value) || "3".equals(value)) {
                data.get(index_video_resolution).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
                data.get(index_video_length).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
                data.get(index_audio_recording).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
            } else {
                data.get(index_video_resolution).setItemType(SettingConstants.SETTING_TYPE_SELECT);
                data.get(index_video_length).setItemType(SettingConstants.SETTING_TYPE_SELECT);
                data.get(index_audio_recording).setItemType(SettingConstants.SETTING_TYPE_SELECT);
            }

            if ("0".equals(value) || "1".equals(value)) {
                data.get(13).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
                data.get(14).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
                data.get(15).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
            } else {
                data.get(13).setItemType(SettingConstants.SETTING_TYPE_SELECT);
                data.get(14).setItemType(SettingConstants.SETTING_TYPE_SELECT);
                data.get(15).setItemType(SettingConstants.SETTING_TYPE_SELECT);
            }

            //判断Opration time是否为1
            String operaValue = data.get(9).getValue();

            if ("0".equals(operaValue)) {
                data.get(10).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
                data.get(11).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
                data.get(12).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
            } else {
                data.get(10).setItemType(SettingConstants.SETTING_TYPE_NEXT);
                data.get(11).setItemType(SettingConstants.SETTING_TYPE_SELECT);
                data.get(12).setItemType(SettingConstants.SETTING_TYPE_SELECT);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void cameraSetSuccess(String operationType, String operationValue) {
        List<DeviceSettingBean> data = mAdapter.getData();
        int index_capture_mode = 0;
        int index_photo_resolution = 1;
        int index_burst_sshot = 2;
        int index_burst_interval = 3;
        int index_video_resolution = 4;
        int index_video_length = 5;
        int index_audio_recording = 6;


        if ("captureMode".equals(operationType)) {
            //判断capture mode是否为1
            String value = data.get(index_capture_mode).getValue();

            if ("1".equals(value)) {
                data.get(index_photo_resolution).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
                data.get(index_burst_sshot).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
                data.get(index_burst_interval).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
            } else {
                data.get(index_photo_resolution).setItemType(SettingConstants.SETTING_TYPE_SELECT);
                data.get(index_burst_sshot).setItemType(SettingConstants.SETTING_TYPE_SELECT);
                data.get(index_burst_interval).setItemType(SettingConstants.SETTING_TYPE_SELECT);
            }

            if ("0".equals(value) || "2".equals(value) || "3".equals(value)) {
                data.get(index_video_resolution).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
                data.get(index_video_length).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
                data.get(index_audio_recording).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
            } else {
                data.get(index_video_resolution).setItemType(SettingConstants.SETTING_TYPE_SELECT);
                data.get(index_video_length).setItemType(SettingConstants.SETTING_TYPE_SELECT);
                data.get(index_audio_recording).setItemType(SettingConstants.SETTING_TYPE_SELECT);
            }

            if ("0".equals(value) || "1".equals(value)) {
                data.get(13).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
                data.get(14).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
                data.get(15).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
            } else {
                data.get(13).setItemType(SettingConstants.SETTING_TYPE_SELECT);
                data.get(14).setItemType(SettingConstants.SETTING_TYPE_SELECT);
                data.get(15).setItemType(SettingConstants.SETTING_TYPE_SELECT);
            }


        }


        //判断Opration time是否为1
        String operaValue = data.get(9).getValue();

        if ("0".equals(operaValue)) {
            data.get(10).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
            data.get(11).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
            data.get(12).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
        } else {
            data.get(10).setItemType(SettingConstants.SETTING_TYPE_NEXT);
            data.get(11).setItemType(SettingConstants.SETTING_TYPE_SELECT);
            data.get(12).setItemType(SettingConstants.SETTING_TYPE_SELECT);
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
