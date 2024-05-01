package com.example.gproject.Listening;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.gproject.R;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class listen_ftest1 extends AppCompatActivity {

    Button next;
    Button pre;
    TextView timer;
    CountDownTimer countDownTimer;
    long totalTime = 30 * 60 * 1000; // 30 minutes in milliseconds
    boolean isTimerPlay = false;
    MediaPlayer mediaPlayer;
    boolean isAudioPlay = false;// 标志用于跟踪播放状态
    int bundleValue;// 保存 Bundle 中的参数值
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_ftest1);
        timer=findViewById(R.id.timer1);

        // 获取传递的 Bundle 对象
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // 从 Bundle 中提取参数值并保存为类级别的成员变量
            bundleValue = bundle.getInt("L_test");
            Log.d("TAG", "L_test value: " + bundleValue);

        }

        // 创建 Fragment 实例并设置参数
        Fragment_listen_question1 myFragment = new Fragment_listen_question1();
        myFragment.setArguments(bundle);

        // 启动 Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, myFragment)
                .commit();

        // 初始化 MediaPlayer
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(mp -> {
            // 音频播放完成时的操作
            stopAudio();
        });


    }

    public void audioPlay(View view) {
        // 检查是否正在播放音频
        if (!isAudioPlay) {
            // 检查 MediaPlayer 是否已经初始化
            if (mediaPlayer != null) {
                if (!mediaPlayer.isPlaying()) {
                    // mediaPlayer.start(); // 開始播放
                    // 播放音频
                    playAudio();
                    isAudioPlay = true;
                    startTimer();
                }
            }
        }
    }
    public void leaveClick(){
        if (mediaPlayer.isPlaying()) {
            // 暫停播放
            // 釋放音频
            stopAudio();
            Log.d("TAG","mediaPlayer leave stop");
        }
        Intent intent = new Intent(this, listen.class);
        // 启动新的 Activity
        startActivity(intent);
    }


    //音頻撥放
    private void playAudio(){
        try {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference("mp3/"+bundleValue+".mp3");

                // 创建临时文件来保存从 Firebase Storage 下载的文件
                File localFile = File.createTempFile("tempfile", ".mp3");

                // 下载 mp3 文件到临时文件
                storageRef.getFile(localFile)
                        .addOnSuccessListener(taskSnapshot -> {
                            // 下载成功，创建 MediaPlayer 并播放 mp3 文件
                            MediaPlayer mediaPlayer = new MediaPlayer();
                            try {
                                mediaPlayer.setDataSource(localFile.getAbsolutePath());
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        })
                        .addOnFailureListener(exception -> {
                            // 下载失败，处理异常
                            Log.e("TAG", "Failed to download audio file: " + exception.getMessage());
                        });
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopAudio() {
        // 停止播放音频
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }



    //倒數計時
    private void startTimer() {
        countDownTimer = new CountDownTimer(totalTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
//                // 计算剩余时间并更新 TextView 的文本内容
                long minutes = millisUntilFinished / (1000 * 60);
                long seconds = (millisUntilFinished / 1000) % 60;
                String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
                timer.setText(timeLeftFormatted);
            }

            @Override
            public void onFinish() {
                // 倒计时结束，重置播放状态
                isTimerPlay = false;
                Log.d("TimerService", "Timer finished");
            }
        }.start();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在 Activity 销毁时释放 MediaPlayer 资源
        if (mediaPlayer != null) {
            mediaPlayer.release(); // 釋放MediaPlayer資源
            mediaPlayer = null;
            Log.d("TAG","mediaPlayer release");
        }
        // 在Activity销毁时停止倒计时器
        if (countDownTimer != null) {
            countDownTimer.cancel();
            Log.d("TAG","countDownTimer cancel");
        }
    }
}