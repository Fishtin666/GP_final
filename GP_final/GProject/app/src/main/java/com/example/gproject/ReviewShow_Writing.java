package com.example.gproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ReviewShow_Writing extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    ImageView home,pic;
    TextView Ques,Ans,Judge;
    String randomCode;

    String Task_passIn;  //Task_passIn:傳入的參數

    //String task=Task_passIn;
    String topic;

    ImageButton back;
    String ques_num; //第幾題
    String num="0";  //第幾次回答
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_show_writing);
        Ques = findViewById(R.id.Question);
        Ans = findViewById(R.id.listenContent);
        Judge = findViewById(R.id.Judge);
        pic=findViewById(R.id.pic);

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReviewShow_Writing.this,MainActivity.class));
            }
        });


        back = findViewById(R.id.back2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(Speaking_part1_answer.this, Speaking_questionAdd.class));
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Task_passIn = bundle.getString("task");
            ques_num = bundle.getString("question");
            randomCode = bundle.getString("times");
        }

        if(Task_passIn.equals("1")){
            topic="Writing";
            getPic();
            //Toast.makeText(ReviewShow_Writing.this, "task1", Toast.LENGTH_SHORT).show();


        } else
            topic="Writing2";

//        getQues();

        getJudge();
        getAns();
//        getRandomCode(new ReviewShow_Speaking.OnRandomCodeGeneratedListener() {
//            @Override
//            public void onRandomCodeGenerated(String randomCode) {
//
//
//
//            }
//        });

    }

//    public interface OnRandomCodeGeneratedListener {
//        void onRandomCodeGenerated(String randomCode);
//    }

    public void getQues(){
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(topic)
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
                                    Ques.setText(question);

                                } else {
                                    Toast.makeText(ReviewShow_Writing.this, "少於n個資料", Toast.LENGTH_SHORT).show();

                                    Log.d("FirestoreData", "There are less than three documents in the collection");
                                }
                            } else {
                                Toast.makeText(ReviewShow_Writing.this, "文件錯誤", Toast.LENGTH_SHORT).show();

                                Log.d("FirestoreData", "QuerySnapshot is null");
                            }
                        } else {
                            Toast.makeText(ReviewShow_Writing.this, "題目資料庫不存在", Toast.LENGTH_SHORT).show();

                            // 处理错误
                            Log.e("FirestoreData", "Error getting documents: ", task.getException());
                        }
                    }
                });



    }

    public void getPic(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collection = db.collection("Writing");
        DocumentReference doc = collection.document(ques_num);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                StorageReference storageRef;
                if (documentSnapshot.exists()) {
                    String ques = documentSnapshot.getString("Question");
                    Ques.setText(ques);
                    storageRef= FirebaseStorage.getInstance().getReference("Writing_Part1/"+ques_num+".png");
                    try {
                        File localfile =File.createTempFile("tempfile",".png");
                        storageRef.getFile(localfile)
                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                        pic.setImageBitmap(bitmap);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ReviewShow_Writing.this, "圖片讀取失敗", Toast.LENGTH_SHORT).show();


                                    }
                                });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                } else {
                    Toast.makeText(ReviewShow_Writing.this, "資料庫不存在", Toast.LENGTH_SHORT).show();


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ReviewShow_Writing.this, "db連接失敗", Toast.LENGTH_SHORT).show();
            }
        });

    }

//    public void getRandomCode(final ReviewShow_Speaking.OnRandomCodeGeneratedListener listener) {
//        auth = FirebaseAuth.getInstance();
//        databaseReference = FirebaseDatabase.getInstance().getReference();
//
//        FirebaseUser currentUser = auth.getCurrentUser();
//        if (currentUser != null) {
//            String userId = currentUser.getUid();
//
//            DatabaseReference RandomCodeRef = databaseReference
//                    .child("users")
//                    .child(userId)
//                    .child("Writing")
//                    .child(Task_passIn)
//                    .child(ques_num);  //###选择是第几题####
//
//            RandomCodeRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    // 检查数据是否有效
//                    if (dataSnapshot.exists()) {
//                        int count = 0;
//                        String code = null;
//
//                        // 遍历子项
//                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//                            //Toast.makeText(ReviewShow_Speaking.this, count+","+num, Toast.LENGTH_SHORT).show();
//
//                            // 判断是否是第 "num" 个子项
//                            if (String.valueOf(count).equals(num)) {
//                                code = childSnapshot.getKey();
//                                //Toast.makeText(ReviewShow_Speaking.this, "找到code", Toast.LENGTH_SHORT).show();
//
//                                break;  // 获取第 "num" 个子项的 key 后即可退出循环
//                            }
//                            count++;  // 增加计数器
//                        }
//                        randomCode = code;
//
//                        if(code!=null){
//
//                            // Toast.makeText(ReviewShow_Speaking.this, randomCode, Toast.LENGTH_SHORT).show();
//
//                            listener.onRandomCodeGenerated(randomCode);
//                        }else Toast.makeText(ReviewShow_Writing.this, "找不到randomcode", Toast.LENGTH_SHORT).show();
//
//
//
//
//                    } else {
//                        Toast.makeText(ReviewShow_Writing.this, "数据库不存在", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    // 读取数据时发生错误时调用此方法
//                    Log.e("FirebaseKey", "Error occurred while reading data", databaseError.toException());
//                }
//            });
//        }
//    }

    public void getAns(){

                auth = FirebaseAuth.getInstance();
                databaseReference = FirebaseDatabase.getInstance().getReference();

                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();

                    DatabaseReference targetRef = databaseReference
                            .child("users")
                            .child(userId)
                            .child("Writing")
                            .child(Task_passIn)
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
                                    Toast.makeText(ReviewShow_Writing.this, "randomCode找不到", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(ReviewShow_Writing.this, "Ans資料庫找不到", Toast.LENGTH_SHORT).show();

                                Log.d("FirebaseValue", "Path not found");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                }

    }

    public void getJudge(){

                auth = FirebaseAuth.getInstance();
                databaseReference = FirebaseDatabase.getInstance().getReference();

                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();

                    DatabaseReference targetRef = databaseReference
                            .child("users")
                            .child(userId)
                            .child("Judge")
                            .child("Writing");
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
                                    Toast.makeText(ReviewShow_Writing.this, "randomCode找不到", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(ReviewShow_Writing.this, "judge資料庫找不到", Toast.LENGTH_SHORT).show();

                                Log.d("FirebaseValue", "Path not found");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                }
            }

}



