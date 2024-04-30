package com.example.gproject.Speaking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.example.gproject.MainActivity;
import com.example.gproject.R;

public class Speaking extends AppCompatActivity {

    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speaking);
        back = findViewById(R.id.back2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Speaking.this,MainActivity.class));
            }
        });
    }



    public void part1Click(View view){
        Intent intent = new Intent();
        intent.setClass(Speaking.this, Speaking_part1.class);
        startActivity(intent);

    }

    public void part2Click(View view){
        Intent intent = new Intent();
        intent.setClass(Speaking.this, Speaking_part2.class);
        startActivity(intent);
    }

    public void homeClick(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
