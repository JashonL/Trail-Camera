package com.shuoxd.camera.module.camera.chart_fragment;

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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.shuoxd.camera.R;
import com.shuoxd.camera.customview.MyMarkerView;
import com.shuoxd.camera.utils.CommentUtils;

import java.util.ArrayList;
import java.util.List;


public class BarChartTotalFrag extends ChartBaseFragment implements OnChartGestureListener {


    private BarChart chart;

    private List<String> xDatas;


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

        chart.setDrawValueAboveBar(false);
        chart.setHighlightFullBarEnabled(false);


        Legend l = chart.getLegend();

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });

        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setGranularity(1);
        chart.getAxisRight().setEnabled(false);

   /*     XAxis xAxis = chart.getXAxis();
        xAxis.setEnabled(false);*/

        xDatas = CommentUtils.getWeeks();
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


    public void setBarChart(List<String> xDatas, List<Integer> readlist, List<Integer> unReadlist) {
        this.xDatas = xDatas;
        Log.i("barchart", "设置数据");

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {

            BarDataSet set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            ArrayList<BarEntry> entries = parserData1(xDatas, readlist, unReadlist);
            set1.setValues(entries);

            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            // create 4 DataSets
            BarData barData = parserYearChart(xDatas, readlist, unReadlist);
            chart.setData(barData);
        }


        chart.getBarData().setBarWidth(0.75f);
        chart.animateY(1000);
        chart.setFitBars(true);
        chart.invalidate();
    }


    protected BarData parserYearChart(List<String> xDatas, List<Integer> readlist, List<Integer> unReadlist) {

        ArrayList<IBarDataSet> sets = new ArrayList<>();
//        for (int i = 0; i < dataSets; i++) {

       /*     ArrayList<BarEntry> entries = new ArrayList<>();

            for (int j = 0; j < xDatas.size(); j++) {
                int value = totalNumList.get(j);
                entries.add(new BarEntry(j, (float) value));
            }
*/

        ArrayList<BarEntry> entries = parserData1(xDatas, readlist, unReadlist);

        BarDataSet ds = new BarDataSet(entries, "");
        ds.setDrawIcons(false);
        ds.setColors(getColors());

        ds.setStackLabels(new String[]{"Read", "UnRead"});


        ds.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });
        ds.setValueTextColor(ContextCompat.getColor(context, R.color.white));
        ds.setValueTextSize(10);
        ds.setDrawValues(false);
        sets.add(ds);
//        }

        return new BarData(sets);
    }


    private ArrayList<BarEntry> parserData(List<String> xDatas, List<Integer> totalNumList) {
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int j = 0; j < xDatas.size(); j++) {
            int value = totalNumList.get(j);
            entries.add(new BarEntry(j, (float) value));
        }
        return entries;
    }


    private ArrayList<BarEntry> parserData1(List<String> xDatas, List<Integer> unReadList, List<Integer> readlist) {
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < xDatas.size(); i++) {
            float val1 = (float) unReadList.get(i);
            float val2 = (float) readlist.get(i);
            entries.add(new BarEntry(i, new float[]{val1, val2}));
        }

        return entries;
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


    private int[] getColors() {
        // have as many colors as stack-values per entry
        int[] colors = new int[]{ContextCompat.getColor(getActivity(), R.color.barchart_color), ContextCompat.getColor(getActivity(), R.color.color_app_main)};
        return colors;
    }
}
