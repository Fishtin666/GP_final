package com.example.gproject.AiTeacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.example.gproject.Adapters.QuestionNumberAdapter;
import com.example.gproject.MainActivity;
import com.example.gproject.Models.QuestionNumberModel;
import com.example.gproject.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AiTeacherHelp extends AppCompatActivity {
    ArrayList<QuestionNumberModel> list;
    QuestionNumberAdapter adapter;
    RecyclerView recyclerView;
    String help,help_answer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aiteacherhelp);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        recyclerView = findViewById(R.id.QuesNum_Recy);

        list =new ArrayList<>();

        LinearLayoutManager manager =new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            help=extras.getString("help_question");

        }
        //list.add(new QuestionNumberModel(help,-1));
        String regex="([^0-9]+)";
        //String regex="\\d+\\..*?\\.";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(help);
        while (matcher.find()) {
            list.add(new QuestionNumberModel(matcher.group().trim(),-1,false));
        }






        adapter = new QuestionNumberAdapter(this,list);
        recyclerView.setAdapter(adapter);

        }
    public void finishCurrentActivity() {
        finish();
    }

}