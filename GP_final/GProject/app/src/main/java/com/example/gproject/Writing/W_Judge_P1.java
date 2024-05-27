package com.example.gproject.Writing;

import static com.example.gproject.MainActivity.apiKey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gproject.MainActivity;
import com.example.gproject.R;
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
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class W_Judge_P1 extends AppCompatActivity {

    TextView judge;
    String Ans,Ques,Part,Key;
    FirebaseAuth auth;
    ImageButton back;
    private DatabaseReference databaseReference;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    //訊息超時timeout改為60s(預設:10s)
    OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.w_judge_p1);
        judge = findViewById(R.id.judge);
        //judge.setMovementMethod(new ScrollingMovementMethod());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Ques= extras.getString("Ques");
            Ans= extras.getString("Ans");
            Part =  extras.getString("part");
            Key = extras.getString("pushKey");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        // 添加初始系统消息
        JSONObject systemMessage = new JSONObject();
        JSONArray messages = new JSONArray();
        try {
            systemMessage.put("role", "system");
            //systemMessage.put("content", "You are an English article correction assistant.");
           systemMessage.put("content",Ques);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        messages.put(systemMessage);

        if(Part=="1")
            callAPI("Please check the following article for spelling and grammar, And tell me where is wrong and why.DO NOT repeat my answer.:"+Ans);
        else
            callAPI("The question is:"+Ques+"And my answer is:"+Ans+"  Please check my answer for spelling and grammar and tell me where is wrong and why.If my answer is too short, tell me how to improve it and give me an example.");


    }

    public void homeClick(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void push(){
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String judgeText = judge.getText().toString();

            DatabaseReference userAnswersRef = databaseReference
                    .child("users")
                    .child(userId)
                    .child("Judge")
                    .child("Writing")
                    .child(Key);

            userAnswersRef.setValue(judgeText)
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

    public void callAPI(String question){
        //OkHttp庫
        JSONObject jsonBody=new JSONObject();
        try {
            jsonBody.put("model","gpt-3.5-turbo");
            //建構對話歷史
            JSONArray messageArr = new JSONArray();

            JSONObject obj=new JSONObject();
            obj.put("role","user");
            obj.put("content",question);
            messageArr.put(obj);
            jsonBody.put("messages",messageArr);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body=RequestBody.create(jsonBody.toString(),JSON);
        Request request =new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization","Bearer "+apiKey)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("Failed to load due to "+e.getMessage());
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //如果網路請求成功
                if(response.isSuccessful()){
                    JSONObject jsonObject= null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray=jsonObject.getJSONArray("choices");
                        String result =jsonArray.getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                judge.setText(result.trim()+"\n");
                                push();

                            }
                        });


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                }else{
                    //addResponse("Failed to load due to "+response.body().toString());
                    String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                    System.out.println("Failed to load due to " + errorBody);
                }
            }
        });
    }


}