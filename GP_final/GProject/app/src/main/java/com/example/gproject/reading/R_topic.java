package com.example.gproject.reading;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gproject.MainActivity;
import com.example.gproject.R;

public class R_topic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r_topic);

        ImageButton backButton = findViewById(R.id.back);
        ImageButton ButBlank = findViewById(R.id.Q1);
        ImageButton ButJudge = findViewById(R.id.Q2);
        ImageButton ButChos = findViewById(R.id.Q3);
        ImageButton ButMatch = findViewById(R.id.Q4);
        ImageButton ButMultiple = findViewById(R.id.Q5);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(R_topic.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ButBlank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChoseTest(1);
            }
        });
        ButJudge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChoseTest(2);
            }
        });
        ButChos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChoseTest(3);
            }
        });
        ButMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChoseTest(4);
            }
        });
        ButMultiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChoseTest(5);
            }
        });
    }

    //Set Next Form's chos Num
    private void goToChoseTest(int number) {
        Intent intent = new Intent(this, ChoseTestActivity.class);
        intent.putExtra("number", number);
        startActivity(intent);
        Log.d("topic", "A" + number);
        //获取SharedPreferences实例
        SharedPreferences sharedPreferences = getSharedPreferences("R_topic", MODE_PRIVATE);

        // 从SharedPreferences中获取当前单词级别，默认值为"未知"
        String wordLevel = sharedPreferences.getString("R_topic", "unKnow");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        // 将单词级别设置为"Login"
        editor.putInt("R_topic", number);
        // 提交编辑
        editor.apply();
    }
}



