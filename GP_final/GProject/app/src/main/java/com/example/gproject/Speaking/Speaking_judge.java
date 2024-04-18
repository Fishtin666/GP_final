package com.example.gproject.Speaking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gproject.MainActivity;
import com.example.gproject.R;

import java.util.Locale;

public class Speaking_judge extends AppCompatActivity {
    TextView judge;
    ImageView home;
    String Judge,Num;

    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speaking_judge);

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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Judge= extras.getString("judge");
            Num = extras.getString("num");
        }
        judge.setText(Judge);
        //tts.speak(Judge, TextToSpeech.QUEUE_FLUSH, null);

    }

    public void homeClick(View v){
        Intent intent =new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}