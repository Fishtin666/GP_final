package com.example.gproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReviewShow_appConversation extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    String value;
    ImageView home;
    ImageButton back;
    MessageAdapter messageAdapter;
    List<Message> messageList;
    RecyclerView recy;




    PopupWindow popupWindow;
    TextView checkJudge,Judge;
//    String randomCode="-NxsG-gP0QuHJweA8BT2";
    String randomCode;  //隨機瑪
    String topic;  //主題
    String num="0";    //第幾個回答

    private ArrayList<String> conversation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_show_app_conversation);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        home = findViewById(R.id.home);
        back =findViewById(R.id.back2);
        recy = findViewById(R.id.recy);
        checkJudge = findViewById(R.id.check_judge);
        Judge = findViewById(R.id.judge);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            topic = bundle.getString("topic");
            randomCode = bundle.getString("radom");
        }



        messageList=new ArrayList<>();
        conversation = new ArrayList<>();

        //設置recycle view
        messageAdapter=new MessageAdapter(messageList);
        recy.setAdapter(messageAdapter);

        LinearLayoutManager llm=new LinearLayoutManager(this);
        llm.setStackFromEnd(true);    //將使 RecyclerView 中的列表項目從底部開始顯示
        recy.setLayoutManager(llm);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReviewShow_appConversation.this, MainActivity.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getConversation();
        getJudge();

//        getRandomCode(new ReviewShow_Speaking.OnRandomCodeGeneratedListener() {
//            @Override
//            public void onRandomCodeGenerated(String randomCode) {
//                getConversation();
//                getJudge();
//
//            }
//        });


    }
//    public interface OnRandomCodeGeneratedListener {
//        void onRandomCodeGenerated(String randomCode);
//    }

    public void getConversation(){
                FirebaseAuth auth;
                DatabaseReference databaseReference;
                auth = FirebaseAuth.getInstance();
                databaseReference = FirebaseDatabase.getInstance().getReference();

                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();

                    DatabaseReference targetRef = databaseReference
                            .child("app_conversation")
                            .child(userId)
                            .child("Conversation")
                            .child(topic)
                            .child(randomCode);


                    targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // 清空先前的数据
                            conversation.clear();

                            // 遍历获取到的数据
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String value = snapshot.getValue(String.class);
                                conversation.add(value);
                            }

                            for (int i = 0; i < conversation.size(); i++) {
                                if(i==0||i % 2 == 0){
                                    addToChat(conversation.get(i), Message.SENT_BY_BOT);
                                }else
                                    addToChat(conversation.get(i), Message.SENT_BY_ME);

                            }
                            recy.smoothScrollToPosition(0);
                            //Toast.makeText(ReviewShow_appConversation.this, conversation.toString(), Toast.LENGTH_SHORT).show();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                }


    }

//    public void getRandomCode(final ReviewShow_Speaking.OnRandomCodeGeneratedListener listener) {
//        auth = FirebaseAuth.getInstance();
//        databaseReference = FirebaseDatabase.getInstance().getReference();
//
//        FirebaseUser currentUser = auth.getCurrentUser();
//        if (currentUser != null) {
//            String userId = currentUser.getUid();
//
//            DatabaseReference targetRef = databaseReference
//                    .child("app_conversation")
//                    .child(userId)
//                    .child("Conversation")
//                    .child(topic);
//
//
//            targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    // 检查数据是否有效
//                    if (dataSnapshot.exists()) {
//                        int count = 0;
//                        String code = null;
//
//                        // 遍历子项
//                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//                            //Toast.makeText(ReviewShow_Speaking.this, count+","+num, Toast.LENGTH_SHORT).show();
//
//                            // 判断是否是第 "num" 个子项
//                            if (String.valueOf(count).equals(num)) {
//                                code = childSnapshot.getKey();
//                                //Toast.makeText(ReviewShow_Speaking.this, "找到code", Toast.LENGTH_SHORT).show();
//
//                                break;  // 获取第 "num" 个子项的 key 后即可退出循环
//                            }
//                            count++;  // 增加计数器
//                        }
//                        randomCode = code;
//
//                        if(code!=null){
//
//                            // Toast.makeText(ReviewShow_Speaking.this, randomCode, Toast.LENGTH_SHORT).show();
//
//                            listener.onRandomCodeGenerated(randomCode);
//                        }else Toast.makeText(ReviewShow_appConversation.this, "找不到randomcode", Toast.LENGTH_SHORT).show();
//
//
//
//
//                    } else {
//                        Toast.makeText(ReviewShow_appConversation.this, "数据库不存在", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    // 读取数据时发生错误时调用此方法
//                    Log.e("FirebaseKey", "Error occurred while reading data", databaseError.toException());
//                }
//            });
//        }
//    }


    public void getJudge(){
                auth = FirebaseAuth.getInstance();
                databaseReference = FirebaseDatabase.getInstance().getReference();

                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();

                    DatabaseReference targetRef = databaseReference
                            .child("app_conversation")
                            .child(userId)
                            .child("Judge")
                            .child(topic);
                    //.child(randomCode);    //###選擇是第幾次回答####


                    targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                // 检查特定 key 的值
                                DataSnapshot valueSnapshot = dataSnapshot.child(randomCode);
                                if (valueSnapshot.exists()) {
                                    value = valueSnapshot.getValue(String.class);
                                    //show_popup(value);
                                    Judge.setText(value);

                                    //Toast.makeText(ReviewShow_appConversation.this, "judge:"+value, Toast.LENGTH_SHORT).show();

                                    Log.d("FirebaseValue", "Value: " + value);
                                } else {
                                    Log.d("FirebaseValue", "Key not found");
                                    Toast.makeText(ReviewShow_appConversation.this, "randomCode找不到", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(ReviewShow_appConversation.this, "judge資料庫找不到", Toast.LENGTH_SHORT).show();

                                Log.d("FirebaseValue", "Path not found");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                }

    }

    void addToChat(String message, String sentBy) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message, sentBy));
                //通知 messageAdapter 資料集已更改，這將觸發 RecyclerView 的重新繪製，以顯示新增的聊天訊息。
                messageAdapter.notifyDataSetChanged();
                //平滑地滾動到最後一個聊天訊息的位置，確保使用者總是能看到最新的訊息。
                //recy.smoothScrollToPosition(messageAdapter.getItemCount());

            }
        });
    }

    public void show_popup(String JudgeText){
        // 弹出 PopupWindow
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.review_judge_popup, null);
        int width = ConstraintLayout.LayoutParams.WRAP_CONTENT;;
        int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;;
        boolean focusable = true; // 让PopupWindow在失去焦点时自动关闭
        popupWindow = new PopupWindow(popupView, width, height, focusable);

        // 设置PopupWindow的内容
        TextView close=popupView.findViewById(R.id.close);
        TextView judge=popupView.findViewById(R.id.judge);

        judge.setText(JudgeText);
        judge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });



    }

}