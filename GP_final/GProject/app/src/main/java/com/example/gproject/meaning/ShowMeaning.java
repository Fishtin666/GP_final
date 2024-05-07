package com.example.gproject.meaning;

import static java.util.Collections.emptyList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gproject.R;
import com.example.gproject.WordCard.WordResult2;
import com.example.gproject.WordCard.WordTopicActivity;
import com.example.gproject.databinding.WordDicSearchBinding;
import com.example.gproject.reading.R_topic;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Response;

public class ShowMeaning extends AppCompatActivity {
    private String currentWord, definitions;
    private final AtomicBoolean isStarred = new AtomicBoolean(false);
    private WordDicSearchBinding binding;
    private MeaningAdapter adapter;
    int is;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = WordDicSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {
            ImageView star = findViewById(R.id.starButton);
            star.setVisibility(View.INVISIBLE);
            ImageButton backButton = findViewById(R.id.back);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ShowMeaning.this, WordTopicActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            //Search Word
            binding.searchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentWord = binding.searchInput.getText().toString();
                    isWordCollected(currentWord);
                    getMeaning(currentWord);
                    star.setVisibility(View.VISIBLE);
                }
            });
            collect_word(star);

            adapter = new MeaningAdapter(emptyList());
            binding.meaningRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            binding.meaningRecyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("showMeaning", "error: " + e.getMessage());
        }
    }

    // Change StarButton's state
    public void collect_word(ImageView star) {
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isWordCollected(currentWord) == 1) {
                    // If ir has already been collected it, click to cancel it.
                    deleteCollectData();
                    binding.starButton.setImageResource(R.drawable.star_black);

                    Log.e("collect", "delete： " + isWordCollected(currentWord));
                } else {
                    // If it hasn't been collected, click to collect it.
                    insertCollectData();
                    binding.starButton.setImageResource(R.drawable.star_yellow);

                    Log.e("collect", "collect：" + isWordCollected(currentWord));
                }
            }
        });
    }
    //Insert Collect Word
    public void insertCollectData() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root = db.getReference("word_collect");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userId = user.getUid();

            String word = binding.wordTextview.getText().toString();
            String speechText = binding.phoneticTextview.getText().toString();
            MeaningAdapter.MeaningViewHolder viewHolder = (MeaningAdapter.MeaningViewHolder) binding.meaningRecyclerView.findViewHolderForAdapterPosition(0);
            String definitionsText = viewHolder.getDefinitionsText();
            DataHolder obj = new DataHolder(definitionsText, speechText, 0);

            root.child(userId).child(word).setValue(obj);
            Log.e("collect", "Added to collection " + userId + 1);

        } else {
            Log.e("collect", "User not authenticated");
        }
    }
    //Delete Collect Word
    public void deleteCollectData() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root = db.getReference("word_collect");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userId = user.getUid();

            String word = binding.wordTextview.getText().toString();
            String SpeechText = binding.phoneticTextview.getText().toString();
            MeaningAdapter.MeaningViewHolder viewHolder = (MeaningAdapter.MeaningViewHolder) binding.meaningRecyclerView.findViewHolderForAdapterPosition(0); // 这里假设只有一项，你需要根据实际情况调整
            String definitionsText = viewHolder.getDefinitionsText();
            DataHolder obj = new DataHolder(definitionsText, SpeechText, 0);

            root.child(userId).child(word).removeValue();
            Log.e("collect", "Added to collection " + userId + 2);

        } else {
            Log.e("collect", "User not authenticated");
        }
    }
    //Identify whether word is collected in Database
    private int isWordCollected(final String word) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root = db.getReference("word_collect");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        root.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    boolean isCollected = dataSnapshot.hasChild(word);
                    if (isCollected) {
                        binding.starButton.setImageResource(R.drawable.star_yellow);
                        is = 1;
                        Log.d("FirebaseData", "Word '" + word + is);
                    } else {
                        binding.starButton.setImageResource(R.drawable.star_black);
                        Log.d("FirebaseData", "Word '" + word + is);
                        is = 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("WordListActivity", "Failed with error: " + e.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Read data error: " + databaseError.getMessage());
            }
        });
        return is;
    }

    public void getMeaning(String word) {
        setInProgress(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<List<WordResult2>> response = RetrofitInstance.getInstance()
                            .create(DictionaryApi.class)
                            .getMeaning(word)
                            .execute();

                    if (response.body() == null) {
                        throw new Exception();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setInProgress(false);
                            List<WordResult2> results = response.body();
                            if (results != null && !results.isEmpty()) {
                                setUI(results.get(0));
                            }
                        }
                    });
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setInProgress(false);
                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void setUI(WordResult2 response) {
        binding.wordTextview.setText(response.getWord());
        binding.phoneticTextview.setText(response.getPhonetic());
        adapter.updateNewData(response.getMeanings());
    }

    private void setInProgress(boolean inProgress) {
        if (inProgress) {
            binding.searchBtn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.searchBtn.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }


}