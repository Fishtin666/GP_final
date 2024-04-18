package com.example.gproject.Review;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gproject.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RReview extends AppCompatActivity {

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private FirebaseFirestore db;
    private DocumentReference content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rreview);

        textView1 = findViewById(R.id.textView7);
        textView2 = findViewById(R.id.textView8);
        textView3 = findViewById(R.id.textView9);
        db = FirebaseFirestore.getInstance();
        content = db.collection("R_judge").document("1");
        content.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // 取得資料並設定給 TextView
                    String title = document.getString("title");
                    String content = document.getString("content"); // 替換為你的字段名稱
                    String Q1 = document.getString("Q1");
                    String Q2 = document.getString("Q2");
                    String Q3 = document.getString("Q3");
                    String Q4 = document.getString("Q4");
                    String A1 = document.getString("A1");
                    String A2 = document.getString("A2");
                    String A3 = document.getString("A3");
                    String A4 = document.getString("A4");
                    String Text = title+"\n\n"+content+"\n";
                    String questionText = Q1+"\n\n"+Q2+"\n\n"+Q3+"\n\n"+Q4;
                    String ansText = "1. "+A1+"\n"+"2. "+A2+"\n"+"3. "+A3+"\n"+"4. "+A4+"\n";
                    textView3.setText(questionText);
                    textView1.setText(Text);
                    textView2.setText(ansText);
                } else {
                    // 文件不存在的處理
                    Log.d("TAG", "Document does not exist");
                }
            } else {
                // 讀取失敗的處理
                Log.e( "TAG","Error getting document: " + task.getException());
            }
        });
    }
}