package com.example.gproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.gproject.Models.QuestionNumberModel;
import com.example.gproject.login.login;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.StringValue;

import java.lang.reflect.Array;

public class profile extends AppCompatActivity {
    Button logout,jump;
    ImageButton back;
    TextView email,Uid,w_done;
    private ProgressBar w_progressBar,s_progressBar,l_progressBar,r_progressBar,voc_progressBar;

    int documentCount=0;
    int w_num,s_num,r_num,l_num;
    long answerCount,progress;
    private DatabaseReference databaseReference;

    private FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        w_progressBar = findViewById(R.id.progressBar);
        s_progressBar =findViewById(R.id.progressBar2);
        l_progressBar=findViewById(R.id.progressBar4);
        r_progressBar = findViewById(R.id.progressBar5);
        voc_progressBar =findViewById(R.id.progressBar6);

        jump = findViewById(R.id.button6);
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(profile.this, getDb.class));
            }
        });

        logout = findViewById(R.id.logOut);
        email = findViewById(R.id.email);
        w_done= findViewById(R.id.writing_done);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(profile.this, login.class));
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

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            String uid = currentUser.getUid();
            email.setText(userEmail);

        }

        db = FirebaseFirestore.getInstance();
        //getDocNum("Writing");
        //getDocNum("Writing2");

        String[] topic = {"Speaking_Study","Speaking_Work","Speaking_Hometown","Speaking_Accommodation",
        "Speaking_Family","Speaking_Friend","Speaking_Entertainment","Speaking_Childhood"
        ,"Speaking_Daily life","Speaking_People","Speaking_Place","Speaking_Item","Speaking_Experience"};

        for(int i=0;i<topic.length;i++){
            //getDocNum(topic[i]);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //getRealNum("Writing");
                //getRealNum("Speaking");
            }
        }, 3000); //




    }
    public void getDocNum(String collectionName){
        db.collection(collectionName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        switch (collectionName){
                            case "Writing":
                            case  "Writing2":
                                w_num+=queryDocumentSnapshots.size();
                                break;
                            case  "Speaking_Study":
                            case  "Speaking_Work":
                            case  "Speaking_Hometown":
                            case  "Speaking_Accommodation":
                            case  "Speaking_Family":
                            case  "Speaking_Friend":
                            case  "Speaking_Entertainment":
                            case  "Speaking_Childhood":
                            case  "Speaking_Daily life":
                            case  "Speaking_People":
                            case  "Speaking_Place":
                            case  "Speaking_Item":
                            case  "Speaking_Experience":
                                l_num+=queryDocumentSnapshots.size();
                                break;
                        }
                        //documentCount = queryDocumentSnapshots.size();
                        //System.out.println("數量:"+String.valueOf(documentCount));

                    }
                });


        //w_done.setText(String.valueOf(answerCount*100/w_num)+"% "+answerCount+","+" "+w_num);

    }

    public void getRealNum(String doc){
        // 获取当前用户的 ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();


            // 构建路径以获取特定问题的答案数量
            String answersPath = "users/" + userId +"/"+doc ;

            // 添加 ValueEventListener 监听器以获取数据快照
            databaseReference.child(answersPath).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // 获取子节点数量
                    answerCount = dataSnapshot.getChildrenCount();
                    w_done.setText(String.valueOf(answerCount)+"/"+w_num);

                    progress=answerCount*100/w_num;
                    // 设置 ProgressBar 的进度
                    w_progressBar.setProgress((int)progress);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 处理取消监听事件
                }
            });
        }
    }

}