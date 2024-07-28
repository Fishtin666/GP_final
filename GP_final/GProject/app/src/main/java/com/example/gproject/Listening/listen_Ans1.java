package com.example.gproject.Listening;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.gproject.MainActivity;
import com.example.gproject.R;
import com.example.gproject.Review.RReview;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class listen_Ans1 extends AppCompatActivity {


    int testValue;// 保存 Bundle 中的参数值
    int AnsExit;//一題都沒寫
    String subKey;//realtime 聽力的子節點(日期)
    String testV;//傳給聽力review的testvalue
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
        TextView scoreView = findViewById(R.id.score);
        Button reviewButton = findViewById(R.id.review);

        // 获取传递的 Bundle 对象
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            testValue = bundle.getInt("testValue");
            subKey = bundle.getString("Ldate");
            AnsExit = bundle.getInt("answerExit");
            testV = String.valueOf(testValue);
            Log.d("TAG", "L_test value: " + testValue);
            Log.d("TAG", "answerExit: " + AnsExit);
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
                        // 打印 hashMap 的内容
                        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                            Log.d("HashMapContent", "Key: " + entry.getKey() + ", Value: " + entry.getValue());
                        }
                        if (AnsExit==0) {
                            reviewButton.setVisibility(View.GONE); // 隐藏 Button
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
                        // 根据正确的题数获取雅思分数并设置到TextView中
                        double score = getIELTSScore(correct);
                        scoreView.setText(String.valueOf(score));
                    })
                    .addOnFailureListener(e -> {
                        Log.e("TAG", "Error getting document count: ", e);
                    });
        }
    }

    // 创建一个方法来根据正确题数返回雅思分数
    public double getIELTSScore(int correct) {
        if (correct >= 39) {
            return 9.0;
        } else if (correct >= 37) {
            return 8.5;
        } else if (correct >= 35) {
            return 8.0;
        } else if (correct >= 33) {
            return 7.5;
        } else if (correct >= 30) {
            return 7.0;
        } else if (correct >= 27) {
            return 6.5;
        } else if (correct >= 23) {
            return 6.0;
        } else if (correct >= 20) {
            return 5.5;
        } else if (correct >= 16) {
            return 5.0;
        } else if (correct >= 13) {
            return 4.5;
        } else if (correct >= 10) {
            return 4.0;
        } else if (correct >= 6) {
            return 3.5;
        } else if (correct >= 4) {
            return 3.0;
        } else if (correct == 3) {
            return 2.5;
        } else if (correct == 2) {
            return 2.0;
        } else if (correct == 1) {
            return 1.0;
        } else {
            return 0.0; // assuming score is 0 for 0 correct answers
        }
    }

    public void NextClick(View view){
        Intent intent = new Intent();
        intent.setClass(this, listen.class);
        startActivity(intent);
        finish();
    }
    public void listenReview(View view){
        Intent intent = new Intent();
        intent.setClass(this, RReview.class);
        intent.putExtra("test", testV);
        intent.putExtra("Ldate", subKey);
        Log.d("LSubKey", testV+","+subKey);
        // 启动新的 Activity
        startActivity(intent);
        finish();
    }
}