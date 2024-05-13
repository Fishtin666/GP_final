//package com.example.gproject.reading;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageButton;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.gproject.R;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//public class ChoseTestActivity2 extends AppCompatActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.chos_test);
//
//        ImageButton backButton = findViewById(R.id.back);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ChoseTestActivity2.this, R_topic.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        //get number from R_topic
//        int R_number = getIntent().getIntExtra("R_topic", 0);
//        Button test1 = findViewById(R.id.test1);
//        Button test2 = findViewById(R.id.test2);
//        Button test3 = findViewById(R.id.test3);
//        test1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                        ChoseTestNum(R_number,1);
//            }
//        });
//        test2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ChoseTestNum(R_number,2);
//            }
//        });
//        test3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ChoseTestNum(R_number,3);
//            }
//        });
//
//    }
//    //set chose to next activity
//    private void ChoseTestNum(int number2, int anotherNumber) {
//        Intent intent = null;
//        switch (number2) {
//            case 1:
//                intent = new Intent(this, R_blank.class);
//                break;
//            case 2:
//                intent = new Intent(this, R_judge.class);
//                break;
//            case 3:
//                intent = new Intent(this, R_chose.class);
//                break;
//            case 4:
//                intent = new Intent(this, R_match.class);
//                break;
//            case 5:
//                intent = new Intent(this, R_multiple.class);
//                break;
//            default:
//                Log.e("chose error", "Invalid number2: " + number2);
//                return;
//        }
//        intent.putExtra("ChoseNumber", anotherNumber);
//        startActivity(intent);
//        finish();
//    }
//}