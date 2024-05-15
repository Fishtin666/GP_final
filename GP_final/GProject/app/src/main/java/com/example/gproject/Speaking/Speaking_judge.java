package com.example.gproject.Speaking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gproject.MainActivity;
import com.example.gproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class Speaking_judge extends AppCompatActivity {
    TextView judge;
    ImageView home;
    String Judge,Num,Key;
    ImageButton back;

    TextToSpeech tts;

    FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speaking_judge);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR)
                    tts.setLanguage(Locale.UK);
            }
        });

        judge = findViewById(R.id.judge);
        home = findViewById(R.id.home);
        back = findViewById(R.id.back2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Judge= extras.getString("judge");
            Num = extras.getString("num");
            Key = extras.getString("key");
        }
        judge.setText(Judge);
        push();
        //tts.speak(Judge, TextToSpeech.QUEUE_FLUSH, null);

    }

    public void homeClick(View v){
        Intent intent =new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void push(){
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference userAnswersRef = databaseReference
                    .child("users")
                    .child(userId)
                    .child("Judge")
                    .child("Speaking")
                    .child(Key);

            userAnswersRef.setValue(Judge)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                            } else {
                                // 存储失败
                                // 处理存储失败的情况
                            }
                        }
                    });

        }
    }
}