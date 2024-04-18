package com.example.gproject.Listening;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gproject.R;

import java.util.Locale;

public class listen_test1_Section1 extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private ImageButton play_Btn;
    private TextView timer;
    private CountDownTimer countDownTimer;
    private  long totalTime = 30 * 60 * 1000; // 30 minutes in milliseconds
    private boolean timerRunning = false;
    EditText A1,A2,A3,A4,A5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_test1_section1);

        mediaPlayer = MediaPlayer.create(this, R.raw.academic_test1_listening_section1); // 指定MP3檔案
        play_Btn = findViewById(R.id.play_Btn);
        timer = findViewById(R.id.timer1);

        play_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start(); // 開始播放
                }
                ttimer();
            }
        });

        A1 = findViewById(R.id.Ans1);
        A2 = findViewById(R.id.Ans2);
        A3 = findViewById(R.id.Ans3);
        A4 = findViewById(R.id.Ans4);
        A5 = findViewById(R.id.Ans5);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            A1.setText(extras.getString("A1"));
            A2.setText(extras.getString("A2"));
            A3.setText(extras.getString("A3"));
            A4.setText(extras.getString("A4"));
            A5.setText(extras.getString("A5"));


        }
    }

    public void ttimer(){
        if(timerRunning){
            StopTimer();
        }else {
            StartTimer();
        }
    }
    public void StopTimer(){
        countDownTimer.cancel();
        timerRunning=false;
    }
    public void StartTimer(){
        countDownTimer=new CountDownTimer(totalTime,1000) {
            @Override
            public void onTick(long l) {
                totalTime = l;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();
        timerRunning=true;
    }
    public void updateTimer() {
        // 在這裡更新計時器文本，例如將時間轉換為分鐘和秒的格式
        long minutes = totalTime / 60000;
        long seconds = (totalTime % 60000) / 1000;
        String timerText = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timer.setText(timerText);
    }

    Bundle bundle=new Bundle();
    public void NextClick(View view){
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause(); // 暫停播放
            mediaPlayer.seekTo(0); // 將播放器重置到開頭
        }
        Intent intent = new Intent();
        intent.setClass(this, listent_test1_section2.class);
        bundle.putString("A1",A1.getText().toString());
        bundle.putString("A2",A2.getText().toString());
        bundle.putString("A3",A3.getText().toString());
        bundle.putString("A4",A4.getText().toString());
        bundle.putString("A5",A5.getText().toString());

        // 在Logcat中輸出edittext內容
        Log.d("EditTextContent", "A1: " + A1.getText().toString());
        Log.d("EditTextContent", "A2: " + A2.getText().toString());
        Log.d("EditTextContent", "A3: " + A3.getText().toString());
        Log.d("EditTextContent", "A4: " + A4.getText().toString());
        Log.d("EditTextContent", "A5: " + A5.getText().toString());
        intent.putExtras(bundle);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release(); // 釋放MediaPlayer資源
        }
    }


}