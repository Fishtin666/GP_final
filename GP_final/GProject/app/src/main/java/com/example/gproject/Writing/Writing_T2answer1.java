package com.example.gproject.Writing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gproject.MainActivity;
import com.example.gproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.IOException;

public class Writing_T2answer1 extends AppCompatActivity {
    Bundle bundle = new Bundle();
    TextView Ques,Ans;
    TextView question;
    FirebaseAuth auth;
    private DatabaseReference databaseReference;
    int QuesNum;

    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writing_t2answer1);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        question = findViewById(R.id.question);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        back = findViewById(R.id.back2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(Speaking_part1_answer.this, Speaking_questionAdd.class));
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {

            if (intent.hasExtra("num")) {
                QuesNum = intent.getIntExtra("num",1);

            }
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collection = db.collection("Writing2");
        DocumentReference doc = collection.document(String.valueOf(QuesNum));
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String ques = documentSnapshot.getString("Question");
                    question.setText(ques);

                } else {
                    Toast.makeText(Writing_T2answer1.this, "資料庫不存在", Toast.LENGTH_SHORT).show();


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Writing_T2answer1.this, "db連接失敗", Toast.LENGTH_SHORT).show();
            }
        });


        Ques = findViewById(R.id.question);
        Ans = findViewById(R.id.answer);
        Ans.setText("English is a gateway to the world and no one should be deprived of the opportunities that\n" +
                "arise for those with a strong command of the English language. This does not mean that\n" +
                "local languages are dispensable, however. They have an important role to play in\n" +
                "supporting education, including English language education.\n" +
                "People who insist on prioritising local languages are often those who can speak fluent\n" +
                "English themselves. Advocates of minority languages need to be fluent in English in\n" +
                "order to research their field and support the cause at international conferences. In doing\n" +
                "so, however, they are also demonstrating why English is so important. Learning English\n" +
                "allows people to gain knowledge, communicate with society, further themselves as\n" +
                "individuals and have a global impact. It is unfair to willingly dispossess people of this\n" +
                "ability in order to preserve tradition.\n");
    }
    public void finishClick(View view) {
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            String answerText = Ans.getText().toString();

            DatabaseReference userAnswersRef = databaseReference
                    .child("users")
                    .child(userId)
                    .child("Writing")
                    .child("2")
                    .child(String.valueOf(QuesNum))
                    .push(); // 使用 push() 生成唯一键

            userAnswersRef.setValue(answerText)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // 存储成功
                                Intent intent = new Intent(Writing_T2answer1.this, W_Judge_P1.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("Ques", Ques.getText().toString());
                                bundle.putString("Ans", Ans.getText().toString());
                                bundle.putString("Part", "2");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            } else {
                                // 存储失败
                                // 处理存储失败的情况
                            }
                        }
                    });
        }
    }




    public void homeClick(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}