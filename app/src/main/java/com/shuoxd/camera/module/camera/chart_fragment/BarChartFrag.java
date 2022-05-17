package com.shuoxd.camera.module.camera.chart_fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.shuoxd.camera.R;
import com.shuoxd.camera.customview.MyMarkerView;
import com.shuoxd.camera.utils.ChartUtils;
import com.shuoxd.camera.utils.CommentUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BarChartFrag extends ChartBaseFragment implements OnChartGestureListener {


    private BarChart chart;

    private List<String>xDatas;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_bar_chart, container, false);

        // create a new chart object
        chart = new BarChart(getActivity());
        chart.getDescription().setEnabled(false);
//        chart.setOnChartGestureListener(this);

        MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv);

        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);


//        chart.setData(generateBarData(1, 20000, 12));

        Legend l = chart.getLegend();

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });

        leftAxis.setGranularity(1);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        chart.getAxisRight().setEnabled(false);

   /*     XAxis xAxis = chart.getXAxis();
        xAxis.setEnabled(false);*/

        xDatas = CommentUtils.getMonth();
        XAxis xAxis = chart.getXAxis();
        xAxis.setAxisLineWidth(1.0f);
        xAxis.setGridLineWidth(0.5f);
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X轴的位置
        xAxis.setDrawLabels(true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                String s = xDatas.get((int) value);
                Log.i("barchart", "x轴" + s);
                return s;
            }
        });


        // programmatically add the chart
        FrameLayout parent = v.findViewById(R.id.parentLayout);
        parent.addView(chart);





        return v;

    }




    public void setBarChart(List<String> xDatas, List<Integer> totalNumList) {
        this.xDatas=xDatas;
        Log.i("barchart", "设置数据");
        BarData barData = parserWeekChart(1, xDatas, totalNumList);

        chart.setData(barData);
        chart.getBarData().setBarWidth(0.75f);
        chart.animateY(1000);
        chart.invalidate();
    }


    protected BarData parserWeekChart(int dataSets,List<String> weekList, List<Integer> totalNumList) {

        ArrayList<IBarDataSet> sets = new ArrayList<>();
        for (int i = 0; i < dataSets; i++) {

            ArrayList<BarEntry> entries = new ArrayList<>();

            for (int j = 0; j < weekList.size(); j++) {
                int value = totalNumList.get(j);
                entries.add(new BarEntry(j, (float) value));
            }

            BarDataSet ds = new BarDataSet(entries, "");
            ds.setColor(ContextCompat.getColor(context, R.color.color_bill_chart));
            ds.setHighLightColor(ContextCompat.getColor(context, R.color.color_navigation_press));
            sets.add(ds);
        }

        BarData d = new BarData(sets);
        return d;
    }


    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START");
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END");
        chart.highlightValues(null);
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart long pressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart fling. VelocityX: " + velocityX + ", VelocityY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }
}
