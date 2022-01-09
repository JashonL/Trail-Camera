package com.shuoxd.camera.utils;

import android.content.Context;

import com.shuoxd.camera.R;
import com.shuoxd.camera.bean.DeviceSettingBean;
import com.shuoxd.camera.module.camera.SettingConstants;

import java.util.ArrayList;
import java.util.List;

public class CameraSetUtils {

    public static List<DeviceSettingBean> getDeviceBasicList(Context context) {
        List<DeviceSettingBean> list = new ArrayList<>();
        String[] titls = new String[]{
                context.getString(R.string.m103_capture_mode),//设备SN号
                context.getString(R.string.m104_photo_resolution),//内部时间
                context.getString(R.string.m105_photo_upload_resolution),//内部时间
                context.getString(R.string.m106_burst_shot),//内部时间
                context.getString(R.string.m107_burst_interval),//内部时间
                context.getString(R.string.m108_video_resolution),//内部时间
                context.getString(R.string.m109_video_length),//内部时间
                context.getString(R.string.m110_audio_recording),//内部时间
                context.getString(R.string.m111_shot_lag),//内部时间
                context.getString(R.string.m112_pir_sensitivity),//内部时间
                context.getString(R.string.m113_timelapse_start),//内部时间
                context.getString(R.string.m114_timelapse_stop),//内部时间
                context.getString(R.string.m115_upload_frequency),//内部时间
                context.getString(R.string.m116_daily_sync_time),//内部时间
                context.getString(R.string.m117_transmit_type),//内部时间
                context.getString(R.string.m118_loop_recording),//内部时间
                context.getString(R.string.m119_info_stamp),//内部时间
                context.getString(R.string.m120_time_format),//内部时间
                context.getString(R.string.m121_time_zone),//内部时间
                context.getString(R.string.m122_temperature_format),//内部时间
                context.getString(R.string.m123_lcd_during_on),//内部时间
                context.getString(R.string.m124_Server_Date),//内部时间
                context.getString(R.string.m125_Server_Time),//内部时间
                context.getString(R.string.m126_Format_Card),//内部时间
        };
        String[] hints = new String[]{
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
        };
        int[] itemTypes = new int[]{
                SettingConstants.SETTING_TYPE_SELECT,//拍照模式
                SettingConstants.SETTING_TYPE_SELECT,//拍照分辨率
                SettingConstants.SETTING_TYPE_SELECT,//相机图片上传分辨率
                SettingConstants.SETTING_TYPE_SELECT,//相机连拍张数
                SettingConstants.SETTING_TYPE_SELECT,//相机连拍的时间间隔
                SettingConstants.SETTING_TYPE_SELECT,//拍照视频分辨率
                SettingConstants.SETTING_TYPE_SELECT,//视频长度
                SettingConstants.SETTING_TYPE_SELECT,//视频是否开启声音
                SettingConstants.SETTING_TYPE_SELECT,//相机拍照时间间隔
                SettingConstants.SETTING_TYPE_SELECT,//拍照灵敏度
                SettingConstants.SETTING_TYPE_NEXT,//拍照起始时间
                SettingConstants.SETTING_TYPE_NEXT,//拍照结束时间
                SettingConstants.SETTING_TYPE_SELECT,//上传频率
                SettingConstants.SETTING_TYPE_NEXT,//每日上传时间
                SettingConstants.SETTING_TYPE_SELECT,//上传类型
                SettingConstants.SETTING_TYPE_SWITCH,//SD卡循环覆盖
                SettingConstants.SETTING_TYPE_SWITCH,//图片概览信息
                SettingConstants.SETTING_TYPE_SELECT,//时间格式
                SettingConstants.SETTING_TYPE_SELECT,//时区
                SettingConstants.SETTING_TYPE_SELECT,//温度单位
                SettingConstants.SETTING_TYPE_SWITCH,//LCD屏是否开启
                SettingConstants.SETTING_TYPE_NEXT,//服务器日期
                SettingConstants.SETTING_TYPE_NEXT,//服务器时间
                SettingConstants.SETTING_TYPE_SWITCH//是否格式化卡
        };

        float[] multiples = new float[]{
                1, 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1
        };
        String[] units = new String[]{
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
        };


        String[][] items = new String[][]{
                {"video","定时拍摄","混合"},
                {"4MP","8MP","16MP","26MP"},
                {"1024*576"},
                {"1","2","3","6","9"},
                {"1s","2s","3s","4s","5s","6s","7s","8s","9s"},
                {"2Mp 1920x1080，1080p","1Mp 1280x720，720p"},
                {"5s","10s","20s","30s"},
                {"OFF","ON"},
                {"1s","5s","10s","30s","60s","120s","240s","480s","960s","1800s","3060s","3600s"},
                {"High","Middle","Low"},
                {""},
                {""},
                {"After Trigger","Twice a Day","Once a Day"},
                {""},
                {"Photo/HQ/Video","Photo/HQ","Daily Sync only"},
                {"OFF","ON"},
                {"OFF","ON"},
                {"12进制","24进制"},
                {"Pacific Time","Mountain Time","Central Time","Eastern Time","Alaska Time","Hawaii Time"},
                {"Fahrenheit ⁰F","Celsius ⁰C"},
                {"OFF","ON"},
                {""},
                {""},
                {"OFF","ON"},

        };


        String[] key = new String[]{
              "captureMode",
              "photoResolution",
              "photoUploadResolution",
              "burstShot",
              "photoBurstInterval",
              "videoResolution",
              "videoLength",
              "audioRecording",
              "shotLag",
              "pirSensitivity",
              "timelapseStart",
              "timelapseStop",
              "uploadFrequency",
              "dailySyncTime",
              "transmitType",
              "loopRecording",
              "infoStamp",
              "timeFormat",
              "timeZone",
              "temperatureFormat",
              "lcdDuringON",
              "serverDate",
              "serverTime",
              "formatCard",

        };

        for (int i = 0; i < titls.length; i++) {
            DeviceSettingBean bean = new DeviceSettingBean();
            bean.setTitle(titls[i]);
            bean.setItemType(itemTypes[i]);
            bean.setUnit(units[i]);
            bean.setItems(items[i]);
            bean.setHint(hints[i]);
            bean.setMul(multiples[i]);
            bean.setKey(key[i]);
            list.add(bean);
        }
        return list;
    }

}
