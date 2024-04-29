package com.example.gproject.WordQuiz;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gproject.MainActivity;
import com.example.gproject.R;
import com.example.gproject.fragment.WordFragment;

public class WordDialogActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_dialog);
        //getScore
        SharedPreferences sharedPreferences = getSharedPreferences("Score", MODE_PRIVATE);
        int GetScore = sharedPreferences.getInt("Score", 0);
        //getLevel
        SharedPreferences Level = getSharedPreferences("level", MODE_PRIVATE);
        String GetLevel = Level.getString("level", "default_value");
        //putScore
        TextView score = findViewById(R.id.Score);
        score.setText(GetScore);

        String log = getIntent().getStringExtra("word");

        Button Cancel = findViewById(R.id.ButCancel);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WordDialogActivity.this, WordFragment.class);
                startActivity(intent);
            }
        });

        //Go to topic or con to do test
        Button OK = findViewById(R.id.ButOK);
        OK.setOnClickListener(new View.OnClickListener() {
            //            int number = getIntent().getIntExtra("ChoseNumber", 0);
            @Override
            public void onClick(View view) {
                if(getIntent().hasExtra("word")){
                    Cancel.setVisibility(View.GONE);
                    if (GetScore < 6) {
                        StayOriginalLevel(GetLevel);
                        getIntent().removeExtra("word");
                    }else {
                        GoToNextLevel(GetLevel);
                        getIntent().removeExtra("word");
                    }
                }else {
                    if (GetScore < 6) {
                        GoToNextLevel(GetLevel);
                        getIntent().removeExtra("word");
                    }

                }
                Log.d("Word", "Word data removed: " + log);
            }
        });

    }
    //Stay in Original Level
    public void StayOriginalLevel(String LevelValue) {
        TextView Hint = null;
        switch (LevelValue) {
            case "A":
                Intent intentA = new Intent(this, LevelAQuizActivity.class);
                startActivity(intentA);
                //setHint
                Hint.setText("再接再厲，要先提升單字能力才能繼續下一個等級喔！");
                break;

            case "b":
                Intent intentB = new Intent(this, LevelBQuizActivity.class);
                startActivity(intentB);
                //setHint
                Hint.setText("加油，單字部分還需要加強!");
                break;

            case "C":
                Intent intentC = new Intent(this, MainActivity.class);
                startActivity(intentC);
                Hint.setText("很棒，你已認識大部分單字！");
                break;
        }
    }
    //Go To Next Level
    public void GoToNextLevel(String LevelValue) {
        switch (LevelValue) {
            case "A":
                Intent intentA = new Intent(this, LevelBQuizActivity.class);
                startActivity(intentA);
                break;
            case "B":
                Intent intentB = new Intent(this, LevelCQuizActivity.class);
                startActivity(intentB);
                break;
            case "C":
                Intent intentC = new Intent(this, MainActivity.class);
                startActivity(intentC);
                break;
        }
    }
    //save data into firebase
    public void SaveWordLevel(String WordLevel){

    }
}
