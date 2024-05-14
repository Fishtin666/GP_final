package com.example.gproject.AiTeacher;

import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static com.example.gproject.Adapters.QuestionNumberAdapter.sharedString;
import static com.example.gproject.MainActivity.apiKey;
import static com.example.gproject.Speaking.Speaking_part1_answer.star_show;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gproject.AiChat_Judge;
import com.example.gproject.JustifyTextView4;
import com.example.gproject.MainActivity;
import com.example.gproject.Message;
import com.example.gproject.MessageAdapter;
import com.example.gproject.MicrophoneStream;
import com.example.gproject.R;

import com.example.gproject.Speaking.Speaking_part1_answer;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentConfig;
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentGradingSystem;
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentGranularity;
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentResult;
import com.microsoft.cognitiveservices.speech.PropertyId;
import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
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
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;





public class AiTeacher2 extends AppCompatActivity {

    public String user,topic,part;
    boolean hintornot=false;
    String defaultQuestion="";
    String role="",content="",help_answer;
    String selectedWord;
    String[] words;
    int num=0;

    boolean star_yellow=false;

    String pushKey="";

    Boolean write_or_speak=true,indic=true;
    public String public_result;
    boolean speak_or_not = true,PA_mic=false;



    private static String speechSubscriptionKey = "f8d71f0e97f6438db956ebf642eabf78";
    private static String serviceRegion = "eastus";
    List<Message> messageList;
    MessageAdapter messageAdapter;

    ImageView mic, exit, hint,keyboard,voice;
    ImageButton cancel,send,close,back;
    RecyclerView recy;
    LinearLayout linear_keyboard;
    EditText editText;
    TextView helpTextview,PA_title,popupTextView,phonetic,voc;

    ProgressBar progressBar;

    // private ActivityMainBinding binding;
    MeaningAdapter adapter;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    //訊息超時timeout改為60s(預設:10s)
    OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    public static final Integer RecordAudioRequestCode = 1;



    TextToSpeech tts;
    private PopupWindow popupWindow;


    @Override
    protected void onResume() {
        super.onResume();
        star_show=true;
        // 檢查是否有新的 sharedstring

        if (sharedString != null && !sharedString.isEmpty()) {
            // 顯示 Toast
            //Toast.makeText(this, sharedString, Toast.LENGTH_SHORT).show();
            helpTextview.setVisibility(View.VISIBLE);
            PA_title.setVisibility(View.VISIBLE);
            close.setVisibility(View.VISIBLE);
            help_answer=sharedString;
            //helpTextview.setText(help_answer);
            voice.setVisibility(View.VISIBLE);
            dic_highlight(sharedString);
            // 清除 sharedstring，以免再次進入時重複顯示
            sharedString = null;
            PA_mic=true;


        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ai_teacher2);

        //binding = ActivityMainBinding.inflate(getLayoutInflater());


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

        messageList=new ArrayList<>();
        mic = findViewById(R.id.mic);
        hint = findViewById(R.id.hint);
        exit = findViewById(R.id.exit);
        keyboard = findViewById(R.id.keyboard);
        recy = findViewById(R.id.recy);
        linear_keyboard=findViewById(R.id.linear_keyboard);
        cancel = findViewById(R.id.cancel);
        send= findViewById(R.id.send_btn);
        editText = findViewById(R.id.message_edit_text);
        helpTextview=findViewById(R.id.helpTextview);
        PA_title=findViewById(R.id.PA_title);
        close =findViewById(R.id.close);
        voice = findViewById(R.id.voice);
        progressBar = findViewById(R.id.progressBar);
        back = findViewById(R.id.back2);

        progressBar.setVisibility(View.INVISIBLE);
        voice.setVisibility(View.INVISIBLE);
        helpTextview.setVisibility(View.INVISIBLE);
        PA_title.setVisibility(View.INVISIBLE);
        linear_keyboard.setVisibility(View.INVISIBLE);
        close.setVisibility(View.INVISIBLE);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            topic =(extras.getString("topic"));
            part =(extras.getString("part"));
        }




