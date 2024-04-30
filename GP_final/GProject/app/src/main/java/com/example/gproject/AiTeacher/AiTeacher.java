package com.example.gproject.AiTeacher;


import static com.example.gproject.MainActivity.apiKey;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gproject.Message;
import com.example.gproject.MessageAdapter;
import com.example.gproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class AiTeacher extends Activity {
    //宣告物件
    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    ImageButton sendButton;
    List<Message> messageList;
    MessageAdapter messageAdapter;

    LinearLayout func_btn,topic;

    Button changeTopic,translate,check;
    Button travel,food,happiness,hobbies;

    public String ch_result,or_question;

    //https://square.github.io/okhttp/
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    //訊息超時timeout改為60s(預設:10s)
    OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();
String cross_question="Describe a beautiful city." + " Describe a memorable trip." + " Describe a place you plan to travel to that is far away from your home?";
//    private String defaultQuestion = "I want you to be an English teacher to have a English conversation with me.  Next,choose a topic and ask me one question to have a conversation with me";
 private String defaultQuestion ="I want you to be my ielts speaking examiner ,you will ask the ielts speaking question about part one,please ask me three question in total.One question one time and wait me answer it before you asking the next one.You should reply to my answer too,like two people are chatting.(*Remember part one's main focus is to have the candidate introduce themselves. )";
//private String defaultQuestion ="I want you to be my ielts speaking examiner ,you will ask the ielts speaking question about part two,do not write all the conversation at once.I want you to ask me each question and wait me answer it before you asking the next one.";
//private String defaultQuestion ="The cross-topic technique involves identifying patterns between speaking topics, uncovering connections between each question, and using a single set of material/story to cover as many topics as possible.Please help me create cross-topic examples based on the following prompts.(50words)"+cross_question;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ai_teacher);

        messageList=new ArrayList<>();
        //取得介面元件，再設定給對應的物件
        recyclerView=findViewById(R.id.recycler_view);
        welcomeTextView=findViewById(R.id.welcome_text);
        messageEditText=findViewById(R.id.message_edit_text);
        sendButton=findViewById(R.id.send_btn);
        changeTopic=findViewById(R.id.change_topic);
        translate=findViewById(R.id.translate);
        check=findViewById(R.id.check);
        func_btn = findViewById(R.id.buttonGroup);
        topic=findViewById(R.id.topic);
        travel=findViewById(R.id.topic_Travel);
        food=findViewById(R.id.topic_Food);
        happiness=findViewById(R.id.topic_Happiness);
        hobbies=findViewById(R.id.topic_Hobbies);

        //隱藏topic按鈕
        topic.setVisibility(View.INVISIBLE);


        //設置recycle view
        messageAdapter=new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);

        LinearLayoutManager llm=new LinearLayoutManager(this);
        llm.setStackFromEnd(true);    //將使 RecyclerView 中的列表項目從底部開始顯示
        recyclerView.setLayoutManager(llm);

        //預設問題
        //addToChat("Choice a topic you want to talk about today!\nTravel,Food,Hobby?", Message.SENT_BY_BOT);
        //welcomeTextView.setVisibility(View.GONE);
        // 添加初始系统消息
        JSONObject systemMessage = new JSONObject();
        JSONArray messages = new JSONArray();
        try {
            systemMessage.put("role", "system");
            systemMessage.put("content", "You are a ielts speaking examiner .Try to make your response simply and clear.");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        messages.put(systemMessage);

        addToChat("我們開始練習英文吧！", Message.SENT_BY_ME);
        callAPI(defaultQuestion);
        welcomeTextView.setVisibility(View.GONE);  //正中間的字消失

        //按下發送鍵 ->使用者打字的部分形成對話框
        sendButton.setOnClickListener((v)->{
            String question =messageEditText.getText().toString().trim();
            or_question=question;
            addToChat(question,Message.SENT_BY_ME);
            messageEditText.setText("");   //打字的地方的字消失
            //welcomeTextView.setVisibility(View.GONE);  //正中間的字消失
            callAPI(question);

        });

        //翻譯
        translate.setOnClickListener((v)->{
            String translate_ch;
            translate_ch="Translate Following sentences into Traditional Chinese."+ch_result;
            callAPI(translate_ch);
            //addToChat(translate_ch,Message.SENT_BY_BOT);

        });

        //換話題
        String ques_travel = "I want you to be an English teacher to have a English conversation with me.ask me one question with to topic Travel.";
        String ques_food = "I want you to be an English teacher to have a English conversation with me.ask me one question with to topic Food.";
        String ques_happiness = "I want you to be an English teacher to have a English conversation with me.ask me one question with to topic Happiness.";
        String ques_hobbies = "I want you to be an English teacher to have a English conversation with me.ask me one question with to topic Hobbies.";
        changeTopic.setOnClickListener((v)->{
            func_btn.setVisibility(View.INVISIBLE);
            topic.setVisibility(View.VISIBLE);
            addToChat("換個話題吧!",Message.SENT_BY_ME);
            addToChat("Sure!!Let's change a topic!!Chose a topic you like above!",Message.SENT_BY_BOT);
        });

        travel.setOnClickListener((v)->{
            callAPI(ques_travel);
            func_btn.setVisibility(View.VISIBLE);
            topic.setVisibility(View.INVISIBLE);
        });
        food.setOnClickListener((v)->{
            callAPI(ques_food);
            func_btn.setVisibility(View.VISIBLE);
            topic.setVisibility(View.INVISIBLE);
        });
        happiness.setOnClickListener((v)->{
            callAPI(ques_happiness);
            func_btn.setVisibility(View.VISIBLE);
            topic.setVisibility(View.INVISIBLE);
        });
        hobbies.setOnClickListener((v)->{
            callAPI(ques_hobbies);
            func_btn.setVisibility(View.VISIBLE);
            topic.setVisibility(View.INVISIBLE);
        });


        //檢查
        check.setOnClickListener((v)->{
            String finish;
            finish="Based on the entire conversation above, please provide me with suggestions and comments.If i make any english mistake ,I want you to correct me and explain the correction.";
            callAPI(finish);
        });


    }

    void addToChat(String message,String sentBy){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message,sentBy));
                //通知 messageAdapter 資料集已更改，這將觸發 RecyclerView 的重新繪製，以顯示新增的聊天訊息。
                messageAdapter.notifyDataSetChanged();
                //平滑地滾動到最後一個聊天訊息的位置，確保使用者總是能看到最新的訊息。
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());

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
                .header("Authorization", "Bearer "+apiKey)
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

                        // 轉中文
                        ch_result = result.trim();
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
