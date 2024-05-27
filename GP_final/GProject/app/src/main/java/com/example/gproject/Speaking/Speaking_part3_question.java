package com.example.gproject.Speaking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.gproject.Adapters.QuestionAdapter;
import com.example.gproject.Models.QuestionModel;
import com.example.gproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Speaking_part3_question extends AppCompatActivity {

    QuestionAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<QuestionModel> list;
    FirebaseAuth auth;

    String Num,Topic,key;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speaking_part3_question);
        auth = FirebaseAuth.getInstance();
        back=findViewById(R.id.back);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Num =extras.getString("num");
            Topic =extras.getString("topic");


        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Toast.makeText(Speaking_part3_question.this, Num, Toast.LENGTH_SHORT).show();

        list = new ArrayList<>();
        recyclerView = findViewById(R.id.recy_P3_question);

        LinearLayoutManager layoutManager =new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new QuestionAdapter(this,list);
        recyclerView.setAdapter(adapter);



        getQuestionsFromFirestore(Num);


        }

    private void getQuestionsFromFirestore(String num) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference speakingPeopleCollection = db.collection("Speaking_"+Topic);
        DocumentReference speakingPeopleDocument = speakingPeopleCollection.document(num);
        CollectionReference q3Collection = speakingPeopleDocument.collection("Q3");

        q3Collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String question = document.getString("Question");
                        // 在這裡處理問題值
                        list.add(new QuestionModel(question, 1,Topic));
                        Log.d("TAG", "資料庫存在: ");

                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("TAG", "資料庫不存在: ", task.getException());
                }
            }
        });
    }

}