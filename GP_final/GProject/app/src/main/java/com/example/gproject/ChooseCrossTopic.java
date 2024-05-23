package com.example.gproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.gproject.Adapters.TopicAdapter;
import com.example.gproject.Models.TopicModel;

import java.util.ArrayList;

public class ChooseCrossTopic extends AppCompatActivity {

    RecyclerView recy;
    ArrayList<TopicModel>list;
    TopicAdapter adapter;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_cross_topic);
        recy=findViewById(R.id.recy);
        back = findViewById(R.id.back2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        list=new ArrayList<>();

        GridLayoutManager layoutManager =new GridLayoutManager(this,2);
        recy.setLayoutManager(layoutManager);

        list.add(new TopicModel("Study",R.drawable.study,3));
        list.add(new TopicModel("Work",R.drawable.work,3));
        list.add(new TopicModel("Hometown",R.drawable.hometown,3));
        list.add(new TopicModel("Accommodation",R.drawable.accommodation,3));
        list.add(new TopicModel("Family",R.drawable.family,3));
        list.add(new TopicModel("Friend",R.drawable.friend,3));
        list.add(new TopicModel("Entertainment",R.drawable.entertainment,3));
        list.add(new TopicModel("Childhood",R.drawable.childhood,3));
        list.add(new TopicModel("Daily life",R.drawable.daily_life,3));
        list.add(new TopicModel("自訂",R.drawable.dic2,3));

        adapter =new TopicAdapter(this,list);
        recy.setAdapter(adapter);
    }
}