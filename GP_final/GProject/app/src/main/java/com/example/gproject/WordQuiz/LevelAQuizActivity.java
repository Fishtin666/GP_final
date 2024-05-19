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
import android.widget.ImageView;
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


public class LevelAQuizActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private WordQuizAdapter adapter;
    private String collectionName;
    Button wordSendButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pra_word);

        collectionName = "R_wordA";
        TextView testWord = findViewById(R.id.testWord);
        testWord.setText("Level_A");

        //delay loading page
        wordSendButton = findViewById(R.id.wordSend);
        wordSendButton.setVisibility(View.INVISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                wordSendButton.setVisibility(View.VISIBLE);
            }
        }, 2000);

        try {
            RecyclerView QuizRecycler = findViewById(R.id.rcyQ);
            List<WordQuizData> questionsList = new ArrayList<>();
            QuizRecycler.setLayoutManager(new LinearLayoutManager(this));
            adapter = new WordQuizAdapter(this, questionsList);
            QuizRecycler.setAdapter(adapter);

            db = FirebaseFirestore.getInstance();

            // Back button
            ImageButton backButton = findViewById(R.id.back);

            //identify whether "word" EXIST
            SharedPreferences sharedPreferences = getSharedPreferences("WordLevel", MODE_PRIVATE);
            String wordLog = sharedPreferences.getString("word_level", "unKnow");

            if (!"unKnow".equals(wordLog)) {
                ShowHelpDialog();
                backButton.setVisibility(View.GONE);
                Log.i("dia11", "Gone 15");
            } else {
                Log.i("dia11", "Gone 16");
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
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
                                            Log.e("Incorrect AAA", QueWord.getText().toString());
                                        Log.e("Incorrect Answer", selectedDocumentId + ", Correct Option: " + selectedWord);
                                    } else {
                                        radioButton.setTextColor(Color.RED);
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("FireStore A2 ", "error: " + e.getMessage());
                            }
                        }
                        Log.e("level A", "show " + "A" + score);
                        ShowScoreDialog(score, "A");

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
            Log.e("FireStore A1 ", "error: " + e.getMessage());
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

            Button cancelButton = dialog.findViewById(R.id.ButCancel);

            //set Hint
            if (Score < 6) {
                setFailHint(dialog, Level);
                cancelButton.setText("關閉");
            } else {
                setPassHint(dialog, Level);
                cancelButton.setText("返回");
            }

            //identify whether "word" EXIST
            if (!"unKnow".equals(wordLog)) {
                cancelButton.setVisibility(View.GONE);
                Log.i("dia11", "Gone 11");
            } else {
                // Cancel button
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Score < 6) {
                            // close dialog, stay in original page
                            dialog.dismiss();
                        } else {
                            // back to wordFragment
                            finish();
                        }
                    }
                });
                Log.i("dia11", "Gone 13");
            }

            //enter to next part
            Button okButton = dialog.findViewById(R.id.ButOK);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if (!"unKnow".equals(wordLog)) {
                        if (Score < 6) {
                            GoToMain(Level);

                        } else {
                            GoToNextLevel(Level);
                        }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("dia11", "error: " + e.getMessage());
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

    public void getHelp(View view) {
        ShowHelpDialog();
    }

    //Stay in Original Level
    public void GoToMain(String LevelValue) {
        //set hint
        Log.e("setHint", "show B ");
        Intent intentA = new Intent(this, MainActivity.class);
        startActivity(intentA);
        finish();
        SaveWordLevel(LevelValue);

    }

    //Go To Next Level
    public void GoToNextLevel(String LevelValue) {
        switch (LevelValue) {
            case "A":
                Intent intentA = new Intent(this, LevelBQuizActivity.class);
                startActivity(intentA);
                break;
            case "B":
                SaveWordLevel(LevelValue);
                Intent intentB = new Intent(this, LevelCQuizActivity.class);
                startActivity(intentB);

                break;
            case "C":
                Intent intentC = new Intent(this, MainActivity.class);
                startActivity(intentC);
                break;
        }
    }

    //set fail hint
    public void setFailHint(Dialog dialog, String LevelValue) {
        TextView Hint = dialog.findViewById(R.id.ScoreHint);

        if (LevelValue == "A") {
            Hint.setText("未通過單字等級 A，你的單字能力需加強");

        } else if (LevelValue == "B") {
            Hint.setText("未通過單字等級 B");

        } else {
            Hint.setText("未通過單字等級 C");
        }
    }

    //set pass Hint
    public void setPassHint(Dialog dialog, String LevelValue) {
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
        String userId = user.getUid();

        DatabaseReference userRef = root.child(userId); // 用户的引用
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // 如果用户已存在，则更新其级别
                    String currentLevel = snapshot.child("WordLevel").getValue(String.class);
                    if (currentLevel != null) {
                        // 用户已有级别，判断是否需要更新
                        if ("C".equals(currentLevel)) {
                            // 如果当前级别为C，不做任何变动
                        } else if ("B".equals(currentLevel) && !"C".equals(WordLevel)) {
                            // 如果当前级别为B，且新级别不是C，则更新为新级别
                            userRef.child("WordLevel").setValue(WordLevel);
                        } else if ("A".equals(currentLevel) && !"C".equals(WordLevel) && !"B".equals(WordLevel)) {
                            // 如果当前级别为A，且新级别不是C或B，则更新为新级别
                            userRef.child("WordLevel").setValue(WordLevel);
                        }
                    } else {
                        // 用户没有级别，直接设置新级别
                        userRef.child("WordLevel").setValue(WordLevel);
                    }
                } else {
                    // 如果用户不存在，则创建用户并设置级别
                    root.child(userId).child("WordLevel").setValue(WordLevel);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //getQuestion
    public void getRandomQuestionAndOptions(String collectionName) {
        db.collection(collectionName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        try {

                            if (task.isSuccessful()) {
                                List<WordQuizData> wordList = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String word = document.getId();
                                    String meaning = document.getString("meaning");
                                    String pos = document.getString("pos");
                                    wordList.add(new WordQuizData(meaning, pos, word, word, word));
//                                    Log.e("Show A", meaning);
                                }
                                setRandomQuestionAndOptions(wordList, wordList);
                            } else {
                                Toast.makeText(LevelAQuizActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("Level_A4567", "getQuestionAndOptions error is： " + e.getMessage());
                        }
                    }
                });
    }

    //setQuestion
    public void setRandomQuestionAndOptions(List<WordQuizData> questionList, List<WordQuizData> optionList) {
        int TestNum = 10;
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

    public void getHelp() {
        ///


        ///
    }

    //save Score and Level
    public void GetScore(int Score, String Level) {
        //save Score
        SharedPreferences sharedPreferences = getSharedPreferences("Score", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Score", Score);
        editor.apply();
        Log.e("leve_A", "Show  " + Score);
        //save level
        SharedPreferences level = getSharedPreferences("level", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = level.edit();
        editor2.putString("level", Level);
        editor2.apply();
        Log.e("leve_A", "Show  " + Level);
    }
}
