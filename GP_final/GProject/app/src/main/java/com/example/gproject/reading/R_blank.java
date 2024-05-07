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

public class R_blank extends AppCompatActivity {
    private static final String TAG = "R_blank";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r_blank);

        int number = getIntent().getIntExtra("ChoseNumber", 0);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentRef = db.collection("R_blank").document(String.valueOf(number));
        int numberOfFields = 5;

        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(R_blank.this, R_topic.class);
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
                                        Log.e("blank", firestoreValue);
                                        Log.e("blank", editTextValue);
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

        try {
            scrollView.fullScroll(ScrollView.FOCUS_UP);
            //set value
            documentRef.get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    try {
                                        //set section
                                        String section1 = document.getString("section1");
                                        if (section1 != null) {
                                            String[] splitSec = section1.split("\\.");
                                            StringBuilder combinedSec = new StringBuilder();
                                            for (String line : splitSec) {
                                                combinedSec.append(line).append("\n");
                                            }
                                            TextView SecTextView = findViewById(R.id.section1);
                                            SecTextView.setText(combinedSec.toString());
                                        } else {
                                            findViewById(R.id.section1).setVisibility(View.GONE);
                                        }
                                    } catch (Exception e) {
                                        Log.e("blank0", "Failed with error: " + e.getMessage());
                                    }

                                    StringBuilder[] resultBuilders = new StringBuilder[numberOfFields];
                                    for (int i = 0; i < numberOfFields; i++) {
                                        resultBuilders[i] = new StringBuilder();
                                    }
                                    for (int i = 0; i < numberOfFields; i++) {
                                        String ansName = "A" + (i + 1);
                                        String optName = "option" + (i + 1);
                                        try {
                                            // check whether A is exit
                                            if (document.contains(ansName)) {
                                                //set content
                                                String content = document.getString("content");
                                                String Q1 = document.getString("Q1");
                                                displayArticle(content, Q1);
                                            } else {
                                                int questionId = getResources().getIdentifier(optName, "id", getPackageName());
                                                findViewById(questionId).setVisibility(View.GONE);
                                                int ansId = getResources().getIdentifier(ansName, "id", getPackageName());
                                                findViewById(ansId).setVisibility(View.GONE);
                                            }
                                        } catch (Exception e) {
                                            Log.e("blank1", "Failed with error: " + e.getMessage());
                                        }
                                    }
                                }
                            } else {
                                Log.e(TAG, "Error getting document", task.getException());
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("blank1", "Failed with error: " + e.getMessage());
        }
    }

    private void displayArticle(String content, String Q1) {
        TextView articleContentTextView = findViewById(R.id.blankContent);
        TextView Q1TextView = findViewById(R.id.Q1);

        articleContentTextView.setText(content);
        Q1TextView.setText(Q1);
    }
}
