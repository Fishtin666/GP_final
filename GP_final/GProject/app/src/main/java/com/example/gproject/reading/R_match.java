package com.example.gproject.reading;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.gproject.JustifyTextView;
import com.example.gproject.JustifyTextView2;
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

public class R_match extends AppCompatActivity {
    private static final String TAG = "R_match";
    private String ReviewName = "R_match";
    // 假設要抓取的欄位數量
    int numberOfFields = 5;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r_match);
        HideCorrectAns();

        //get Document ID
        int Dnumber = getIntent().getIntExtra("ChoseNumber", 0);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentRef = db.collection("R_match").document(String.valueOf(Dnumber));


        //back button
        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(R_match.this, R_topic.class);
                startActivity(intent);
                finish();
            }
        });

        // send n correct answer
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
                                for (int i = 0; i < numberOfFields; i++) {
                                    String ansName = "A" + (i + 1);
                                    int ansId = getResources().getIdentifier(ansName, "id", getPackageName());
                                    EditText editText = findViewById(ansId);
                                    String editTextValue = editText.getText().toString().trim();

                                    saveReviewData(Dnumber, currentTime, ansName, editTextValue);
                                    List<String> incorrectAnswers = new ArrayList<>();
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
                                    for (String incorrectAnswer : incorrectAnswers) {
                                        // only call SetCorrectAns if the answer is incorrect
                                        Log.e("correct", "A：" + incorrectAnswer);
                                    }
                                    if (!incorrectAnswers.isEmpty()) {
                                        SetCorrectAns(incorrectAnswers);
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

    // save Review data
    public void saveReviewData(int documentID, long currentTime, String cul, String ans) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root = db.getReference("R_Review");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference R_ReviewRef = FirebaseDatabase.getInstance().getReference().child("R_Review").child(ReviewName);
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
                    root.child(ReviewName).child(userId).child(D_ID).child(formattedDate).child(cul).setValue(ans);
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

    public void HideCorrectAns() {
        for (int i = 0; i < numberOfFields; i++) {
            String correctName = "c" + (i + 1);
            int correctID = getResources().getIdentifier(correctName, "id", getPackageName());
            TextView textC = findViewById(correctID);
            textC.setVisibility(View.GONE);
        }
    }

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

