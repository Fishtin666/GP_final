package com.example.gproject.reading;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class R_multiple extends AppCompatActivity {
    private static final String TAG = "R_multiple";
    String ReviewName = "R_multiple";
    int numberOfFields = 4; // Set the Number of Questions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r_multiple);
        HideCorrectAns();
        showReminderDialog();

        int Dnumber = getIntent().getIntExtra("DocumentId", 0);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentRef = db.collection(ReviewName).document(String.valueOf(Dnumber));

        // Back button
        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Delay loading page
        ConstraintLayout load = findViewById(R.id.load);
        load.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                load.setVisibility(View.VISIBLE);
            }
        }, 500);

        // Send answers
        Button send = findViewById(R.id.sendAns);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send.setVisibility(View.GONE);
                documentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            try {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    long currentTime = System.currentTimeMillis();
                                    List<String> incorrectAnswers = new ArrayList<>();

                                    //replace textview
                                    TextView ContentTextView = findViewById(R.id.Content);
                                    TextView matchTextView = findViewById(R.id.match);

                                    if (ContentTextView != null) {
                                        CharSequence conText = ContentTextView.getText();
                                        CharSequence matText = matchTextView.getText();

                                        replaceTextview(ContentTextView, conText);
                                        replaceTextview2(matchTextView,matText);

                                        Log.e(ReviewName, "successful getting content: " + conText);

                                    } else {
                                        Log.e(ReviewName, "Q1TextView is null");
                                    }

                                    for (int i = 0; i < numberOfFields; i++) {
                                        String ansName = "A" + (i + 1);
                                        int ansId = getResources().getIdentifier(ansName, "id", getPackageName());
                                        EditText AnsEditText = findViewById(ansId);
                                        String editTextValue = AnsEditText.getText().toString().trim();

                                        AnsEditText.setKeyListener(null); //set edittext unable to edit
                                        hideKeyboard(AnsEditText); // Hide keyboard after editing each field

                                        if (document.contains(ansName)) {
                                            saveReviewData(document, Dnumber, currentTime, ansName, editTextValue);

                                            String firestoreValue = document.getString(ansName);
                                            Log.e("correct", "A：" + firestoreValue);

                                            if (!editTextValue.equals(firestoreValue)) {

                                                AnsEditText.setTextColor(Color.RED); //mark incorrect answer
                                                incorrectAnswers.add(firestoreValue); // add the incorrect answer to the list

                                            } else {
                                                incorrectAnswers.add(" ");
                                            }
                                        } else {
                                            Log.e(ReviewName, "EditText with id " + ansId + " not found");
                                            break;
                                        }
                                    }
                                    if (!incorrectAnswers.isEmpty()) {
                                        SetCorrectAns(incorrectAnswers);
                                    }

                                } else {
                                    Log.d(ReviewName, "No such document");
                                }
                            } catch (Exception e) {
                                Log.e(ReviewName, "Failed with error: " + e.getMessage());
                            }
                        } else {
                            Log.d(ReviewName, "get failed with ", task.getException());
                        }
                    }
                });
            }
        });

        ScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_UP); // Move the scroll position to top
            }
        });

        // Set value
        documentRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            try {
                                if (document.exists()) {

                                    for (int i = 0; i < numberOfFields; i++) {
                                        String optName = "opt" + (i + 1);
                                        String fieldName = "Q" + (i + 1);
                                        String ansName = "A" + (i + 1);
                                        String titleName = "title" + (i + 1);

                                        int questionId = getResources().getIdentifier(fieldName, "id", getPackageName());
                                        int optId = getResources().getIdentifier(optName, "id", getPackageName());
                                        int ansId = getResources().getIdentifier(ansName, "id", getPackageName());
                                        int titleId = getResources().getIdentifier(titleName, "id", getPackageName());
                                        int matchId = getResources().getIdentifier("match", "id", getPackageName());
                                        int content = getResources().getIdentifier("Content", "id", getPackageName());

                                        //set Content
                                        CheckSetData(document, "content", content);
                                        Log.d(ReviewName, fieldName + "=" + questionId);
                                        Log.d(ReviewName, optName + "=" + optId);

                                        // check whether ans/option is exit
                                        CheckHideData(document, ansName, ansId);

                                        // check whether match is exit
                                        CheckSetData(document, "match", matchId);

                                        // check whether title is exit
                                        CheckSetData(document, titleName, titleId);

                                        //set opt
                                        CheckSetData(document, optName, optId);

                                        //set Q
                                        CheckSetData(document, fieldName, questionId);

                                    }
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } catch (Exception e) {
                                Log.e("multiple", "Failed with error: " + e.getMessage());
                            }
                        } else {
                            Log.e(TAG, "Error getting document", task.getException());
                            Toast.makeText(R_multiple.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
            Log.e(TAG, "TextView with id " + dataName + " not found");
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
                Log.e(TAG, "Can't hide： " + dataName + " not found");
            }
        } else {
            Log.e(TAG, "TextView with id " + dataName + " not found");
        }
    }

    public void homeClick(View view) {
        Intent intent = new Intent(R_multiple.this, MainActivity.class);
        startActivity(intent);
        finish();
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

    // Save Review data
    public void saveReviewData(DocumentSnapshot document, int documentID, long currentTime, String cul, String ans) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root = db.getReference("R_Review");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference R_ReviewRef = FirebaseDatabase.getInstance().getReference().child("R_Review");
        String userId = user.getUid();
        String D_ID = String.valueOf(documentID);
        R_ReviewRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (user != null) {
                    if (document.contains(cul)) {
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
                        Log.e(ReviewName, "No cul： " + cul);
                    }
                } else {
                    Log.e(ReviewName, "review save data failed");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(ReviewName, "review save data failed 2");
            }
        });
    }

    // Hide correct ans cul
    public void HideCorrectAns() {
        for (int i = 0; i < numberOfFields; i++) {
            String correctName = "c" + (i + 1);
            int correctID = getResources().getIdentifier(correctName, "id", getPackageName());
            TextView textC = findViewById(correctID);
            if (textC != null) {
                textC.setVisibility(View.GONE);
            } else {
                Log.e(TAG, "TextView with id " + correctID + " not found");
            }
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
                            Log.d(TAG, "TextView with id " + correctID + " set to: " + correct);
                        } else {
                            textC.setText(" ");
                            Log.d(TAG, "TextView with id " + correctID + " set to empty string.");
                        }
                    } else {
                        Log.d(TAG, "TextView with id " + correctID + " not found.");
                    }
                } else {
                    Log.d(TAG, "correctList does not have enough elements for index " + i);
                }
            }
        } catch (Exception e) {
            Log.d(ReviewName, "Failed with error: " + e.getMessage());
        }
    }

    //show Reminder dialog
    private void showReminderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("作答提醒");
        builder.setMessage("輸入答案時，請依照選項大小寫作達，並且不要有任何空格。");
        builder.setPositiveButton("了解", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();

        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.dimAmount = 2f;
            window.setAttributes(layoutParams);
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        dialog.show();
        // set background transparent degree
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
