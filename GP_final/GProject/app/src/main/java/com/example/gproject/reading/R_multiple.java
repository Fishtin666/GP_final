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

import okhttp3.internal.ws.RealWebSocket;

public class R_multiple extends AppCompatActivity {
    private static final String TAG = "R_multiple";
    String ReviewName = "R_multiple";
    int numberOfFields = 4; //Set the Number of Question
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r_chose);

        int Dnumber = getIntent().getIntExtra("DocumentId", 0);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentRef = db.collection(ReviewName).document(String.valueOf(Dnumber));

        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(R_multiple.this, R_topic.class);
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
                            Toast.makeText(R_multiple.this, "failed", Toast.LENGTH_SHORT).show();
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
            case "opt1":
                optTextView = findViewById(R.id.opt1);
                break;
            case "opt2":
                optTextView = findViewById(R.id.opt2);
                break;
            case "opt3":
                optTextView = findViewById(R.id.opt3);
                break;
            case "opt4":
                optTextView = findViewById(R.id.opt4);
                break;
        }
        optTextView.setText(que);
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

    //Hide correct ans cul
    public void HideCorrectAns() {
        for (int i = 0; i < numberOfFields; i++) {
            String correctName = "c" + (i + 1);
            int correctID = getResources().getIdentifier(correctName, "id", getPackageName());
            TextView textC = findViewById(correctID);
            if(textC != null){
                textC.setVisibility(View.GONE);
            }else{
                break;
            }
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
