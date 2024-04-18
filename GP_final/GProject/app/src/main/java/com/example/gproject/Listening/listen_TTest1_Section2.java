package com.example.gproject.Listening;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gproject.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class listen_TTest1_Section2 extends AppCompatActivity {

    private Button button_next;
    protected Button button1;
    protected Button button2;
    protected Button button3;
    protected Button button4;
    //顯示資料庫中的題目
    private TextView questionTextView;
    private TextView question2TextView;
    private TextView question3TextView;
    private FirebaseFirestore db;
    private DocumentReference documentRefQ11_17;
    private DocumentReference documentRefQ18;
    private DocumentReference documentRefQ19_20;

    //mediaplayer
    private MediaPlayer mediaPlayer;
    private ImageButton play_Btn;


    //timer
    private TextView timer;
    private TimerReceiver timerReceiver=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_ttest1_section2);

        //mediaplayer
        mediaPlayer = MediaPlayer.create(this, R.raw.academic_test1_listening_section2); // 指定MP3檔案
        play_Btn = findViewById(R.id.play_Btn);
        timer = findViewById(R.id.timer3);
        button_next = findViewById(R.id.button_next);
        button1 = findViewById(R.id.button1);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        //註冊廣播
        timerReceiver = new TimerReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.demo.timer");
        registerReceiver(timerReceiver, filter);

        play_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start(); // 開始播放
                }
                if(timerReceiver==null){
                    timerReceiver = new TimerReceiver();
                    IntentFilter filter = new IntentFilter();
                    filter.addAction("com.demo.timer");
                    registerReceiver(timerReceiver, filter);
                }
                //TimerService開始服務
                startService(new Intent(listen_TTest1_Section2.this, TimerService.class));
            }
        });
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause(); // 暫停播放
                    if (timerReceiver != null) {
                    stopService(new Intent(listen_TTest1_Section2.this, TimerService.class));
                    Log.d("TimerService", "Timer stop");
                    unregisterReceiver(timerReceiver);
                    timerReceiver = null;
                    }
                }
                Intent intent = new Intent(listen_TTest1_Section2.this, listen_TTest1_Section3.class);
                startActivity(intent);
                finish();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause(); // 暫停播放
                }
                Intent intent = new Intent(listen_TTest1_Section2.this, listen_TTest1_Section1.class);
                startActivity(intent);
                finish();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause(); // 暫停播放
                }
                Intent intent = new Intent(listen_TTest1_Section2.this, listen_TTest1_Section3.class);
                startActivity(intent);
                finish();
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause(); // 暫停播放
                }
                Intent intent = new Intent(listen_TTest1_Section2.this, listen_TTest1_Section4.class);
                startActivity(intent);
                finish();
            }
        });

        //show Question in firebase
        questionTextView = findViewById(R.id.question4);
        question2TextView = findViewById(R.id.question3);
        question3TextView = findViewById(R.id.question9);
        db = FirebaseFirestore.getInstance();
        // 指定 Firestore 中的文件路徑
        documentRefQ11_17 = db.collection("Listening").document("L_test1").collection("Section2").document("Q11-17");
        documentRefQ18 = db.collection("Listening").document("L_test1").collection("Section2").document("Q18");
        documentRefQ19_20 = db.collection("Listening").document("L_test1").collection("Section2").document("Q19-20");
        // 讀取文件內容
        //private void readAndSetQuestion(DocumentReference documentRefQ1_5, TextView questionTextView) {
        documentRefQ11_17.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // 取得資料並設定給 TextView
                    String question = document.getString("Question"); // 替換為你的字段名稱
                    String questionText = question.replace("\\n","\n");
                    questionTextView.setText(questionText);
                } else {
                    // 文件不存在的處理
                    Log.d("TAG", "Document does not existQ11_17");
                }
            } else {
                // 讀取失敗的處理
                Log.e( "TAG","Error getting documentQ11_17: " + task.getException());
            }
        });
        documentRefQ18.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // 取得資料並設定給 TextView
                    String question = document.getString("Question"); // 替換為你的字段名稱
                    String questionText = question.replace("\\n","\n");
                    question2TextView.setText(questionText);
                } else {
                    // 文件不存在的處理
                    Log.d("TAG", "Document does not existQ18");
                }
            } else {
                // 讀取失敗的處理
                Log.e("TAG", "Error getting documentQ18: " + task.getException());
            }
        });
        documentRefQ19_20.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // 取得資料並設定給 TextView
                    String question = document.getString("Question"); // 替換為你的字段名稱
                    String questionText = question.replace("\\n","\n");
                    question3TextView.setText(questionText);
                } else {
                    // 文件不存在的處理
                    Log.d("TAG", "Document does not exist");
                }
            } else {
                // 讀取失敗的處理
                Log.e("TAG", "Error getting document: " + task.getException());
            }
        });

    }

    //廣播，實現timerReciver
    public class TimerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String intentAction= intent.getAction();
            if(intentAction.equals("com.demo.timer")){
                int timeInSeconds = intent.getIntExtra("time", 0);

                int minutes = timeInSeconds / 60;
                int seconds = timeInSeconds % 60;
                String timerText = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                if (timer != null) {
                    timer.setText(timerText);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release(); // 釋放MediaPlayer資源
        }
        // 解除註冊廣播接收器
        if (timerReceiver != null) {
            unregisterReceiver(timerReceiver);
            timerReceiver = null;
        }
    }
}