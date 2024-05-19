package com.example.gproject.Speaking;

import static android.content.ContentValues.TAG;

import static com.example.gproject.MainActivity.apiKey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gproject.MainActivity;
import com.example.gproject.R;
import com.example.gproject.dictionary.MeaningAdapter;
import com.example.gproject.dictionary.RetrofitInstance;
import com.example.gproject.dictionary.WordResult;
import com.example.gproject.meaning.DataHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Speaking_part2_answer extends AppCompatActivity {

    TextView answer,taskcard,time;
    String answerKey;
    ImageButton back;
    ImageView home;

    String[] words;

    Boolean eye_open = true,clockup = true,star_yellow=false,indic=true,send_1=false;;

    Bundle bundle=new Bundle();

    ImageView eye,hint,clock,voice;
    ProgressBar progressBar;

    String Ques,ROf_ques,selectedWord, Num,hintanswer,Topic;

    TextToSpeech tts;

    MeaningAdapter adapter;
    ImageView mic;

    private PopupWindow popupWindow,popup;
    private CountDownTimer timer;

    private static String speechSubscriptionKey = "f8d71f0e97f6438db956ebf642eabf78";
    private static String serviceRegion = "eastus";

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    //訊息超時timeout改為60s(預設:10s)
    OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speaking_part2_answer);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR)
                    tts.setLanguage(Locale.UK);
            }
        });

        answer = findViewById(R.id.listenContent);
        taskcard = findViewById(R.id.TaskCard);
        time = findViewById(R.id.time);
        answer.setMovementMethod(new ScrollingMovementMethod());
        eye = findViewById(R.id.eye);
        hint = findViewById(R.id.hint);
        progressBar = findViewById(R.id.progressBar);
        clock = findViewById(R.id.clock);
        voice = findViewById(R.id.voice);
        progressBar.setVisibility(View.INVISIBLE);
        back = findViewById(R.id.back2);
        home=findViewById(R.id.home);
        mic  =findViewById(R.id.mic);
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mic.setImageResource(R.drawable.mic_gray);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        micClick();
                    }
                }, 100); //
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Speaking_part2_answer.this, MainActivity.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //Ques= getIntent().getStringExtra("question");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Ques=extras.getString("question");
            Num =extras.getString("num");
            Topic = extras.getString("topic");
            //Toast.makeText(Speaking_part2_answer.this, Num, Toast.LENGTH_SHORT).show();
        }
        ROf_ques = getIntent().getStringExtra("ROf_ques");

        taskcard.setText(Ques);

        if(S_Judge_P2.ROf_ques==true){
            taskcard.setText(ROf_ques);
            S_Judge_P2.ROf_ques=false;
        }

        long countdownTimeInMillis = 6000; // 60秒

        // 创建并启动倒计时器
        timer = new CountDownTimer(countdownTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 每秒更新倒计时显示
                long secondsUntilFinished = millisUntilFinished / 1000;
                String formattedTime = String.format("0:%02d", secondsUntilFinished);
                time.setText(formattedTime);
            }


            @Override
            public void onFinish() {
                // 倒计时结束时的操作
                AlertDialog.Builder P2_start = new AlertDialog.Builder(Speaking_part2_answer.this);
                P2_start.setTitle("請開始回答");
                P2_start.setMessage("TaskCard 閱讀時間結束，請開始回答，您最多可以回答兩分鐘。");
                P2_start.setCancelable(false);
                P2_start.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        taskcard.setTextColor(Color.TRANSPARENT);
                        eye.setImageResource(R.drawable.eye_off);
                        voice.setEnabled(false);
                        voice.setImageResource(R.drawable.voice_gray);
                        eye_open=false;
                        // 重新启动计时器，如果需要的话
                        long newCountdownTimeInMillis = 12000; // 新的倒计时时间，单位为毫秒120000
                        //CountDownTimer timer;
                        timer = new CountDownTimer(newCountdownTimeInMillis, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                // 每秒更新倒计时显示
                                long minutes = millisUntilFinished / 1000 / 60;
                                long seconds = (millisUntilFinished / 1000) % 60;
                                String timeLeftFormatted = String.format("%d:%02d", minutes, seconds);
                                time.setText(timeLeftFormatted );
                            }


                            @Override
                            public void onFinish() {
                                AlertDialog.Builder P2_finish= new AlertDialog.Builder(Speaking_part2_answer.this);
                                P2_finish.setTitle("回答結束");
                                P2_finish.setCancelable(false);
                                P2_finish.setMessage("時間到");
                                P2_finish.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                if(clockup) P2_finish.show();
                            }
                        }.start();
                    }
                });
                P2_start.show();
            }
        }.start();



        answer.setOnClickListener(v -> {
            StringTokenizer tokenizer = new StringTokenizer(hintanswer, " \t\n\r\f,.?!;:\"");
            List<String> wordsList = new ArrayList<>();
            while (tokenizer.hasMoreTokens()) {
                String word = tokenizer.nextToken();
                wordsList.add(word);
            }
            String[] words = wordsList.toArray(new String[0]);


            int index = answer.getSelectionStart();
            // 找到點擊的單字
            selectedWord = findSelectedWord(words, index);


            // 弹出 PopupWindow
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_layout, null);
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = 1200;
            boolean focusable = true; // 让PopupWindow在失去焦点时自动关闭
            popupWindow = new PopupWindow(popupView, width, height, focusable);

            // 设置PopupWindow的内容
            TextView voc = popupView.findViewById(R.id.Voc);
            TextView phonetic = popupView.findViewById(R.id.phonetics);
            voc.setText(selectedWord);
            getMeaning(selectedWord, phonetic);

            //dic adapter
            RecyclerView meaningRecyclerView = popupView.findViewById(R.id.meaningRecyclerView);
            adapter = new MeaningAdapter(Collections.emptyList());
            meaningRecyclerView.setAdapter(adapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            meaningRecyclerView.setLayoutManager(layoutManager);
            //voice
            ImageButton voice = popupView.findViewById(R.id.voice);
            voice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tts.speak(selectedWord, TextToSpeech.QUEUE_FLUSH, null);

                }
            });
            TextView close = popupView.findViewById(R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();

                }
            });
            ImageView star = popupView.findViewById(R.id.star);

            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (star_yellow) {
                        star.setImageResource(R.drawable.star_black);
                        star_yellow = false;
                        voc_delete_db(selectedWord);

                    } else {
                        star.setImageResource(R.drawable.star_yellow);
                        star_yellow = true;
                        voc_insert_db(selectedWord,adapter,phonetic.getText().toString(),0);
                    }


                }
            });


        });


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在Activity销毁时取消倒计时器，以避免内存泄漏
        if (timer != null) {
            timer.cancel();
        }
    }

    public  void voiceClick(View view){
        if(eye_open)
            tts.speak(Ques, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void hintClick(View view){
        hint.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        callAPI("Give me an example paragraph ,use complete sentences, the question is:"+Ques);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 處理完耗時操作後，將 ProgressBar 隱藏，並顯示 TextView
                progressBar.setVisibility(View.INVISIBLE);
                hint.setVisibility(View.VISIBLE);
            }
        }, 5000);

    }

    public void timerClick(View view){
       if(clockup){
           clockup=false;
           clock.setImageResource(R.drawable.notifications_off);

       }else{
           clockup=true;
           clock.setImageResource(R.drawable.notifications);
       }


    }

    public void eyeClick(View view){
        if(eye_open){
            eye.setImageResource(R.drawable.eye_off);
            taskcard.setTextColor(Color.TRANSPARENT);
            voice.setEnabled(false);
            voice.setImageResource(R.drawable.voice_gray);
            eye_open=false;
        }else{
            voice.setEnabled(true);
            eye.setImageResource(R.drawable.eye);
            taskcard.setTextColor(Color.BLACK);
            voice.setImageResource(R.drawable.voice);
            eye_open=true;
        }


    }




    //mic按下
    public void micClick(){
        timer.cancel();
        TextView txt =  this.findViewById(R.id.listenContent);

        try (SpeechConfig config = SpeechConfig.fromSubscription(speechSubscriptionKey, serviceRegion);
             SpeechRecognizer reco = new SpeechRecognizer(config);) {

            Future<SpeechRecognitionResult> task = reco.recognizeOnceAsync();


            SpeechRecognitionResult result = task.get();

            if (result.getReason() == ResultReason.RecognizedSpeech) {
                txt.setText(result.getText());
                mic.setImageResource(R.drawable.mic);
            } else {
                txt.setText("Error recognizing. Did you update the subscription info?" + System.lineSeparator() + result.toString());
            }

        } catch (Exception ex) {
            Log.e("SpeechSDKDemo", "unexpected " + ex.getMessage());
            assert (false);
        }

    }


    public void retryClick(View v){
        answer.setText("");
    }

    public void finishClick(View v){
        //send_1=true;
        //callAPI("You are a IELTS examiner for speaking part 2.The question is"+Ques+"My answer is"+answer.getText().toString()+"Check my answer for spelling and grammar errors, correct them.And tell me where is wrong and why.If my answer is too short,please tell me how to improve it and give me example.");
        insert_to_db();
        bundle.putString("question",taskcard.getText().toString());
        bundle.putString("answer",answer.getText().toString());
        bundle.putString("num",Num);
        bundle.putString("topic",Topic);
        bundle.putString("key",answerKey);
        Intent intent =new Intent(this, S_Judge_P2.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    public void helpClick(View view){
        // 弹出 PopupWindow
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.pop_layout2, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // 让PopupWindow在失去焦点时自动关闭
        popup = new PopupWindow(popupView, width, height, focusable);
        popup.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);

    }

    public void homeClick(View v){
        Intent intent =new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private  boolean InDic(String word){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    retrofit2.Call<List<WordResult>> call = RetrofitInstance.dictionaryApi.getMeaning(word);
                    retrofit2.Response<List<WordResult>> response = call.execute();

                    if (response.body() == null) {
                        indic = false;
                    }else indic = true;

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "錯誤:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
        return indic;
    }


    private void getMeaning(String word, TextView phonetic) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    retrofit2.Call<List<WordResult>> call = RetrofitInstance.dictionaryApi.getMeaning(word);
                    retrofit2.Response<List<WordResult>> response = call.execute();

                    if (response.body() == null) {
                        throw new Exception();

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (response.body() != null) {
                                setUI(response.body().get(0), phonetic);
                                popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                                //Toast.makeText(getApplicationContext(), "成功", Toast.LENGTH_SHORT).show();
                                Log.i(String.valueOf(Log.DEBUG),"回答" + response.body().get(0).getPhonetic());
                            }
                        }
                    });
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(String.valueOf(Log.DEBUG),"錯誤"+e.getMessage());
                            if(selectedWord==null){
                                Toast.makeText(getApplicationContext(), "未點選", Toast.LENGTH_SHORT).show();

                            }else
                                Toast.makeText(getApplicationContext(), "Something went wrong:"+selectedWord, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private String findSelectedWord(String[] words, int index) {
        for (String word : words) {
            int start = answer.getText().toString().indexOf(word);
            int end = start + word.length();
            if (index >= start && index <= end) {
                return word;
            }
        }
        return null;
    }

    private void SpanningString(){
        StringTokenizer tokenizer = new StringTokenizer(hintanswer, " \t\n\r\f,.?!;:\"");
        List<String> wordsList = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();
            wordsList.add(word);
        }
        words = wordsList.toArray(new String[0]);
        SpannableString spannableString = new SpannableString(hintanswer);

        for (int i = 0; i < words.length; i++) {
            int startIndex = hintanswer.indexOf(words[i]);
            int endIndex = startIndex + words[i].length();
            int customColor = Color.rgb(120, 59, 55);
            //Toast.makeText(getApplicationContext(), words[i], Toast.LENGTH_LONG).show();
            if (InDic(words[i])) {
                spannableString.setSpan(new UnderlineSpan(), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(customColor), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }//else spannableString.setSpan(new ForegroundColorSpan(Color.YELLOW), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        answer.setText(spannableString);

    }


    private void setUI(WordResult response, TextView phonetic) {
        phonetic.setText(response.getPhonetic());
        adapter.updateNewData(response.getMeanings());
    }

    public void callAPI(String question) {
        // OkHttp庫
        // messageList.add(new Message("Typing...", Message.SENT_BY_BOT));
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "gpt-3.5-turbo");

            // 建構對話歷史，包括系統消息
            JSONArray messageArr = new JSONArray();

            // 添加系统消息
            JSONObject obj = new JSONObject();
            obj.put("role","system");
            obj.put("content",question);
            messageArr.put(obj);

            jsonBody.put("messages",messageArr);
            // messageArr.put(systemMessage);

//            // 添加用户消息
//            JSONObject userMessage = new JSONObject();
//            userMessage.put("role", "user");
//            userMessage.put("content", question);
//            messageArr.put(userMessage);



            jsonBody.put("messages", messageArr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer "+apiKey)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG,"錯誤:" + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // 如果網路請求成功
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");


                        //answer.setText(result.trim());
                        hintanswer = result.trim();
                        SpanningString();




//                        if(pass_answer){
//                            judge = result.trim();
//                            Pass_to_judge();
//                            pass_answer=false;
//                        }else{
//                            //要修改UI(TextView:bot)->需切換到主要執行緒
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    hint_answer= result.trim();
//                                    pass();
//
//                                }
//                            });
//                       }





                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                    Log.e(TAG,errorBody);
                }
            }
        });
    }

    //將答案存入db
    public void insert_to_db(){
        DatabaseReference databaseReference;
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            String answerText = answer.getText().toString();


            // 生成唯一的键，并存储答案
            DatabaseReference userAnswersRef = databaseReference
                    .child("users")
                    .child(userId)
                    .child("Speaking")
                    .child(Topic)
                    .child(Num)
                    .push(); // 使用 push() 生成隨機碼

            answerKey = userAnswersRef.getKey();

            userAnswersRef.setValue(answerText)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                callAPI("You are a IELTS examiner for speaking part 3.The question is"+taskcard.getText().toString()+"My answer is"+answer.getText().toString()+"Check my answer for spelling and grammar errors, correct them.And tell me where is wrong and why.If my answer is too short,please tell me how to improve it and give me example.");

                            } else {

                            }
                        }
                    });
        }
    }

    public void voc_insert_db(String word, MeaningAdapter adapter,String phonetic,int count){

        String definitionsText="";
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();


        definitionsText=adapter.getDefinitionsText();
        DataHolder obj = new DataHolder(definitionsText, phonetic, 0);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference Ref = databaseReference
                    .child("word_collect")
                    .child(userId)
                    .child(word);


            Ref.setValue(obj)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Speaking_part2_answer.this, "單字收藏成功", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(Speaking_part2_answer.this,"新增失敗", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }

    public void voc_delete_db(String word){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();



        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference Ref = databaseReference
                    .child("word_collect")
                    .child(userId)
                    .child(word);


            Ref.removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Speaking_part2_answer.this, "單字刪除成功", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(Speaking_part2_answer.this, "刪除失敗", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }



}