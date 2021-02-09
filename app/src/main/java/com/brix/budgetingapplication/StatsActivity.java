package com.brix.budgetingapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.brix.budgetingapplication.models.GraphData;
import com.brix.budgetingapplication.persistance.Repository;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatsActivity extends AppCompatActivity {

    PieChart PieChart;
    private Repository mRepository;
    ImageButton btnStatsBack;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        mRepository = new Repository(this);
        PieChart = findViewById(R.id.PieChart);
        btnStatsBack = (ImageButton)findViewById(R.id.btnStatsBack);

        FirstAndLastDate();
        PieChart.callOnClick();
        listener();
    }


    public void FirstAndLastDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,  0);
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date monthFirstDay = calendar.getTime();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date monthLastDay = calendar.getTime();

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String startDateStr = df.format(monthFirstDay);
        String endDateStr = df.format(monthLastDay);

        retrieveData(startDateStr,endDateStr);

    }
    public void listener(){
        btnStatsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }


    private void retrieveData(String date1, String date2) {

        mRepository.getExpenseTotal(date1,date2).observe(this, new Observer<List<GraphData>>() {
            @Override
            public void onChanged(List<GraphData> graphData) {
                if (graphData != null) {
                    PieChart.getDescription().setEnabled(false);
                    PieChart.setHoleRadius(0);
                    PieChart.setUsePercentValues(true);
                    List<PieEntry> data = new ArrayList<PieEntry>();
                    for(int i = 0; i< graphData.size();i++){
                        float amount = (float) graphData.get(i).getAmount();
                        data.add(new PieEntry(amount,graphData.get(i).getCategory()));
                    }
                    PieDataSet pieDataSet = new PieDataSet(data,"Expense");
                    PieData pieData = new PieData(pieDataSet);
                    pieData.setValueFormatter(new PercentFormatter(PieChart));
                    pieData.setValueTextSize(16);
                    pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                    pieDataSet.setValueLinePart1OffsetPercentage(90.f);
                    pieDataSet.setValueLinePart1Length(1f);
                    pieDataSet.setValueLinePart2Length(.2f);
                    pieDataSet.setValueTextColor(Color.BLACK);
                    pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                    pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                    pieDataSet.setValueLinePart1OffsetPercentage(100f);
                    pieDataSet.setSelectionShift(60);
                    PieChart.setData(pieData);
                    PieChart.animateXY(1400,1400);
                    pieData.setValueTextColor(Color.BLACK);

                }
            }
        });
    }
}