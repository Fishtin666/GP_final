package com.example.gproject;

import static android.content.ContentValues.TAG;
import static com.example.gproject.MainActivity.apiKey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

public class AiChat_Judge extends AppCompatActivity {
    TextView judge;
    String pushKey,topic;
    String judge_question,result;
    ImageView home;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    //訊息超時timeout改為60s(預設:10s)
    OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ai_chat_judge);
        judge = findViewById(R.id.result);
        home = findViewById(R.id.home);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AiChat_Judge.this,MainActivity.class));
            }
        });




        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pushKey=extras.getString("pushKey");
            topic=extras.getString("topic");

        }

        judge_question="The following is a conversation between AI and a user. First,provide feedback about user's whole responses.YOU DON'T NEED to judge about AI responses.Then Check each response for grammar errors, correct them.And tell me where is wrong and why.If my answer is too short,please tell me how to improve it and give me example.";

        // 调用示例
        getConversation(new ConversationListener() {
            @Override
            public void onConversationLoaded(String conversation) {
                // 在对话加载完成后调用 API
                System.out.println(judge_question + ":" + conversation);
               //Toast.makeText(AiChat_Judge.this, "呼叫gpt"+judge_question + ":" + conversation, Toast.LENGTH_SHORT).show();

                callAPI(judge_question + ":" + conversation);
            }
        });
        //callAPI(judge_question+":"+result);

    }

//    public void getConversation(){
//        ArrayList<String> conversationList = new ArrayList<>();
//        FirebaseAuth auth= FirebaseAuth.getInstance();;
//        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
//
//        FirebaseUser currentUser = auth.getCurrentUser();
//        if (currentUser != null) {
//            String userId = currentUser.getUid();
//
//            DatabaseReference targetRef = databaseReference
//                    .child("app_conversation")
//                    .child(userId)
//                    .child("Conversation")
//                    .child(pushKey);
//            targetRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    // 遍历每个子节点
//                    if (dataSnapshot.exists()) {
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            // 获取子节点的键值
//                            String key= snapshot.getKey();
//                            String value= snapshot.getValue().toString();
//
//                            //奇數
//                            if(Integer.parseInt(key)%2==1){
//                                conversationList.add("User:"+value);
//                            }else
//                                conversationList.add("Ai:"+value);
//
//
//                        }
//
//                    }
//
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    // 读取数据失败时的处理
//                    Log.e("Firebase", "读取数据失败：" + databaseError.getMessage());
//                }
//            });
//
//        }
//
//
//        StringBuilder sb = new StringBuilder();
//        for (String str : conversationList) {
//            sb.append(str).append(", ");  // 在每个元素后面添加逗号和空格
//        }
//        // 删除最后多余的逗号和空格
//        if (sb.length() > 0) {
//            sb.setLength(sb.length() - 2);  // 删除最后添加的逗号和空格
//        }
//        result = sb.toString();
//        System.out.println("對話: "+result);
//        judge_question="The following is a conversation between AI and a user. Based on the user's answers, provide feedback (strictly) and  correcting any grammar or other mistakes in user's responses.";
//
//    }


    public void getConversation(ConversationListener listener) {
        ArrayList<String> conversationList = new ArrayList<>();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference targetRef = databaseReference
                    .child("app_conversation")
                    .child(userId)
                    .child("Conversation")
                    .child(topic)
                    .child(pushKey);
            targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String key = snapshot.getKey();
                            String value = snapshot.getValue(String.class);

                            if (Integer.parseInt(key) % 2 == 1) {
                                conversationList.add("User:" + value);
                            } else {
                                conversationList.add("Ai:" + value);
                            }
                        }

                        StringBuilder sb = new StringBuilder();
                        for (String str : conversationList) {
                            sb.append(str).append(", ");
                        }
                        if (sb.length() > 0) {
                            sb.setLength(sb.length() - 2);
                        }
                        String result = sb.toString();
                        System.out.println("對話string: " + result);
                       // Toast.makeText(AiChat_Judge.this, "對話string: " + result, Toast.LENGTH_SHORT).show();

                        // 将对话内容传递给回调函数
                        if (listener != null) {
                            listener.onConversationLoaded(result);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "读取数据失败：" + databaseError.getMessage());
                }
            });
        }
    }

    public void push(String Judge){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference userAnswersRef = databaseReference
                    .child("app_conversation")
                    .child(userId)
                    .child("Judge")
                    .child(topic)
                    .child(pushKey);

            userAnswersRef.setValue(Judge)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                            } else {
                                // 存储失败
                                // 处理存储失败的情况
                            }
                        }
                    });

        }
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

                            //要修改UI(TextView:bot)->需切換到主要執行緒
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    judge.setText(result.trim());
                                    push(result.trim());
                                }
                            });


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


    public interface ConversationListener {
        void onConversationLoaded(String conversation);
    }
}

