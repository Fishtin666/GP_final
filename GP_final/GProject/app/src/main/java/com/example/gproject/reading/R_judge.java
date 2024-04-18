package com.example.gproject.reading;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class R_judge extends AppCompatActivity {
    private static final String TAG = "R_judge"; // 用你的类名替代 YourClassName

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r_judge);

        int number = getIntent().getIntExtra("ChoseNumber", 0);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentRef = db.collection("R_judge").document(String.valueOf(number));
        // 假設要抓取的欄位數量
        int numberOfFields = 5;

        ScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN); // 将滚动位置移动到底部
            }
        });
        scrollView.fullScroll(ScrollView.FOCUS_UP);

        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(R_judge.this, R_topic.class);
                startActivity(intent);
                finish();
            }
        });

        Button send = findViewById(R.id.sendAns);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                for (int i = 0; i < numberOfFields; i++) {
                                    String ansName = "A" + (i + 1);
                                    int ansId = getResources().getIdentifier(ansName, "id", getPackageName());

                                    if (document.contains(ansName)) {
                                        EditText editText = findViewById(ansId);
                                        String editTextValue = editText.getText().toString().trim();

                                        //get Firestore's ans colum
                                        String firestoreValue = document.getString(ansName);
                                        Log.e("mattttt",firestoreValue );
                                        Log.e("mattttt2",editTextValue );
                                        // compare the value of EditText and Firestore's colum
                                        if (!editTextValue.equals(firestoreValue)) {
                                            //mark incorrect answer
                                            editText.setTextColor(Color.RED);
                                        }
                                    }
                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

            }
        });
        //set value
        documentRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        try {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    //set Content
                                    String content = document.getString("content");
                                    TextView ContentTextView = findViewById(R.id.Content);
                                    ContentTextView.setText(content.toString());
                                    //set title
                                    String title = document.getString("title");
                                    TextView titleTextview = findViewById(R.id.articleTitle);
                                    titleTextview.setText(title.toString());
                                } else {
                                    Log.d("judge", "No such document");
                                }
                                //set match
                                String match = document.getString("match");
                                String[] splitMatch = match.split("\\.");
                                StringBuilder combinedMatch = new StringBuilder();
                                for (String line : splitMatch) {
                                    combinedMatch.append(line).append(".\n");
                                }
                                TextView MatchTextView = findViewById(R.id.match);
                                MatchTextView.setText(combinedMatch.toString());

                                int numberOfFields = 4;
                                StringBuilder[] resultBuilders = new StringBuilder[numberOfFields];
                                for (int i = 0; i < numberOfFields; i++) {
                                    resultBuilders[i] = new StringBuilder();
                                }
                                for (int i = 0; i < numberOfFields; i++) {
                                    String fieldName = "Q" + (i + 1);
                                    try {
                                        // check whether Q is exit
                                        if (document.contains(fieldName)) {
                                            String fieldData = document.getString(fieldName);
                                            String[] splitField = fieldData.split("\\.");
                                            StringBuilder combinedField = new StringBuilder();
                                            for (String line : splitField) {
                                                combinedField.append(line.trim()).append(".\n");
                                            }
                                            Log.d("Qjudge", fieldName);
                                            Log.d("Qjudge", combinedField.toString());
                                            displayQuestion(fieldName, combinedField.toString());
                                        } else {
                                            // hide the view
                                            int questionId = getResources().getIdentifier(fieldName, "id", getPackageName());
                                            findViewById(questionId).setVisibility(View.GONE);
                                            String ansName = "A" + (i + 1);
                                            int ansId = getResources().getIdentifier(ansName, "id", getPackageName());
                                            findViewById(ansId).setVisibility(View.GONE);
                                        }
                                    } catch (Exception e) {
                                        Log.e("judge1", "Failed with error: " + e.getMessage());
                                    }
                                }
                            } else {
                                Log.e(TAG, "Error getting document", task.getException());
                            }
                        } catch (Exception e) {
                            Log.e("judge", "Failed with error: " + e.getMessage());
                        }
                    }
                });
    }

    private void displayQuestion(String cul, String que) {
        TextView optTextView = null;
        switch (cul) {
            case "Q1":
                optTextView = findViewById(R.id.Q1);
                break;
            case "Q2":
                optTextView = findViewById(R.id.Q2);
                break;
            case "Q3":
                optTextView = findViewById(R.id.Q3);
                break;
            case "Q4":
                optTextView = findViewById(R.id.Q4);
                break;
        }
        if (optTextView != null) {
            optTextView.setText(que);
        } else {
            Log.e(TAG, "OptTextView is null for cul: " + cul);
        }
    }
}
