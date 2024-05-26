package com.example.gproject.Review;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReviewShow_VR extends AppCompatActivity {

    String randomVR;
    TextView EN,CH;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_show_vr);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        EN = findViewById(R.id.EN);
        CH = findViewById(R.id.CH);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            randomVR = bundle.getString("subkey");
            Log.d("VR", randomVR);
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference targetRef = databaseReference
                    .child("Conversation")
                    .child(userId)
                    .child(randomVR);
            targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                            String enJudge = dataSnapshot.child("gpt").getValue(String.class);
                            Log.d("EN", ":"+enJudge);
                            EN.setText(enJudge);
                            if(dataSnapshot.hasChild("chinese")){
                                String chJudge = dataSnapshot.child("chinese").getValue(String.class);
                                Log.d("CH", ":"+chJudge);
                                CH.setText(chJudge);
                            }
                    }else {
                        // 数据不存在时的处理
                        Log.d("Data", "Data does not exist");
                        // 可以设置默认的文本或者采取其他操作
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}