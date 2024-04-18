package com.example.gproject.reading;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gproject.ChoseTestActivity;
import com.example.gproject.R;
import com.example.gproject.WordListActivity;
import com.example.gproject.WordQuiz.LevelAQuizActivity;
import com.example.gproject.meaning.ShowMeaning;

public class R_topic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r_topic);

        ImageButton backButton = findViewById(R.id.back);
        ImageButton ButBlank = findViewById(R.id.Q1);
        ImageButton ButJudge = findViewById(R.id.Q2);
        ImageButton BUtChos = findViewById(R.id.Q3);
        ImageButton BUtMatch = findViewById(R.id.Q4);
        ImageButton BUtWord = findViewById(R.id.Q6);
        ImageButton ButTest = findViewById(R.id.Q7);
        ImageButton ButCollect = findViewById(R.id.Q8);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(R_topic.this, R_blank.class);
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
        BUtChos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChoseTest(3);
            }
        });
        BUtMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChoseTest(4);
            }
        });

        BUtWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(R_topic.this, LevelAQuizActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ButTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(R_topic.this, ShowMeaning.class);
                startActivity(intent);
                finish();
            }
        });
        ButCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(R_topic.this, WordListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void goToChoseTest(int number) {
        Intent intent = new Intent(this, ChoseTestActivity.class);
        intent.putExtra("number", number);
        startActivity(intent);
        Log.d("topic","A"+ number);
    }
}



