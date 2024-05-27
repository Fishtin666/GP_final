package com.example.gproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class profile extends AppCompatActivity {
    Button logout;
    ImageButton back;
    TextView email,Uid,w_done,l_done,s_done,r_done,voc;
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
        l_done = findViewById(R.id.listening_done);
        s_done = findViewById(R.id.speaking_done);
        r_done = findViewById(R.id.reading_done);
        voc = findViewById(R.id.voc);



        logout = findViewById(R.id.logOut);
        email = findViewById(R.id.email);
        w_done= findViewById(R.id.writing_done);



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(profile.this, login.class));
            }
        });

        back = findViewById(R.id.back);

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
            email.setText(userEmail);

        }



        db = FirebaseFirestore.getInstance();
        getWordLevel();


        String[] w_topic={"Writing","Writing2"};
        for(int i=0;i<w_topic.length;i++){
            getDocNum(w_topic[i],new profile.OnRandomCodeGeneratedListener() {
                @Override
                public void onRandomCodeGenerated(int w_num, int s_num, int r_num, int l_num) {
                    getRealNum("Writing","0");
                }


            });
        }


        String[] s_topic = {"Speaking_Study","Speaking_Work","Speaking_Hometown","Speaking_Accommodation",
        "Speaking_Family","Speaking_Friend","Speaking_Entertainment","Speaking_Childhood"
        ,"Speaking_Daily life","Speaking_People","Speaking_Place","Speaking_Item","Speaking_Experience"};
        for(int i=0;i<s_topic.length;i++){
            getDocNum(s_topic[i],new profile.OnRandomCodeGeneratedListener() {
                @Override
                public void onRandomCodeGenerated(int w_num, int s_num, int r_num, int l_num) {
                    getRealNum("Speaking","1");
                }


            });
        }

        String[] r_topic ={"R_blank","R_chose", "R_judge", "R_match", "R_word,", "R_wordA", "R_wordB", "R_wordC"};
        for(int i=0;i<r_topic.length;i++){
            getDocNum(r_topic[i],new profile.OnRandomCodeGeneratedListener() {
                @Override
                public void onRandomCodeGenerated(int w_num, int s_num, int r_num, int l_num) {
                    getRealNum2("Reading");
                }


            });
        }

        getRealNum3();