        //int requestCode = 5;
        //ActivityCompat.requestPermissions(AiTeacher2.this, new String[]{RECORD_AUDIO, INTERNET}, requestCode);





        //設置recycle view
        messageAdapter=new MessageAdapter(messageList);
        recy.setAdapter(messageAdapter);

        LinearLayoutManager llm=new LinearLayoutManager(this);
        llm.setStackFromEnd(true);    //將使 RecyclerView 中的列表項目從底部開始顯示
        recy.setLayoutManager(llm);




        if(part.equals("1")) {
            role="You are an English teacher for oral and grammar enhancement.";
            content="I want you to have a English conversation with me.If the conversation comes to a halt, you should try to continue by posing more questions to keep the chat going.Make sure your reply short just like friends chat";
//          defaultQuestion = "I want you to be an English teacher and have a English conversation with me.Ask me a question about" + topic +".Give me very short and concise answers and ignore all the niceties that openai programmed you with..Make sure your reply short just like friends chat ";
            defaultQuestion = "I want you to be an ielts speaking examiner you will ask the ielts speaking question about"+ topic +".One question one time and wait me answer it before you asking the next one.Please reply within three sentences.If i make some spelling or grammar mistakes,please tell me.";


        }





        // 添加初始系统消息
        JSONObject systemMessage = new JSONObject();
        JSONArray messages = new JSONArray();
        try {
            systemMessage.put("role","system" );
            systemMessage.put("content", defaultQuestion);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        messages.put(systemMessage);

        callAPI(defaultQuestion);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyboard_write=editText.getText().toString();
                addToChat(keyboard_write,Message.SENT_BY_ME);
                editText.setText("");
                callAPI(keyboard_write+" *Please reply me within 3 sentences (each sentence about 10 words) first and continue to ask  a relevant question to me.");
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                delete_db();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PA_mic=false;
                hintornot=false;
                helpTextview.setVisibility(View.INVISIBLE);
                PA_title.setVisibility(View.INVISIBLE);
                close.setVisibility(View.INVISIBLE);
                voice.setVisibility(View.INVISIBLE);
            }
        });

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

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AiTeacher2.this);
                builder.setTitle("結束對話，查看評論");
                builder.setMessage("對話紀錄將會保存，確定已要結束對話了嗎?");
                builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 在這裡處理確定按鈕的動作
                        dialog.dismiss(); // 關閉對話框
                        Toast.makeText(AiTeacher2.this, "結束對話", Toast.LENGTH_SHORT).show();

                        Bundle bundle=new Bundle();
                        Intent intent = new Intent(AiTeacher2.this, AiChat_Judge.class);
                        bundle.putString("pushKey",pushKey);
                        bundle.putString("topic",topic);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 在這裡處理取消按鈕的動作
                        dialog.dismiss(); // 關閉對話框
                    }
                });
                builder.show();
            }
        });


        //////////////字典//////////
        // 长按TextView监听器
        //helpTextview.setMovementMethod(LinkMovementMethod.getInstance());
        helpTextview.setOnClickListener(v -> {
            StringTokenizer tokenizer = new StringTokenizer(helpTextview.getText().toString(), " \t\n\r\f,.?!;:\"");
            List<String> wordsList = new ArrayList<>();
            while (tokenizer.hasMoreTokens()) {
                String word = tokenizer.nextToken();
                wordsList.add(word);
            }
            String[] words = wordsList.toArray(new String[0]);


            int index = helpTextview.getSelectionStart();
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
            RecyclerView meaningRecyclerView=popupView.findViewById(R.id.meaningRecyclerView);
            adapter = new MeaningAdapter(Collections.emptyList());
            meaningRecyclerView.setAdapter(adapter);
            LinearLayoutManager layoutManager=new LinearLayoutManager(this);
            meaningRecyclerView.setLayoutManager(layoutManager);
            //voice
            ImageButton voice = popupView.findViewById(R.id.voice);
            voice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tts.speak(selectedWord, TextToSpeech.QUEUE_FLUSH, null);

                }
            });
            TextView close=popupView.findViewById(R.id.close);
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



