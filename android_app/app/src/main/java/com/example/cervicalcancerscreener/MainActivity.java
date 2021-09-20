package com.example.cervicalcancerscreener;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    public void toSecondActivity(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
        ImageView home = findViewById(R.id.homeView);
        home.animate().alpha(0.5f).setDuration(2000);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}