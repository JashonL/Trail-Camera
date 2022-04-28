package com.shuoxd.camera.module.bill;

import com.shuoxd.camera.base.BaseView;
import com.shuoxd.camera.bean.YearBean;

import java.util.List;

public interface BillChartView extends BaseView {

    void showChartData(List<YearBean>beans);

    void upChart( List<String> weekList, List<Integer> readList);
}
