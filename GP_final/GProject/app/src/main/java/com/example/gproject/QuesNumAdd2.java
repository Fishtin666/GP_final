package com.example.gproject;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gproject.Adapters.QuestionNumberAdapter;
import com.example.gproject.Models.QuestionNumberModel;

import java.util.ArrayList;

public class QuesNumAdd2 extends AppCompatActivity {


    ArrayList<QuestionNumberModel> list;
    QuestionNumberAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ques_num_add);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        recyclerView = findViewById(R.id.QuesNum_Recy);

        list =new ArrayList<>();

        LinearLayoutManager manager =new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);




        for(int i=1;i<13;i++)
            list.add(new QuestionNumberModel("考古題"+i,1,false));



        adapter = new QuestionNumberAdapter(this,list);
        recyclerView.setAdapter(adapter);
    }
}
