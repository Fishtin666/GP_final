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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class R_blank extends AppCompatActivity {
    private static final String TAG = "R_blank";
    String ReviewName = "R_blank" ;
    int numberOfFields = 5;  // Set the Number of Question

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r_blank);
        HideCorrectAns();

        int Dnumber = getIntent().getIntExtra("DocumentId", 0);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentRef = db.collection(ReviewName).document(String.valueOf(Dnumber));

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
                                long currentTime = System.currentTimeMillis();
                                List<String> incorrectAnswers = new ArrayList<>();

                                for (int i = 0; i < numberOfFields; i++) {
                                    String ansName = "A" + (i + 1);
                                    int ansId = getResources().getIdentifier(ansName, "id", getPackageName());
                                    EditText editText = findViewById(ansId);
                                    String editTextValue = editText.getText().toString().trim();

                                    saveReviewData(Dnumber, currentTime, ansName, editTextValue);

                                    if (document.contains(ansName)) {
                                        //get Firestore's ans colum
                                        String firestoreValue = document.getString(ansName);
                                        Log.e("correct", "A：" + firestoreValue);

                                        // compare the value of EditText and Firestore's colum
                                        if (!editTextValue.equals(firestoreValue)) {
                                            //mark incorrect answer
                                            editText.setTextColor(Color.RED);
                                            // add the incorrect answer to the list
                                            incorrectAnswers.add(firestoreValue);

                                        }else {
                                            incorrectAnswers.add("");
                                        }
                                    }

                                }if (!incorrectAnswers.isEmpty()) {
                                    SetCorrectAns(incorrectAnswers);
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

    // save Review data
    public void saveReviewData(int documentID, long currentTime, String cul, String ans) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root = db.getReference("R_Review");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference R_ReviewRef = FirebaseDatabase.getInstance().getReference().child("R_Review");
        String userId = user.getUid();
        String D_ID = String.valueOf(documentID);
//        String A_cul = String.valueOf(cul);
        R_ReviewRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                long currentTime = System.currentTimeMillis();

                if (user != null) {
                    Date date = new Date(currentTime);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String formattedDate = sdf.format(date);
                    root.child(userId)
                            .child(ReviewName)
                            .child(D_ID)
                            .child(formattedDate)
                            .child(cul)
                            .setValue(ans);
                } else {
                    Log.e(ReviewName, "review save data failed");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("R_match", "review save data failed 2");
            }
        });

    }
    //Hide correct ans cul
    public void HideCorrectAns() {
        for (int i = 0; i < numberOfFields; i++) {
            String correctName = "c" + (i + 1);
            int correctID = getResources().getIdentifier(correctName, "id", getPackageName());
            TextView textC = findViewById(correctID);
            textC.setVisibility(View.GONE);
        }
    }
    //Show the correct ans
    public void SetCorrectAns(List<String> correctList) {
        for (int i = 0; i < numberOfFields; i++) {
            String correct = correctList.get(i);
            String correctName = "c" + (i + 1);
            int correctID = getResources().getIdentifier(correctName, "id", getPackageName());
            TextView textC = findViewById(correctID);
            textC.setVisibility(View.VISIBLE);

            if (correct != null) {
                textC.setText(correct);
            } else {
                textC.setText(""); // 如果答案正确，将文本设置为空
            }
        }
    }
}
