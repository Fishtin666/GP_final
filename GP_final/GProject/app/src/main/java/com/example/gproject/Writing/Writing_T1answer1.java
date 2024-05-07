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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gproject.MainActivity;
import com.example.gproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
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
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class Writing_T1answer1 extends AppCompatActivity {
    TextView question;
    int QuesNum;
    FirebaseAuth auth;
    private DatabaseReference databaseReference;
    StorageReference storageRef;

    ImageView pic;

    ImageButton back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writing_t1answer1);
        //FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        back = findViewById(R.id.back2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(Speaking_part1_answer.this, Speaking_questionAdd.class));
                finish();
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        question = findViewById(R.id.question);
        pic = findViewById(R.id.pic);

        Intent intent = getIntent();
        if (intent != null) {

            if (intent.hasExtra("num")) {
                QuesNum = intent.getIntExtra("num",1);

            }
        }







        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collection = db.collection("Writing");
        DocumentReference doc = collection.document(String.valueOf(QuesNum));
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String ques = documentSnapshot.getString("Question");
                    question.setText(ques);
                    storageRef= FirebaseStorage.getInstance().getReference("Writing_Part1/"+QuesNum+".png");
                    try {
                        File localfile =File.createTempFile("tempfile",".png");
                        storageRef.getFile(localfile)
                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                        pic.setImageBitmap(bitmap);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                } else {
                    Toast.makeText(Writing_T1answer1.this, "資料庫不存在", Toast.LENGTH_SHORT).show();


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Writing_T1answer1.this, "db連接失敗", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void nextClick(View v){
//        Intent intent=new Intent(Writing_T1answer1.this,Writing_T1answer2.class);
//        intent.putExtra("Ans",Writing_T1answer2.PassAns);
//        startActivity(intent);

        Intent intent = new Intent(Writing_T1answer1.this, Writing_T1answer2.class);
        Bundle bundle = new Bundle();
        bundle.putString("Ans", Writing_T1answer2.PassAns);
        bundle.putInt("num",QuesNum);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    public void homeClick(View v){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}