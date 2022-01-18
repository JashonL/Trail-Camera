package com.shuoxd.camera.module.camera;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.shuoxd.camera.R;
import com.shuoxd.camera.app.App;
import com.shuoxd.camera.base.BaseObserver;
import com.shuoxd.camera.base.BasePresenter;
import com.shuoxd.camera.bean.CameraBean;
import com.shuoxd.camera.utils.CommentUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.http.Field;
import retrofit2.http.Path;

public class ChartPresenter extends BasePresenter<ChartView> {

    private String dateType;
    private String isAllCamera = "-1";
    private String imeis;
    private String date;


    public String[] dataTypes = {"day", "week", "month", "year", "total"};


    private List<CameraBean> cameraList = new ArrayList<>();

    ChartPresenter(Context context, ChartView baseView) {
        super(context, baseView);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = simpleDateFormat.format(new Date());
        dateType = dataTypes[0];
    }


    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getIsAllCamera() {
        return isAllCamera;
    }

    public void setIsAllCamera(String isAllCamera) {
        this.isAllCamera = isAllCamera;
    }

    public String getImeis() {
        return imeis;
    }

    public void setImeis(String imeis) {
        this.imeis = imeis;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /**
     * 获取图表数据
     */
    public void getChartData() {
        //正式登录
        addDisposable(apiServer.chart(dateType, isAllCamera, imeis, date), new BaseObserver<String>(baseView, true) {

            @Override
            public void onSuccess(String bean) {

           /*     "result": 0,
                        "msg": "请求成功",
                        "obj": {
                    "date": "2021-12-27",
                            "amNum": 0,
                            "pmNum": 1,
                            "totalNum": 1,
                            "weather": 0
                }*/

                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)) {//请求成功
                        if ("day".equals(dateType)) {
                            JSONObject obj = jsonObject.optJSONObject("obj");
                            if (obj != null) {
                                String date = obj.optString("date");
                                int amNum = obj.optInt("amNum");
                                int pmNum = obj.optInt("pmNum");
                                int totalNum = obj.optInt("totalNum");
                                String weather = obj.optString("weather");
                                String[] split = date.split("[\\D]");

                                String year = split[0];
                                String month = split[1];
                                String day = split[2];



                                String d=day+" "+ CommentUtils.getMonth().get(Integer.parseInt(month)-1)+" "+year;
                                baseView.upDate(d);
                                String d2=CommentUtils.getMonth().get(Integer.parseInt(month)-1)+" "+year;
                                baseView.upDateYear(d2);

                                //设置图表
                                baseView.upPieChart(totalNum, amNum, pmNum);
                            }

                        } else if ("week".equals(dateType)) {
                            JSONObject obj = jsonObject.optJSONObject("obj");
                            if (obj != null) {
                                JSONArray dateArray = obj.optJSONArray("dateList");
                                String start = dateArray.optString(0);

                                String[] split = start.split("[\\D]");

                                String year = split[0];
                                String month = split[1];
                                String day = split[2];

                                String end = dateArray.optString(dateArray.length()-1);

                                String[] e_split = end.split("[\\D]");
                                String year_end = e_split[0];
                                String month_end = e_split[1];
                                String day_end = e_split[2];

                                String d=day+" "+ CommentUtils.getMonth().get(Integer.parseInt(month)-1)+" "+year+

                                        "-"+day_end+" "+CommentUtils.getMonth().get(Integer.parseInt(month_end)-1)+" "+year_end;

                                baseView.upDate(d);
                                String d2=CommentUtils.getMonth().get(Integer.parseInt(month)+1)+" "+year;
                                baseView.upDateYear(d2);


                                JSONArray jsonArray = obj.optJSONArray("weekList");
                                List<String> weekList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    weekList.add(jsonArray.optString(i));
                                }

                                JSONArray total = obj.optJSONArray("totalNumList");
                                List<Integer> totalNumList = new ArrayList<>();
                                for (int i = 0; i < total.length(); i++) {
                                    totalNumList.add(total.optInt(i));
                                }
                                //设置图表
                                baseView.upWeekChart(weekList,totalNumList);
                            }


                        } else if ("month".equals(dateType)) {
                            JSONObject obj = jsonObject.optJSONObject("obj");
                            if (obj != null) {
                                JSONArray jsonArray = obj.optJSONArray("dateList");
                                String start = jsonArray.optString(0);
                                String[] split = start.split("[\\D]");
                                String year = split[0];
                                String month = split[1];
                                String d= CommentUtils.getMonth().get(Integer.parseInt(month)-1)+" "+year;
                                baseView.upDate(d);
                                String d2=CommentUtils.getMonth().get(Integer.parseInt(month)-1)+" "+year;
                                baseView.upDateYear(d2);

                                List<String> monthList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String s = jsonArray.optString(i);
                                    String label = s.substring(s.lastIndexOf("-")+1, s.length());
                                    monthList.add(label);
                                }

                                JSONArray total = obj.optJSONArray("totalNumList");
                                List<Integer> totalNumList = new ArrayList<>();
                                for (int i = 0; i < total.length(); i++) {
                                    totalNumList.add(total.optInt(i));
                                }
                                //设置图表
                                baseView.upMonthChart(monthList,totalNumList);
                            }


                        } else if ("year".equals(dateType)) {

                            JSONObject obj = jsonObject.optJSONObject("obj");
                            if (obj != null) {
                                JSONArray jsonArray = obj.optJSONArray("dateList");
                                String start = jsonArray.optString(0);
                                String[] split = start.split("[\\D]");
                                String year = split[0];
                                String d= year;
                                baseView.upDate(d);
                                String d2=year;
                                baseView.upDateYear(d2);

                                List<String> monthList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String label = jsonArray.optString(i);
//                                    String label = s.substring(s.lastIndexOf("-")+1, s.length());
                                    monthList.add(label);
                                }

                                JSONArray total = obj.optJSONArray("totalNumList");
                                List<Integer> totalNumList = new ArrayList<>();
                                for (int i = 0; i < total.length(); i++) {
                                    totalNumList.add(total.optInt(i));
                                }
                                //设置图表
                                baseView.upYearChart(monthList,totalNumList);
                            }

                        } else if ("total".equals(dateType)) {
                            JSONObject obj = jsonObject.optJSONObject("obj");
                            if (obj != null) {
                                JSONArray jsonArray = obj.optJSONArray("dateList");
                                String start = jsonArray.optString(0);
                                String end = jsonArray.optString(jsonArray.length()-1);
                                String d= start+"-"+end;
                                baseView.upDate(d);

                                String d2=end;
                                baseView.upDateYear(d2);

                                List<String> monthList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String s = jsonArray.optString(i);
                                    monthList.add(s);
                                }

                                JSONArray total = obj.optJSONArray("totalNumList");
                                List<Integer> totalNumList = new ArrayList<>();
                                for (int i = 0; i < total.length(); i++) {
                                    totalNumList.add(total.optInt(i));
                                }
                                //设置图表
                                baseView.upTotalChart(monthList,totalNumList);
                            }
                        }


                    } else {
                        String msg = jsonObject.optString("msg");
                        baseView.showResultError(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String msg) {
                baseView.showServerError(msg);
            }
        });

    }



