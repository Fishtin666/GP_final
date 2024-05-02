package com.example.gproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class getDb extends AppCompatActivity {
    FirebaseAuth auth;
    private DatabaseReference databaseReference;
    TextView ans;

    Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_db);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        ans=findViewById(R.id.answer);
        search = findViewById(R.id.button5);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                W_getOneQuestion_all();
            }
        });

    }

    //取writing某一題的所有作答
    public void W_getOneQuestion_all(){
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();


            DatabaseReference targetRef = databaseReference
                    .child("users")
                    .child(userId)
                    .child("Writing")
                    .child("1")
                    .child(String.valueOf(1));  //###選擇是第幾題####


            targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                            String key = childSnapshot.getKey();
                            Object value = childSnapshot.getValue();

                            ans.setText(String.valueOf(value));
                            //System.out.println("Key: " + key + ", Value: " + value);
                        }
                    } else {


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }
    }
}