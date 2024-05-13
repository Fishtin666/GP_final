package com.example.gproject.WordQuiz;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gproject.MainActivity;
import com.example.gproject.R;
import com.example.gproject.Adapters.WordQuizAdapter;
import com.example.gproject.Adapters.WordQuizData;
import com.example.gproject.WordCard.WordTopicActivity;
import com.example.gproject.fragment.WordFragment;
import com.example.gproject.reading.R_topic;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LevelBQuizActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private WordQuizAdapter adapter;
    private String collectionName;
    Button wordSendButton;

    LevelAQuizActivity levelAQuizActivity = new LevelAQuizActivity(); // Build LevelAQuizActivity

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pra_word);

        collectionName = "R_wordB";
        TextView testWord = findViewById(R.id.testWord);
        testWord.setText("Level_B");

        wordSendButton = findViewById(R.id.wordSend);
        wordSendButton.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 處理完耗時操作後，將 ProgressBar 隱藏，並顯示 TextView
                wordSendButton.setVisibility(View.VISIBLE);
            }
        }, 2000);

        try {
            RecyclerView QuizRecycler = findViewById(R.id.rcyQ);
            List<WordQuizData> questionsList = new ArrayList<>();
            QuizRecycler.setLayoutManager(new LinearLayoutManager(this));
            QuizRecycler.setAdapter(adapter);
            adapter = new WordQuizAdapter(this, questionsList);
            QuizRecycler.setAdapter(adapter);

            db = FirebaseFirestore.getInstance();

            // Back button
            ImageButton backButton = findViewById(R.id.back);

            //identify whether "word" EXIST
            SharedPreferences sharedPreferences = getSharedPreferences("WordLevel", MODE_PRIVATE);
            String wordLog = sharedPreferences.getString("word_level", "unKnow");

            if (!"unKnow".equals(wordLog)) {
                backButton.setVisibility(View.GONE);
                Log.i("dia11", "Gone 15");
            } else {
                Log.i("dia11", "Gone 16");
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LevelBQuizActivity.this, WordFragment.class);
                        startActivity(intent);
                    }
                });
            }

            // Send Answer button




            wordSendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int score = 0;
                        // 遍历 RecyclerView 中的每个单词
                        for (int i = 0; i < adapter.getItemCount(); i++) {
                            try {
                                // 获取当前位置的单词数据
                                WordQuizData word = adapter.getQuestions().get(i);
                                // 获取当前单词的定义和词性
                                String questionText = word.getDefinition() + " " + word.getPartOfSpeech();
                                // 获取当前 RecyclerView 中的 ViewHolder
                                RecyclerView.ViewHolder viewHolder = QuizRecycler.findViewHolderForAdapterPosition(i);
                                if (viewHolder != null) {
                                    // 在 ViewHolder 中查找 RadioButton 和 QueWord
                                    RadioButton radioButton = adapter.getRadioButtonAtPosition(i, QuizRecycler);
                                    String selectedWord = radioButton.getText().toString();

                                    TextView QueWord = viewHolder.itemView.findViewById(R.id.Que);
                                    // 检查用户选择的答案是否与正确答案匹配
                                    String selectedDocumentId = (String) viewHolder.itemView.getTag();
                                    if (selectedDocumentId.equals(selectedWord)) {
                                        if (radioButton.isChecked()) {
                                            // 回答正确
                                            score++;
                                            Log.e("Correct check", QueWord.getText().toString());
                                            Log.e("Correct Answer", selectedDocumentId + ", Correct Option: " + selectedWord);
                                        } else
//                                            radioButton.setTextColor(Color.RED);
                                            Log.e("Incorrect B", QueWord.getText().toString());
                                        Log.e("Incorrect Answer", selectedDocumentId + ", Correct Option: " + selectedWord);
                                    } else {
                                        radioButton.setTextColor(Color.RED);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("FireStore B2 ", "error: " + e.getMessage());
                            }
                        }

                        ShowScoreDialog(score, "B");

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("FireStore B1 ", "error: " + e.getMessage());
                    }
                }
            });

            // Get random questions and options from Firestore
            getRandomQuestionAndOptions(collectionName);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("FireStore B1 ", "error: " + e.getMessage());
        }
    }

    //show Level n Score dialog
    public void ShowScoreDialog(int Score, String Level) {
        try {

            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.word_dialog);
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); // 设置宽高为全屏
            dialog.show();

            // get Extra Word from SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("WordLevel", MODE_PRIVATE);
            String wordLog = sharedPreferences.getString("word_level", "unKnow");

            // put Score
            TextView scoreTextView = dialog.findViewById(R.id.ShowScore);
            scoreTextView.setText(String.valueOf(Score));

            // Cancel button
            Button cancelButton = dialog.findViewById(R.id.ButCancel);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(LevelBQuizActivity.this, WordFragment.class);
