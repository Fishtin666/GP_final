package com.example.gproject.Listening;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gproject.MainActivity;
import com.example.gproject.R;

public class listen_Ans1 extends AppCompatActivity {

    TextView ans1, ans2, ans3, ans4, ans5;
    String Ans1, Ans2, Ans3, Ans4, Ans5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_ans1);

        ans1 = findViewById(R.id.ans1);
        ans2 = findViewById(R.id.ans2);
        ans3 = findViewById(R.id.ans3);
        ans4 = findViewById(R.id.ans4);
        ans5 = findViewById(R.id.ans5);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Ans1 = extras.getString("A1");
            Ans2 = extras.getString("A2");
            Ans3 = extras.getString("A3");
            Ans4 = extras.getString("A4");
            Ans5 = extras.getString("A5");

            ans1.setText(Ans1);
            ans2.setText(Ans2);
            ans3.setText(Ans3);
            ans4.setText(Ans4);
            ans5.setText(Ans5);
        }
        String Section1[] = {"Cream", "Brass", "65", "Perfect", "30"};
        if (Ans1!=null&&Ans1.equals(Section1[0])) {
            ans1.setText(Ans1);
        } else if (Ans1 == null) {
            ans1.setText(Section1[0]);
            ans1.setTextColor(Color.RED);
        } else {
            ans1.setTextColor(Color.RED);
        }

        if (Ans2!=null&&Ans2.equals(Section1[1])) {
            ans2.setText(Ans2);
        } else if (Ans2 == null) {
            ans2.setText(Section1[1]);
            ans2.setTextColor(Color.RED);
        }else {
            ans2.setTextColor(Color.RED);
        }

        if (Ans3!=null&&Ans3.equals(Section1[2])) {
            ans3.setText(Ans3);
        } else if (Ans3 == null) {
            ans3.setText(Section1[2]);
            ans3.setTextColor(Color.RED);
        }else {
            ans3.setTextColor(Color.RED);
        }

        if (Ans4!=null&&Ans4.equals(Section1[3])) {
            ans4.setText(Ans4);
        } else if (Ans4 == null) {
            ans4.setText(Section1[3]);
            ans4.setTextColor(Color.RED);
        }else {
            ans4.setTextColor(Color.RED);
        }

        if (Ans5!=null&&Ans5.equals(Section1[4])) {
            ans5.setText(Ans5);
        } else if (Ans5 == null) {
            ans5.setText(Section1[4]);
            ans5.setTextColor(Color.RED);
        }else {
            ans5.setTextColor(Color.RED);
        }




    }

    public void NextClick(View view){
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
    }
}