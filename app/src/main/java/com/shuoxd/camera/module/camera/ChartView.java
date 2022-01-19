package com.shuoxd.camera.module.camera;

import com.shuoxd.camera.base.BaseView;

import java.util.List;

public interface ChartView extends BaseView {

    void upDate(String date);

    void upDateYear(String date);

    void upPieChart(int total,int am,int pm);

    void upWeekChart( List<String> weekList, List<Integer> readList,List<Integer> unreadlist);

    void upMonthChart( List<String> monthList, List<Integer> readList,List<Integer> unreadlist );

    void upYearChart( List<String> yearList, List<Integer> readList,List<Integer> unreadlist);

    void upTotalChart( List<String> yearList, List<Integer> readList,List<Integer> unreadlist);



}
