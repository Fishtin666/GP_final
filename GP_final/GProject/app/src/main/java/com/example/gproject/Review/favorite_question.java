package com.example.gproject.Review;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gproject.Adapters.QuestionAdapter;
import com.example.gproject.Adapters.QuestionNumberAdapter;
import com.example.gproject.MainActivity;
import com.example.gproject.Models.QuestionModel;
import com.example.gproject.Models.QuestionNumberModel;
import com.example.gproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class favorite_question extends AppCompatActivity {

    RecyclerView recyclerFav;
    Button speaking,writing;

    ArrayList<QuestionModel>list;
    QuestionAdapter adapter;
    ArrayList<QuestionNumberModel> list2;
    QuestionNumberAdapter adapter2;

    FirebaseAuth auth;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_favorite_question);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerFav = findViewById(R.id.favquestion);
        speaking = findViewById(R.id.speaking);
        writing = findViewById(R.id.writing);

        list = new ArrayList<>();
        list2 =new ArrayList<>();
        LinearLayoutManager layoutManager =new LinearLayoutManager(this);
        recyclerFav.setLayoutManager(layoutManager);


//        LinearLayoutManager manager =new LinearLayoutManager(this);
//        recyclerFav.setLayoutManager(manager);

        adapter = new QuestionAdapter(this,list);
        adapter2 = new QuestionNumberAdapter(this,list2);
        recyclerFav.setAdapter(adapter);

        speaking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speaking.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(favorite_question.this,R.color.darkGrey)));
                writing.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(favorite_question.this,R.color.gray)));
                loadQuestions("Speaking");
            }
        });
        writing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writing.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(favorite_question.this,R.color.darkGrey)));
                speaking.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(favorite_question.this,R.color.gray)));
                loadQuestions("Writing");
            }
        });

        //back button
        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(favorite_question.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    @NonNull
    public void loadQuestions(String category) {
        Log.d("FavoriteQuestion", "开始加载" + category + "数据");
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference targetRef = databaseReference
                    .child("empty_question")
                    .child(userId)
                    .child(category);
            // Clear the list and notify the adapter
            if (category.equals("Speaking")) {
                list.clear();
                adapter.notifyDataSetChanged();
                recyclerFav.setAdapter(adapter);
            } else if (category.equals("Writing")) {
                list2.clear();
                adapter2.notifyDataSetChanged();
                recyclerFav.setAdapter(adapter2);
            }
            targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            int key = Integer.parseInt(childSnapshot.getKey());
                            Log.d("Key", String.valueOf(key)); //speaking是part1或part2 "OR" writing是task1還是task2
                            // 获取子节点下的所有子节点名称
                            for (DataSnapshot subChildSnapshot : childSnapshot.getChildren()) {
                                String subKey = subChildSnapshot.getKey();
                                Log.d("SubKey", subKey); //topic "OR" question幾
                                if (category.equals("Writing")) {
                                    list2.add(new QuestionNumberModel("Task "+key+"\nQuestion"+subKey,key,true));
                                    Log.d("fav Writing","Task "+key+"  Question"+subKey+","+key);
                                    //adapter2.notifyDataSetChanged();

                                }
                                // 获取子节点下的所有子节点名称
                                for (DataSnapshot subSubChildSnapshot : subChildSnapshot.getChildren()) {
                                    String subSubKey = subSubChildSnapshot.getKey();
                                    String question = (String) subSubChildSnapshot.getValue();
                                    Log.d("SubSubKey", subSubKey);
                                    Log.d("收藏的ref",key+"-"+"-"+subKey+"-"+subSubKey);
                                    if(category.equals("Speaking")) {
                                        list.add(new QuestionModel("Part " + String.valueOf(key) + "&Part3\n" + question, key, subKey,true));
                                        //adapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    }
                    if (category.equals("Speaking")) {
                        adapter.notifyDataSetChanged();
                    } else if (category.equals("Writing")) {
                        adapter2.notifyDataSetChanged();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
}