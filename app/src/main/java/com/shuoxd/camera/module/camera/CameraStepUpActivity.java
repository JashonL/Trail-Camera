package com.shuoxd.camera.module.camera;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mylhyl.circledialog.CircleDialog;
import com.shuoxd.camera.R;
import com.shuoxd.camera.adapter.CameraPicAdapter;
import com.shuoxd.camera.adapter.CameraSettingAdapter;
import com.shuoxd.camera.adapter.CheckedAdapter;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseActivity;
import com.shuoxd.camera.bean.DeviceSettingBean;
import com.shuoxd.camera.customview.CustomLoadMoreView;
import com.shuoxd.camera.customview.GridDivider;
import com.shuoxd.camera.eventbus.FreshCameraLocation;
import com.shuoxd.camera.eventbus.FreshCameraName;
import com.shuoxd.camera.module.map.LocationActivity;
import com.shuoxd.camera.module.map.MapActivity;
import com.shuoxd.camera.utils.CameraSetUtils;
import com.shuoxd.camera.utils.CircleDialogUtils;
import com.shuoxd.camera.utils.CommentUtils;
import com.shuoxd.camera.utils.DateUtils;
import com.shuoxd.camera.utils.LogUtil;
import com.shuoxd.camera.utils.MyToastUtils;
import com.shuoxd.camera.utils.PickViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shuoxd.camera.module.camera.SettingConstants.SETTING_TYPE_INPUT;
import static com.shuoxd.camera.module.camera.SettingConstants.SETTING_TYPE_NEXT;
import static com.shuoxd.camera.module.camera.SettingConstants.SETTING_TYPE_ONLYREAD;

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

    private List<String> mins = new ArrayList<>();
    private List<String> seconds = new ArrayList<>();


    private List<String> hours = new ArrayList<>();
    private List<String> minutes = new ArrayList<>();
    private List<String> ss = new ArrayList<>();


    private boolean is24HourView = false;

    private String lat;
    private String lng;


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
        EventBus.getDefault().register(this);
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

        for (int i = 0; i < 119; i++) {
            if (i < 59) {
                mins.add((i + 1) + "sec");
            } else {
                mins.add((i - 58) + "min");
            }
            seconds.add(i + "");
        }


        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                hours.add("0" + i);
                minutes.add("0" + i);
                ss.add("0" + i);
            } else {
                hours.add(i + "");
                minutes.add(i + "");
                ss.add(i + "");
            }

        }


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
        String setKey = settingBean.getSetKey();
        String title = settingBean.getTitle();
        int itemType = settingBean.getItemType();
        String value1 = settingBean.getValue();
        String valueStr = settingBean.getValueStr();

        if (itemType == SettingConstants.SETTING_TYPE_SELECT) {
            if ("shotLag".equals(key)) {
                int time = 0;
                if (!TextUtils.isEmpty(value1)) {
                    time = Integer.parseInt(value1);
                }
                int min_index;//这里是下标
                int second = 0;
                if (time < 60) {
                    min_index = time - 1;
                } else {
                    min_index = time / 60 + 58;
                }
                CircleDialogUtils.showTimeValueDialog(this, title, mins, min_index, seconds, second, (min1, second1) -> {
                    String sValue;
                    int value;
                    if (min1 < 59) {
                        value = min1 + 1;//下标加1是真实值
                        sValue = value + "sec";
                    } else {
                        value = (min1 - 58) * 60;
                        sValue = (min1 - 58) + "min";
                    }
                    if (value == 0) {
                        MyToastUtils.toast(R.string.m163_cannot_zero);
                        return;
                    }

                    mAdapter.getData().get(position).setValueStr(sValue);
                    mAdapter.getData().get(position).setValue(String.valueOf(value));
                    mAdapter.notifyDataSetChanged();
                    String operationValue = String.valueOf(value);
                    presenter.control(imei, setKey, operationValue);
                });
            } else if ("photoBurstInterval".equals(key)) {
                int index = 0;
                if (!TextUtils.isEmpty(value1)) {
                    index = Integer.parseInt(value1) - 1;
                }
                List<String> items = Arrays.asList(settingBean.getItems());
                int[] items_value = settingBean.getItems_value();
                CircleDialogUtils.showTimeValueDialog(this, title, items, index, seconds, 0, (min1, second1) -> {
                    String sValue = items.get(min1);
                    int value = items_value[min1];
                    mAdapter.getData().get(position).setValueStr(sValue);
                    mAdapter.getData().get(position).setValue(String.valueOf(value));
                    mAdapter.notifyDataSetChanged();
                    String operationValue = String.valueOf(value);
                    presenter.control(imei, setKey, operationValue);
                });
            } else {
                setSelectItem(position, title);
            }
        } else if (itemType == SETTING_TYPE_NEXT) {
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
                            presenter.control(imei, setKey, value);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if ("operationDayText".equals(key)) {
                String[] weeks = (String[]) CommentUtils.getWeeks2().toArray();
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
                            presenter.control(imei, setKey, s1);
                            mAdapter.notifyDataSetChanged();
                        })
                        .show(getSupportFragmentManager());

            } else if ("timelapseInterval".equals(key)) {
                PickViewUtils.showPickView(this, hours, minutes, ss, 0, 0, 0, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {

                        String hh = hours.get(options1);
                        String mm = minutes.get(options2);
                        String s = ss.get(options3);
                        if (options1 == 0 && options2 == 0 && options3 < 3) {
                            hh="00";
                            mm="00";
                            s="03";
                        }
                        String time = hh + ":" + mm + ":" + s;
                        String value = hh + mm + s;
                        settingBean.setValueStr(time);
                        presenter.control(imei, setKey, value);
                    }
                }, title);

            } else if ("longitude_latitude".equals(key)) {
                Intent intent1 = new Intent(this, LocationActivity.class);

                startActivity(intent1);
            } else {
                try {


                    int hour = Integer.parseInt(value1.substring(0, 2));
                    int min = Integer.parseInt(value1.substring(2));

                    DateUtils.showTimeDialogViews(this, new DateUtils.TimeSelectListener() {
                        @Override
                        public void seletedListener(String date) {

                        }

                        @Override
                        public void result(String hh, String mm, String ss) {
                            if ("timelapseStart".equals(key) || "timelapseStop".equals(key)
                                    || "dailySyncTime".equals(key) || "operationStart".equals(key) || "operationStop".equals(key)) {
                                //判断是不是24小时
                                int hour = Integer.parseInt(hh);

                                if (is24HourView) {
                                    String startTime = (hour < 10 ? ("0" + hour) : hour) + ":" + mm;
                                    settingBean.setValueStr(startTime);
                                } else {
                                    if (hour > 12) {
                                        hour -= 12;
                                        String startTime = (hour < 10 ? ("0" + hour) : hour) + ":" + mm + "PM";
                                        settingBean.setValueStr(startTime);
                                    } else {
                                        if (hour==0){
                                            String startTime = 12 + ":" + mm + "AM";
                                            settingBean.setValueStr(startTime);
                                        }else {
                                            String startTime = (hour < 10 ? ("0" + hour) : hour) + ":" + mm + "AM";
                                            settingBean.setValueStr(startTime);
                                        }

                                    }
                                }
                                String value = hh + mm;
                                presenter.control(imei, key, value);
                            }

                            if ("serverTime".equals(key)) {
                                String time = hh + ":" + mm + ":" + ss;
                                String value = hh + mm + ss;
                                settingBean.setValueStr(time);
                                presenter.control(imei, setKey, value);
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }, is24HourView, hour, min);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        } else if (itemType == SETTING_TYPE_INPUT) {
            setInputValue(position, title, valueStr, 1f);
        }


    }


    private void setInputValue(int pos, String title, String hint, float mul) {
        CircleDialogUtils.showInputValueDialog(this, title,
                hint, "", "", value -> {
                    List<DeviceSettingBean> data = mAdapter.getData();
                    DeviceSettingBean bean = data.get(pos);
                    bean.setValueStr(value);
                    String setKey = bean.getSetKey();
                    mAdapter.notifyDataSetChanged();
                    //调用接口
                    presenter.cameraOperation(imei, setKey, value);
                });
    }


    private void setSelectItem(int pos, String title) {
        List<DeviceSettingBean> data = mAdapter.getData();
        if (data.size() > pos) {
            DeviceSettingBean bean = data.get(pos);
            String[] items = bean.getItems();
//            String key = bean.getKey();
            String setKey = bean.getSetKey();
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
                        presenter.control(imei, setKey, operationValue);
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
        mAdapter.getData().get(position).setValue(value);
        presenter.control(imei, key, value);
    }

    @Override
    public void showSetting(List<DeviceSettingBean> list) {
        try {
            int index_capture_mode = 1;
            int index_photo_resolution = 2;
            int index_burst_sshot = 3;
            int index_burst_interval = 4;
            int index_video_resolution = 5;
            int index_video_length = 6;
            int index_audio_recording = 7;

            List<DeviceSettingBean> data = mAdapter.getData();
            //先找到小时制
            for (int i = 0; i < list.size(); i++) {
                String key = list.get(i).getKey();
                String value = list.get(i).getValue();
                if (key.equals("timeFormat")) {
                    is24HourView = "1".equals(value);
                    break;
                }
            }

            //经纬度单独解析
            for (int i = 0; i < list.size(); i++) {
                DeviceSettingBean settingBean1 = list.get(i);
                String key = list.get(i).getKey();
                String value1 = settingBean1.getValue();

                if ("latitude".equals(key)) {
                    lat = value1;
                }

                if ("longitude".equals(key)) {
                    lng = value1;
                }


                for (int j = 0; j < data.size(); j++) {
                    DeviceSettingBean settingBean = data.get(j);
                    String key1 = settingBean.getKey();
                    String value = settingBean1.getValue();
                    if (TextUtils.isEmpty(value)) continue;
                    if (key1.equals(key)) {
                        int itemType = settingBean.getItemType();
                        settingBean.setValue(value);
                        if (itemType == SettingConstants.SETTING_TYPE_SELECT) {
                            if ("shotLag".equals(key1)) {
                                if (!TextUtils.isEmpty(value)) {
                                    int time = Integer.parseInt(value);
                                    if (time < 60) {
                                        String sValue = time + "sec";
                                        settingBean.setValueStr(sValue);
                                    } else {
                                        int min = time / 60;
                                        String sValue = min + "min";
                                        settingBean.setValueStr(sValue);
                                    }
                                }
                            } else {
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
                            }


                        } else if (itemType == SETTING_TYPE_NEXT || itemType == SETTING_TYPE_ONLYREAD) {
                            settingBean.setValueStr(value);
                            if ("timelapseStart".equals(key1) || "timelapseStop".equals(key1)
                                    || "dailySyncTime".equals(key1) || "operationStart".equals(key1) || "operationStop".equals(key1)) {//时间
                                if (!TextUtils.isEmpty(value)) {
                                    int hour = Integer.parseInt(value.substring(0, 2));
                                    if (is24HourView) {
                                        String startTime = (hour < 10 ? ("0" + hour) : hour) + ":" + value.substring(2);
                                        settingBean.setValueStr(startTime);
                                    } else {
                                        if (hour > 12) {
                                            hour -= 12;
                                            String startTime = (hour < 10 ? ("0" + hour) : hour) + ":" + value.substring(2) + "PM";
                                            settingBean.setValueStr(startTime);
                                        } else {
                                            if (hour==0){
                                                String startTime = 12 + ":" +  value.substring(2) + "AM";
                                                settingBean.setValueStr(startTime);
                                            }else {
                                                String startTime = (hour < 10 ? ("0" + hour) : hour) + ":" + value.substring(2) + "AM";
                                                settingBean.setValueStr(startTime);
                                            }


                                        }
                                    }

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

                            if ("operationDayText".equals(key1)) {
                                //
                                if (!TextUtils.isEmpty(value)) {
                                    char[] chars = value.toCharArray();
                                    StringBuilder loopStyle = new StringBuilder();
                                    for (int k = 0; k < chars.length; k++) {
                                        if (String.valueOf(chars[k]).equals("1")) {
                                            String week = CommentUtils.getWeeks2().get(k);
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


                        } else if (itemType == SETTING_TYPE_INPUT) {
                            settingBean.setValueStr(value);
                        }
                        break;
                    }
                }


                data.get(data.size()-1).setValueStr(lat+","+lng);


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
                data.get(index_audio_recording).setItemType(SettingConstants.SETTING_TYPE_SWITCH);
            }

            if ("0".equals(value) || "1".equals(value)) {
                data.get(14).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
                data.get(15).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
                data.get(16).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
            } else {
                data.get(14).setItemType(SETTING_TYPE_NEXT);
                data.get(15).setItemType(SETTING_TYPE_NEXT);
                data.get(16).setItemType(SETTING_TYPE_NEXT);
            }

            //判断Opration time是否为1
            String operaValue = data.get(11).getValue();

            if ("0".equals(operaValue)) {
                data.get(12).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
                data.get(13).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
            } else {
                data.get(12).setItemType(SETTING_TYPE_NEXT);
                data.get(13).setItemType(SETTING_TYPE_NEXT);
            }

            //判断是否12小时制

            mAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cameraSetSuccess(String operationType, String operationValue) {
        //重新获取
        if ("timeFormat".equals(operationType)) {
            String accountName = App.getUserBean().getAccountName();
            presenter.cameraParamter(imei, accountName);
        } else {
            List<DeviceSettingBean> data = mAdapter.getData();
            int index_capture_mode = 1;
            int index_photo_resolution = 2;
            int index_burst_sshot = 3;
            int index_burst_interval = 4;
            int index_video_resolution = 5;
            int index_video_length = 6;
            int index_audio_recording = 7;


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
                    data.get(index_audio_recording).setItemType(SettingConstants.SETTING_TYPE_SWITCH);
                }

                if ("0".equals(value) || "1".equals(value)) {
                    data.get(14).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
                    data.get(15).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
                    data.get(16).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
                } else {
                    data.get(14).setItemType(SETTING_TYPE_NEXT);
                    data.get(15).setItemType(SETTING_TYPE_NEXT);
                    data.get(16).setItemType(SETTING_TYPE_NEXT);
                }


            }


            //判断Opration time是否为1
            String operaValue = data.get(11).getValue();

            if ("0".equals(operaValue)) {
                data.get(12).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
                data.get(13).setItemType(SettingConstants.SETTING_TYPE_ONLYREAD);
            } else {
                data.get(12).setItemType(SETTING_TYPE_NEXT);
                data.get(13).setItemType(SETTING_TYPE_NEXT);
            }


            if (operationType.equals("timeFormat")) {
                is24HourView = "1".equals(operationValue);

            }

            mAdapter.notifyDataSetChanged();
        }

    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdata(FreshCameraLocation bean) {
        String accountName = App.getUserBean().getAccountName();
        presenter.cameraParamter(imei, accountName);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        String accountName = App.getUserBean().getAccountName();
        presenter.cameraParamter(imei, accountName);
        return true;
    }
}
