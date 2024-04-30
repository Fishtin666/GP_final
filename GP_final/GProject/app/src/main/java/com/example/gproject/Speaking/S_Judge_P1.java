package com.example.gproject.Speaking;

import static com.example.gproject.MainActivity.apiKey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.example.gproject.R;

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

public class S_Judge_P1 extends AppCompatActivity {

    TextView question;
    TextView answer;
    static TextView GPTResponse;
    TextView Judge;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    //訊息超時timeout改為60s(預設:10s)
    static OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_judge_p1);
        question = findViewById(R.id.item_question);
        answer = findViewById(R.id.answer);
        GPTResponse = findViewById(R.id.Response);
        Judge = findViewById(R.id.judge);
        Judge.setMovementMethod(new ScrollingMovementMethod());


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String Ques= extras.getString("question");
            String Ans= extras.getString("answer");
            question.setText(Ques);
            answer.setText(Ans);
        }

        // 添加初始系统消息
        JSONObject systemMessage = new JSONObject();
        JSONArray messages = new JSONArray();
        try {
            systemMessage.put("role", "system");
            //systemMessage.put("content", "You are an English teacher for oral and grammar enhancement.Try to make your response simply and clear.");
            systemMessage.put("content",question.getText().toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        messages.put(systemMessage);

        callAPI(answer.getText().toString()+"Reply only to the above sentence but do not ask further questions.",1);
        callAPI("Check the following sentences for spelling and grammar errors, correct them.And tell me where is wrong and why."+answer.getText().toString(),2);

    }

    public void callAPI(String question, int who){
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

                        if(who==1) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    GPTResponse.setText(result.trim());
                                }
                            });
                        } else if (who==2) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Judge.setText(result.trim());
                                }
                            });
                        }
                        //轉中文
                        //ch_result=result.trim();
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

    public void homeClick(View v){
        Intent intent =new Intent(this,Speaking.class);
        startActivity(intent);
    }

}