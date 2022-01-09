package com.shuoxd.camera.utils;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.shuoxd.camera.R;
import com.shuoxd.camera.customview.MyMarkerView;

import java.util.ArrayList;
import java.util.List;

public class ChartUtils {


    public static void initBarChart(Context context, BarChart mChart,List<String>xLabels,
                                    boolean isTouchEnable, int XYTextColorId, int XAxisLineColorId,
                                    int yGridLineColorId, boolean hasYAxis, int yAxisColor,
                                    boolean hasXGrid, int xGridColor) {
        //颜色转换
        int mXYTextColorId = ContextCompat.getColor(context, XYTextColorId);
        int mXAxisLineColorId = ContextCompat.getColor(context, XAxisLineColorId);
        int myGridLineColorId = ContextCompat.getColor(context, yGridLineColorId);
        int myYAxisColor = ContextCompat.getColor(context, yAxisColor);
        int myXGridColor = ContextCompat.getColor(context, xGridColor);
        mChart.getDescription().setEnabled(false);
        mChart.setTouchEnabled(isTouchEnable);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawGridBackground(false);
        if (isTouchEnable) {
            MyMarkerView mv = new MyMarkerView(context, R.layout.custom_marker_view);
            mv.setChartView(mChart); // For bounds control
            mChart.setMarker(mv);
        }
        mChart.animateY(1000);
        Legend l = mChart.getLegend();
        l.setEnabled(false);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawGridLines(hasXGrid);
        xAxis.setGridColor(myXGridColor);
        xAxis.setAxisLineWidth(1.0f);
        xAxis.setGridLineWidth(0.5f);
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X轴的位置
        xAxis.setTextColor(mXYTextColorId);//设置x轴文本颜色
        xAxis.setAxisLineColor(mXAxisLineColorId);
        xAxis.setDrawLabels(true);


        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return xLabels.get((int) value);
            }
        });


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMinimum(0.0f);//设置0值下面没有间隙
        leftAxis.setDrawAxisLine(hasYAxis);
        leftAxis.setAxisLineColor(myYAxisColor);
        leftAxis.setAxisLineWidth(1.0f);
        leftAxis.setGridLineWidth(0.5f);
        leftAxis.setTextColor(mXYTextColorId);
        leftAxis.setGridColor(myGridLineColorId);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }


    public static void setBarChartData(Context context, BarChart mChart,
                                       List<List<BarEntry>> barYList,
                                       int[] colors, int[] colorHights, int count) {
        if (mChart == null || barYList == null) return;
        List<IBarDataSet> barSetDatas = new ArrayList<>();
        BarData barData = mChart.getBarData();
        if (barData != null && barData.getDataSetCount() >= count) {
            for (int i = 0; i < count; i++) {
                BarDataSet dataSet = (BarDataSet) barData.getDataSetByIndex(i);
                dataSet.setValues(barYList.get(i));
            }
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            for (int i = 0; i < count; i++) {
                BarDataSet barSet = new BarDataSet(barYList.get(i), "");
                barSet.setColor(ContextCompat.getColor(context, colors[i]));
                if (colorHights[i] != -1) {
                    barSet.setHighLightColor(ContextCompat.getColor(context, colorHights[i]));
                }
                barSet.setDrawValues(false);
                barSetDatas.add(barSet);
            }
            BarData data = new BarData(barSetDatas);
            mChart.setData(data);
            mChart.setFitBars(true);
            mChart.getBarData().setBarWidth(0.75f);
        }
        mChart.animateY(1000);
        mChart.invalidate();
    }
}
