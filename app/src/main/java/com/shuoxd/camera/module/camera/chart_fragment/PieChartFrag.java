package com.shuoxd.camera.module.camera.chart_fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.shuoxd.camera.R;

import java.util.ArrayList;

public class PieChartFrag extends ChartBaseFragment {

    private int total;
    private int am;
    private int pm;


    public PieChartFrag() {

    }

    @SuppressWarnings("FieldCanBeLocal")
    private PieChart chart;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_pie_chart, container, false);
        //初始化图表
        chart = v.findViewById(R.id.pieChart1);
        chart.getDescription().setEnabled(false);


        chart.setCenterText(generateCenterText());
        chart.setCenterTextSize(10f);

        // radius of the center hole in percent of maximum radius
        chart.setHoleRadius(45f);
        chart.setTransparentCircleRadius(50f);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);


        setChartData(total,am,pm);

        //设置图表
//        chart.setData(generatePieData());
        return v;
    }

    private SpannableString generateCenterText() {
        SpannableString s = new SpannableString("Revenues\nQuarters 2015");
        s.setSpan(new RelativeSizeSpan(2f), 0, 8, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 8, s.length(), 0);
        return s;
    }


    public void setChartData(int total, int am, int pm) {
        Log.i("PilChart", "设置数据");
        String all = total + "\n" + "Photos";
        chart.setCenterText(all);


        int count = 2;

        ArrayList<PieEntry> entries1 = new ArrayList<>();
        //am
        entries1.add(new PieEntry((float) (am), "AM"));
        //pm
        entries1.add(new PieEntry((float) (pm), "PM"));

        PieDataSet ds1 = new PieDataSet(entries1, "");
        ds1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.WHITE);
        ds1.setValueTextSize(12f);

        PieData d = new PieData(ds1);

        chart.setData(d);
    }



}
