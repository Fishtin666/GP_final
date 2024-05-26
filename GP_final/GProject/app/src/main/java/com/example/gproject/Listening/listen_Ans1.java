package com.example.gproject.Listening;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.gproject.MainActivity;
import com.example.gproject.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class listen_Ans1 extends AppCompatActivity {


    int testValue;// 保存 Bundle 中的参数值
    int correct=0;
//    private Map<Integer, Integer> docCountMap = new HashMap<>();
    private Map<Integer, String> answerMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_ans1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        TextView corView = findViewById(R.id.correcttext);

        // 获取传递的 Bundle 对象
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            testValue = bundle.getInt("test");
            Log.d("TAG", "L_test value: " + testValue);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Listen_"+testValue)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                            String documentName = document.getId();
//                            // 计算文档数量
//                            for(int section=0 ;section<=4 ;section++) {
//                                if (documentName.startsWith("S" + section)) {
//                                    Integer currentCount = docCountMap.get(section);
//                                    if (currentCount == null) {
//                                        currentCount = 0;
//                                    }
//                                    docCountMap.put(section, currentCount + 1);
////                                    for (Map.Entry<Integer, Integer> entry : docCountMap.entrySet()) {
////                                        Log.d("TAG", "Key: " + entry.getKey() + ", Count: " + entry.getValue());
////                                    }
//                                    //Log.d("TAG", "Document count for " + section + ": " + docCountMap.get(section));
//                                    break; // 一旦找到匹配的前缀就可以停止检查其他前缀
//                                }
//                            }

                            Map<String, Object> documentData = document.getData();
                            for (Map.Entry<String, Object> entry : documentData.entrySet()) {
                                String fieldName = entry.getKey();
                                Object fieldValue = entry.getValue();
                                // 如果字段名以 "A" 开头，并且字段值不为空
                                if (fieldName.startsWith("A")) {
                                    // 使用正则表达式从字段名称中提取数字部分
                                    Pattern pattern = Pattern.compile("\\d+");
                                    Matcher matcher = pattern.matcher(fieldName);
                                    if (matcher.find()) {
                                        String number = matcher.group(); // 提取到的数字部分
                                        int Anskey = Integer.parseInt(number);
                                        answerMap.put(Anskey, (String) fieldValue);
                                    }
                                }
                            }
                        }
                        // 输出 answerMap 中的内容
                        for (Map.Entry<Integer, String> entry : answerMap.entrySet()) {
                            Log.d("AnswerMap", "AnswerMap Key: " + entry.getKey() + ", Value: " + entry.getValue());
                        }
//                        for (Map.Entry<Integer, Integer> entry : docCountMap.entrySet()) {
//                            Log.d("TAG", "docCountMap Key: " + entry.getKey() + ", Count: " + entry.getValue());
//                        }

                        HashMap<String, String> hashMap = new HashMap<>();
                        for (String key : bundle.keySet()) {
                            hashMap.put(key, bundle.getString(key));
                        }

                        // 设置40个TextView的内容
                        for (int i = 1; i <= 40; i++) {
                            // 构建TextView的ID
                            String textViewId = "ans" + i;
                            String ansViewId = "c" + i;
                            // 获取TextView的资源ID
                            int resId = getResources().getIdentifier(textViewId, "id", getPackageName());
                            int resAnsId = getResources().getIdentifier(ansViewId, "id", getPackageName());

                            if (resId != 0) {
                                TextView textView = findViewById(resId);
                                TextView ansView = findViewById(resAnsId);

                                if (textView != null) {
                                    // 将int类型的i转换为String类型
                                    String key = String.valueOf(i);

                                    if (answerMap.containsKey(i)) {
                                        // 如果 answerMap 中包含指定的键，则从 answerMap 中获取对应的值
                                        String valueOFans = answerMap.get(i);
                                        Log.d("TAG", "answerMap contains key " + key + ", value: " + valueOFans);
                                    }else {Log.d("TAG","NO");}

                                    // 检查HashMap中是否包含指定的键
                                    if (hashMap.containsKey(key)) {
                                        // 如果HashMap中包含指定的键，则从HashMap中获取对应的值并设置到TextView中
                                        String value = hashMap.get(key);
                                        Log.d("HashMap", "hashmap listen_ans1 get " + key + ": " + value);
                                        textView.setText(value);
                                        if (answerMap.containsKey(i)) {
                                            // 如果 answerMap 中包含指定的键，则从 answerMap 中获取对应的值
                                            String valueOFans = answerMap.get(i);
                                            Log.d("TAG", "answerMap contains key " + key + ", value: " + valueOFans);
                                            // 如果 hashMap 和 answerMap 中对应键值的值不同（不考虑大小写）
                                            if (!value.equalsIgnoreCase(valueOFans)) {
                                                Log.d("TAG", "Values for key " + key + " are different");
                                                textView.setText(value);
                                                // 将 TextView 的文本颜色设置为红色
                                                textView.setTextColor(Color.RED);
                                                ansView.setText(valueOFans);
                                            } else {
                                                Log.d("TAG", "Values for key " + key + " are the same");
                                                textView.setText(value);
                                                // 将 TextView 的文本颜色设置为默认颜色（黑色）
                                                textView.setTextColor(Color.BLACK);
                                                ansView.setText("");
                                                correct++;
                                            }
                                        }
                                    } else {
                                        // 如果HashMap中不包含指定的键，则将TextView的文本设置为空字符串
                                        //Log.d("TAG", "empty i " + i);
                                        textView.setText(answerMap.get(i));
                                        textView.setTextColor(ContextCompat.getColor(this, R.color.blue));
                                        if (ansView != null) {
                                            ansView.setText("");
                                        }
                                    }

                                }
                            }
                        }
                        corView.setText(correct+"/40");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("TAG", "Error getting document count: ", e);
                    });
        }
    }

    public void NextClick(View view){
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}