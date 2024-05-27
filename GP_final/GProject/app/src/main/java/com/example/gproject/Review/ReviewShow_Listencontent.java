package com.example.gproject.Review;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.gproject.MainActivity;
import com.example.gproject.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class ReviewShow_Listencontent extends AppCompatActivity {

    String test;
    int section;  //第幾次回答
    int Qcount;
    TextView contentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_show_listencontent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        contentText = findViewById(R.id.listenContent2);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            test = bundle.getString("test");
            section = bundle.getInt("section");
            Qcount = bundle.getInt("Qcount");
            Log.d("TAG","Listen_" + test+"/S"+section+"_"+Qcount);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference Q = db.collection("Listen_" + test).document("S" + section + "_" + Qcount);
            Q.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    //查找此題組有幾題
                    if (document.exists()) {
                        for (Map.Entry<String, Object> entry : document.getData().entrySet()) {
                            String fieldName = entry.getKey();
                            // 檢查是否有picture欄位
                            if (fieldName.equals("content")) {
                                String fieldValue = document.getString(fieldName);
                                String cont = fieldValue.replace("\\n", "\n");
                                contentText.setText(cont);
                            } else {
                                Log.d("TAG", "content does not exist");
                            }
                        }
                    } else {
                        // 文件不存在的處理
                        Log.d("TAG", "Document does not exist");
                    }
                }
            });
        }

        //back button
        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewShow_Listencontent.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}