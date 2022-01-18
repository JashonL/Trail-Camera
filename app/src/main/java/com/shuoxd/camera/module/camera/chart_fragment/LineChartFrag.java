package com.shuoxd.camera.module.camera.chart_fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.FileUtils;
import com.shuoxd.camera.R;

import java.util.ArrayList;
import java.util.List;

public class LineChartFrag extends ChartBaseFragment{

    private List<String>xDatas;

    @NonNull
    public static Fragment newInstance() {
        return new LineChartFrag();
    }

    private LineChart chart;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.frag_line_chart, container, false);

        chart = v.findViewById(R.id.lineChart1);

        chart.getDescription().setEnabled(false);

        chart.setDrawGridBackground(false);

//        chart.setData(generateLineData());
        chart.animateX(500);


        Legend l = chart.getLegend();

        YAxis leftAxis = chart.getAxisLeft();
//        leftAxis.setAxisMaximum(1.2f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });

        chart.getAxisRight().setEnabled(false);

//        XAxis xAxis = chart.getXAxis();
//        xAxis.setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return xDatas.get((int) value);
            }
        });

        xAxis.setAxisLineWidth(1.0f);
        xAxis.setGridLineWidth(0.5f);
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X轴的位置
        xAxis.setDrawLabels(true);

        return v;
    }




    public void setLineChart(List<String> xDatas, List<Integer> totalNumList) {
        this.xDatas=xDatas;
 /*       ArrayList<BarEntry> entries = new ArrayList<>();
        List<List<BarEntry>>list=new ArrayList<>();
        for (int i = 0; i <weekList.size() ; i++) {
            entries.add(new BarEntry(i, (float) totalNumList.get(i)));
        }
        list.add(entries);


        ChartUtils.setBarChartData(getContext(),chart,list,new int[]{R.color.barchart_color},new int[]{R.color.barchart_hight_color},1);
*/
        Log.i("barchart", "设置数据");
        LineData barData = parserWeekChart(1,xDatas.size(), totalNumList);
        chart.setData(barData);
        chart.animateY(3000);
        chart.invalidate();
    }




    protected LineData parserWeekChart(int dataSets,  int count, List<Integer> totalNumList) {




  /*      ArrayList<ILineDataSet> sets = new ArrayList<>();
        LineDataSet ds1 = new LineDataSet(FileUtils.loadEntriesFromAssets(context.getAssets(), "sine.txt"), "");

        ds1.setLineWidth(2f);

        ds1.setDrawCircles(true);

        ds1.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);

        // load DataSets from files in assets folder
        sets.add(ds1);

        LineData d = new LineData(sets);
        return d;*/






        ArrayList<ILineDataSet> sets = new ArrayList<>();

        for (int i = 0; i < dataSets; i++) {

            ArrayList<Entry> entries = new ArrayList<>();

            for (int j = 0; j < count; j++) {
                entries.add(new BarEntry(j, (float) totalNumList.get(j)));
            }

            LineDataSet ds = new LineDataSet(entries, "");
            ds.setDrawFilled(true);
            ds.setFillDrawable(getResources().getDrawable(R.drawable.line_gradient_bg_shape));

            ds.setDrawCircles(false);
            ds.setColor(ContextCompat.getColor(context, R.color.barchart_color));
            ds.setHighLightColor(ContextCompat.getColor(context, R.color.barchart_hight_color));
            sets.add(ds);
        }

        LineData d = new LineData(sets);
        return d;
    }


}
