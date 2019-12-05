package com.example.fundus_app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fundus_app.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private PieChart pieChart_gender_ratio;
    private Float nonsymptom, symptom;
    private TextView tv_result;
    private Button btn_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent(); /*데이터 수신*/
        String sym = intent.getStringExtra("nonsymptom");
        String nonsym = intent.getStringExtra("symptom");
        symptom = Float.parseFloat(sym);
        nonsymptom = Float.parseFloat(nonsym);

        bindUI();

        setPieChart(symptom, nonsymptom);
        setResultText(symptom,nonsymptom);

    }

    private void bindUI(){
        pieChart_gender_ratio = (PieChart) findViewById(R.id.pieChart_gender_ratio);
        tv_result = findViewById(R.id.tv_result);
        btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setResultText(Float symptom, Float nonsymptom){
        if (symptom > nonsymptom){
            tv_result.setText("눈에 질환이 있을 확률이 높습니다. \n안과에 방문하여 정밀 검진을 받아보는 것을 \n추천드립니다.");
        }else{
            tv_result.setText("눈에 질환이 있을 확률이 낮습니다. \n그러나 방심은 금물! 정기 검진은 필수입니다.");

        }

    }

    private void setPieChart(Float sym, Float nonsym) {

        pieChart_gender_ratio.setTouchEnabled(true);
        pieChart_gender_ratio.setNoDataText("데이터 없음");
        pieChart_gender_ratio.setUsePercentValues(true);

        pieChart_gender_ratio.setHoleRadius(20f);
        pieChart_gender_ratio.setDrawEntryLabels(true);

        pieChart_gender_ratio.setUsePercentValues(true);
        pieChart_gender_ratio.getDescription().setEnabled(false);
        pieChart_gender_ratio.setExtraOffsets(0, 0, 0, 0);

        pieChart_gender_ratio.setDragDecelerationFrictionCoef(0.95f);

        pieChart_gender_ratio.setDrawHoleEnabled(true);
        pieChart_gender_ratio.setHoleColor(Color.WHITE);
        pieChart_gender_ratio.setTransparentCircleRadius(0);

        pieChart_gender_ratio.setEntryLabelColor(Color.WHITE);

        ArrayList<PieEntry> yValues = new ArrayList<>();

        //%로 바꾸기

        yValues.add(new PieEntry(sym, "비정상"));
        yValues.add(new PieEntry(nonsym, "정상"));


        Description description = new Description();
        description.setText("THIS IS DESCRPITION");
        description.setTextSize(15);

        pieChart_gender_ratio.animateY(1000, Easing.EasingOption.EaseInOutCubic);

        PieDataSet dataSet = new PieDataSet(yValues, "");
        dataSet.setSliceSpace(0);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(new int[]{R.color.button_4_pressed, R.color.pie1, R.color.pie2, R.color.pie3, R.color.pie4, R.color.pie5, R.color.pie6,}, this);
        dataSet.setDrawValues(true);

        dataSet.setDrawIcons(true);


        com.github.mikephil.charting.data.PieData data = new com.github.mikephil.charting.data.PieData(dataSet);
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.WHITE);
        pieChart_gender_ratio.setData(data);
        pieChart_gender_ratio.invalidate();
    }
}