//        for(int i=0;i<topic.length;i++){
//            getDocNum(topic[i]);
//        }
//
//        String[] topic_r ={"R_blank","R_chose", "R_judge", "R_match", "R_word,", "R_wordA", "R_wordB", "R_wordC"};
//        for(int i=0;i<topic_r.length;i++){
//            getDocNum(topic[i]);
//
//        }Toast.makeText(profile.this, r_num, Toast.LENGTH_SHORT).show();

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                getRealNum("Writing","0");
//                getRealNum("Speaking","1");
//                //getRealNum("Listening","2");
////                for(int i=0;i< topic.length;i++)
////                    getRealNum2(topic_r[i]);
//            }
//        }, 3000); //




    }

    public interface OnRandomCodeGeneratedListener {
        void onRandomCodeGenerated(int w_num,int s_num,int r_num,int l_num);
    }


    public void getDocNum(String collectionName,final profile.OnRandomCodeGeneratedListener listener){
        db.collection(collectionName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        switch (collectionName){
                            case "Writing":
                            case "Writing2":
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
                                s_num+=queryDocumentSnapshots.size();
                                break;
                            case "R_blank":
                            case "R_chose":
                            case "R_judge":
                            case "R_match":
                            case "R_word":
                                r_num+=queryDocumentSnapshots.size();
                                break;
                            case "Listen_1":
                            case "Listen_2":
                            case "Listen_3":
                            case "Listen_4":
                                l_num+=queryDocumentSnapshots.size();
                                break;

                        }
                        listener.onRandomCodeGenerated(w_num,s_num,r_num,l_num);
                        //documentCount = queryDocumentSnapshots.size();
                        //System.out.println("數量:"+String.valueOf(documentCount));
                    }
                });
    }

    public void getRealNum(String part,String partCode){
        getDocNum(part,new OnRandomCodeGeneratedListener() {
            @Override
            public void onRandomCodeGenerated(int w_num, int s_num, int r_num, int l_num) {
                // 获取当前用户的 ID
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();

                    // 构建路径以获取特定问题的答案数量
                    String answersPath = "users/" + userId +"/"+part;

                    // 添加 ValueEventListener 监听器以获取数据快照
                    databaseReference.child(answersPath).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // 获取子节点数量
                            answerCount = dataSnapshot.getChildrenCount();

                            switch (partCode){
                                case "0": //Writing
                                    w_done.setText(String.valueOf(answerCount)+"/"+w_num);
                                    progress=answerCount*100/w_num;
                                    w_progressBar.setProgress((int)progress);
                                    break;
                                case "1": //Speaking
                                    s_done.setText((answerCount)+"/"+s_num);
                                    progress=answerCount*100/s_num;
                                    s_progressBar.setProgress((int)progress);
                                    break;
                                case "2" ://Listening
                                    l_done.setText((answerCount)+"/"+l_num);
                                    progress=answerCount*100/l_num;
                                    l_progressBar.setProgress((int)progress);

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // 处理取消监听事件
                        }
                    });
                }
            }
        });
    }



    public void getRealNum2(String part){
        getDocNum(part,new OnRandomCodeGeneratedListener() {
            @Override
            public void onRandomCodeGenerated(int w_num, int s_num, int r_num, int l_num) {
                // 获取当前用户的 ID
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();

                    // 构建路径以获取特定问题的答案数量
                    String answersPath = "R_Review/" + userId ;//+"/"+part

                    // 添加 ValueEventListener 监听器以获取数据快照
                    databaseReference.child(answersPath).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // 获取子节点数量
                                answerCount = dataSnapshot.getChildrenCount();
                                r_done.setText(answerCount + "/" + r_num);
                                progress = answerCount * 100 / r_num;
                                r_progressBar.setProgress((int) progress);
                            } else {
                                Toast.makeText(profile.this, "資料庫不存在", Toast.LENGTH_SHORT).show();

                                // 路径不存在时的处理逻辑，例如显示错误消息或日志记录
                                Log.d("Firebase", "Path does not exist: " + answersPath);
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // 处理取消监听事件
                        }
                    });
                }
            }
        });

    }

    public void getRealNum3(){
        // 获取当前用户的 ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // 构建路径以获取特定问题的答案数量
            String answersPath = "R_Review/" + userId ;//+"/"+part

            // 添加 ValueEventListener 监听器以获取数据快照
            databaseReference.child(answersPath).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // 获取子节点数量
                        answerCount = dataSnapshot.getChildrenCount();
                        l_done.setText(answerCount + "/" + 4);
                        progress = answerCount * 100 / 4;
                        l_progressBar.setProgress((int) progress);
                    } else {
                        Toast.makeText(profile.this, "資料庫不存在", Toast.LENGTH_SHORT).show();

                        // 路径不存在时的处理逻辑，例如显示错误消息或日志记录
                        Log.d("Firebase", "Path does not exist: " + answersPath);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 处理取消监听事件
                }
            });
        }



    }

    public void getWordLevel(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference targetRef = databaseReference
                    .child("word_Level")
                    .child(userId);
            //String answersPath = "word_Level/" + userId;

            targetRef.orderByKey().equalTo("WordLevel").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {


                            Object value = childSnapshot.getValue();
                            String word_level=String.valueOf(value);
                            voc.setText(word_level);
                            if(word_level.equals("A"))
                                voc_progressBar.setProgress(33);
                            else if (word_level.equals("B"))
                                voc_progressBar.setProgress(66);
                            else
                                voc_progressBar.setProgress(100);




                        }
                    } else {
                        Toast.makeText(profile.this, "未搜尋到db", Toast.LENGTH_SHORT).show();


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });

        }
        }
}

