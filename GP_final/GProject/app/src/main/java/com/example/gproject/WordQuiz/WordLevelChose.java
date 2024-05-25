package com.example.gproject.WordQuiz;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gproject.MainActivity;
import com.example.gproject.R;
import com.example.gproject.reading.ChoseTestActivity;

public class WordLevelChose extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_level_chos);

        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void homeClick(View view) {
        Intent intent = new Intent(WordLevelChose.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void LevelA_Click(View view) {
        Intent intent = new Intent(WordLevelChose.this, LevelAQuizActivity.class);
        startActivity(intent);

    }

    public void LevelB_Click(View view) {
        Intent intent = new Intent(WordLevelChose.this, LevelBQuizActivity.class);
        startActivity(intent);

    }

    public void LevelC_Click(View view) {
        Intent intent = new Intent(WordLevelChose.this, LevelCQuizActivity.class);
        startActivity(intent);

    }

    //show Help dialog
    public void ShowHelpDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.word_dialog_help);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); // 设置宽高为全屏
        dialog.show();

        ImageButton close = dialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }

    public void getHelp(View view) {
        ShowHelpDialog();
    }

    //Set Next Form's chos Num
    private void goToChoseTest(String Level_ID) {
        SharedPreferences sharedPreferences = getSharedPreferences("Level_ID", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Level_ID", Level_ID);
        editor.apply();
        Log.e("WordLevelChose", "Level_ID" + Level_ID);
    }
}
