package com.example.gproject.Speaking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
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

public class S_Judge_P2 extends AppCompatActivity {

    TextView Response,rof1,rof2;
    String Ques,Ans,Num,Topic;

    public static boolean ROf_ques=false;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    //訊息超時timeout改為60s(預設:10s)
    OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_judge_p2);

        Response = findViewById(R.id.Response);
        rof1 = findViewById(R.id.ROf_Q1);
        Response.setMovementMethod(new ScrollingMovementMethod());

        Bundle extras=getIntent().getExtras();
        if(extras!=null){
            Ques= extras.getString("question");
            Ans= extras.getString("answer");
            Num = extras.getString("num");
            Topic = extras.getString("topic");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }
        callAPI("You are a IELTS examiner for speaking part 2.The question is"+Ques+"My answer is"+Ans+" Do not repeat my answer. Check my answer for spelling and grammar errors, correct them.And tell me where is wrong and why.If my answer is too short,please tell me how to improve it and give me example.",0);
        //callAPI("You are a ielts examiner.My answer for"+Ques+"is"+Ans+", Please ask me another question related to my answer.Do not reply to my answer,just ask question.",1);
        // 添加初始系统消息
//        JSONObject systemMessage = new JSONObject();
//        JSONArray messages = new JSONArray();
//        try {
//            systemMessage.put("role", "system");
//            //systemMessage.put("content", "You are an English teacher for oral and grammar enhancement.Try to make your response simply and clear.");
//            systemMessage.put("content",Ques);
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
//        messages.put(systemMessage);
//
//        callAPI("Check the following sentences for spelling and grammar errors, correct them.And tell me where is wrong and why."+Ans,2);

    }

    public void callAPI(String question,int num){
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
                .header("Authorization","Bearer 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")
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
                                if(num==0){
                                    Response.setText(result.trim());
                                    callAPI("You are a ielts examiner.My answer for"+Ques+"is"+Ans+", Please ask me another question related to my answer.Do not reply to my answer,just ask question.",1);

                                }else
                                    rof1.setText(result.trim());
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

    public void ROf_Q1Click(View v){
        TextView ROf_Q1 = findViewById(R.id.ROf_Q1);
        Intent intent =new Intent(this, Speaking_part1_answer.class);
        intent.putExtra("ROf_ques",ROf_Q1.getText().toString());
        startActivity(intent);
        ROf_ques=true;
    }



    public void nextClick(View v){
        Bundle bundle=new Bundle();
        Intent intent =new Intent(this, Speaking_part3_question.class);
        bundle.putString("num",Num);
        bundle.putString("topic",Topic);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void homeClick(View v){
        Intent intent =new Intent(this,Speaking.class);
        startActivity(intent);
    }



}