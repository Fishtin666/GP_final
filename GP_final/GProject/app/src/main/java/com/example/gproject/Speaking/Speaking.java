package com.example.gproject.Speaking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.gproject.MainActivity;
import com.example.gproject.R;
import com.example.gproject.getDb;
import com.example.gproject.profile;

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
        show();

    }

    public void homeClick(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void show(){
        AlertDialog.Builder P2_start = new AlertDialog.Builder(Speaking.this);
        P2_start.setTitle("確定要進入Speaking Part2+Part3?");
        P2_start.setMessage("1. Part2及Part3的題目有相關，當Part2結束後，您能選擇是否要繼續進入Part3。(*注意:Part3無法單獨進行練習，須先進行Part2練習" +
                "才能進入Part3)\n2. 進入Part2後會有一分鐘的時間讓您查 看TaskCard，時間到後會跳出提醒。\n" +
                "3. 回答時間有兩分鐘，時間到時會提醒， 如怕影響回答，可以按下鈴鐺圖案的按鈕，將不會跳出回答時間到之提醒。" +
                "\n4. 其他按鈕之說明可參照練習頁面右上方 灰色問號。");
        P2_start.setCancelable(false);
        P2_start.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setClass(Speaking.this, Speaking_part2.class);
                startActivity(intent);
            }
        });
        P2_start.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        P2_start.show();
    }

}
