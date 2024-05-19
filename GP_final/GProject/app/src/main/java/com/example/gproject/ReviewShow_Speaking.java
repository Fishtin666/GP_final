package com.example.gproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ReviewShow_Speaking extends AppCompatActivity {
    FirebaseAuth auth;
    private DatabaseReference databaseReference;
    ImageView home;

    TextView Ques,Ans,Judge;
    String topic;
    String ques_num; //第幾題
    String num="1";  //第幾次回答

    String randomCode;

    //part3
    TextView part3,p3_question_title,p3_question,p3_ans_title,p3_answer,p3_judge_title,p3_judge;

    //String randomCode="-NxTC6YNpganZiCH4JPg"; //隨機碼  //"NxTC6YNpganZiCH4JPg"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_show_speaking);
        Ques = findViewById(R.id.Question);
        Ans = findViewById(R.id.listenContent);
        Judge = findViewById(R.id.Judge);
        home = findViewById(R.id.home);

        //part3
        part3 = findViewById(R.id.part3);
        p3_question_title= findViewById(R.id.P3_ques_title);
        p3_question= findViewById(R.id.P3_Question);
        p3_ans_title=findViewById(R.id.P3_Ans_title);
        p3_answer=findViewById(R.id.P3_Answer);
        p3_judge_title=findViewById(R.id.P3_Judge_title);
        p3_judge=findViewById(R.id.P3_Judge);

        part3.setVisibility(View.GONE);
        p3_question_title.setVisibility(View.GONE);
        p3_question.setVisibility(View.GONE);
        p3_ans_title.setVisibility(View.GONE);
        p3_answer.setVisibility(View.GONE);
        p3_judge_title.setVisibility(View.GONE);
        p3_judge.setVisibility(View.GONE);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            topic = bundle.getString("topic");
            ques_num = bundle.getString("speakQ");
        }

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReviewShow_Speaking.this,MainActivity.class));
            }
        });

        getQues();
        //Toast.makeText(ReviewShow_Speaking.this, randomCode, Toast.LENGTH_SHORT).show();


        getRandomCode(new OnRandomCodeGeneratedListener() {
            @Override
            public void onRandomCodeGenerated(String randomCode) {

                getJudge();
                getAns();
                getPart3();

            }
        });


    }

    public interface OnRandomCodeGeneratedListener {
        void onRandomCodeGenerated(String randomCode);
    }

    public void getQues(){
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Speaking_" + topic)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();

                            if (querySnapshot != null) {
                                // 获取所有文档的列表
                                List<DocumentSnapshot> documents = querySnapshot.getDocuments();

                                // 检查是否至少有ques_num个文档
                                if (documents.size() >= Integer.parseInt(ques_num)) {
                                    // 获取第ques_num个文档的 question 字段的值
                                    DocumentSnapshot thirdDocument = documents.get(Integer.parseInt(ques_num)-1); // 第三个文档在列表中的索引是 ques_num-1
                                    String question = (String) thirdDocument.get("Question");
                                    //Toast.makeText(ReviewShow_Speaking.this, question, Toast.LENGTH_SHORT).show();
                                    String newQuestion = question.replace("\\n", "\n");

                                    Ques.setText(newQuestion);

                                } else {
                                    Toast.makeText(ReviewShow_Speaking.this, "少於n個資料", Toast.LENGTH_SHORT).show();

                                    Log.d("FirestoreData", "There are less than three documents in the collection");
                                }
                            } else {
                                Toast.makeText(ReviewShow_Speaking.this, "文件錯誤", Toast.LENGTH_SHORT).show();

                                Log.d("FirestoreData", "QuerySnapshot is null");
                            }
                        } else {
                            Toast.makeText(ReviewShow_Speaking.this, "題目資料庫不存在", Toast.LENGTH_SHORT).show();

                            // 处理错误
                            Log.e("FirestoreData", "Error getting documents: ", task.getException());
                        }
                    }
                });



    }

    public void getJudge(){
        getRandomCode(new OnRandomCodeGeneratedListener() {
            @Override
            public void onRandomCodeGenerated(String randomCode) {
                auth = FirebaseAuth.getInstance();
                databaseReference = FirebaseDatabase.getInstance().getReference();

                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();

                    DatabaseReference targetRef = databaseReference
                            .child("users")
                            .child(userId)
                            .child("Judge")
                            .child("Speaking");
                            //.child(randomCode);    //###選擇是第幾次回答####


                    targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                // 检查特定 key 的值
                                DataSnapshot valueSnapshot = dataSnapshot.child(randomCode);
                                if (valueSnapshot.exists()) {
                                    String value = valueSnapshot.getValue(String.class);
                                    Judge.setText(value);
                                    Log.d("FirebaseValue", "Value: " + value);
                                } else {
                                    Log.d("FirebaseValue", "Key not found");
                                    Toast.makeText(ReviewShow_Speaking.this, "randomCode找不到", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(ReviewShow_Speaking.this, "judge資料庫找不到", Toast.LENGTH_SHORT).show();

                                Log.d("FirebaseValue", "Path not found");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                }
            }
        });

    }

    public void getPart3Judge(){
        getRandomCode(new OnRandomCodeGeneratedListener() {
            @Override
            public void onRandomCodeGenerated(String randomCode) {
                auth = FirebaseAuth.getInstance();
                databaseReference = FirebaseDatabase.getInstance().getReference();

                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();

                    DatabaseReference targetRef = databaseReference
                            .child("users")
                            .child(userId)
                            .child("Judge")
                            .child("Speaking_Part3");
                    //.child(randomCode);    //###選擇是第幾次回答####


                    targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                // 检查特定 key 的值
                                DataSnapshot valueSnapshot = dataSnapshot.child(randomCode);
                                if (valueSnapshot.exists()) {
                                    String value = valueSnapshot.getValue(String.class);
                                    p3_judge.setText(value);
                                    Log.d("FirebaseValue", "Value: " + value);
                                } else {
                                    Log.d("FirebaseValue", "Key not found");
                                    Toast.makeText(ReviewShow_Speaking.this, "randomCode找不到", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(ReviewShow_Speaking.this, "judge資料庫找不到", Toast.LENGTH_SHORT).show();

                                Log.d("FirebaseValue", "Path not found");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                }
            }
        });

    }

    public void getRandomCode(final OnRandomCodeGeneratedListener listener) {
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference RandomCodeRef = databaseReference
                    .child("users")
                    .child(userId)
                    .child("Speaking")
                    .child(topic)
                    .child(ques_num);  //###选择是第几题####

            RandomCodeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // 检查数据是否有效
                    if (dataSnapshot.exists()) {
                        int count = 0;
                        String code = null;

                        // 遍历子项
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            //Toast.makeText(ReviewShow_Speaking.this, count+","+num, Toast.LENGTH_SHORT).show();

                            // 判断是否是第 "num" 个子项
                            if (String.valueOf(count).equals(num)) {
                                code = childSnapshot.getKey();
                                //Toast.makeText(ReviewShow_Speaking.this, "找到code", Toast.LENGTH_SHORT).show();

                                break;  // 获取第 "num" 个子项的 key 后即可退出循环
                            }
                            count++;  // 增加计数器
                        }
                        randomCode = code;

                        if(code!=null){

                           // Toast.makeText(ReviewShow_Speaking.this, randomCode, Toast.LENGTH_SHORT).show();

                            listener.onRandomCodeGenerated(randomCode);
                        }else Toast.makeText(ReviewShow_Speaking.this, "找不到randomcode", Toast.LENGTH_SHORT).show();




                    } else {
                        Toast.makeText(ReviewShow_Speaking.this, "数据库不存在", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // 读取数据时发生错误时调用此方法
                    Log.e("FirebaseKey", "Error occurred while reading data", databaseError.toException());
                }
            });
        }
    }


    public void getAns(){
        getRandomCode(new OnRandomCodeGeneratedListener() {
            @Override
            public void onRandomCodeGenerated(String randomCode) {
                auth = FirebaseAuth.getInstance();
                databaseReference = FirebaseDatabase.getInstance().getReference();

                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();



                    DatabaseReference targetRef = databaseReference
                            .child("users")
                            .child(userId)
                            .child("Speaking")
                            .child(topic)
                            .child(ques_num) ; //###選擇是第幾題####
                            //.child(randomCode);    //###選擇是第幾次回答####


                    targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                // 检查特定 key 的值
                                DataSnapshot valueSnapshot = dataSnapshot.child(randomCode);
                                if (valueSnapshot.exists()) {
                                    String value = valueSnapshot.getValue(String.class);
                                    Ans.setText(value);
                                    Log.d("FirebaseValue", "Value: " + value);
                                } else {
                                    Log.d("FirebaseValue", "Key not found");
                                    Toast.makeText(ReviewShow_Speaking.this, "randomCode找不到", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(ReviewShow_Speaking.this, "Ans資料庫找不到", Toast.LENGTH_SHORT).show();

                                Log.d("FirebaseValue", "Path not found");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
        }
            }
        });


    }


    public void getPart3(){
        getRandomCode(new OnRandomCodeGeneratedListener() {
            @Override
            public void onRandomCodeGenerated(String randomCode) {
                auth = FirebaseAuth.getInstance();
                databaseReference = FirebaseDatabase.getInstance().getReference();

                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();

                    DatabaseReference targetRef = databaseReference
                            .child("users")
                            .child(userId)
                            .child("Part3")
                            .child(randomCode);



                    targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                //Toast.makeText(ReviewShow_Speaking.this, "part3資料庫存在", Toast.LENGTH_SHORT).show();

                                part3.setVisibility(View.VISIBLE);
                                p3_question_title.setVisibility(View.VISIBLE);
                                p3_question.setVisibility(View.VISIBLE);
                                p3_ans_title.setVisibility(View.VISIBLE);
                                p3_answer.setVisibility(View.VISIBLE);
                                p3_judge_title.setVisibility(View.VISIBLE);
                                p3_judge.setVisibility(View.VISIBLE);
                                String question = dataSnapshot.child("Question").getValue(String.class);
                                String answer = dataSnapshot.child("Ans").getValue(String.class);
                                //Toast.makeText(ReviewShow_Speaking.this, question+","+answer+","+randomCode, Toast.LENGTH_SHORT).show();
                                p3_question.setText(question);
                                p3_answer.setText(answer);
                                getPart3Judge();



                            } else {
                                Toast.makeText(ReviewShow_Speaking.this, "Ans資料庫找不到", Toast.LENGTH_SHORT).show();

                                Log.d("FirebaseValue", "Path not found");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                }
            }
        });


    }
}