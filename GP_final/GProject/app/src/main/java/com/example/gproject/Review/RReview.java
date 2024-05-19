package com.example.gproject.Review;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gproject.Listening.Fragment_listen_question1;
import com.example.gproject.MainActivity;
import com.example.gproject.R;
import com.example.gproject.ReviewShow_Writing;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RReview extends AppCompatActivity {

    FirebaseAuth auth;
    DatabaseReference databaseReference;
    ImageView home,pic;
    ImageView pre,next;
    TextView Ques;
    LinearLayout Ans;
    int documentcount;
    int question=1;//S1_"1"，目前在section1的第""部分
    String test;  //Task_passIn:傳入的參數
    int section=1; //由1開始有4個section
    int Qcount=1; //每個section有幾個部分
    String date_num;  //第幾次回答
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rreview);

        Ans=findViewById(R.id.Answer);
        Ques = findViewById(R.id.Question);
        pic=findViewById(R.id.pic);
        pre = findViewById(R.id.pre);
        next = findViewById(R.id.next);


        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RReview.this, MainActivity.class));
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            test = bundle.getString("test");
            date_num = bundle.getString("Ldate");
            Log.d("TAG","T,D "+test+","+date_num);
        }

        getData();

    }



    private void getData(){
        Ans.removeAllViews();
        getpic();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //int section = 1;
        DocumentReference Q = db.collection("Listen_" + test).document("S" + section + "_" + Qcount);
        Log.d("Ref", "Listen_" + test+"/S"+section+"_"+Qcount);
        Q.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();

                //查找此題組有幾題
                if (document.exists()) {
                    countDocument();
                    // 获取文档中字段的数量
                    //int numberOfAnswer = 0;
                    // 假设 document 是您从 Firestore 中获取的文档
                    for (Map.Entry<String, Object> entry : document.getData().entrySet()) {
                        String fieldName = entry.getKey();
                        // 检查字段名称是否以 "" 开头
                        if (fieldName.startsWith("A")) {
                            //numberOfAnswer++;
                            // 使用正则表达式从字段名称中提取数字部分
                            Pattern pattern = Pattern.compile("\\d+");
                            Matcher matcher = pattern.matcher(fieldName);
                            if (matcher.find()) {
                                String number = matcher.group(); // 提取到的数字部分
                                int Anskey = Integer.parseInt(number);
                                // 创建新的 TextView
                                TextView textView = new TextView(this);
                                textView.setText(number + "."); // 设置 TextView 文本内容为提取到的数字
                                // 将 TextView 添加到 LinearLayout 中
                                Ans.addView(textView);

                            }
                        }
                    }

                    //顯示題目到textview
                    if (document.exists()) {
                        // 根据字段的数量来处理文档中的数据
                        String fieldName = "Q";
                        if (document.contains(fieldName)) {
                            // 根据字段名称获取字段的值并设置到相应的视图上
                            String fieldValue = document.getString(fieldName);
                            String questionText = fieldValue.replace("\\n", "\n");
                            Ques.setText(questionText);
                        }
                    } else {
                        // 文件不存在的處理
                        Log.d("TAG", "Document does not exist");
                    }
                } else {
                    // 讀取失敗的處理
                    Log.e("TAG", "Error getting document: " + task.getException());
                }
            }
        });
    }

    //換題目頁面
    public void nextClick2(View view){
        //Log.d("TAG", "question count: " + question);
        pre.setVisibility(View.VISIBLE);
        if(question < documentcount ){
            question++;
            Log.d("TAG", "question count: " + question);

            Qcount=question;
//            pre.setVisibility(View.VISIBLE);
            if (question == documentcount) {
                next.setVisibility(View.INVISIBLE);
            }
        }
        getData();

    }

    public void preClick2(View view){
        //Log.d("TAG", "question count: " + question);
        next.setVisibility(View.VISIBLE);
        if(1<question && question <= documentcount){
            question--;
            Log.d("TAG", "question count: " + question);

            Qcount=question;
//            next.setVisibility(View.VISIBLE);
            if (question==1) {
                pre.setVisibility(View.INVISIBLE);
            }
        }
        getData();
    }

    // 将 sectionClick 传递按钮标识作为参数
    public void sectionClick(int buttonIdentifier) {
        pre.setVisibility(View.VISIBLE);
        next.setVisibility(View.VISIBLE);

        section = buttonIdentifier;
        Qcount=1;
        Log.d("TAG", "現在選擇的Activity section: " + section);
        Log.d("TAG", "選擇section button後 Document count: " + documentcount); // 打印文档数量
    }
    //当点击 button1 按钮时调用该方法
    public void s1Click2(View view) {
        // 调用 testClick 方法，传递按钮标识为 1 和目标 Activity 的类
        sectionClick(1);
        countDocument();
        getData();
    }
    public void s2Click2(View view) {
        sectionClick(2);
        countDocument();
        getData();
    }

    public void s3Click2(View view) {
        sectionClick(3);
        countDocument();
        getData();
    }

    public void s4Click2(View view) {
        sectionClick(4);
        countDocument();
        getData();
    }

    //計算每個section有幾題題目
    public void  countDocument(){
        // 查询以 "S1" 开头的文档数量
        documentcount = 0 ;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Listen_"+test)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                        String documentName = document.getId();
                        //计算文档数量
                        //for(int sec=0 ;sec<=4 ;sec++) {
                        if (documentName.startsWith("S" + section)) {
                            documentcount++;
                        }
                        // }
                    }

                })
                .addOnFailureListener(e -> {
                    Log.e("TAG", "Error getting document count: ", e);
                });
    }

    private void getpic(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //int section = 1;
        DocumentReference Q = db.collection("Listen_" + test).document("S" + section + "_" + Qcount);
        Log.d("Ref", "Listen_" + test+"/S"+section+"_"+Qcount);
        Q.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    //檢查這題是否需要圖片
                    boolean haspicture = false;
                    //int[] p = {1}; //圖片名稱: 1_s1_p2.png
                    for (Map.Entry<String, Object> entry : document.getData().entrySet()) {
                        String fieldName = entry.getKey();
                        // 檢查是否有picture欄位
                        if (fieldName.equals("picture")) {
                            haspicture = true;
                            //final int pValue = p[0]; // 保存循环开始时的 p[0] 的值
                            StorageReference storageRef = FirebaseStorage.getInstance().getReference("Listen/" + test + "_s" + section + "_p" + Qcount + ".png");
                            //Log.d("TAG", "pic ref: " + test + "_p" + pValue);
                            try {
                                File localfile = File.createTempFile("tempfile", ".png");
                                storageRef.getFile(localfile)
                                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                                pic.setImageBitmap(bitmap);
                                                // 成功下载图片后，递增 p 变量
                                                //p[0]++;
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    if (!haspicture) {
                        pic.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
}