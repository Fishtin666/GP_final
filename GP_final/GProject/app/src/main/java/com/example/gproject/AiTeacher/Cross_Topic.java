package com.example.gproject.AiTeacher;

import static android.content.ContentValues.TAG;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gproject.Cross_Topic_topic;
import com.example.gproject.MainActivity;
import com.example.gproject.R;
import com.example.gproject.dictionary.MeaningAdapter;
import com.example.gproject.dictionary.RetrofitInstance;
import com.example.gproject.dictionary.WordResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Cross_Topic extends AppCompatActivity {
    EditText editText;
    ImageView voice;
    Button start;
    TextView cross_result;
    //String cross_question="Describe a beautiful city." + " Describe a memorable trip." + " Describe a place you plan to travel to that is far away from your home?";
    String cross_question,defaultQuestion,cross_answer;
    String selectedWord;
    String[] words;
    Boolean indic=false;
    ImageButton back;

    TextToSpeech tts;
    private PopupWindow popupWindow;
    MeaningAdapter adapter;


    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    //訊息超時timeout改為60s(預設:10s)
    OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cross_topic);

        editText = findViewById(R.id.editText);
        //voice = findViewById(R.id.voice);
        start = findViewById(R.id.start_CrossTopic);
        cross_result = findViewById(R.id.result);
        back = findViewById(R.id.back2);

        cross_result.setMovementMethod(new ScrollingMovementMethod());


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

        //defaultQuestion ="The cross-topic technique involves identifying patterns between speaking topics, uncovering connections between each question, and using a single set of material/story to cover as many topics as possible.Please help me create cross-topic examples based on the following prompts.(50words)"+cross_question;

        // 添加初始系统消息
        JSONObject systemMessage = new JSONObject();
        JSONArray messages = new JSONArray();
        try {
            systemMessage.put("role", "system");
            //systemMessage.put("content", "You are a ielts speaking examiner .Try to make your response simply and clear.");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        messages.put(systemMessage);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setText("正在串題...");
                cross_question = editText.getText().toString();
                cross_result.setText("");
                //defaultQuestion="Please help me generate an answer that can address the following questions."+cross_question;
                //defaultQuestion ="The cross-topic technique involves identifying patterns between speaking topics, uncovering connections between each question, and using a single set of material/story to cover as many topics as possible.Please help me create cross-topic examples based on the following prompts."+cross_question;
                defaultQuestion = cross_question+"[Ielts speaking test part2]Please generate an answer for me based on the above sentence, so that no matter which of the above questions I encounter, I can answer according to this response.";
                callAPI(defaultQuestion);



               }
        });
        //describe a beautiful city.describe a memorable trip.Describe a place you plan to travel to that is far away from your home.

        cross_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringTokenizer tokenizer = new StringTokenizer(cross_result.getText().toString(), " \t\n\r\f,.?!;:\"");
                List<String> wordsList = new ArrayList<>();
                while (tokenizer.hasMoreTokens()) {
                    String word = tokenizer.nextToken();
                    wordsList.add(word);
                }
                String[] words = wordsList.toArray(new String[0]);
                // 獲取點擊的位置
                int index = cross_result.getSelectionStart();
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
                LinearLayoutManager layoutManager=new LinearLayoutManager(Cross_Topic.this);
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

            }
        });



    }

     //找到點擊的單字
    private String findSelectedWord(String[] words, int index) {
        for (String word : words) {
            int start = cross_result.getText().toString().indexOf(word);
            int end = start + word.length();
            if (index >= start && index <= end) {
                return word;
            }
        }
        return null;
    }




    private void SpanningString(){
        StringTokenizer tokenizer = new StringTokenizer(cross_answer, " \t\n\r\f,.?!;:\"");
        List<String> wordsList = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();
            wordsList.add(word);
        }
        words = wordsList.toArray(new String[0]);
        SpannableString spannableString = new SpannableString(cross_answer);

        for (int i = 0; i < words.length; i++) {
            int startIndex = cross_answer.indexOf(words[i]);
            int endIndex = startIndex + words[i].length();
            int customColor = Color.rgb(120, 59, 55);
            //Toast.makeText(getApplicationContext(), words[i], Toast.LENGTH_LONG).show();
            if (InDic(words[i])==false) {
                spannableString.setSpan(new UnderlineSpan(), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(customColor), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }//else spannableString.setSpan(new ForegroundColorSpan(Color.YELLOW), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        cross_result.setText(spannableString);
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
                            }else
                                Toast.makeText(getApplicationContext(), "失敗response.body=null", Toast.LENGTH_SHORT).show();

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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // JSONArray messageArr = new JSONArray();
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
                .header("Authorization", "Bearer ")
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

                        //要修改UI(TextView:bot)->需切換到主要執行緒
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                //cross_result.setText(result.trim());
                                //cross_answer = cross_result.getText().toString();
                                cross_answer = result.trim();
                                SpanningString();
                                start.setText("開始串題");
                            }
                        });

//                        // 添加 ChatGPT 的回應到對話歷史
//                        JSONObject chatGPTMessage = new JSONObject();
//                        chatGPTMessage.put("role", "assistant");
//                        chatGPTMessage.put("content", result.trim());
//                        messageArr.put(chatGPTMessage);


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

    public void hintCLick(View v){
        Intent intent = new Intent(this, Cross_Topic_topic.class);
        startActivity(intent);
    }
}