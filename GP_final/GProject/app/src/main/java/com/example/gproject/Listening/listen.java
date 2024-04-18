package com.example.gproject.Listening;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gproject.R;

public class listen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listen);


    }
    public void test1Click(View view){
        Intent intent = new Intent();
        intent.setClass(this, listen_test1_Section1.class);
        startActivity(intent);
    }
    public void test2Click(View view){
        Intent intent = new Intent();
        intent.setClass(this, listen_TTest1_Section1.class);
        startActivity(intent);
    }

}