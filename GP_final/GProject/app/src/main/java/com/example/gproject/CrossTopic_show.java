package com.example.gproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gproject.AiTeacher.CrossTopic_db;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CrossTopic_show extends AppCompatActivity {
    TextView ans;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cross_topic_show);

        ImageButton back=findViewById(R.id.back2);
        ImageView home=findViewById(R.id.home2);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CrossTopic_show.this, MainActivity.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView ques=findViewById(R.id.Question);
        ans =findViewById(R.id.Ans);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ques.setText(extras.getString("question"));
            ans.setText(extras.getString("ans"));
            key=extras.getString("key");

        }




    }

//    public void getDb(){
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//
//        FirebaseUser currentUser = auth.getCurrentUser();
//        if (currentUser != null) {
//            String userId = currentUser.getUid();
//
//
//            DatabaseReference targetRef = databaseReference
//                    .child("CrossTopic")
//                    .child(userId)
//                    .child("Hometown")
//                    .child(key);
//
//
//            targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                    if (dataSnapshot.exists()) {
//                        // 检查特定 key 的值
//                        DataSnapshot valueSnapshot = dataSnapshot.child(randomCode);
//                        if (valueSnapshot.exists()) {
//                            String value = valueSnapshot.getValue(String.class);
//                            Ans.setText(value);
//                            Log.d("FirebaseValue", "Value: " + value);
//                        } else {
//                            Log.d("FirebaseValue", "Key not found");
//                            Toast.makeText(ReviewShow_Speaking.this, "randomCode找不到", Toast.LENGTH_SHORT).show();
//
//                        }
//                    } else {
//                        Toast.makeText(ReviewShow_Speaking.this, "Ans資料庫找不到", Toast.LENGTH_SHORT).show();
//
//                        Log.d("FirebaseValue", "Path not found");
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//
//            });
//        }
//
//    }
}