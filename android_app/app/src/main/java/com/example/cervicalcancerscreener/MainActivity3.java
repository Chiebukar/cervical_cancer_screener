package com.example.cervicalcancerscreener;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class MainActivity3 extends AppCompatActivity {
    ImageView typeView;

    // variable for our bar chart
    BarChart barChart;

    // variable for our bar data.
    BarData barData;

    // variable for our bar data set.
    BarDataSet barDataSet;

    // array list for storing entries.
    ArrayList<BarEntry> barEntries;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Intent intent = getIntent();
        String prediction = intent.getStringExtra("response");
        typeView = findViewById(R.id.typeView);

        try {
            JSONObject jsonObject = new JSONObject(prediction);
            String classType = jsonObject.getString("class");
            String probability = jsonObject.getString("probability");
            JSONArray probaArr = new JSONArray(probability);
            showClass(classType);
            Log.i("probability", probability);

            // initializing variable for bar chart.
            barChart = findViewById(R.id.barChart);

            // calling method to get bar entries.
            getBarEntries(probaArr);

            // creating a new bar data set.
            barDataSet = new BarDataSet(barEntries, "Class probablity");

            // creating a new bar data and
            // passing our bar data set.
            barData = new BarData(barDataSet);

            // below line is to set data
            // to our bar chart.
            barChart.setData(barData);

            // adding color to our bar data set.
            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

            // setting text color.
            barDataSet.setValueTextColor(Color.BLACK);


            // setting text size
            barDataSet.setValueTextSize(16f);
            barChart.getDescription().setEnabled(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void showClass(String classType) {
        Log.i("class type", classType);
        switch (classType) {
            case "Type 1":
                typeView.setImageResource(R.drawable.cervical_cancer_type1);
                break;
            case "Type 2":
                typeView.setImageResource(R.drawable.cervical_cancer_type2);
                break;
            case "Type 3":
                typeView.setImageResource(R.drawable.cervical_cancer_type3);
                break;
            default:
                Log.i("message", "classType mismatch");
                break;
        }
    }
    private void getBarEntries(JSONArray probaArr) throws JSONException {
        String[] labels = {"Type 1", "Type 2", "Type 3"};
        // creating a new array list
        barEntries = new ArrayList<>();
        XAxis xAxis = barChart.getXAxis();
        xAxis.setTextSize(13);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        for(int i=0; i<probaArr.length(); i++ ){
            // adding new entry to our array list with bar
            // entry and passing x and y axis value to it.
           barEntries.add(new BarEntry(i, Float.parseFloat(probaArr.getString(i))));

        }




    }
}