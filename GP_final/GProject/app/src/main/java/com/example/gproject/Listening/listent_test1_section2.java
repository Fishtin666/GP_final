package com.example.gproject.Listening;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gproject.R;


public class listent_test1_section2 extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private ImageButton play_Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_test1_section2);

        mediaPlayer = MediaPlayer.create(this, R.raw.academic_test1_listening_section2); // 指定MP3檔案
        play_Btn = findViewById(R.id.play_Btn);

        play_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start(); // 開始播放
                }
            }
        });
    }
    public void NextClick(View view){
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause(); // 暫停播放
            mediaPlayer.seekTo(0); // 將播放器重置到開頭
        }
        Intent intent = new Intent();
        intent.setClass(this, listent_test1_section3.class);
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