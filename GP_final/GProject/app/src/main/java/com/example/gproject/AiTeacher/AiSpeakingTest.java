package com.example.gproject.AiTeacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.gproject.Message;
import com.example.gproject.MessageAdapter;
import com.example.gproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AiSpeakingTest extends AppCompatActivity {
    List<Message> messageList;
    MessageAdapter messageAdapter;

    Boolean write_or_speak=true;
    ImageView mic, exit,keyboard;
    ImageButton cancel,send,close;
    RecyclerView recy;
    LinearLayout linear_keyboard;
    EditText editText;

    TextToSpeech tts;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    //訊息超時timeout改為60s(預設:10s)
    OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    public static final Integer RecordAudioRequestCode = 1;

    //private String defaultQuestion ="I want you to be my ielts speaking examiner ,you will ask the ielts speaking question about part one,please ask me three question in total.One question one time and wait me answer it before you asking the next one.You should reply to my answer too,like two people are chatting.(*Remember part one's main focus is to have the candidate introduce themselves. )";
//    private  String defaultQuestion =
//            "I want you to be my ielts speaking examiner and ask me ielts speaking question " +
//                    "all the way part one,part two ,part three.Do not write all the conversation at once." +
//                    "(For part one you should ask 1-2 questions,for part 2 you should ask about 3 question," +
//                    "for part 3 you should ask 5 question.)I want you to ask me each question and wait me answer it before you asking the next one.After we finish speaking part one,part two,part three,if i make any english mistake ,I want you to correct me and explain the correction.";

    private String defaultQuestion =
            "I want you to help me simulate the IELTS speaking test. You are the examiner." +
                    "DO NOT write all the conversation at once.I want you to ask me each question one by one" +
                    " and wait me answer it before you asking the next one." +
                    "For part one you should ask 2 questions,for part 2 you should ask about 3 question,for part 3 you should ask 3 question."+
                    "Please notify me when starting a new part."+
                    "After we finish whole speaking test,give me judgement about my presentation.";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aispeaking_test);

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
        exit = findViewById(R.id.exit);
        keyboard = findViewById(R.id.keyboard);
        recy = findViewById(R.id.recy);
        linear_keyboard=findViewById(R.id.linear_keyboard);
        cancel = findViewById(R.id.cancel);
        send= findViewById(R.id.send_btn);
        editText = findViewById(R.id.message_edit_text);

        linear_keyboard.setVisibility(View.INVISIBLE);

        messageAdapter=new MessageAdapter(messageList);
        recy.setAdapter(messageAdapter);

        LinearLayoutManager llm=new LinearLayoutManager(this);
        llm.setStackFromEnd(true);    //將使 RecyclerView 中的列表項目從底部開始顯示
        recy.setLayoutManager(llm);


        // 添加初始系统消息
        JSONObject systemMessage = new JSONObject();
        JSONArray messages = new JSONArray();
        try {
            systemMessage.put("role", "system");
            systemMessage.put("content", "You are a ielts speaking examiner.Have a speaking test with me.");
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
    }

    public void keyboardClick(View view){
        //打字
        if(write_or_speak){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mic.setVisibility(View.INVISIBLE);
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
                    exit.setVisibility(View.VISIBLE);
                    keyboard.setImageResource(R.drawable.keyboard);
                    linear_keyboard.setVisibility(View.INVISIBLE);
                    write_or_speak=true;
                }
            });

        }

    }



    void addToChat(String message,String sentBy){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message,sentBy));
                //通知 messageAdapter 資料集已更改，這將觸發 RecyclerView 的重新繪製，以顯示新增的聊天訊息。
                messageAdapter.notifyDataSetChanged();
                //平滑地滾動到最後一個聊天訊息的位置，確保使用者總是能看到最新的訊息。
                recy.smoothScrollToPosition(messageAdapter.getItemCount());

            }
        });
    }

    //Bot回應
    void addResponse(String response){

        addToChat(response,Message.SENT_BY_BOT);
    }


    //調用API
    JSONArray messageArr = new JSONArray();
    public void callAPI(String question) {
        // OkHttp庫
        // messageList.add(new Message("Typing...", Message.SENT_BY_BOT));
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "gpt-3.5-turbo");

            // 建構對話歷史，包括系統消息
            //JSONArray messageArr = new JSONArray();

            // 添加系统消息
            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            //systemMessage.put("content", "I want you to be my ielts speaking examiner ,you will ask the ielts speaking question about part one,please ask me three question in total.One question one time and wait me answer it before you asking the next one.You should reply to my answer too,like two people are chatting.");
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
                .header("Authorization", "Bearer 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")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load due to " + e.getMessage());
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
                        addResponse(result.trim());

                        // 添加 ChatGPT 的回應到對話歷史
                        JSONObject chatGPTMessage = new JSONObject();
                        chatGPTMessage.put("role", "assistant");
                        chatGPTMessage.put("content", result.trim());
                        messageArr.put(chatGPTMessage);


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    // addResponse("Failed to load due to " + response.body().toString());
                    String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                    addResponse("Failed to load due to " + errorBody);
                }
            }
        });
    }

}