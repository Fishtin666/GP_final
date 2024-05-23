package com.example.gproject.Writing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gproject.MainActivity;
import com.example.gproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Writing_T1answer2 extends AppCompatActivity {
    int QuesNum;

    EditText Ans;
    Bundle bundle=new Bundle();

    public static String PassAns;
    private DatabaseReference databaseReference;
    FirebaseAuth auth;
    ImageButton back;



    String ans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writing_t1answer2);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        back = findViewById(R.id.back2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(Speaking_part1_answer.this, Speaking_questionAdd.class));
                PassAns = Ans.getText().toString();
                finish();
            }
        });

        //QuesNum=getIntent().getIntExtra("QuesNum",1);
        Ans=findViewById(R.id.editText);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ans = extras.getString("Ans");
            QuesNum = extras.getInt("num");


        }
        Ans.setText(ans);



//        Ans.setText("The chart shows the percentages of paper and cardboard, glass containers, aluminium" +
//                "cans and plastics that were recycled in one country between 1982 and 2010." +
//                "In 1982, about 65% of paper and cardboard was recycled. This figure fluctuated before" +
//                "rising steeply to reach a peak of 80% in 1994. From then on, however, it decreased" +
//               "steadily to a level of 70% in 2010. In 1982, half of all glass containers were recycled;" +
//               "after dipping to a low of 40% in 1990, the glass recycling rate gradually increased to 60%" +
//                "by 2010.");

    }


    public void previousCLick(View v){
//        Intent intent=new Intent(this, Writing_T1answer1.class);
//        bundle.putInt("QuesNum",QuesNum);
//        bundle.putString("Ans",Ans.getText().toString());
//        startActivity(intent);
        PassAns = Ans.getText().toString();
        finish();
    }

    public void finishClick(View v){
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            String answerText = Ans.getText().toString();


            // 生成唯一的键，并存储答案
            DatabaseReference userAnswersRef = databaseReference
                    .child("users")
                    .child(userId)
                    .child("Writing")
                    .child("1")
                    .child(String.valueOf(QuesNum))
                    .push(); // 使用 push() 生成唯一键

            String answerKey = userAnswersRef.getKey();

            userAnswersRef.setValue(answerText)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // 存储成功
                                //Toast.makeText(Writing_T1answer2.this,answerKey , Toast.LENGTH_SHORT).show();

                                bundle.putString("Ans",Ans.getText().toString());
                                bundle.putString("pushKey",answerKey);
                                bundle.putString("part","1");
                                Intent intent =new Intent(Writing_T1answer2.this, W_Judge_P1.class);
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
        Intent intent = new Intent(this,W_topic.class);
        startActivity(intent);
    }

}