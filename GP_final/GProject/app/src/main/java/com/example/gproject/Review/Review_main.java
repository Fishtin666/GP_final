package com.example.gproject.Review;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gproject.R;


public class Review_main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review);
        Spinner spinner1 = findViewById(R.id.spinner1);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this,R.array.Review_array, android.R.layout.simple_dropdown_item_1line);
        spinner1.setAdapter(adapter1);

        ImageButton pencil = findViewById(R.id.pencil1);
        Button button = findViewById(R.id.button);

        pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Review_main.this, Review_choose.class);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener(){
            public  void onClick(View v){
                Intent intent = new Intent(Review_main.this, RReview.class);
                startActivity(intent);
            }
        });
    }
}