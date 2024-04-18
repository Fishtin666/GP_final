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

public class R_chose extends AppCompatActivity {
    private static final String TAG = "R_chose";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r_chose);

        int number = getIntent().getIntExtra("ChoseNumber", 0);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentRef = db.collection("R_chose").document(String.valueOf(number));
        // 假設您要抓取的欄位數量是3
        int numberOfFields = 3;

        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(R_chose.this, R_topic.class);
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
                scrollView.fullScroll(View.FOCUS_UP); // 将滚动位置移动到top
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
                            //q
                            //options
                            if (document.exists()) {

                                String content = document.getString("content");
                                TextView ContentTextView = findViewById(R.id.Content);
                                ContentTextView.setText(content);
                                // build a StringBuilder array to save
                                StringBuilder[] resultBuilders = new StringBuilder[numberOfFields];
                                StringBuilder[] resultBuilders2 = new StringBuilder[numberOfFields];
                                for (int i = 0; i < numberOfFields; i++) {
                                    resultBuilders[i] = new StringBuilder();
                                    resultBuilders2[i] = new StringBuilder();
                                }

                                for (int i = 0; i < numberOfFields; i++) {

                                    String fieldName = "Q" + (i + 1);
                                    String fieldData = document.getString(fieldName);

                                    String optName = "opt" + (i + 1);
                                    String optData = document.getString(optName);

                                    String ansName = "A" + (i + 1);

                                    int questionId = getResources().getIdentifier(fieldName, "id", getPackageName());
                                    int optionId = getResources().getIdentifier(optName, "id", getPackageName());
                                    int ansId = getResources().getIdentifier(ansName, "id", getPackageName());
                                    if (document.contains(fieldName) && document.contains(optName)) {
                                        if (optData != null) {
                                            String[] splitQue = fieldData.split("\\.");
                                            resultBuilders2[i] = new StringBuilder();
                                            String[] splitData = optData.split("\\.");
                                            resultBuilders[i] = new StringBuilder();
                                            for (int k = 0; k < splitQue.length; k++) {
                                                resultBuilders2[i].append(splitQue[k].trim()).append(".\n");
                                            }
                                            for (int j = 0; j < splitData.length; j++) {
                                                resultBuilders[i].append(splitData[j].trim()).append(".\n");
                                            }
                                            displayQuestion(fieldName, resultBuilders2[i].toString());
                                            displayQuestion(optName, resultBuilders[i].toString());
                                        }
                                    } else {
                                        findViewById(questionId).setVisibility(View.GONE);
                                        findViewById(optionId).setVisibility(View.GONE);
                                        findViewById(ansId).setVisibility(View.GONE);
                                    }
                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.e(TAG, "Error getting document", task.getException());
                            Toast.makeText(R_chose.this, "failed", Toast.LENGTH_SHORT).show();
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
            case "opt1":
                optTextView = findViewById(R.id.opt1);
                break;
            case "opt2":
                optTextView = findViewById(R.id.opt2);
                break;
            case "opt3":
                optTextView = findViewById(R.id.opt3);
                break;
        }
        optTextView.setText(que);
    }
}
