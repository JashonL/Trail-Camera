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
                context.getString(R.string.m106_burst_shot),//内部时间
                context.getString(R.string.m107_burst_interval),//内部时间
                context.getString(R.string.m108_video_resolution),//内部时间
                context.getString(R.string.m109_video_length),//内部时间
                context.getString(R.string.m110_audio_recording),//内部时间
                context.getString(R.string.m111_shot_lag),//内部时间
                context.getString(R.string.m112_pir_sensitivity),//内部时间
                context.getString(R.string.m153_operation_time),//

                context.getString(R.string.m154_operation_day),//
                context.getString(R.string.m155_operation_start_time),//
                context.getString(R.string.m156_operation_stop_time),//



                context.getString(R.string.m113_timelapse_start),//内部时间
                context.getString(R.string.m114_timelapse_stop),//内部时间


                context.getString(R.string.m157_time_lapse_start_interval),//拍照间隔时间


                context.getString(R.string.m115_upload_frequency),//内部时间
                context.getString(R.string.m116_daily_sync_time),//内部时间
                context.getString(R.string.m117_transmit_type),//内部时间
                context.getString(R.string.m118_loop_recording),//内部时间
                context.getString(R.string.m119_info_stamp),//内部时间
                context.getString(R.string.m120_time_format),//内部时间
                context.getString(R.string.m121_time_zone),//内部时间
                context.getString(R.string.m122_temperature_format),//内部时间
                context.getString(R.string.m123_lcd_during_on),//内部时间
                context.getString(R.string.m126_Format_Card),//内部时间
        };
        String[] hints = new String[]{
                "", "", "", "", "", "", "", "", "", "", "", "", "", "", "","", "", "", "", "", "", "", "", "", "", "",
        };
        int[] itemTypes = new int[]{
                SettingConstants.SETTING_TYPE_SELECT,//拍照模式
                SettingConstants.SETTING_TYPE_SELECT,//拍照分辨率
                SettingConstants.SETTING_TYPE_SELECT,//相机连拍张数
                SettingConstants.SETTING_TYPE_SELECT,//相机连拍的时间间隔
                SettingConstants.SETTING_TYPE_SELECT,//拍照视频分辨率
                SettingConstants.SETTING_TYPE_SELECT,//视频长度
                SettingConstants.SETTING_TYPE_SWITCH,//视频是否开启声音
                SettingConstants.SETTING_TYPE_SELECT,//相机拍照时间间隔
                SettingConstants.SETTING_TYPE_SELECT,//拍照灵敏度
                SettingConstants.SETTING_TYPE_SWITCH,//拍照起始时间



                SettingConstants.SETTING_TYPE_NEXT,//Opration
                SettingConstants.SETTING_TYPE_NEXT,//Opration
                SettingConstants.SETTING_TYPE_NEXT,//Opration


                SettingConstants.SETTING_TYPE_NEXT,//拍照起始时间
                SettingConstants.SETTING_TYPE_NEXT,//拍照结束时间
                SettingConstants.SETTING_TYPE_NEXT,//拍照时间间隔



                SettingConstants.SETTING_TYPE_SELECT,//上传频率
                SettingConstants.SETTING_TYPE_NEXT,//每日上传时间
                SettingConstants.SETTING_TYPE_SELECT,//上传类型
                SettingConstants.SETTING_TYPE_SWITCH,//SD卡循环覆盖
                SettingConstants.SETTING_TYPE_SWITCH,//图片概览信息
                SettingConstants.SETTING_TYPE_SELECT,//时间格式
                SettingConstants.SETTING_TYPE_SELECT,//时区
                SettingConstants.SETTING_TYPE_SELECT,//温度单位
                SettingConstants.SETTING_TYPE_SWITCH,//LCD屏是否开启
                SettingConstants.SETTING_TYPE_SWITCH//是否格式化卡
        };

        float[] multiples = new float[]{
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,1
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
                "",
                "",
                "",
                "",
                "",
                "",
        };


        String[] time = new String[119];
        for (int i = 0; i < time.length; i++) {
            if (i < 59) {
                time[i] = (i + 1) + "sec";
            } else {
                time[i] = (i - 58) + "min";
            }
        }


        String[][] items = new String[][]{
                {"PIR Photo", "PIR Video", "Time Lapse", "PIR&Time Lapse"},
                {"4MP", "8MP", "16MP", "26MP"},
                {"1 photo in series", "2 photo in series", "3 photo in series", "6 photo in series", "9 photo in series"},
                {"1sec", "2sec", "3sec", "4sec", "5sec", "6sec", "7sec", "8sec", "9sec"},
                {"1080p", "720p"},
                {"5sec", "10sec", "20sec", "30sec"},
                {"OFF", "ON"},
                time,
                {"High", "Middle", "Low"},
                {"OFF", "ON"},

                {""},
                {""},
                {""},

                {""},
                {""},
                {""},
                {"After Trigger", "Twice a Day", "Once a Day"},
                {""},
                {"Photo/HQ/Video", "Photo/HQ", "Daily Sync Only"},
                {"OFF", "ON"},
                {"OFF", "ON"},
                {"12-hour", "24-hour"},
                {"Pacific Time", "Mountain Time", "Central Time", "Eastern Time", "Alaska Time", "Hawaii Time"},
                {"Fahrenheit ⁰F", "Celsius ⁰C"},
                {"OFF", "ON"},
                {"OFF", "ON"},

        };

        int[] timeValue = new int[119];
        for (int i = 0; i < timeValue.length; i++) {
            if (i < 59) {
                timeValue[i] = i + 1;
            } else {
                timeValue[i] = (i - 58) * 60;
            }
        }

        int[][] items_values = new int[][]{
                {0, 1, 2, 3},
                {4, 8, 16, 26},
                {1, 2, 3, 6, 9},
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {0, 2},
                {5, 10, 20, 30},
                {0, 1},
                timeValue,
                {0, 1, 2},
                {0, 1},

                {0, 1},
                {0, 1},
                {0, 1},


                {0, 1, 2},
                {0, 1, 2},
                {0, 1, 2},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12},

        };


        String[] key = new String[]{
                "captureMode",
                "photoResolution",
                "burstShot",
                "photoBurstInterval",
                "videoResolution",
                "videoLength",
                "audioRecording",
                "shotLag",
                "pirSensitivity",
                "operationTime",

                "operationDay",
                "operationStart",
                "operationStop",



                "timelapseStart",
                "timelapseStop",
                "timelapseInterval",


                "uploadFrequency",
                "dailySyncTime",
                "transmitType",
                "loopRecording",
                "infoStamp",
                "timeFormat",
                "timeZone",
                "temperatureFormat",
                "lcdDuringON",
                "formatCard",

        };

        for (int i = 0; i < titls.length; i++) {
            DeviceSettingBean bean = new DeviceSettingBean();
            bean.setTitle(titls[i]);
            bean.setItemType(itemTypes[i]);
            bean.setUnit(units[i]);
            bean.setItems(items[i]);
            bean.setItems_value(items_values[i]);
            bean.setHint(hints[i]);
            bean.setMul(multiples[i]);
            bean.setKey(key[i]);
            list.add(bean);
        }
        return list;
    }

}
