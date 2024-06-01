package com.example.gproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.example.gproject.Adapters.QuestionNumberAdapter;
import com.example.gproject.Models.QuestionNumberModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class QuesNumAdd extends AppCompatActivity {


    ArrayList<QuestionNumberModel> list;
    QuestionNumberAdapter adapter;
    RecyclerView recyclerView;

    private FirebaseFirestore db;
    int num;
    String task;

    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ques_num_add);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        recyclerView = findViewById(R.id.QuesNum_Recy);

        list =new ArrayList<>();

        LinearLayoutManager manager =new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        task=getIntent().getStringExtra("task");

        db = FirebaseFirestore.getInstance();
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(QuesNumAdd.this, Writing_T1answer1.class));
                finish();
            }
        });


        if(task.equals("1")){
            getDocNum("Writing");

        } else if (task.equals("2")) {
            getDocNum("Writing2");
//            for(int i=1;i<13;i++)
//                list.add(new QuestionNumberModel("Question"+i,2));
        }




        adapter = new QuestionNumberAdapter(this,list);
        recyclerView.setAdapter(adapter);


    }

    public void getDocNum(String collectionName){
        db.collection(collectionName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int documentCount = queryDocumentSnapshots.size();
                        for(int i=1;i<=documentCount;i++)
                            list.add(new QuestionNumberModel("Question"+i,Integer.parseInt(task),false));

                        adapter.notifyDataSetChanged();
                    }
                });

    }
}