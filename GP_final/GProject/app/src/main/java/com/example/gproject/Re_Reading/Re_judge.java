package com.example.gproject.Re_Reading;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.gproject.JustifyTextView;
import com.example.gproject.JustifyTextView2;
import com.example.gproject.JustifyTextView4;
import com.example.gproject.MainActivity;
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

import java.util.ArrayList;
import java.util.List;

public class Re_judge extends AppCompatActivity {

    String ReviewName = "R_judge";
    int numberOfFields = 4;

    String documentID;
    String saveTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r_judge);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        try {


        Button sendBut = findViewById(R.id.sendAns);
        sendBut.setVisibility(View.GONE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            documentID = bundle.getString("docID");
            saveTime = bundle.getString("saveT");
        }
        //delay loading page
        ConstraintLayout load = findViewById(R.id.load);
        load.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                load.setVisibility(View.VISIBLE);
            }
        }, 1000);

        //back button
        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(R_blank.this, R_topic.class);
//                startActivity(intent);
                finish();
            }
        });
        ImageView home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Re_judge.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        int Dnumber = getIntent().getIntExtra("DocumentId", 0);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentRef = db.collection(ReviewName).document(String.valueOf(Dnumber));
        for (int i = 0; i < numberOfFields; i++) {

            Log.e(ReviewName, "i = " + i);
            String culName = "A" + (i + 1);

            getReviewData(documentID, saveTime, culName);
        }
        } catch (Exception e) {
            Log.e("blank1", "Failed with error: " + e.getMessage());
        }
    }

    // get save data from database
    public void getReviewData(String documentID, String saveTime, String cul) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        DatabaseReference R_ReviewRef = FirebaseDatabase.getInstance().getReference()
                .child("R_Review")
                .child(userId)
                .child(ReviewName)
                .child(documentID)
                .child(saveTime)
                .child(cul);

        Log.d(ReviewName,"FirebasePath Path: " + R_ReviewRef.toString());  // Print the path

        R_ReviewRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(ReviewName,"DataSnapshot Data: " + dataSnapshot.toString());  // Print the data snapshot

                if (dataSnapshot.exists()) {
                    String ANS = dataSnapshot.getValue(String.class);
                    Log.d(ReviewName,"DataSnapshot ANS: " + ANS);  // Print the value

                    FirebaseFirestore.getInstance().collection(ReviewName)
                            .document(documentID)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot firestoreDocument = task.getResult();
                                        if (firestoreDocument != null && firestoreDocument.exists()) {
                                            setFireStoreData(firestoreDocument);
                                            List<String> incorrectAnswers = new ArrayList<>();

                                            //replace textview
                                            TextView ContentTextView = findViewById(R.id.Content);
                                            TextView matchTextView = findViewById(R.id.match);

                                            if (ContentTextView != null) {
                                                CharSequence conText = ContentTextView.getText();
                                                CharSequence matchText = matchTextView.getText();
                                                replaceTextview(ContentTextView, conText);
                                                replaceTextview2(matchTextView,matchText);

                                                Log.e(ReviewName, "successful getting content: " + conText);

                                            } else {
                                                Log.e(ReviewName, "Q1TextView is null");
                                            }

                                            for (int i = 0; i < numberOfFields; i++) {
                                                Log.e(ReviewName, "i = " + i);
                                                String ansName = "A" + (i + 1);

                                                int ansId = getResources().getIdentifier(ansName, "id", getPackageName());
                                                TextView ansTextView = findViewById(ansId);

                                                ansTextView.setKeyListener(null); // Set EditText unable to edit

                                                if (ansTextView != null) {
                                                    String firestoreValue = firestoreDocument.getString(ansName);
                                                    if (firestoreDocument.contains(ansName) && cul.equals(ansName)) {
                                                        ansTextView.setText(ANS);
                                                    } else {
                                                        Log.e(ReviewName, "No match for cul: " + cul + " with ANS: " + ANS);
                                                    }

                                                    Log.e("correct", "A: " + firestoreValue);
                                                    if (!ANS.equals(firestoreValue)) {
                                                        ansTextView.setTextColor(Color.RED); // Mark incorrect answer
                                                        incorrectAnswers.add(firestoreValue); // Add the incorrect answer to the list
                                                    } else {
                                                        incorrectAnswers.add(" ");
                                                    }
                                                }else{
                                                    Log.e(ReviewName, "EditText with id " + ansId + " not found");
                                                    break;
                                                }
                                                Log.e(ReviewName, "correct ans: " + incorrectAnswers.get(i) );
                                            }
                                            if (!incorrectAnswers.isEmpty()) {
                                                SetCorrectAns(incorrectAnswers);
                                            }
                                        } else {
                                            Log.e(ReviewName, "Failed with error: " + task.getException());
                                        }
                                    } else {
                                        Log.e(ReviewName, "get Failed error: " + task.getException());
                                    }
                                }
                            });
                } else {
                    Log.e(ReviewName, "No data found in Realtime Database for the given path.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(ReviewName, "Error reading Realtime Database", databaseError.toException());
            }
        });
    }

    // set question data
    public void setFireStoreData(DocumentSnapshot document) {
        try {

            for (int i = 0; i < numberOfFields; i++) {

                String fieldName = "Q" + (i + 1);
                String ansName = "A" + (i + 1);

                int questionId = getResources().getIdentifier(fieldName, "id", getPackageName());
                int ansId = getResources().getIdentifier(ansName, "id", getPackageName());
                int titleId = getResources().getIdentifier("title", "id", getPackageName());
                int matchId = getResources().getIdentifier("match", "id", getPackageName());
                int content = getResources().getIdentifier("Content", "id", getPackageName());

                //set Content
                CheckSetData(document, "content", content);

                //set Q
                CheckSetData(document, fieldName, questionId);

                //set ans
                CheckHideData(document, ansName, ansId);

                //set title
                CheckSetData(document, "title", titleId);

                //set match
                CheckSetData(document, "match", matchId);

            }
        } catch (Exception e) {
            Log.e(ReviewName, "Failed with error: " + e.getMessage());
        }
    }

    //check whether data is exit, set data
    private void CheckSetData(DocumentSnapshot document, String dataName, int dataID) {

        TextView QTextView = findViewById(dataID);
        if (QTextView != null) {
            if (document.contains(dataName)) {
                // Set data
                String fieldData = document.getString(dataName);
                String[] splitQue = fieldData.split("。");
                StringBuilder combinedField = new StringBuilder();
                for (String line : splitQue) {
                    combinedField.append(line.trim()).append("\n");
                }
                QTextView.setText(combinedField.toString());
            } else {
                QTextView.setVisibility(View.GONE);
            }
        } else {
            Log.e(ReviewName, "TextView with id " + dataName + " not found");
        }
    }

    //check ans opt
    private void CheckHideData(DocumentSnapshot document, String dataName, int dataID) {

        TextView QTextView = findViewById(dataID);
        if (QTextView != null) {
            if (document.contains(dataName)) {
                // Set data
                QTextView.setVisibility(View.VISIBLE);
            } else {
                QTextView.setVisibility(View.GONE);
                Log.e(ReviewName, "Can't hide： " + dataName + " not found");
            }
        } else {
            Log.e(ReviewName, "TextView with id " + dataName + " not found");
        }
    }

    //Show the correct ans
    public void SetCorrectAns(List<String> correctList) {
        try {
            for (int i = 0; i < numberOfFields; i++) {
                // 检查 correctList 是否有足够的元素
                if (i < correctList.size()) {
                    String correct = correctList.get(i);
                    String correctName = "c" + (i + 1);
                    int correctID = getResources().getIdentifier(correctName, "id", getPackageName());
                    TextView textC = findViewById(correctID);

                    if (textC != null) {
                        textC.setVisibility(View.VISIBLE);
                        if (correct != null && !correct.isEmpty()) {
                            textC.setText(correct);
                            Log.d(ReviewName, "TextView with id " + correctID + " set to: " + correct);
                        } else {
                            textC.setText(" ");
                            Log.d(ReviewName, "TextView with id " + correctID + " set to empty string.");
                        }
                    } else {
                        Log.d(ReviewName, "TextView with id " + correctID + " not found.");
                    }
                } else {
                    Log.d(ReviewName, "correctList does not have enough elements for index " + i);
                }
            }
        } catch (Exception e) {
            Log.d(ReviewName, "Failed with error: " + e.getMessage());
        }
    }

    //replace textview to JustifyTextview
    public void replaceTextview(View textView, CharSequence content) {
        // Get the parent layout
        ConstraintLayout parentLayout = (ConstraintLayout) textView.getParent();

        //Create a new JustifyTextView
        JustifyTextView4 JustifyText = new JustifyTextView4(this, null);
        JustifyText.setId(textView.getId());  // Keep the same ID
        JustifyText.setLayoutParams(textView.getLayoutParams());
        JustifyText.setText(content);

        // Copy TextView attributes to JustifyTextView
        if (textView instanceof TextView && JustifyText instanceof TextView) {
            TextView originalTextView = (TextView) textView;
            TextView newJustifyTextView = (TextView) JustifyText;
            newJustifyTextView.setTextColor(originalTextView.getCurrentTextColor());
            newJustifyTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, originalTextView.getTextSize());
            newJustifyTextView.setTypeface(originalTextView.getTypeface(), originalTextView.getTypeface().getStyle());
            newJustifyTextView.setGravity(originalTextView.getGravity());
            newJustifyTextView.setPadding(originalTextView.getPaddingLeft(), originalTextView.getPaddingTop(), originalTextView.getPaddingRight(), originalTextView.getPaddingBottom());
            newJustifyTextView.setBackgroundColor(getResources().getColor(R.color.grey));
        }

        // Replace the old TextView with the new CustomTextView
        int index = parentLayout.indexOfChild(textView);
        parentLayout.removeView(textView);
        parentLayout.addView(JustifyText, index);

        Log.e(ReviewName, "replace Textview");
    }

    // Replace TextView with JustifyTextView
    public void replaceTextview2(View textView, CharSequence content) {
        // Get the parent layout
        ConstraintLayout parentLayout = (ConstraintLayout) textView.getParent();

        // Create a new JustifyTextView
        JustifyTextView2 JustifyText = new JustifyTextView2(this, null);
        JustifyText.setId(textView.getId());  // Keep the same ID
        JustifyText.setLayoutParams(textView.getLayoutParams());
        JustifyText.setText(content);

        // Copy TextView attributes to JustifyTextView
        if (textView instanceof TextView && JustifyText instanceof TextView) {
            TextView originalTextView = (TextView) textView;
            TextView newJustifyTextView = (TextView) JustifyText;
            newJustifyTextView.setTextColor(originalTextView.getCurrentTextColor());
            newJustifyTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, originalTextView.getTextSize());
            newJustifyTextView.setTypeface(Typeface.create(originalTextView.getTypeface(), Typeface.NORMAL)); // Remove bold style
            newJustifyTextView.setGravity(originalTextView.getGravity());
            newJustifyTextView.setPadding(originalTextView.getPaddingLeft(), originalTextView.getPaddingTop(), originalTextView.getPaddingRight(), originalTextView.getPaddingBottom());
        }

        // Replace the old TextView with the new CustomTextView
        int index = parentLayout.indexOfChild(textView);
        parentLayout.removeView(textView);
        parentLayout.addView(JustifyText, index);

        Log.e(ReviewName, "replace Textview");
    }

}