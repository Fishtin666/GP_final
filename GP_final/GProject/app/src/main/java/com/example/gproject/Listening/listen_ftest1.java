package com.example.gproject.Listening;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.gproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class listen_ftest1 extends AppCompatActivity {

    TextView title;
    ImageView next;
    ImageView pre;
    TextView timer;
    CountDownTimer countDownTimer;
    long totalTime = 30 * 60 * 1000; // 30 minutes in milliseconds
    boolean isTimerPlay = false;
    MediaPlayer mediaPlayer;
    boolean isAudioPlay = false;// 标志用于跟踪播放状态
    int bundleValue;// 保存 Bundle 中的参数值
    int doccount;//此section有幾個題目
    int question=1;//要傳給fragment的值: S1_"1"
    public int section = 1;//section變數
    private HashMap<Integer, String> editTextMapFG = new HashMap<>();
    // 定义 HashMap 用于存储 Fragment 中编辑的文本内容


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_ftest1);
        timer = findViewById(R.id.timer1);
        pre = findViewById(R.id.pre);
        next = findViewById(R.id.next);
        title = findViewById(R.id.title);

        // 获取传递的 Bundle 对象
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // 从 Bundle 中提取参数值并保存为类级别的成员变量
            bundleValue = bundle.getInt("L_test");
            //Log.d("TAG", "Activity L_test value: " + bundleValue);

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


        // 查询以 "S1" 开头的文档数量
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Listen_"+bundleValue)
//                .whereGreaterThanOrEqualTo(FieldPath.documentId(), "S"+section)
//                .whereLessThan(FieldPath.documentId(), "S"+section+1)
                //.document( "S"+section)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                        String documentName = document.getId();
                        //Log.d("TAG", "Document name: " + documentName);
                        //Log.d("TAG", "Activity section: " + section);
                        // 计算文档数量
                        if (documentName.startsWith("S"+section)) {
                            doccount++;
                            //Log.d("TAG", "Document count: " + doccount);
                        }
                    }
                    //Log.d("TAG", "Activity section: " + section);
                    //Log.d("TAG", "ALL Document count: " + doccount); // 打印文档数量
                    // 将 fileCount 的值传递给 Fragment
                    //Fragment_listen_question1 myFragment = new Fragment_listen_question1();
                    myFragment.setDocCount(doccount);

                })
                .addOnFailureListener(e -> {
                    Log.e("TAG", "Error getting document count: ", e);
                });

    }




    // 将 sectionClick 传递按钮标识作为参数
    public void sectionClick(int buttonIdentifier) {
        title.setText("test"+bundleValue+" Section"+buttonIdentifier);
        pre.setVisibility(View.VISIBLE);
        next.setVisibility(View.VISIBLE);
        // 获取当前显示的 Fragment
        this.section = buttonIdentifier;
        question=1;
        //Log.d("TAG", "現在選擇的Activity section: " + section);
        //Log.d("TAG", "選擇section button後 Document count: " + doccount); // 打印文档数量
        Fragment_listen_question1 fragment = (Fragment_listen_question1) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        if (fragment != null) {
            Bundle bundle = fragment.getArguments(); // 获取已有的 Bundle
            if (bundle == null) {
                bundle = new Bundle(); // 如果没有 Bundle，则创建一个新的
            }
            bundle.putInt("L_section", buttonIdentifier);// 将 L_section 参数放入 Bundle
            bundle.putInt("Qcount",1);
            //Log.d("TAG", "choose L_section value: " + buttonIdentifier);

            // 将更新后的 Bundle 设置给 Fragment
            // 创建 Fragment 实例并设置参数
            Fragment_listen_question1 myFragment = new Fragment_listen_question1();
            myFragment.setArguments(bundle);

            // 启动 Fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, myFragment)
                    .commit();
        }
    }
     //当点击 button1 按钮时调用该方法
    public void s1Click(View view) {
        // 调用 testClick 方法，传递按钮标识为 1 和目标 Activity 的类
        sectionClick(1);
        countDocument();
    }
    public void s2Click(View view) {
        sectionClick(2);
        countDocument();
    }

    public void s3Click(View view) {
        sectionClick(3);
        countDocument();
    }

    public void s4Click(View view) {
        sectionClick(4);
        countDocument();
    }



    //計算每個section有幾題題目
    public void  countDocument(){
        // 查询以 "S1" 开头的文档数量
        doccount = 0 ;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Listen_"+bundleValue)
//                .whereGreaterThanOrEqualTo(FieldPath.documentId(), "S"+section)
//                .whereLessThan(FieldPath.documentId(), "S"+section+1)
                //.document( "S"+section)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                        String documentName = document.getId();
                        //Log.d("TAG", "Document name: " + documentName);
                        //Log.d("TAG", "Activity section: " + section);
                        // 计算文档数量
                        if (documentName.startsWith("S"+section)) {
                            doccount++;
                            //Log.d("TAG", "Document count: " + doccount);
                        }
                    }
                    //Log.d("TAG", "Activity section: " + section);
                    //Log.d("TAG", "ALL Document count: " + doccount); // 打印文档数量
                    // 将 fileCount 的值传递给 Fragment
                    Fragment_listen_question1 myFragment = new Fragment_listen_question1();
                    myFragment.setDocCount(doccount);

                })
                .addOnFailureListener(e -> {
                    Log.e("TAG", "Error getting document count: ", e);
                });
    }

    //換題目頁面
    public void nextClick(View view){
        //Log.d("TAG", "question count: " + question);
        pre.setVisibility(View.VISIBLE);
        if(question < doccount){
            question++;
            //Log.d("TAG", "question count: " + question);
            // 将 fileCount 的值传递给 Fragment
            Fragment_listen_question1 myFragment = new Fragment_listen_question1();
            myFragment.setQuestionCount(question);
//            pre.setVisibility(View.VISIBLE);
            if (question == doccount) {
                next.setVisibility(View.INVISIBLE);
            }

            Fragment_listen_question1 fragment = (Fragment_listen_question1) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
            if (fragment != null) {
                Bundle bundle = fragment.getArguments(); // 获取已有的 Bundle
                if (bundle == null) {
                    bundle = new Bundle(); // 如果没有 Bundle，则创建一个新的
                }
                bundle.putInt("Qcount", question); // 将 L_section 参数放入 Bundle
                //Log.d("TAG", "choose L_section value: " + buttonIdentifier);

                // 将更新后的 Bundle 设置给 Fragment
                // 创建 Fragment 实例并设置参数
                //Fragment_listen_question1 myFragment = new Fragment_listen_question1();
                myFragment.setArguments(bundle);

                // 启动 Fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, myFragment)
                        .commit();
            }

        }
    }

    public void preClick(View view){
        //Log.d("TAG", "question count: " + question);
        next.setVisibility(View.VISIBLE);
        if(1<question && question <= doccount){
            question--;
            //Log.d("TAG", "question count: " + question);
            // 将 fileCount 的值传递给 Fragment
            Fragment_listen_question1 myFragment = new Fragment_listen_question1();
            myFragment.setQuestionCount(question);
//            next.setVisibility(View.VISIBLE);
            if (question==1) {
                pre.setVisibility(View.INVISIBLE);
            }

            Fragment_listen_question1 fragment = (Fragment_listen_question1) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
            if (fragment != null) {
                Bundle bundle = fragment.getArguments(); // 获取已有的 Bundle
                if (bundle == null) {
                    bundle = new Bundle(); // 如果没有 Bundle，则创建一个新的
                }
                bundle.putInt("Qcount", question); // 将 L_section 参数放入 Bundle
                //Log.d("TAG", "choose L_section value: " + buttonIdentifier);

                // 将更新后的 Bundle 设置给 Fragment
                // 创建 Fragment 实例并设置参数
                //Fragment_listen_question1 myFragment = new Fragment_listen_question1();
                myFragment.setArguments(bundle);

                // 启动 Fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, myFragment)
                        .commit();
            }

        }
    }


    //Edittext useranswer
    public void setEditTextValueFromFG(HashMap<Integer, String> editTextMap) {
        this.editTextMapFG = editTextMap;
        for (Map.Entry<Integer, String> entry : editTextMapFG.entrySet()) {
            Log.d("HashMap", "Activity map" + entry.getKey() + ": " + entry.getValue());
        }
    }
    public HashMap<Integer, String> getEditTextValues() {
        for (Map.Entry<Integer, String> entry : editTextMapFG.entrySet()) {
            Log.d("HashMap", "hashmap Activity get" + entry.getKey() + ": " + entry.getValue());
        }
        return editTextMapFG;
    }

    
    //音檔和倒數計時器
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
    public void leaveClick(View view){
        if (mediaPlayer.isPlaying()) {
            // 暫停播放
            // 釋放音频
            stopAudio();
            Log.d("TAG","mediaPlayer leave stop");
        }
        Intent intent = new Intent(this, listen.class);
        // 启动新的 Activity
        startActivity(intent);
        finish();
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
        editTextMapFG.clear();
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