//    public String[] dataTypes = {"day", "week", "month", "year", "total"};

    public void dateNext(){
        //声明日期格式化样式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date dateNow = new Date();
        try {
            //将格式化的日期字符串转为Date
            dateNow = dateFormat.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //通过Calendar的实现类获得Calendar实例
        Calendar calendar = Calendar.getInstance();
        //设置格式化的日期
        calendar.setTime(dateNow);

        switch (dateType){
             case "day"://加一天
                 calendar.add(Calendar.DATE, 1);
                 try {
                     date = dateFormat.format(calendar.getTime());
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
                 break;
             case "week"://加一周
                 calendar.add(Calendar.DATE, 7);
                 try {
                     date = dateFormat.format(calendar.getTime());
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
                 break;
             case "month"://往前一个月
                 calendar.add(Calendar.MONTH, 1);
                 try {
                     date = dateFormat.format(calendar.getTime());
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
                 break;
             case "year":
                 calendar.add(Calendar.YEAR, 1);
                 try {
                     date = dateFormat.format(calendar.getTime());
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
                 break;
             case "total":
                 calendar.add(Calendar.YEAR, 5);
                 try {
                     date = dateFormat.format(calendar.getTime());
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
                 break;
         }

         getChartData();
    }


    public void datePrevious(){
        //声明日期格式化样式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date dateNow = new Date();
        try {
            //将格式化的日期字符串转为Date
            dateNow = dateFormat.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //通过Calendar的实现类获得Calendar实例
        Calendar calendar = Calendar.getInstance();
        //设置格式化的日期
        calendar.setTime(dateNow);

        switch (dateType){
            case "day"://加一天
                calendar.add(Calendar.DATE, -1);
                try {
                    date = dateFormat.format(calendar.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "week"://加一周
                calendar.add(Calendar.DATE, -7);
                try {
                    date = dateFormat.format(calendar.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "month"://往前一个月
                calendar.add(Calendar.MONTH, -1);
                try {
                    date = dateFormat.format(calendar.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "year":
                calendar.add(Calendar.YEAR, -1);
                try {
                    date = dateFormat.format(calendar.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "total":
                calendar.add(Calendar.YEAR, -5);
                try {
                    date = dateFormat.format(calendar.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

        getChartData();
    }




    public void getAlldevice() {
        String accountName = App.getUserBean().getAccountName();
        //获取设备
        addDisposable(apiServer.allCameraList(accountName), new BaseObserver<String>(baseView, true) {

            @Override
            public void onSuccess(String bean) {
                try {
                    JSONObject jsonObject = new JSONObject(bean);
                    String result = jsonObject.optString("result");
                    if ("0".equals(result)) {//请求成功
                        JSONArray obj = jsonObject.getJSONArray("obj");
                        //解析相机数据
                        int totalNum=0;
                        cameraList.clear();
                        List<String> ids = Arrays.asList(imeis.split("_"));
                        for (int i = 0; i < obj.length(); i++) {
                            JSONObject jsonObject1 = obj.getJSONObject(i);
                            CameraBean cameraBean = new Gson().fromJson(jsonObject1.toString(), CameraBean.class);
                            String imei = cameraBean.getCamera().getImei();
                            cameraBean.setSelected(ids.contains(imei));
                            String totalPhotoNum = cameraBean.getTotalPhotoNum();
                            totalNum += Integer.parseInt(totalPhotoNum);
                            cameraList.add(cameraBean);
                        }

                        CameraBean cameraBean =new CameraBean();
                        cameraBean.setTotalPhotoNum(String.valueOf(totalNum));
                        CameraBean.CameraInfo info=new CameraBean.CameraInfo();
                        info.setAlias(context.getString(R.string.m77_all_camera));
                        cameraBean.setCamera(info);
                        //如果当前是选中全部
                        cameraBean.setSelected("-1".equals(imeis));
                        cameraList.add(0,cameraBean);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {
                baseView.showServerError(msg);
            }
        });


    }


    public List<CameraBean> getCameraList() {
        return cameraList;
    }

    public void setCameraList(List<CameraBean> cameraList) {
        this.cameraList = cameraList;
    }
}
