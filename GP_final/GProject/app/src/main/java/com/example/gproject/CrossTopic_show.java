package com.example.gproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gproject.AiTeacher.CrossTopic_db;

public class CrossTopic_show extends AppCompatActivity {
    TextView ans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cross_topic_show);

        ImageButton back=findViewById(R.id.back2);
        ImageView home=findViewById(R.id.home2);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CrossTopic_show.this, MainActivity.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView ques=findViewById(R.id.Question);
        ans =findViewById(R.id.Ans);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ques.setText(extras.getString("question"));


        }

    }

    public void getDb(){

    }
}