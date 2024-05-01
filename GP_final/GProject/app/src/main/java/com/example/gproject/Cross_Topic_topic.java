package com.example.gproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.example.gproject.Adapters.TopicAdapter;
import com.example.gproject.Models.TopicModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Cross_Topic_topic extends AppCompatActivity {
    RecyclerView recyclerView;
    TopicAdapter adapter;
    FirebaseAuth auth;
    ArrayList<TopicModel>list;

    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cross_topic_topic);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        back=findViewById(R.id.back2);
        recyclerView = findViewById(R.id.recy);
        list = new ArrayList<>();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        list.add(new TopicModel("Study",R.drawable.study,2));
        list.add(new TopicModel("Work",R.drawable.work,2));
        list.add(new TopicModel("Hometown",R.drawable.hometown,2));
        list.add(new TopicModel("Accommodation",R.drawable.accommodation,2));
        list.add(new TopicModel("Family",R.drawable.family,2));
        list.add(new TopicModel("Friend",R.drawable.friend,2));
        list.add(new TopicModel("Entertainment",R.drawable.entertainment,2));
        list.add(new TopicModel("Childhood",R.drawable.childhood,2));
        list.add(new TopicModel("Daily life",R.drawable.daily_life,2));

        adapter = new TopicAdapter(this, list);
        recyclerView.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}