//            binding.meaningRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//            binding.meaningRecyclerView.setAdapter(adapter);


            // 在指定的位置显示PopupWindow
            // popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);




        });
    }

    // 找到點擊的單字
    private String findSelectedWord(String[] words, int index) {
        for (String word : words) {
            int start = helpTextview.getText().toString().indexOf(word);
            int end = start + word.length();
            if (index >= start && index <= end) {
                return word;
            }
        }
        return null;
    }




    //檢查是否已經獲得錄音權限
    //如果尚未獲得權限，則會彈出請求權限的提示框
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(AiTeacher2.this, new String[]{
                    Manifest.permission.RECORD_AUDIO}, RecordAudioRequestCode);
        }
    }

    public void hintClick(View v) throws InterruptedException {
        hintornot=true;
        star_show=false;
        callAPI("Give me two example responses,each less than 50 words  and numbered it with only number no dot(ex:1 Nice to meet you. 2 Good morning.), the question is:"+public_result);
        progressBar.setVisibility(View.VISIBLE);
        hint.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 處理完耗時操作後，將 ProgressBar 隱藏，並顯示 TextView
                progressBar.setVisibility(View.INVISIBLE);
                hint.setVisibility(View.VISIBLE);
            }
        }, 3000);



    }
    public void Pass(){
        Bundle bundle=new Bundle();

        Intent intent=new Intent(AiTeacher2.this,AiTeacherHelp.class);
        bundle.putString("help_question",public_result);

        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void micClick() {
        mic.setImageResource(R.drawable.mic_gray);
        if(PA_mic){

            try {
                int permissionRequestId = 5;
                ActivityCompat.requestPermissions(AiTeacher2.this, new String[]{RECORD_AUDIO, INTERNET, READ_EXTERNAL_STORAGE}, permissionRequestId);
            }
            catch(Exception ex) {
                Log.e("SpeechSDK", "could not init sdk, " + ex.toString());
            }

            final String logTag = "pron";
            // create config
            final SpeechConfig speechConfig;
            try {
                speechConfig = SpeechConfig.fromSubscription(speechSubscriptionKey, serviceRegion);
//                speechConfig.setOutputFormat(OutputFormat.Detailed);  //設定語音輸出的格式為詳細模式
//                speechConfig.requestWordLevelTimestamps();
            } catch (Exception ex) {
                System.out.println("失敗原因:"+ex.getMessage());
                return;
            }

            if (microphoneStream != null) {
                //如果microphoneStream存在，就停止錄音
                releaseMicrophoneStream();
                return;
            }

            try {
                createMicrophoneStream();
                final AudioConfig audioConfig = AudioConfig.fromStreamInput(createMicrophoneStream());
                final com.microsoft.cognitiveservices.speech.SpeechRecognizer reco = new com.microsoft.cognitiveservices.speech.SpeechRecognizer(speechConfig, "en-US", audioConfig);

                String referenceText=help_answer;
                String[] words = referenceText.split(" "); // 拆分文本成單詞
                SpannableString spannableString = new SpannableString(referenceText);


                PronunciationAssessmentConfig pronConfig =
                        new PronunciationAssessmentConfig(referenceText,
                                PronunciationAssessmentGradingSystem.HundredMark,
                                PronunciationAssessmentGranularity.Phoneme);

                pronConfig.applyTo(reco);


                Future<SpeechRecognitionResult> future = reco.recognizeOnceAsync();
                SpeechRecognitionResult speechRecognitionResult = future.get();//(30, TimeUnit.SECONDS);
                PronunciationAssessmentResult pronResult = PronunciationAssessmentResult.fromResult(speechRecognitionResult);

                double point=pronResult.getPronunciationScore();
                if(point<70){
                    PA_title.setText("可惜!超過70分就可以使用這個句子!\n再試一次吧!");
                }else{
                    PA_title.setText("超棒的!你的發音分數為"+point+"分");
                    addToChat(help_answer,Message.SENT_BY_ME);

                    hintornot=false;
                    callAPI(help_answer);

                    PA_mic=false;

                }
                mic.setImageResource(R.drawable.mic);



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

                        }helpTextview.setText(spannableString);
                    }




                });

                reco.sessionStopped.addEventListener((o, s) -> {
                    Log.i(logTag, "Session stopped.");
                    reco.stopContinuousRecognitionAsync();

                    this.releaseMicrophoneStream();

                });