//                    startActivity(intent);
                    finish();
                }
            });

            //identify whether "word" EXIST
            if (!"unKnow".equals(wordLog)) {
                cancelButton.setVisibility(View.GONE);
                Log.i("dia22", "Gone 11");
            } else {
                Log.i("dia22", "Gone 13");
            }

            //set Hint
            if(Score<2){
                setStayHint(dialog,Level);
            }else {
                setNextHint(dialog,Level);
            }

            //enter to next part
            Button okButton = dialog.findViewById(R.id.ButOK);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!"unKnow".equals(wordLog)) {

                        if (Score < 2) {
                            Intent intent = new Intent(LevelBQuizActivity.this, MainActivity.class);
                            startActivity(intent);

                        } else {
                            GoToNextLevel(dialog, Level);
                            // 移除SharedPreferences中的值
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.remove("word_level");
//                            editor.apply();
//                            getIntent().removeExtra("word");
//                            Log.i("dia11", "exist " + wordLog);
                        }
                    } else {
                        if (Score > 2) {
                            GoToNextLevel(dialog, Level);
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("dia22", "error: " + e.getMessage());
        }
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
    public void getHelp(View view){
        ShowHelpDialog();
    }
    //Stay in Original Level
    public void StayOriginalLevel(Dialog dialog, String LevelValue) {
        //set hint
        TextView Hint = dialog.findViewById(R.id.ScoreHint);
        Log.e("setHint", "show B ");

        switch (LevelValue) {
            case "A":
                Hint.setText("未得級數，你的單字能力需加強");
                Intent intentA = new Intent(this, LevelAQuizActivity.class);
                startActivity(intentA);
                break;

            case "B":
                Hint.setText("你的單字等級為 A ");
                Intent intentB = new Intent(this, LevelBQuizActivity.class);
                startActivity(intentB);
                break;

            case "C":
                Hint.setText("你的單字等級為 B ");
                Intent intentC = new Intent(this, MainActivity.class);
                startActivity(intentC);
                break;
        }
    }
    //Go To Next Level
    public void GoToNextLevel(Dialog dialog, String LevelValue) {

        switch (LevelValue) {
            case "A":
                Intent intentA = new Intent(this, LevelBQuizActivity.class);
                startActivity(intentA);
                break;

            case "B":
                Intent intentB = new Intent(this, LevelCQuizActivity.class);
                startActivity(intentB);
                SaveWordLevel(LevelValue);
                break;

            case "C":
                Intent intentC = new Intent(this, MainActivity.class);
                startActivity(intentC);
                SaveWordLevel(LevelValue);
                break;

        }
    }
    //set faild hint
    public void setStayHint(Dialog dialog, String LevelValue) {
        TextView Hint = dialog.findViewById(R.id.ScoreHint);

        if (LevelValue == "A") {
            Hint.setText("未通過單字等級 A，你的單字能力需加強");

        } else if (LevelValue == "B") {
            Hint.setText("未通過單字等級 B");

        } else {
            Hint.setText("未通過單字等級 C");

        }

    }
    // set pass Hint
    public void setNextHint(Dialog dialog, String LevelValue) {
        TextView Hint = dialog.findViewById(R.id.ScoreHint);

        if (LevelValue == "A") {
            Hint.setText("恭喜通過單字等級 A");

        } else if (LevelValue == "B") {
            Hint.setText("恭喜通過單字等級 B");

        } else {
            Hint.setText("恭喜通過單字等級 C ");

        }
    }
    //save data into firebase
    public void SaveWordLevel(String WordLevel) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root = db.getReference("word_Level");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference levelRef = FirebaseDatabase.getInstance().getReference().child("word_Level").child(WordLevel);
        String userId = user.getUid();
        levelRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.child("WordLevel").exists()) {
                    DataSnapshot speechTextSnapshot = snapshot.child("WordLevel");
                    String WordLevel1 = speechTextSnapshot.getValue(String.class);
                    if ("C".equals(WordLevel1)) {
                        root.child(userId).child("WordLevel").setValue("C");

                    }else if("B".equals(WordLevel)){
                        root.child(userId).child("WordLevel").setValue("B");
                    }else{
                        root.child(userId).child("WordLevel").setValue("A");
                    }
                }else {
                    root.child(userId).child("WordLevel").setValue(WordLevel);
                    Log.i("SaveData", "show B fail2");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    //get Question
    public void getRandomQuestionAndOptions(String collectionName) {
        db.collection(collectionName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        try {
                            boolean isDuplicate = false;
                            if (task.isSuccessful()) {
                                List<WordQuizData> wordList = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String word = document.getId();
                                    String meaning = document.getString("meaning");
                                    String pos = document.getString("pos");
                                    wordList.add(new WordQuizData(meaning, pos, word, word, word));
                                }
                                setRandomQuestionAndOptions(wordList, wordList);
                            } else {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("FireStore", "Failed with error: " + e.getMessage());
                        }
                    }
                });
    }
    //set Question
    public void setRandomQuestionAndOptions(List<WordQuizData> questionList, List<WordQuizData> optionList) {
        int TestNum = 3;
        try {
            if (questionList.size() >= TestNum) {
                int numberOfQuestions = TestNum;
                for (int i = 0; i < numberOfQuestions; i++) {
                    // Select a random word as the question
                    WordQuizData questionWord = questionList.get(new Random().nextInt(questionList.size()));

                    // Remove the selected questionWord from questionList
                    questionList.remove(questionWord);

                    // Remove the selected questionWord from optionList
                    List<WordQuizData> optionWords = new ArrayList<>(optionList);
                    optionWords.remove(questionWord);

                    Collections.shuffle(optionWords);
                    WordQuizData option = optionWords.get(0);

                    // Randomly assign correctOption and incorrectOptions
                    boolean isCorrectOptionFirst = new Random().nextBoolean();
                    String correctOption;
                    String incorrectOption;
                    if (isCorrectOptionFirst) {
                        correctOption = questionWord.getCorrectWord();
                        incorrectOption = option.getIncorrectWord();
                    } else {
                        correctOption = option.getCorrectWord();
                        incorrectOption = questionWord.getIncorrectWord();
                    }
                    String pos = questionWord.getPartOfSpeech();

                    // set Question n option
                    String questionText2 = questionWord.getDefinition();

                    adapter.addWordId(questionText2, questionWord.getWord());
                    adapter.getQuestions().add(new WordQuizData(questionText2, pos, correctOption, incorrectOption, questionWord.getDocumentId()));
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Insufficient number of words to conduct quiz", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Level B", "Failed with error: " + e.getMessage());
        }
    }

    //save Score and Level
    public void GetScore(int Score, String Level) {
        //save Score
        SharedPreferences sharedPreferences = getSharedPreferences("Score", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Score", Score);
        editor.apply();
        //save level
        SharedPreferences level = getSharedPreferences("level", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = level.edit();
        editor2.putString("level", Level);
        editor2.apply();
    }
}
