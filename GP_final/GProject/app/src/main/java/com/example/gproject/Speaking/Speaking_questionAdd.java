package com.example.gproject.Speaking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gproject.Adapters.QuestionAdapter;
import com.example.gproject.Models.QuestionModel;
import com.example.gproject.R;
import com.example.gproject.Writing.Writing_T2answer1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Speaking_questionAdd extends AppCompatActivity {

    QuestionAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<QuestionModel>list;

    FirebaseAuth auth;
    String[] dataArray;
    ImageButton back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speaking_question_add);
        auth = FirebaseAuth.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        list = new ArrayList<>();
        recyclerView = findViewById(R.id.questionRecy);
        back = findViewById(R.id.back2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Speaking_questionAdd.this, Speaking_part1.class));
            }
        });


        String topic=getIntent().getStringExtra("topic");

        LinearLayoutManager layoutManager =new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new QuestionAdapter(this,list);
        recyclerView.setAdapter(adapter);

        //ArrayList<String> documentDataList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Speaking_"+topic)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String question = document.getString("Question").replace("\\n", "\n");
                                String num = document.getString("num");
                                if (question != null) {
                                    if (num == null)
                                        list.add(new QuestionModel(question, 1,topic)); // 在这里添加 QuestionModel 到列表中
                                    else
                                        list.add(new QuestionModel(question, 2,topic));
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            // 处理错误
                        }
                    }
                });



        }


}