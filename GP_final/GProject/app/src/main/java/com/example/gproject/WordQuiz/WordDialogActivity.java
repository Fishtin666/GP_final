//package com.example.gproject.WordQuiz;
//
//import static android.content.Context.MODE_PRIVATE;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.gproject.MainActivity;
//import com.example.gproject.R;
//import com.example.gproject.fragment.WordFragment;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//public class WordDialogActivity extends AppCompatActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.word_dialog);
//        try{
//        //getScore
//        SharedPreferences sharedPreferences = getSharedPreferences("Score", MODE_PRIVATE);
//        int GetScore = sharedPreferences.getInt("Score", 0);
//            Log.i("dia11","Show "+GetScore+" ");
//        //getLevel
//        SharedPreferences Level = getSharedPreferences("level", MODE_PRIVATE);
//        String GetLevel = Level.getString("level", "default_value");
//            Log.i("dia11","Show "+GetLevel);
////        Log.e("dialog111","Show"+GetLevel+GetScore);
//
//        //putScore
//        TextView score = findViewById(R.id.Score);
//        score.setText(GetScore);
//
//        String log = getIntent().getStringExtra("word");
//        setHint(GetLevel,log);
//
//        Log.i("dia11","Show "+GetLevel+" "+GetScore+" "+log);
//
//        Button Cancel = findViewById(R.id.ButCancel);
//        Cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(WordDialogActivity.this, WordFragment.class);
//                startActivity(intent);
//            }
//        });
//
//        //Go to topic or con to do test
//        Button OK = findViewById(R.id.ButOK);
//        OK.setOnClickListener(new View.OnClickListener() {
//            //            int number = getIntent().getIntExtra("ChoseNumber", 0);
//            @Override
//            public void onClick(View view) {
//                Log.i("dia11","Show "+GetLevel+GetScore);
//                if (getIntent().hasExtra("word")) {
//                    Cancel.setVisibility(View.GONE);
//                    if (GetScore < 6) {
//                        StayOriginalLevel(GetLevel);
////                        getIntent().removeExtra("word");
//                    } else {
//                        GoToNextLevel(GetLevel);
////                        getIntent().removeExtra("word");
//                    }
//                } else {
//                    if (GetScore < 6) {
//                        GoToNextLevel(GetLevel);
////                        getIntent().removeExtra("word");
//                    }
//
//                }
//                Log.d("Word", "Word data removed: " + log);
//            }
//        });
//        getIntent().removeExtra("word");
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.i("dia11", "error: " + e.getMessage());
//        }
//    }
//
//    //Stay in Original Level
//    public void StayOriginalLevel(String LevelValue) {
//        TextView Hint = null;
//        switch (LevelValue) {
//            case "A":
//                Intent intentA = new Intent(this, LevelAQuizActivity.class);
//                startActivity(intentA);
//                break;
//
//            case "b":
//                SaveWordLevel(LevelValue);
//                Intent intentB = new Intent(this, LevelBQuizActivity.class);
//                startActivity(intentB);
//                break;
//
//            case "C":
//                SaveWordLevel(LevelValue);
//                Intent intentC = new Intent(this, MainActivity.class);
//                startActivity(intentC);
//                break;
//        }
//    }
//
//    //Go To Next Level
//    public void GoToNextLevel(String LevelValue) {
//        TextView Hint = null;
//        switch (LevelValue) {
//            case "A":
//                Intent intentA = new Intent(this, LevelBQuizActivity.class);
//                startActivity(intentA);
//                break;
//            case "B":
//                SaveWordLevel(LevelValue);
//                Intent intentB = new Intent(this, LevelCQuizActivity.class);
//                startActivity(intentB);
//
//                break;
//            case "C":
//                Intent intentC = new Intent(this, MainActivity.class);
//                startActivity(intentC);
//                break;
//        }
//    }
//
//    //set hint
//    public void setHint(String LevelValue, String LevelWord) {
//        TextView Hint = null;
//        String combine = LevelValue + LevelWord;
//        switch (combine) {
//            case "Aword":
//                Hint.setText("再接再厲，要先提升單字能力才能繼續下一個等級");
//                break;
//            case "Bword":
//                Hint.setText("加油，單字部分還有進步空間");
//                break;
//            case "Cword":
//                Hint.setText("很棒，你已認識大部分單字\n開始使用！");
//                break;
//            case "A":
//                Hint.setText("再接再厲，需要加強單字練習！");
//                break;
//            case "B":
//                Hint.setText("加油，單字部分還有進步空間喔！\n");
//                break;
//            case "C":
//                Hint.setText("很棒，你的詞彙量非常豐富\n");
//                break;
//            default:
//                Hint.setText("No hint available.");
//                break;
//        }
//    }
//
//    //save data into firebase
//    public void SaveWordLevel(String WordLevel) {
//        FirebaseDatabase db = FirebaseDatabase.getInstance();
//        DatabaseReference root = db.getReference("word_Level");
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference levelRef = FirebaseDatabase.getInstance().getReference().child("word_collect").child(WordLevel);
//        String userId = user.getUid();
//        levelRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists() && snapshot.hasChild("WordLevel") && WordLevel != "A") {
//                    DataSnapshot speechTextSnapshot = snapshot.child("WordLevel");
//                    String WordLevel1 = speechTextSnapshot.getValue(String.class);
//                    if (WordLevel1 == "C") {
//                        root.child(userId).child("WordLevel").setValue("C");
//                    } else {
//                        root.child(userId).child("WordLevel").setValue(WordLevel);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//    }
//}
