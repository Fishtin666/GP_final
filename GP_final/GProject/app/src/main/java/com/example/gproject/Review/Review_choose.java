package com.example.gproject.Review;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.gproject.R;

public class Review_choose extends AppCompatActivity {

    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_choose);

        Spinner spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this,R.array.Review_array, android.R.layout.simple_dropdown_item_1line);
        spinner2.setAdapter(adapter1);

        manager = getSupportFragmentManager();

        ImageButton pencil = findViewById(R.id.pencil2);

        pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Review_choose.this, Review_main.class);
                startActivity(intent);
                finish();
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        loadFragment(new Fragment_Review_reading());
                        break;
                    case 1:
                        loadFragment(new Fragment_Review_listening());
                        break;
                    case 2:
                        loadFragment(new Fragment_Review_writing());
                        break;
                    case 3:
                        loadFragment(new Fragment_Review_speaking());
                        break;
                    case 4:
                        loadFragment(new Fragment_Review_VR());
                        break;
                    default:
                        // 默认加载第一个 Fragment
                        loadFragment(new Fragment_Review_reading());
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.center, fragment);
        transaction.commit();
    }
}