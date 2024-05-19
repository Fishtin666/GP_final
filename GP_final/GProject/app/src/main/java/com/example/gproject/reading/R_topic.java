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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(R_topic.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void homeClick(View view){
        Intent intent = new Intent(R_topic.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void blankClick(View View){
        goToChoseTest(1);
    }
    public void judge_click(View view){
        goToChoseTest(2);
    }

    public void singleChos_click(View view){
        goToChoseTest(3);
    }

    public void match_click(View view){
        goToChoseTest(4);
    }
    public void multipleChos_click(View view){
        goToChoseTest(5);
    }

    //Set Next Form's chos Num
    private void goToChoseTest(int number) {
//        Intent intent = new Intent(this, ChoseTestActivity.class);
        Intent intent = new Intent(this, ChoseTestActivity.class);
//        intent.putExtra("R_topic", number);
        startActivity(intent);
        
        SharedPreferences sharedPreferences = getSharedPreferences("R_topic", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("R_topic", number);
        editor.apply();
        Log.e("R_topic", "A" + number);
    }
}



