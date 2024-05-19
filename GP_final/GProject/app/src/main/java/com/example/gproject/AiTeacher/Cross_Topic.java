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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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

    ImageButton back;




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
                defaultQuestion = cross_question+"Please generate an answer for me based on the above sentence, so that no matter which of the above questions I encounter, I can answer according to this response.";
                callAPI(defaultQuestion);



               }
        });
        //describe a beautiful city.describe a memorable trip.Describe a place you plan to travel to that is far away from your home.






    }




















    public void homeClick(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void push(String cross_ans,String cross_ques){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        //將答案存入db
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            long currentTime = System.currentTimeMillis();
            Date date = new Date(currentTime);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String formattedDate = sdf.format(date);


            // 生成唯一的键，并存储答案
            DatabaseReference userAnswersRef = databaseReference
                    .child("CrossTopic")
                    .child(userId)
                    .child("self")
                    .child(String.valueOf(formattedDate));


            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("Ques", cross_ques);
            dataMap.put("Ans", cross_ans);
            userAnswersRef.setValue(dataMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                            } else {

                            }
                        }
                    });
        }
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
                                //cross_answer = result.trim();
                                cross_result.setText(result.trim());
                                push(result.trim(),editText.getText().toString());
                                //SpanningString();
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