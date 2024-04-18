package com.example.gproject.WordCard;

import static java.util.Collections.emptyList;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gproject.R;
import com.example.gproject.WordListActivity;
import com.example.gproject.Adapters.WordListData;
import com.example.gproject.databinding.AddCardActivityBinding;
import com.example.gproject.meaning.DictionaryApi;
import com.example.gproject.meaning.MeaningAdapter;
import com.example.gproject.meaning.RetrofitInstance;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Response;

public class WordCardActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private GestureDetector gestureDetector;
    private MeaningAdapter MeaningAdapter;
    private AddCardActivityBinding binding;
    private int currentPos = 0;
    private List<WordListData> dataList;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddCardActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CardView flashcardQuestion = findViewById(R.id.flashcard_cardview);
        RecyclerView flashcardHint = findViewById(R.id.card_recycler_view);

        // 初始化 dataList
        dataList = new ArrayList<>();
        setCollectWordData();

        // 初始化 TextToSpeech
        textToSpeech = new TextToSpeech(this, this);
        //soundButton
        ImageButton sound = findViewById(R.id.sound);
        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textToSpeech != null) {
                    String wordText = binding.wordTextview.getText().toString();
                    // use TextToSpeech reading
                    textToSpeech.speak(wordText, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
        });
        // Gesture
        gestureDetector = new GestureDetector(this, new GestureListener());

        MeaningAdapter = new MeaningAdapter(emptyList());
        binding.cardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.cardRecyclerView.setAdapter(MeaningAdapter);

        //backButton
        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WordCardActivity.this, WordListActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // 獲取傳遞的數據
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        // 初始化currentPos
        currentPos = position;

        String word = intent.getStringExtra("word");
        String phonetic = intent.getStringExtra("phonetic");
        // refresh UI
        TextView wordTextView = findViewById(R.id.word_textview);
        TextView phoneticTextView = findViewById(R.id.phonetic_textview);
        wordTextView.setText(word);
        phoneticTextView.setText(phonetic);

        // word TouchListener
        flashcardQuestion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        // User can tap on question to see answer
        flashcardQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcardQuestion.setCameraDistance(25000);
                flashcardHint.setCameraDistance(25000);
                String wordText = binding.wordTextview.getText().toString();
                flashcardQuestion.animate()
                        .rotationY(90)
                        .setDuration(200)
                        .withEndAction(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        flashcardQuestion.setVisibility(View.INVISIBLE);
                                        flashcardHint.setVisibility(View.VISIBLE);
                                        // second quarter turn
                                        flashcardHint.setRotationY(-90);
                                        flashcardHint.animate()
                                                .rotationY(0)
                                                .setDuration(200)
                                                .start();
                                    }
                                }
                        ).start();
                binding.sound.setVisibility(View.INVISIBLE);
                getMeaning(wordText);
                Log.e("card", "success to get meaning");
            }
        });

        // User can tap on answer to toggle back to question
        MeaningAdapter.setOnItemClickListener(new MeaningAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                flashcardQuestion.setCameraDistance(25000);
                flashcardHint.setCameraDistance(25000);

                flashcardHint.animate()
                        .rotationY(90)
                        .setDuration(200)
                        .withEndAction(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        flashcardHint.setVisibility(View.INVISIBLE);
                                        flashcardQuestion.setVisibility(View.VISIBLE);
                                        // second quarter turn
                                        flashcardQuestion.setRotationY(-90);
                                        flashcardQuestion.animate()
                                                .rotationY(0)
                                                .setDuration(200)
                                                .start();
                                    }
                                }
                        ).start();
                binding.sound.setVisibility(View.VISIBLE);
            }
        });
    }

    //TextToSpeech setting
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // setting English
            int result = textToSpeech.setLanguage(Locale.ENGLISH);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TextToSpeech", "Language not supported");
            }
        } else {
            Log.e("TextToSpeech", "Initialization failed");
        }
    }

    @Override
    protected void onDestroy() {
        // release TextToSpeech resource
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    //collect wordData from firebase
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
                        WordListData cardData = new WordListData(word, speechText);
                        dataList.add(cardData); // Add Word into adapter
                        Log.d("FirebaseData", "Key: " + word + ", speechText: " + cardData);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("WordCardActivity", "Failed with error: " + e.getMessage());
                    }
                }
                //show click wordcard
                if (!dataList.isEmpty()) {
                    showWordData(currentPos);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Read data error: " + databaseError.getMessage());
            }
        });
    }

    // According to index to show WordData
    private void showWordData(int position) {
        WordListData currentItem = dataList.get(position);
        binding.wordTextview.setText(currentItem.getWord());
        binding.phoneticTextview.setText(currentItem.getPhonetic());
    }

    // GestureListener
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float diffX = e2.getX() - e1.getX();
                float diffY = e2.getY() - e1.getY();
                if (Math.abs(diffX) > Math.abs(diffY) &&
                        Math.abs(diffX) > SWIPE_THRESHOLD &&
                        Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        // right
                        showPreviousFlashcard();
                    } else {
                        // left
                        showNextFlashcard();
                    }
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    // show previous Flashcard (right)
    private void showPreviousFlashcard() {
        if (currentPos < dataList.size() - 1) {
            currentPos++;
            showWordData(currentPos);

            // loading slide animation
            Animation leftInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
            Animation rightOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
            // set AnimationListener to change wordcard
            rightOutAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    binding.flashcardCardview.setVisibility(View.VISIBLE);
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            // set Right slide animation
            binding.flashcardCardview.startAnimation(leftInAnim);
            binding.wordTextview.startAnimation(rightOutAnim);
            binding.phoneticTextview.startAnimation(rightOutAnim);
        }
        Log.e("WordCardActivity", "left" + currentPos);
    }
    //show next Flashcard (left)
    private void showNextFlashcard() {
        if (currentPos > 0) {
            currentPos--;
            showWordData(currentPos);

            // loading slide animation
            Animation leftOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
            Animation rightInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
            Animation rightOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
            // set AnimationListener to change wordcard
            leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    binding.flashcardCardview.setVisibility(View.VISIBLE);
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            // set Left slide animation
            binding.flashcardCardview.startAnimation(rightInAnim);
            binding.wordTextview.startAnimation(rightOutAnim);
            binding.phoneticTextview.startAnimation(rightOutAnim);
        }
        Log.e("WordCardActivity", "right" + currentPos);
    }
    public void getMeaning(String word) {
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
        MeaningAdapter.updateNewData(response.getMeanings());
    }
}

