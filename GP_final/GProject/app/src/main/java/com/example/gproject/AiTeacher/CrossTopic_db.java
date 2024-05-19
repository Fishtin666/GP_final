package com.example.gproject.AiTeacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gproject.Adapters.CrossAdapter;
import com.example.gproject.MainActivity;
import com.example.gproject.Models.TopicModel;
import com.example.gproject.R;
import com.example.gproject.ReviewShow_Speaking;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CrossTopic_db extends AppCompatActivity {


    CrossAdapter adapter;
    ArrayList<String> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cross_topic_db);

        RecyclerView recy=findViewById(R.id.recy);
        ImageButton back=findViewById(R.id.back2);
        ImageView home=findViewById(R.id.home2);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CrossTopic_db.this, MainActivity.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getDb();

        LinearLayoutManager layoutManager =new LinearLayoutManager(this);
        recy.setLayoutManager(layoutManager);
        adapter=new CrossAdapter(this,list);
        recy.setAdapter(adapter);

    }

    public void getDb(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference targetRef = databaseReference
                    .child("CrossTopic")
                    .child(userId)
                    .child("Hometown");



            targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String ques = childSnapshot.child("Ques").getValue(String.class);
                        String ans = childSnapshot.child("Ans").getValue(String.class);

                       list.add(ques);
                       adapter.notifyDataSetChanged();
                       //Toast.makeText(CrossTopic_db.this, list.toString(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }



    }

}