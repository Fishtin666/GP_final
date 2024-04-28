package com.example.gproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gproject.WordCard.WordCardActivity;
import com.example.gproject.Adapters.WordListAdapter;
import com.example.gproject.Adapters.WordListData;
import com.example.gproject.WordCard.WordTopicActivity;
import com.example.gproject.reading.R_topic;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WordListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WordListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pra_word);

        Button send = findViewById(R.id.wordSend);
        send.setVisibility(View.INVISIBLE);

        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WordListActivity.this, WordTopicActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 初始化 RecyclerView
        recyclerView = findViewById(R.id.rcyQ);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // set Adapter
        List<WordListData> wordListDataList = new ArrayList<>();
        adapter = new WordListAdapter(wordListDataList);
        recyclerView.setAdapter(adapter);

        // set Word Data
        setCollectWordData();

        adapter.setOnItemClickListener(new WordListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Launch the WordCardActivity and pass the clicked position.
                Intent intent = new Intent(WordListActivity.this, WordCardActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }
    private void setCollectWordData() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root = db.getReference("word_collect");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        root.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot wordSnapshot : dataSnapshot.getChildren()) {
                    try {
                        String word = wordSnapshot.getKey();
                        DataSnapshot speechTextSnapshot = wordSnapshot.child("speechText");
                        String speechText = speechTextSnapshot.getValue(String.class);

                        if (speechTextSnapshot.exists()) {
                            WordListData cardData = new WordListData(word, speechText);
                            adapter.getDataList().add(cardData); // Add Word into adapter
                            Log.d("FirebaseData", "Key: " + word + ", speechText: " + cardData);
                        } else {
                            Log.d("FirebaseData2", "Key: " + word + ", speechText: " + speechText);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("WordListActivity", "Failed with error: " + e.getMessage());
                    }
                }
                adapter.notifyDataSetChanged(); // refresh RecyclerView
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Read data error: " + databaseError.getMessage());
            }
        });
    }
}
