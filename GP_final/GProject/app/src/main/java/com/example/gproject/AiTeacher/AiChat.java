package com.example.gproject.AiTeacher;

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
import com.example.gproject.R;

import java.util.ArrayList;


public class AiChat extends AppCompatActivity {

    ArrayList<TopicModel> list;
    TopicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ai_chat);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        ImageButton back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView recy = findViewById(R.id.aiChat_recy);
        list=new ArrayList<>();
        GridLayoutManager layoutManager =new GridLayoutManager(this,2);
        recy.setLayoutManager(layoutManager);
        adapter =new TopicAdapter(this,list);
        recy.setAdapter(adapter);

        list.clear();
        list.add(new TopicModel("Travel",R.drawable.travel,1));
        list.add(new TopicModel("Hobby",R.drawable.hobby,1));
        list.add(new TopicModel("Movie",R.drawable.movie,1));
        list.add(new TopicModel("Sport",R.drawable.sport,1));
        list.add(new TopicModel("Weather",R.drawable.weather,1));
        list.add(new TopicModel("Country",R.drawable.country,1));
        list.add(new TopicModel("Food",R.drawable.food,1));
        list.add(new TopicModel("Pet",R.drawable.pet,1));

        adapter.notifyDataSetChanged();
    }
}