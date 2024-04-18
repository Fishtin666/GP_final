package com.example.gproject.Speaking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import com.example.gproject.Adapters.TopicAdapter;
import com.example.gproject.Models.TopicModel;
import com.example.gproject.R;

import java.util.ArrayList;

public class Speaking_part2 extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<TopicModel>list;
    TopicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speaking_part2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        recyclerView = findViewById(R.id.aiChat_recy);
        list=new ArrayList<>();

        GridLayoutManager layoutManager =new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        list.add(new TopicModel("People",R.drawable.friend,0));
        list.add(new TopicModel("Place",R.drawable.accommodation,0));
        list.add(new TopicModel("Item",R.drawable.study,0));
        list.add(new TopicModel("Experience",R.drawable.hometown,0));

        adapter =new TopicAdapter(this,list);
        recyclerView.setAdapter(adapter);
    }

}                                                                                                                                