package com.example.gproject.Speaking;

import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.content.ContentValues.TAG;

import static com.example.gproject.Adapters.QuestionNumberAdapter.sharedString;
import static com.example.gproject.MainActivity.apiKey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gproject.AiTeacher.AiTeacher2;
import com.example.gproject.AiTeacher.AiTeacherHelp;
import com.example.gproject.AiTeacher.Cross_Topic;
import com.example.gproject.MainActivity;
import com.example.gproject.Message;
import com.example.gproject.MessageAdapter;
import com.example.gproject.MicrophoneStream;
import com.example.gproject.R;
import com.example.gproject.Writing.W_Judge_P1;
import com.example.gproject.Writing.Writing_T1answer2;
import com.example.gproject.dictionary.MeaningAdapter;
import com.example.gproject.dictionary.RetrofitInstance;
import com.example.gproject.dictionary.WordResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.cognitiveservices.speech.CancellationReason;
import com.microsoft.cognitiveservices.speech.OutputFormat;
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentConfig;
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentGradingSystem;
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentGranularity;
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentResult;
import com.microsoft.cognitiveservices.speech.PropertyId;
import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisCancellationDetails;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisResult;
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Speaking_part1_answer extends AppCompatActivity {

    private static String speechSubscriptionKey = "031b6e9e93ed4df69bd33327b1daf1d1";
    private static String serviceRegion = "eastus";

    private DatabaseReference databaseReference;
    FirebaseAuth auth;

    public static boolean star_show;

    boolean speakover=false,indic=false,PA_mic=false,star_yellow=false,pass_answer=false,spanning=false;
    Context context;

    String[] words;

    String hint_answer,help_answer,selectedWord,judge,ROf_ques;  //hint_answer是一串,help_answer某個提示回答(一個)

    ImageView mic,voice,hint,home;

    ImageButton back;
    TextView answer,question;
    ProgressBar progressBar;

    MeaningAdapter adapter;

    Button send,retry;

    TextToSpeech tts;
    private PopupWindow popupWindow,popup;

    String Question,QuesNum,Topic;

    SpeechConfig speechConfig;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    //訊息超時timeout改為60s(預設:10s)
    OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    @Override
    protected void onResume() {
        super.onResume();

        // 檢查是否有新的 sharedstring
        if (sharedString != null && !sharedString.isEmpty()) {
            // 顯示 Toast
            //Toast.makeText(this, sharedString, Toast.LENGTH_SHORT).show();

            //answer.setText(sharedString);
            help_answer = sharedString;

            // 清除 sharedstring，以免再次進入時重複顯示
            sharedString = null;
            PA_mic=true;
            SpanningString();
        }

        ROf_ques = getIntent().getStringExtra("ROf_ques");
        if(ROf_ques!=null)
            question.setText(ROf_ques);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speaking_part1_answer);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        mic = findViewById(R.id.mic);
        voice = findViewById(R.id.Voice);
        question = findViewById(R.id.item_question);
        answer = findViewById(R.id.Answer);
        hint = findViewById(R.id.hint);
        progressBar = findViewById(R.id.progressBar);
        send = findViewById(R.id.Finish);
        retry = findViewById(R.id.Retry);
//        dic = findViewById(R.id.dic);

        progressBar.setVisibility(View.INVISIBLE);
        answer.setMovementMethod(new ScrollingMovementMethod());
        back = findViewById(R.id.back2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(Speaking_part1_answer.this, Speaking_questionAdd.class));
                finish();
            }
        });




        try {
            int permissionRequestId = 5;
            ActivityCompat.requestPermissions(Speaking_part1_answer.this, new String[]{RECORD_AUDIO, INTERNET, READ_EXTERNAL_STORAGE}, permissionRequestId);
        } catch (Exception ex) {
            Log.e("SpeechSDK", "could not init sdk, " + ex);
        }

        final String logTag = "pron";
        // create config

        try {
            speechConfig = SpeechConfig.fromSubscription(speechSubscriptionKey, serviceRegion);

        } catch (Exception ex) {
            System.out.println("失敗原因:" + ex.getMessage());
            return;
        }

        //String Question = getIntent().getStringExtra("question");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Question=extras.getString("question");
            QuesNum =extras.getString("num");
            Topic=extras.getString("topic");

        }


        int requestCode = 5;
        ActivityCompat.requestPermissions(Speaking_part1_answer.this, new String[]{RECORD_AUDIO, INTERNET}, requestCode);

        question.setText(Question);

        answer.setOnClickListener(v -> {
            StringTokenizer tokenizer = new StringTokenizer(answer.getText().toString(), " \t\n\r\f,.?!;:\"");
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
                    } else {
                        star.setImageResource(R.drawable.star_yellow);
                        star_yellow = true;
                    }


                }
            });

        });
    }
    Bundle bundle=new Bundle();

    public void hintClick(View v) throws InterruptedException {
        hint.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        star_show =false;

        callAPI("Give me two example responses,use complete sentences,each less than 50 words  and numbered it with only number no dot(ex:1 Nice to meet you. 2 Good morning.), the question is:"+question.getText().toString());
        //Thread.sleep(5000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 處理完耗時操作後，將 ProgressBar 隱藏，並顯示 TextView
                progressBar.setVisibility(View.INVISIBLE);
                hint.setVisibility(View.VISIBLE);
            }
        }, 3000);
        send.setEnabled(false);
        send.setBackgroundColor(Color.rgb(198,198,198));
        send.setTextColor(Color.WHITE);
        //Toast.makeText(Speaking_part1_answer.this, "提示:"+hint_answer, Toast.LENGTH_LONG).show();




    }
    public void pass(){
        Bundle bundle=new Bundle();

        Intent intent=new Intent(Speaking_part1_answer.this, AiTeacherHelp.class);
        bundle.putString("help_question",hint_answer);

        intent.putExtras(bundle);
        startActivity(intent);
    }


    private void SpanningString(){
        //if(spanning==false){
            StringTokenizer tokenizer = new StringTokenizer(help_answer, " \t\n\r\f,.?!;:\"");
            List<String> wordsList = new ArrayList<>();
            while (tokenizer.hasMoreTokens()) {
                String word = tokenizer.nextToken();
                wordsList.add(word);
            }
            String[] words = wordsList.toArray(new String[0]);
            SpannableString spannableString = new SpannableString(help_answer);

            for (int i = 0; i < words.length; i++) {
                int startIndex = help_answer.indexOf(words[i]);
                int endIndex = startIndex + words[i].length();
                int customColor = Color.rgb(120, 59, 55);
                //Toast.makeText(getApplicationContext(), words[i], Toast.LENGTH_LONG).show();
                if (InDic(words[i])==false) {
                    spannableString.setSpan(new UnderlineSpan(), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new ForegroundColorSpan(customColor), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }
            }
            answer.setText(spannableString);

        //}else{
            //answer.setText(help_answer);

        //}


    }
    // 找到點擊的單字
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

    private void setUI(WordResult response, TextView phonetic) {
        phonetic.setText(response.getPhonetic());
        adapter.updateNewData(response.getMeanings());
    }

    public  void Pass_to_judge(){
        bundle.putString("judge",judge);

        Intent intent =new Intent(Speaking_part1_answer.this, Speaking_judge.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void helpClick(View view){
        // 弹出 PopupWindow
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.pop_layout1, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // 让PopupWindow在失去焦点时自动关闭
        popup = new PopupWindow(popupView, width, height, focusable);
        popup.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);

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

                        if(pass_answer){
                            judge = result.trim();
                            Pass_to_judge();
                            pass_answer=false;
                        }else{
                            //要修改UI(TextView:bot)->需切換到主要執行緒
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hint_answer= result.trim();
                                    pass();

                                }
                            });
                        }





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


    public void retryClick(View v){
        answer.setText("");
        PA_mic=false;
        send.setEnabled(true);
        send.setBackgroundColor(Color.rgb(167, 53, 52));
        send.setTextColor(Color.WHITE);

    }

    public void finishClick(View v){
        send.setBackgroundColor(Color.BLACK);
        pass_answer=true;
        //將答案存入db
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
                    .child(String.valueOf(QuesNum))
                    .push(); // 使用 push() 生成隨機碼

            String answerKey = userAnswersRef.getKey();

            userAnswersRef.setValue(answerText)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                bundle.putString("key",answerKey);
                                callAPI("You are a IELTS examiner for speaking part 1.The question is"+question.getText().toString()+"My answer is"+answer.getText().toString()+"Check my answer for spelling and grammar errors, correct them.And tell me where is wrong and why.If my answer is too short,please tell me how to improve it and give me example.");

                            } else {
                                // 存储失败
                                // 处理存储失败的情况
                            }
                        }
                    });
        }

    }




    //mic按下
