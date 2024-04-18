package com.example.gproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gproject.Adapters.CrossTopicAdapter;
import com.example.gproject.Adapters.QuestionAdapter;
import com.example.gproject.Models.QuestionModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Cross_Topic_qusetion extends AppCompatActivity {
    CrossTopicAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<String> list;

    FirebaseAuth auth;
    Button start;
    JustifyTextView Result;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    //訊息超時timeout改為60s(預設:10s)
    OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cross_topic_qusetion);
        auth = FirebaseAuth.getInstance();

        start = findViewById(R.id.start_CrossTopic);
        Result = findViewById(R.id.result);
        Result.setMovementMethod(new ScrollingMovementMethod());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

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

        list = new ArrayList<>();
        recyclerView = findViewById(R.id.recy);
        LinearLayoutManager layoutManager =new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CrossTopicAdapter(this,list);
        recyclerView.setAdapter(adapter);

        String topic=getIntent().getStringExtra("topic");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Speaking_"+topic)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String question = document.getString("Question").replace("\\n", "\n");

                                if (question != null) {
                                        list.add(question);

                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(Cross_Topic_qusetion.this, "db錯誤", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start.setText("正在串題...");
                // 获取被选中的复选框文本内容
                StringBuilder crossQuestionsBuilder = new StringBuilder();
                for (String selectedQuestion : adapter.getSelectedQuestions()) {
                    crossQuestionsBuilder.append(selectedQuestion).append("\n");
                }

                // 将所有被选中的文本存储在一个字符串中
                String crossQuestions = crossQuestionsBuilder.toString();
                String order=crossQuestions+"Please generate an answer for me based on the above sentence, so that no matter which of the above questions I encounter, I can answer according to this response.";
                callAPI(order);
                //Toast.makeText(Cross_Topic_qusetion.this, crossQuestions, Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void callAPI(String question) {
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
                                Result.setText(result.trim());
                                System.out.println("結果"+result.trim());
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
}