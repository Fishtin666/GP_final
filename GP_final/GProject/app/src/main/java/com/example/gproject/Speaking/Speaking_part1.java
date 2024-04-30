package com.example.gproject.Speaking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.example.gproject.Adapters.TopicAdapter;
import com.example.gproject.Models.TopicModel;
import com.example.gproject.R;

import java.util.ArrayList;

public class Speaking_part1 extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<TopicModel>list;
    TopicAdapter adapter;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speaking_part1);

        back = findViewById(R.id.back2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        recyclerView = findViewById(R.id.aiChat_recy);
        list=new ArrayList<>();

        GridLayoutManager layoutManager =new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        list.add(new TopicModel("Study",R.drawable.study,0));
        list.add(new TopicModel("Work",R.drawable.work,0));
        list.add(new TopicModel("Hometown",R.drawable.hometown,0));
        list.add(new TopicModel("Accommodation",R.drawable.accommodation,0));
        list.add(new TopicModel("Family",R.drawable.family,0));
        list.add(new TopicModel("Friend",R.drawable.friend,0));
        list.add(new TopicModel("Entertainment",R.drawable.entertainment,0));
        list.add(new TopicModel("Childhood",R.drawable.childhood,0));
        list.add(new TopicModel("Daily life",R.drawable.daily_life,0));

        adapter =new TopicAdapter(this,list);
        recyclerView.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Speaking_part1.this,Speaking.class));
            }
        });
    }



}