//    public void micClick(View view){
//        TextView txt =  this.findViewById(R.id.Answer);
//
//        try (SpeechConfig config = SpeechConfig.fromSubscription(speechSubscriptionKey, serviceRegion);
//             SpeechRecognizer reco = new SpeechRecognizer(config);) {
//
//            Future<SpeechRecognitionResult> task = reco.recognizeOnceAsync();
//
//
//            SpeechRecognitionResult result = task.get();
//
//            if (result.getReason() == ResultReason.RecognizedSpeech) {
//                txt.setText(result.getText());
//            } else {
//                txt.setText("Error recognizing. Did you update the subscription info?" + System.lineSeparator() + result.toString());
//            }
//
//        } catch (Exception ex) {
//            Log.e("SpeechSDKDemo", "unexpected " + ex.getMessage());
//            assert (false);
//        }
//
//    }
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    public void micClick(View view) {
        if(PA_mic){


            if (microphoneStream != null) {
                //如果microphoneStream存在，就停止錄音
                releaseMicrophoneStream();
                return;
            }

            try {
                createMicrophoneStream();
                final AudioConfig audioConfig = AudioConfig.fromStreamInput(createMicrophoneStream());
                //final com.microsoft.cognitiveservices.speech.SpeechRecognizer reco = new com.microsoft.cognitiveservices.speech.SpeechRecognizer(speechConfig, "en-US", audioConfig);
                final SpeechRecognizer reco = new SpeechRecognizer(speechConfig, "en-US", audioConfig);

                String referenceText=help_answer;
                //String[] words = referenceText.split(" "); // 拆分文本成單詞
                //SpannableString spannableString = new SpannableString(referenceText);


                StringTokenizer tokenizer = new StringTokenizer(help_answer, " \t\n\r\f,.?!;:\"");
                List<String> wordsList = new ArrayList<>();
                while (tokenizer.hasMoreTokens()) {
                    String word = tokenizer.nextToken();
                    wordsList.add(word);
                }
                String[] words = wordsList.toArray(new String[0]);
                SpannableString spannableString = new SpannableString(referenceText);


                PronunciationAssessmentConfig pronConfig =
                        new PronunciationAssessmentConfig(referenceText,
                                PronunciationAssessmentGradingSystem.HundredMark,
                                PronunciationAssessmentGranularity.Phoneme);

                pronConfig.applyTo(reco);

                //Future<SpeechRecognitionResult> future = executor.submit(() -> reco.recognizeOnceAsync().get(30, TimeUnit.SECONDS));

                Future<SpeechRecognitionResult> future = reco.recognizeOnceAsync();
                SpeechRecognitionResult speechRecognitionResult = future.get(30, TimeUnit.SECONDS);
                PronunciationAssessmentResult pronResult = PronunciationAssessmentResult.fromResult(speechRecognitionResult);

                double point=pronResult.getPronunciationScore();
//                speechConfig.close();
//                audioConfig.close();
//                pronConfig.close();
//                speechRecognitionResult.close();
                handlePronunciationResult(point);



                reco.recognized.addEventListener((o, speechRecognitionResultEventArgs) -> {
                    String pronunciationAssessmentResultJson = speechRecognitionResult.getProperties().getProperty(PropertyId.SpeechServiceResponse_JsonResult);

                    int i=0;
                    int start=0;


                    JsonParser jsonParser = new JsonParser();
                    JsonElement jsonElement = jsonParser.parse(pronunciationAssessmentResultJson);
                    if (jsonElement.isJsonObject()) {
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        JsonArray nBestArray = jsonObject.getAsJsonArray("NBest");

                        for (JsonElement nBestElement : nBestArray) {
                            JsonObject nBestObject = nBestElement.getAsJsonObject();
                            JsonArray wordsArray = nBestObject.getAsJsonArray("Words");

                            for (JsonElement wordElement : wordsArray) {
                                JsonObject wordObject = wordElement.getAsJsonObject();
                                JsonObject pronunciationAssessment = wordObject.getAsJsonObject("PronunciationAssessment");

                                if (pronunciationAssessment != null && pronunciationAssessment.has("ErrorType")) {

                                    String errorType = pronunciationAssessment.get("ErrorType").getAsString();
                                    System.out.println("ErrorType: " + errorType);

                                    int end = start + words[i].length();



                                    if (errorType.trim().equals("None")) {
                                        //spannableString.setSpan(new BackgroundColorSpan(Color.GREEN), start, end, 0);
                                    } else if (errorType.trim().equals("Mispronunciation")) {
                                        spannableString.setSpan(new BackgroundColorSpan(Color.YELLOW), start, end, 0);
                                    } else if (errorType.trim().equals("Omission")) {
                                        spannableString.setSpan(new BackgroundColorSpan(Color.GRAY), start, end, 0);
                                    } else {
                                        spannableString.setSpan(new BackgroundColorSpan(Color.RED), start, end, 0);
                                    }
                                    start = end + 1;

                                }
                                i++;

                            }

                        }answer.setText(spannableString);
                    }




                });



                reco.sessionStopped.addEventListener((o, s) -> {

                    System.out.println("Session stopped.");
                    reco.stopContinuousRecognitionAsync();

                    this.releaseMicrophoneStream();

                });


               try {
                reco.recognizeOnceAsync();
               } catch (Exception ex) {
                   System.out.println("辨識過程中發生錯誤: " + ex.getMessage());
                }


            } catch (Exception ex) {
                System.out.println("辨識過程中發生錯誤"+ex.getMessage());
                ex.printStackTrace();
                displayException(ex);
            }

        }else{
            try (SpeechConfig config = SpeechConfig.fromSubscription(speechSubscriptionKey, serviceRegion);
                 com.microsoft.cognitiveservices.speech.SpeechRecognizer reco = new com.microsoft.cognitiveservices.speech.SpeechRecognizer(config);) {

                Future<SpeechRecognitionResult> task = reco.recognizeOnceAsync();


                SpeechRecognitionResult result = task.get();

                if (result.getReason() == ResultReason.RecognizedSpeech) {
                    answer.setText(result.getText());
                } else {
                    System.out.println("錯誤:" + System.lineSeparator() + result.toString());
                }

            } catch (Exception ex) {
                Log.e("SpeechSDKDemo", "unexpected " + ex.getMessage());
                assert (false);
            }
        }

    }

    private void handlePronunciationResult(double point) {
        runOnUiThread(() -> {
            AlertDialog.Builder PA_result = new AlertDialog.Builder(Speaking_part1_answer.this);
            PA_result.setTitle("發音評定結果");
            PA_result.setCancelable(false);
            PA_result.setPositiveButton("知道了", (dialogInterface, i) -> {});

            if (point < 70) {
                PA_result.setMessage("可惜!超過70分即可使用此句子!\n再試一次吧!");
            } else {
                PA_result.setMessage("超棒的!你的發音分数為" + point + "分");
                send.setEnabled(true);
                send.setBackgroundColor(Color.rgb(167, 53, 52));
                send.setTextColor(Color.WHITE);
                PA_mic = false;
            }
            PA_result.show();
        });


    }

    private MicrophoneStream microphoneStream;
    private MicrophoneStream createMicrophoneStream() {
        releaseMicrophoneStream();
        microphoneStream = new MicrophoneStream();

        return microphoneStream;
    }
    private void releaseMicrophoneStream() {
        if (microphoneStream != null) {
            microphoneStream.close();
            microphoneStream = null;
        }
    }

    private void displayException(Exception ex) {
        System.out.println("失敗因為:"+ex.getMessage() + System.lineSeparator() + TextUtils.join(System.lineSeparator(), ex.getStackTrace()));
    }

    public void homeClick(View v){
        Intent intent =new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void eyeClick(View v){
        if(question.getVisibility()==View.VISIBLE)
            question.setVisibility(View.INVISIBLE);
        else
            question.setVisibility(View.VISIBLE);
    }

    public void voiceClick(View v){
        tts.speak(question.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
        //tts();
    }
    //tts
    public void tts() {

        try (SpeechConfig config = SpeechConfig.fromSubscription(speechSubscriptionKey, serviceRegion)) {
            // https://aka.ms/speech/voices/neural 腔調

            config.setSpeechSynthesisVoiceName("en-US-AriaNeural");   //美國
            //config.setSpeechSynthesisVoiceName("en-GB-LibbyNeural");  //英國
            //config.setSpeechSynthesisVoiceName("en-IN-NeerjaNeural");   //印度
            //config.setSpeechSynthesisVoiceName("en-IN-PrabhatNeural");


            try (SpeechSynthesizer synth = new SpeechSynthesizer(config)) {
                assert (config != null);
                assert (synth != null);

                String ques=question.getText().toString();
                Future<SpeechSynthesisResult> task = synth.SpeakTextAsync(ques);
                assert (task != null);

                SpeechSynthesisResult result = task.get();
                assert (result != null);


                if (result.getReason() == ResultReason.SynthesizingAudioCompleted) {
                    //System.out.println("Speech synthesized to speaker for text [" + text + "]");
                    speakover=true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(Speaking_part2_question.this, "done", Toast.LENGTH_LONG).show();
                        }
                    });


                } else if (result.getReason() == ResultReason.Canceled) {
                    SpeechSynthesisCancellationDetails cancellation = SpeechSynthesisCancellationDetails
                            .fromResult(result);
                    //Toast.makeText(speech_to_text.this, "CANCELED: Reason=" + cancellation.getReason(), Toast.LENGTH_LONG).show();
                    System.out.println("CANCELED: Reason=" + cancellation.getReason());

                    if (cancellation.getReason() == CancellationReason.Error) {
                        //Toast.makeText(speech_to_text.this, "CANCELED: ErrorCode=" + cancellation.getErrorCode(), Toast.LENGTH_LONG).show();
                        System.out.println("CANCELED: ErrorCode=" + cancellation.getErrorCode());
                        //System.out.println("CANCELED: ErrorDetails=" + cancellation.getErrorDetails());
                        //System.out.println("CANCELED: Did you update the subscription info?");
                    }
                }


            }
        } catch (Exception ex) {
            //Toast.makeText(speech_to_text.this,"Unexpected exception: " + ex.getMessage(),Toast.LENGTH_LONG).show();
            assert (false);
            System.exit(1);
        }
    }



}