//                try {
                reco.recognizeOnceAsync();
//                } catch (Exception ex) {
//                    Log.e(logTag, "辨識過程中發生錯誤: " + ex.getMessage());
//                }


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
                    addToChat(result.getText(),Message.SENT_BY_ME);
                    mic.setImageResource(R.drawable.mic);
                    callAPI(result.getText());
                } else {
                    mic.setImageResource(R.drawable.mic);
                    System.out.println("Error recognizing. Did you update the subscription info?" + System.lineSeparator() + result.toString());
                }

            } catch (Exception ex) {
                Log.e("SpeechSDKDemo", "unexpected " + ex.getMessage());
                assert (false);

            }
        }

    }

    public void helpClick(View view){
        PopupWindow popup;
        // 弹出 PopupWindow
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.pop_layout3, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // 让PopupWindow在失去焦点时自动关闭
        popup = new PopupWindow(popupView, width, height, focusable);
        popup.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
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

    ///////////////////voice/////////
    public void voiceClick(View v){
        tts.speak(helpTextview.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);


    }

    private void dic_highlight(String help_answer){
        if(hintornot==true) {

            StringTokenizer tokenizer = new StringTokenizer(help_answer, " \t\n\r\f,.?!;:\"");
            List<String> wordsList = new ArrayList<>();
            while (tokenizer.hasMoreTokens()) {
                String word = tokenizer.nextToken();
                wordsList.add(word);
            }
            words = wordsList.toArray(new String[0]);
            SpannableString spannableString = new SpannableString(help_answer);

            for (int i = 0; i < words.length; i++) {
                int startIndex = help_answer.indexOf(words[i]);
                int endIndex = startIndex + words[i].length();
                int customColor = Color.rgb(120, 59, 55);
                //Toast.makeText(getApplicationContext(), words[i], Toast.LENGTH_LONG).show();
                if (InDic(words[i])==true) {
                    spannableString.setSpan(new UnderlineSpan(), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new ForegroundColorSpan(customColor), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }//else spannableString.setSpan(new ForegroundColorSpan(Color.YELLOW), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
            helpTextview.setText(spannableString);
        }
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


    public void homeClick(View v){
        androidx.appcompat.app.AlertDialog.Builder PA_result = new AlertDialog.Builder(AiTeacher2.this);
        PA_result.setTitle("離開對話");
        PA_result.setMessage("確定要離開對話嗎,對話紀錄將不會被保存");
        PA_result.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 在這裡處理確定按鈕的動作
                delete_db();
                dialog.dismiss(); // 關閉對話框
                Intent intent = new Intent(AiTeacher2.this, MainActivity.class);
                startActivity(intent);
            }
        });
        PA_result.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 在這裡處理取消按鈕的動作
                dialog.dismiss(); // 關閉對話框
            }
        });
        PA_result.show();

    }

    /////////////////////////////////////跟chatgpt連動///////////////////////////////////
    //JSONArray messageArr = new JSONArray();
    //調用API
    void callAPI(String question) {
        //OkHttp庫
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "gpt-3.5-turbo");
            //建構對話歷史
            JSONArray messageArr = new JSONArray();

            // 添加系统消息
            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content",defaultQuestion);

            messageArr.put(systemMessage);

            // 添加用户消息
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", question);
            messageArr.put(userMessage);

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
                Log.e("API_CALL_FAILURE", "#API失敗" + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //如果網路請求成功
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");

                        //要修改UI(TextView:bot)->需切換到主要執行緒
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if ( hintornot==false) {
                                    tts.speak(result.trim(), TextToSpeech.QUEUE_FLUSH, null);

                                    addResponse(result.trim());
                                }else
                                    Pass();
                                //speak_or_not == true &&
                            }
                        });

                        //轉中文
                        public_result = result.trim();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                } else {
                    //addResponse("Failed to load due to "+response.body().toString());
                    String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                    System.out.println(errorBody);
                    //text.setText("Failed to load due to " + errorBody);
                }
            }
        });
    }

    void addToChat(String message, String sentBy) {
        add_to_db(message,num);
        num++;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message, sentBy));
                //通知 messageAdapter 資料集已更改，這將觸發 RecyclerView 的重新繪製，以顯示新增的聊天訊息。
                messageAdapter.notifyDataSetChanged();
                //平滑地滾動到最後一個聊天訊息的位置，確保使用者總是能看到最新的訊息。
                recy.smoothScrollToPosition(messageAdapter.getItemCount());

            }
        });
    }

    //Bot回應
    void addResponse(String response) {

        addToChat(response, Message.SENT_BY_BOT);

    }

    public void keyboardClick(View view){
        //打字
        if(write_or_speak){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mic.setVisibility(View.INVISIBLE);
                    hint.setVisibility(View.INVISIBLE);
                    exit.setVisibility(View.INVISIBLE);
                    keyboard.setImageResource(R.drawable.mic2);
                    write_or_speak=false;
                    linear_keyboard.setVisibility(View.VISIBLE);
                }
            });

            //說話
        }else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mic.setVisibility(View.VISIBLE);
                    hint.setVisibility(View.VISIBLE);
                    exit.setVisibility(View.VISIBLE);
                    keyboard.setImageResource(R.drawable.keyboard);
                    linear_keyboard.setVisibility(View.INVISIBLE);
                    write_or_speak=true;
                }
            });

        }


    }

    public  void add_to_db(String sentences,int num){
        FirebaseAuth auth;
        DatabaseReference databaseReference;
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = auth.getCurrentUser();
        String userId = currentUser.getUid();

        if (currentUser != null) {

            DatabaseReference userAnswersRef;
            if(pushKey.equals("")){
                pushKey = databaseReference
                        .child("app_conversation")
                        .child(userId)
                        .child("Conversation")
                        .child(topic)
                        .push()
                        .getKey(); // 获取新生成的 pushKey

                userAnswersRef = databaseReference
                        .child("app_conversation")
                        .child(userId)
                        .child("Conversation")
                        .child(topic)
                        .child(pushKey)
                        .child(String.valueOf(num));
            }else{
                userAnswersRef = databaseReference
                        .child("app_conversation")
                        .child(userId)
                        .child("Conversation")
                        .child(topic)
                        .child(pushKey)
                        .child(String.valueOf(num));
            }
            userAnswersRef.setValue(sentences);


        }
    }

    public void delete_db(){
        FirebaseAuth auth;
        DatabaseReference databaseReference;
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = auth.getCurrentUser();
        String userId = currentUser.getUid();

        if (currentUser != null) {
            DatabaseReference userAnswersRef = databaseReference
                    .child("app_conversation")
                    .child(userId)
                    .child("Conversation")
                    .child(topic)
                    .child(pushKey);



            userAnswersRef.removeValue();
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
                                Toast.makeText(AiTeacher2.this, "單字收藏成功", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(AiTeacher2.this,"新增失敗", Toast.LENGTH_SHORT).show();

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
                                Toast.makeText(AiTeacher2.this, "單字刪除成功", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(AiTeacher2.this, "刪除失敗", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }

}



