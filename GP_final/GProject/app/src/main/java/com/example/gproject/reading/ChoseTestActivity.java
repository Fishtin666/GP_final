package com.example.gproject.reading;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gproject.Adapters.ChosReadingAdapter;
import com.example.gproject.Models.QuestionNumberModel;
import com.example.gproject.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ChoseTestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChosReadingAdapter adapter;
    private ArrayList<QuestionNumberModel> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.pra_word);

            Button send = findViewById(R.id.wordSend);
            send.setVisibility(View.GONE);

            TextView testWord = findViewById(R.id.testWord);
            testWord.setVisibility(View.GONE);

            ImageView imageView = findViewById(R.id.imageView3);
            imageView.setVisibility(View.GONE);

            SharedPreferences sharedPreferences = getSharedPreferences("R_topic", MODE_PRIVATE);
            int R_Log = sharedPreferences.getInt("R_topic", 0);

            ImageButton backButton = findViewById(R.id.back);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ChoseTestActivity.this, R_topic.class);
                    startActivity(intent);
                    finish();
                }
            });

            recyclerView = findViewById(R.id.rcyQ);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            questionList = new ArrayList<>();
            adapter = new ChosReadingAdapter(this, R_Log);
            recyclerView.setAdapter(adapter);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            int R_number = getIntent().getIntExtra("R_topic", 0);
//        getQuestionList(db, R_number);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("WordListActivity", "Failed with error: " + e.getMessage());
        }
    }


    private void getQuestionList(FirebaseFirestore db, int R_number) {
        CollectionReference collection = db.collection("Reading");
        collection.document(String.valueOf(R_number)).collection("Questions")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            QuestionNumberModel model = document.toObject(QuestionNumberModel.class);
                            questionList.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ChoseTestActivity.this, "No questions found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error getting documents: " + e.getMessage()));
    }

    //set chose to next activity
    private String ChoseTestNum(int R_number, int anotherNumber) {
        Intent intent = null;
        String part = null;
        switch (R_number) {
            case 1:
                intent = new Intent(this, R_blank.class);
                break;
            case 2:
                intent = new Intent(this, R_judge.class);
                break;
            case 3:
                intent = new Intent(this, R_chose.class);
                break;
            case 4:
                intent = new Intent(this, R_match.class);
                break;
            case 5:
                intent = new Intent(this, R_multiple.class);
                break;
            default:
                Log.e("chose error", "Invalid number2: " + R_number);

        }
        intent.putExtra("ChoseNumber", anotherNumber);
        startActivity(intent);
        finish();
        return part;
    }
}

