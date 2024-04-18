package com.example.gproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.example.gproject.Speaking.Speaking;

public class Learning extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learning);
    }


    public void SpeakingClick(View view){
        Intent intent = new Intent();
        intent.setClass(Learning.this, Speaking.class);
        startActivity(intent);

    }

    public void ReadingClick(View view){
        Intent intent = new Intent();
        //intent.setClass(Learning.this, Reading.class);
        startActivity(intent);

    }
}