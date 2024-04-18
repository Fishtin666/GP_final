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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class R_match extends AppCompatActivity {
    private static final String TAG = "R_match";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r_match);

        int number = getIntent().getIntExtra("ChoseNumber", 0);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentRef = db.collection("R_match").document(String.valueOf(number));
        // 假設要抓取的欄位數量
        int numberOfFields = 5;

        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(R_match.this, R_topic.class);
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
                                        Log.e("mattttt", firestoreValue);
                                        Log.e("mattttt2", editTextValue);
                                        // compare the value of EditText and Firestore's colum
                                        if (!editTextValue.equals(firestoreValue)) {
                                            //mark incorrect answer
                                            editText.setTextColor(Color.RED);
                                        }
                                    }
                                }
                            } else {
                                Log.d(TAG, "No such document");
                                // Firestore 中不存在文档时，您可以在此处进行其他操作，比如显示一条消息
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
            }
        });

        ScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_UP);
            }
        });
        scrollView.fullScroll(ScrollView.FOCUS_UP);

        //set value
        documentRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            if (document.exists()) {
                                //set Content
                                String content = document.getString("content");
                                String[] splitCont = content.split("。");
                                StringBuilder combinedContent = new StringBuilder();
                                for (String line : splitCont) {
                                    combinedContent.append(line).append("\n");
                                }
                                TextView ContentTextView = findViewById(R.id.Content);
                                ContentTextView.setText(combinedContent.toString());
                                //set section
                                String section1 = document.getString("section1");
                                String[] splitSec = section1.split("\\.");
                                StringBuilder combinedSec = new StringBuilder();
                                for (String line : splitSec) {
                                    combinedSec.append(line).append("\n");
                                }
                                TextView SecTextView = findViewById(R.id.section1);
                                SecTextView.setText(combinedSec.toString());
                                //set match
                                String match = document.getString("match");
                                String[] splitMatch = match.split("\\.");
                                StringBuilder combinedMatch = new StringBuilder();
                                for (String line : splitMatch) {
                                    combinedMatch.append(line).append(".\n");
                                }
                                TextView MatchTextView = findViewById(R.id.match);
                                MatchTextView.setText(combinedMatch.toString());

                                // build a StringBuilder array to save
                                StringBuilder[] resultBuilders = new StringBuilder[numberOfFields];
                                StringBuilder[] resultBuilders2 = new StringBuilder[numberOfFields];
                                for (int i = 0; i < numberOfFields; i++) {
                                    resultBuilders[i] = new StringBuilder();
                                    resultBuilders2[i] = new StringBuilder();
                                }

                                for (int i = 0; i < numberOfFields; i++) {

                                    String titleName = "title" + (i + 1);
                                    String fieldName = "Q" + (i + 1);
                                    Log.d("QQQQQ", fieldName);
                                    try {
                                        // check whether Q is exit
                                        if (document.contains(fieldName)) {
                                            String fieldData = document.getString(fieldName);
                                            String[] splitField = fieldData.split("\\.");
                                            StringBuilder combinedField = new StringBuilder();
                                            for (String line : splitField) {
                                                combinedField.append(line.trim()).append(".\n");
                                            }
                                            Log.d("QQ2", fieldName);
                                            Log.d("QQ2", combinedField.toString());
                                            displayQuestion(fieldName, combinedField.toString());
                                        } else {
                                            int questionId = getResources().getIdentifier(fieldName, "id", getPackageName());
                                            findViewById(questionId).setVisibility(View.GONE);
                                            String ansName = "A" + (i + 1);
                                            int ansId = getResources().getIdentifier(ansName, "id", getPackageName());
                                            findViewById(ansId).setVisibility(View.GONE);
                                        }

                                        // check whether title is exit
                                        if (document.contains(titleName)) {
                                            String titleData = document.getString(titleName);
                                            String[] splitTitle = titleData.split("\\.");
                                            StringBuilder combinedTitle = new StringBuilder();
                                            for (String line : splitTitle) {
                                                combinedTitle.append(line.trim()).append(".\n");
                                            }
                                            displayTitle(titleName, combinedTitle.toString());
                                        } else {
                                            int titleId = getResources().getIdentifier(titleName, "id", getPackageName());
                                            findViewById(titleId).setVisibility(View.GONE);
                                        }
                                    } catch (Exception e) {

                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.e(TAG, "Error getting document", task.getException());
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
            case "Q5":
                optTextView = findViewById(R.id.Q5);
                break;
        }
        if (optTextView != null) {
            optTextView.setText(que);
        } else {
            Log.e(TAG, "OptTextView is null for cul: " + cul);
        }
    }

    private void displayTitle(String cul, String que) {
        TextView titleTextview = null;
        switch (cul) {
            case "title1":
                titleTextview = findViewById(R.id.title1);
                break;
            case "title2":
                titleTextview = findViewById(R.id.title2);
                break;
            default:
                Toast.makeText(R_match.this, cul, Toast.LENGTH_SHORT).show();

                break;
        }
        if (titleTextview != null) {
            titleTextview.setText(que);
        } else {
            Log.e(TAG, "OptTextView is null for cul: " + cul);
        }
    }
}

