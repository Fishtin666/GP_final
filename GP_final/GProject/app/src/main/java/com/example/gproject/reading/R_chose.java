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

public class R_chose extends AppCompatActivity {
    private static final String TAG = "R_chose";
    String ReviewName = "R_chose";
    int numberOfFields = 4;  // Set the Number of Question

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r_chose);
        HideCorrectAns();
        showReminderDialog();

        int Dnumber = getIntent().getIntExtra("DocumentId", 0);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentRef = db.collection(ReviewName).document(String.valueOf(Dnumber));

        // back button
        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(R_chose.this, R_topic.class);
//                startActivity(intent);
                finish();
            }
        });

        //delay loading page
        ConstraintLayout load = findViewById(R.id.load);
        load.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                load.setVisibility(View.VISIBLE);
            }
        }, 500);

        //send ans
        Button send = findViewById(R.id.sendAns);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send.setVisibility(View.GONE);
                documentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            try {
                                if (document.exists()) {
                                    long currentTime = System.currentTimeMillis();
                                    List<String> incorrectAnswers = new ArrayList<>();

                                    //replace textview
                                    TextView ContentTextView = findViewById(R.id.Content);
                                    if (ContentTextView != null) {
                                        CharSequence conText = ContentTextView.getText();
                                        replaceTextview(ContentTextView, conText);
                                        Log.e("blank0", "successful getting content: " + conText);

                                    } else {
                                        Log.e("blank0", "Q1TextView is null");
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

                                            //get Firestore's ans colum
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
                                        Log.e(ReviewName, "correct ans: " + incorrectAnswers.get(i));
                                    }
                                    if (!incorrectAnswers.isEmpty()) {
                                        SetCorrectAns(incorrectAnswers);
                                    }

                                } else {
                                    Log.d(TAG, "No such document");

                                }
                            } catch (Exception e) {
                                Log.e("blank0", "Failed with error: " + e.getMessage());

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

                            if (document.exists()) {

                                for (int i = 0; i < numberOfFields; i++) {

                                    String fieldName = "Q" + (i + 1);
                                    String optName = "opt" + (i + 1);
                                    String ansName = "A" + (i + 1);


                                    int questionId = getResources().getIdentifier(fieldName, "id", getPackageName());
                                    int optionId = getResources().getIdentifier(optName, "id", getPackageName());
                                    int ansId = getResources().getIdentifier(ansName, "id", getPackageName());
                                    int content = getResources().getIdentifier("Content", "id", getPackageName());
                                    int titleId = getResources().getIdentifier("title1", "id", getPackageName());

                                    //set Content
                                    CheckSetData(document, "content", content);

                                    //set Q
                                    CheckSetData(document, fieldName, questionId);

                                    //set opt
                                    CheckSetData(document, optName, optionId);

                                    //set ans
                                    CheckHideData(document, ansName, ansId);

                                    // check whether title is exit
                                    CheckSetData(document, "title1", titleId);
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
        }

        // Replace the old TextView with the new CustomTextView
        int index = parentLayout.indexOfChild(textView);
        parentLayout.removeView(textView);
        parentLayout.addView(JustifyText, index);

        Log.e("blank0", "replace Textview");
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

    public void homeClick(View view) {
        Intent intent = new Intent(R_chose.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // save Review data
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
            if (textC != null) {
                textC.setVisibility(View.GONE);
            } else {
                break;
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
        builder.setMessage("輸入答案時，請依照選項大小寫作答，並且不要有任何空格。");
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
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        dialog.show();
        // set background transparent degree

    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
