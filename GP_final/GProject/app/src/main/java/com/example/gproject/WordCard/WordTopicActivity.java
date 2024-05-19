package com.example.gproject.WordCard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gproject.MainActivity;
import com.example.gproject.R;
import com.example.gproject.WordQuiz.WordLevelChose;
import com.example.gproject.WordQuiz.WordListActivity;
import com.example.gproject.WordQuiz.LevelAQuizActivity;
import com.example.gproject.meaning.ShowMeaning;

public class WordTopicActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_topic);

    }
    public void dic_click(View view){
        Intent intent = new Intent(WordTopicActivity.this, ShowMeaning.class);
        startActivity(intent);
        finish();
    }
    public void word_test_click(View view){
        Intent intent = new Intent(WordTopicActivity.this, WordLevelChose.class);
        startActivity(intent);
        finish();
    }
    public void word_collect_click(View view){
        Intent intent = new Intent(WordTopicActivity.this, WordListActivity.class);
        startActivity(intent);
        finish();
    }
}
