package com.example.gproject.Writing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.gproject.MainActivity;
import com.example.gproject.QuesNumAdd;
import com.example.gproject.R;

public class W_topic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.w_topic);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }
    }

    Bundle bundle=new Bundle();
    public void T1Click(View view){
        bundle.putString("task", String.valueOf(1));
        Intent intent =new Intent(this, QuesNumAdd.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    public void T2Click(View view){
        bundle.putString("task", String.valueOf(2));
        Intent intent =new Intent(this, QuesNumAdd.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    public void homeClick(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}