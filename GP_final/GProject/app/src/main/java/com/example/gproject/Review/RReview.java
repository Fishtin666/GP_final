package com.example.gproject.Review;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.gproject.MainActivity;
import com.example.gproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RReview extends AppCompatActivity {

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        Ans=findViewById(R.id.Answer);
        Ques = findViewById(R.id.EN);
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

        getpic();
        getData();

    }



    private void getData(){
        Ans.removeAllViews();
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
                                String fieldValue = document.getString(fieldName);
                                // 创建新的 TextView
                                TextView textView = new TextView(this);
                                textView.setText(number + "."); // 设置 TextView 文本内容为提取到的数字
                                // 将 TextView 添加到 LinearLayout 中
                                Ans.addView(textView);

                                // 从 Realtime Database 获取与 number 对应的子节点数据
                                FirebaseAuth auth = FirebaseAuth.getInstance();
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                FirebaseUser currentUser = auth.getCurrentUser();
                                if (currentUser != null) {
                                    String userId = currentUser.getUid();
                                    DatabaseReference targetRef = databaseReference
                                            .child("Listen")
                                            .child(userId)
                                            .child(test)
                                            .child(date_num)
                                            .child(number); // 子节点是按数字存储的

                                    targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                // 获取与数字对应的子节点数据
                                                String childData = dataSnapshot.getValue(String.class);
//                                                // 设置子节点数据到 TextView
//                                                textView.setText(number + ". " + childData);
                                                SpannableString Answer;
                                                if (childData.equals(fieldValue)) {
                                                    // 如果 childData 和 number 所表示的资料相同
                                                    textView.setText(number + ". " + childData);
                                                    //Answer.setSpan(new ForegroundColorSpan(Color.BLACK), number.length() + 2, Answer.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                } else {
                                                    // 如果 childData 和 number 所表示的资料不相同
                                                    Answer = new SpannableString(number + ". " + childData + " " + fieldValue);
                                                    Answer.setSpan(new ForegroundColorSpan(Color.RED), number.length() + 2, number.length() + 2 + childData.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                    Answer.setSpan(new ForegroundColorSpan(Color.BLUE), number.length() + 3 + childData.length(), Answer.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                    textView.setText(Answer);
                                                }
                                            } else {
                                                // 如果 Realtime Database 不存在子节点
                                                SpannableString Answer = new SpannableString(number + ". " + fieldValue);
                                                Answer.setSpan(new ForegroundColorSpan(Color.BLUE), number.length() + 2, Answer.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                textView.setText(Answer);
                                                Log.d("TAG", "Child node does not exist");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            Log.e("TAG", "Error getting child node: " + databaseError.getMessage());
                                        }
                                    });
                                }

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
        Log.d("按下next按鈕", "question count:doc count " + question+":"+documentcount);
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
        getpic();

    }

    public void preClick2(View view){
        Log.d("按下pre按鈕", "question count:doc count " + question+":"+documentcount);
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
        getpic();
    }

    // 将 sectionClick 传递按钮标识作为参数
    public void sectionClick(int buttonIdentifier) {
        pre.setVisibility(View.VISIBLE);
        next.setVisibility(View.VISIBLE);

        section = buttonIdentifier;
        Qcount=1;
        question=1;
        //countDocument();
        Log.d("TAG", "現在選擇的Activity section: " + section);

    }
    //当点击 button1 按钮时调用该方法
    public void s1Click2(View view) {
        // 调用 testClick 方法，传递按钮标识为 1 和目标 Activity 的类
        sectionClick(1);
        //countDocument();
        getpic();
        getData();
    }
    public void s2Click2(View view) {
        sectionClick(2);
        //countDocument();
        getpic();
        getData();
    }

    public void s3Click2(View view) {
        sectionClick(3);
        //countDocument();
        getpic();
        getData();
    }

    public void s4Click2(View view) {
        sectionClick(4);
        //countDocument();
        getpic();
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
                        if (documentName.startsWith("S" + section)) {
                            documentcount++;
                        }
                    }
                    Log.d("TAG", "選擇section button後 Document count: " + documentcount); // 打印文档数量

                })
                .addOnFailureListener(e -> {
                    Log.e("TAG", "Error getting document count: ", e);
                });
    }

    private void getpic(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //int section = 1;
        DocumentReference Q = db.collection("Listen_" + test).document("S" + section + "_" + Qcount);
        Log.d("Refpic DOC", "Listen_" + test+"/S"+section+"_"+Qcount);
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
                            Log.d("Picref", "pic ref: " + test + "_p" + Qcount);
                            try {
                                File localfile = File.createTempFile("tempfile", ".png");
                                storageRef.getFile(localfile)
                                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                                pic.setImageBitmap(bitmap);
                                                pic.setVisibility(View.VISIBLE);  // 确保图片视图可见
                                                // 成功下载图片后，递增 p 变量
                                                //p[0]++;
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("TAG", "Error downloading picture: " + e.getMessage());
                                                pic.setVisibility(View.GONE);  // 隐藏图片视图
                                            }
                                        });
                            } catch (IOException e) {
                                Log.e("TAG", "File creation failed: " + e.getMessage());
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    if (!haspicture) {
                        pic.setVisibility(View.GONE);
                    }
                }else {
                    pic.setVisibility(View.GONE);
                    Log.d("TAG", "Document does not exist");
                }
            }else {
                pic.setVisibility(View.GONE);
                Log.e("TAG", "Error getting document: " + task.getException());
            }
        });
    }

    public void contentClick(View view){
        Intent intent = new Intent(this, ReviewShow_Listencontent.class);
        intent.putExtra("test", test);
        intent.putExtra("section", section);
        intent.putExtra("Qcount",Qcount);
        Log.d("content ref", test+","+section+","+Qcount);
        startActivity(intent);
    }

}