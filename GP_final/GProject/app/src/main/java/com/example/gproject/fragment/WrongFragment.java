package com.example.gproject.fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.gproject.Listening.listen_ftest1;
import com.example.gproject.R;
import com.example.gproject.Re_Reading.Re_chose;
import com.example.gproject.Re_Reading.Re_judge;
import com.example.gproject.Re_Reading.Re_match;
import com.example.gproject.Re_Reading.Re_multiple;
import com.example.gproject.Review.RReview;
import com.example.gproject.Review.ReviewShow_VR;
import com.example.gproject.Review.Review_choose;
import com.example.gproject.Review.favorite_question;
import com.example.gproject.ReviewShow_Speaking;
import com.example.gproject.ReviewShow_Writing;
import com.example.gproject.ReviewShow_appConversation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.gproject.Re_Reading.Re_blank;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WrongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WrongFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "testkey";
    private static final String ARG_PARAM2 = "spinner";
    private static final String ARG_PARAM3 = "testSubKey";

    // TODO: Rename and change types of parameters
    private String selectedTestKey;
    private String selectedSpinner;
    private String selectedTestSubKey;

    public WrongFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static WrongFragment newInstance(String selectedTestKey, String selectedSpinner, String selectedTestSubKey) {
        WrongFragment fragment = new WrongFragment();
        Bundle args = new Bundle();
        args.putString("ARG_PARAM1", selectedTestKey);
        args.putString("ARG_PARAM2", selectedSpinner);
        args.putString("ARG_PARAM3", selectedTestSubKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedTestKey = getArguments().getString("ARG_PARAM1");
            selectedSpinner = getArguments().getString("ARG_PARAM2");
            selectedTestSubKey = getArguments().getString("ARG_PARAM3");
            Log.d("TAG",":"+selectedSpinner+","+selectedTestKey);
        }else {
            Log.d("WrongFragment", "No arguments received");
        }
    }

//    // 公共方法用于设置参数
//    public void setParameters(String param1, String param2) {
//        selectedTestKey = param1;
//        selectedSpinner = param2;
//        Log.d("TAG", ":" + selectedSpinner + "," + selectedTestKey);
//    }

    FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private LinearLayout reviewLayout;
    Spinner spinner1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_wrong, container, false);

        ImageButton pencil = rootview.findViewById(R.id.pencil1);
        Button all = rootview.findViewById(R.id.all);
        ImageButton fav = rootview.findViewById(R.id.empty_Q);
        reviewLayout = rootview.findViewById(R.id.reviewLayout);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Log.d("TAG",":"+selectedSpinner+","+selectedTestKey);

        spinner1 = rootview.findViewById(R.id.spinner1);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(getContext(),R.array.Review_array, android.R.layout.simple_dropdown_item_1line);
        spinner1.setAdapter(adapter1);


        // 检查传入的参数 selectedSpinner 是否为 null
        if (selectedSpinner != null) {
//            if(selectedSpinner.equals("reading")){
//                spinner1.setSelection(0); // 设置为 "reading"
//                getSelectR();
//                // 禁用 Spinner，防止用户交互
//                spinner1.setEnabled(false);
//            } else if (selectedTestKey.equals("listening")) {
//                spinner1.setSelection(1); // 设置为 "listening"
//                getListening();
//                spinner1.setEnabled(false);
//            }
            // 根据 selectedSpinner 的值设置 Spinner 的默认选项
            switch (selectedSpinner) {
                case "reading":
                    spinner1.setSelection(0); // 设置为 "reading"
                    GetSelectR();
                    spinner1.setEnabled(false); // 禁用 Spinner，防止用户交互
                    break;
                case "listening":
                    spinner1.setSelection(1); // 设置为 "listening"
                    GetSelectL();
                    spinner1.setEnabled(false);
                    break;
                case "writing":
                    spinner1.setSelection(2); // 设置为 "writing"
                    GetSelectW();
                    spinner1.setEnabled(false);
                    break;
                case "speaking":
                    spinner1.setSelection(3); // 设置为 "speaking"
                    GetselectS();
                    spinner1.setEnabled(false);
                    break;
                case "conversation":
                    spinner1.setSelection(4); // 设置为 "conversation"
                    GetselectAPPchat();
                    spinner1.setEnabled(false);
                    break;
                case "VR":
                    spinner1.setSelection(5); // 设置为 "VR"
                    getVRjudge();
                    spinner1.setEnabled(false);
                    break;
                default:
                    // 默认情况下不设置任何选项
                    break;
            }
        }
        else {
            setupSpinnerListener();
        }

        pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Review_choose.class);
                startActivity(intent);
            }
        });
        all.setOnClickListener(new View.OnClickListener(){
            public  void onClick(View v){
                spinner1.setEnabled(true);
                setupSpinnerListener();
                selectedSpinner=null;
                selectedTestKey=null;
            }
        });
        fav.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getContext(), favorite_question.class);
                startActivity(intent);
            }
        });

    return rootview;
    }

    private void setupSpinnerListener() {
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: //R
                        getReading();
                        break;
                    case 1: //L
                        getListening();
                        break;
                    case 2: //W
                        getWriting();
                        break;
                    case 3: //S
                        getSpeaking();
                        break;
                    case 4: //conversation
                        getAPPchat();
                        break;
                    case 5://VR
                        getVRjudge();
                        break;
                    default: //R
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }




    public Button setbutton(){
        // 创建一个按钮
        Button button = new Button(getContext());
        // 设置按钮的布局参数
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                250); // 设置高度为110dp
        layoutParams.setMargins(0, 20, 0, 0); // 设置按钮之间的间距
        button.setLayoutParams(layoutParams);

        // 设置按钮的背景色和圆角
        button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
        button.setTextColor(getResources().getColor(R.color.black));
        button.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        button.setPadding(20, 0, 0, 0); // 设置文本的左内边距

        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[] { 40, 40, 40, 40, 40, 40, 40, 40 }); // 设置圆角半径
        shape.setColor(getResources().getColor(R.color.gray)); // 设置背景颜色
        button.setBackground(shape);

        return button;
    }

    @NonNull
    public void getReading(){
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference targetRef = databaseReference
                    .child("R_Review")
                    .child(userId);
                    //.child("Writing");

            targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // 清空布局中的按钮
                    reviewLayout.removeAllViews();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String key = childSnapshot.getKey();

                            // 获取子节点下的所有子节点名称
                            for (DataSnapshot subChildSnapshot : childSnapshot.getChildren()) {
                                String subKey = subChildSnapshot.getKey();
                                // 这里可以处理子节点名称，例如打印输出或者其他操作
                                Log.d("SubKey", subKey);
                                for (DataSnapshot subSubChildSnapshot : subChildSnapshot.getChildren()) {
                                    String subSubKey = subSubChildSnapshot.getKey();
                                    long childCount = subSubChildSnapshot.getChildrenCount();
                                    // 打印subSubKey
                                    Log.d("SubSubKey", subSubKey);
                                    Log.d("SubSubKey childCount", String.valueOf(childCount));
                                    // 创建一个按钮
                                    Button button = setbutton();
                                    // 设置按钮的文本为子节点的键名
                                    button.setText("Reading \n" + key + "    question " + subKey + "\n" +subSubKey);
                                    // 将按钮添加到布局中
                                    reviewLayout.addView(button);
                                    // 为按钮设置点击事件
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (key.equals("R_blank")) {
                                                // 创建一个新的 Intent
                                                Intent intent = new Intent(getContext(), Re_blank.class);
                                                //将 key 和 subKey 作为额外数据传递给下一个 Activity
                                                intent.putExtra("docID", subKey);
                                                intent.putExtra("saveT", subSubKey);
                                                Log.d("r_blank",subKey+","+subSubKey);
                                                // 启动新的 Activity
                                                startActivity(intent);
                                            }else if (key.equals("R_chose")){
                                                // 创建一个新的 Intent
                                                Intent intent = new Intent(getContext(), Re_chose.class);
                                                //将 key 和 subKey 作为额外数据传递给下一个 Activity
                                                intent.putExtra("docID", subKey);
                                                intent.putExtra("saveT", subSubKey);
                                                Log.d("r_chose",subKey+","+subSubKey);
                                                // 启动新的 Activity
                                                startActivity(intent);
                                            }else if (key.equals("R_judge")){
                                                // 创建一个新的 Intent
                                                Intent intent = new Intent(getContext(), Re_judge.class);
                                                //将 key 和 subKey 作为额外数据传递给下一个 Activity
                                                intent.putExtra("docID", subKey);
                                                intent.putExtra("saveT", subSubKey);
                                                Log.d("r_judge",subKey+","+subSubKey);
                                                // 启动新的 Activity
                                                startActivity(intent);
                                            }else if (key.equals("R_match")){
                                                // 创建一个新的 Intent
                                                Intent intent = new Intent(getContext(), Re_match.class);
                                                //将 key 和 subKey 作为额外数据传递给下一个 Activity
                                                intent.putExtra("docID", subKey);
                                                intent.putExtra("saveT", subSubKey);
                                                Log.d("r_match",subKey+","+subSubKey);
                                                // 启动新的 Activity
                                                startActivity(intent);
                                            }else {
                                                // 创建一个新的 Intent
                                                Intent intent = new Intent(getContext(), Re_multiple.class);
                                                //将 key 和 subKey 作为额外数据传递给下一个 Activity
                                                intent.putExtra("docID", subKey);
                                                intent.putExtra("saveT", subSubKey);
                                                Log.d("r_mutlipe",subKey+","+subSubKey);
                                                // 启动新的 Activity
                                                startActivity(intent);
                                            }

                                        }
                                    });
                                }
                            }
                        }
                    } else {
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }


    @NonNull
    public void getWriting(){
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference targetRef = databaseReference
                    .child("users")
                    .child(userId)
                    .child("Writing");

            targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // 清空布局中的按钮
                    reviewLayout.removeAllViews();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String key = childSnapshot.getKey();

                            // 获取子节点下的所有子节点名称
                            for (DataSnapshot subChildSnapshot : childSnapshot.getChildren()) {
                                String subKey = subChildSnapshot.getKey();
                                // 这里可以处理子节点名称，例如打印输出或者其他操作
                                Log.d("SubKey", subKey);
                                // 获取subKey下的所有子节点
                                for (DataSnapshot subSubChildSnapshot : subChildSnapshot.getChildren()) {
                                    String subSubKey = subSubChildSnapshot.getKey();
                                    long childCount = subSubChildSnapshot.getChildrenCount();
                                    // 打印subSubKey
                                    Log.d("SubSubKey", subSubKey);
                                    Log.d("SubSubKey childCount", String.valueOf(childCount));

                                    // 创建一个按钮
                                    Button button = setbutton();
                                    // 设置按钮的文本为子节点的键名
                                    button.setText("Writing task" + key + " \nquestion " + subKey);
                                    // 将按钮添加到布局中
                                    reviewLayout.addView(button);

                                    // 为按钮设置点击事件
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // 创建一个新的 Intent
                                            Intent intent = new Intent(getContext(), ReviewShow_Writing.class);
                                            //将 key 和 subKey 作为额外数据传递给下一个 Activity
                                            intent.putExtra("task", key);
                                            intent.putExtra("question", subKey);
                                            intent.putExtra("times", subSubKey);
                                            // 启动新的 Activity
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }
                        }
                    } else {
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    @NonNull
    public void getListening(){
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference targetRef = databaseReference
                    .child("Listen")
                    .child(userId);

            targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // 清空布局中的按钮
                    reviewLayout.removeAllViews();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String key = childSnapshot.getKey();

                            // 获取子节点下的所有子节点名称
                            for (DataSnapshot subChildSnapshot : childSnapshot.getChildren()) {
                                String subKey = subChildSnapshot.getKey();
                                // 这里可以处理子节点名称，例如打印输出或者其他操作
                                //
                                // 创建一个按钮
                                Button button = setbutton();
                                // 设置按钮的文本为子节点的键名
                                button.setText("Listening test"+key+" \n"+subKey);
                                // 将按钮添加到布局中
                                reviewLayout.addView(button);

                                // 为按钮设置点击事件
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // 创建一个新的 Intent
                                        Intent intent = new Intent(getContext(), RReview.class);
                                         //将 key 和 subKey 作为额外数据传递给下一个 Activity
                                        intent.putExtra("test", key);
                                        intent.putExtra("Ldate", subKey);
                                        Log.d("SubKey", key+","+subKey);
                                        // 启动新的 Activity
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    } else {
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    @NonNull
    public void getSpeaking(){
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference targetRef = databaseReference
                    .child("users")
                    .child(userId)
                    .child("Speaking");

            targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // 清空布局中的按钮
                    reviewLayout.removeAllViews();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String key = childSnapshot.getKey();

                            // 获取子节点下的所有子节点名称
                            for (DataSnapshot subChildSnapshot : childSnapshot.getChildren()) {
                                String subKey = subChildSnapshot.getKey();
                                // 这里可以处理子节点名称，例如打印输出或者其他操作
                                Log.d("SubKey", subKey);
                                // 获取子节点下的所有子节点名称
                                for (DataSnapshot subSubChildSnapshot : subChildSnapshot.getChildren()) {
                                    String subSubKey = subSubChildSnapshot.getKey();
                                    long childCount = subSubChildSnapshot.getChildrenCount();
                                    // 打印subSubKey
                                    Log.d("SubSubKey", subSubKey);
                                    Log.d("SubSubKey childCount", String.valueOf(childCount));

                                    // 创建一个按钮
                                    Button button = setbutton();
                                    // 设置按钮的文本为子节点的键名
                                    button.setText("Speaking --- " + key+" \nquestion "+subKey);
                                    // 将按钮添加到布局中
                                    reviewLayout.addView(button);

                                    // 为按钮设置点击事件
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // 创建一个新的 Intent
                                            Intent intent = new Intent(getContext(), ReviewShow_Speaking.class);
                                            //将 key 和 subKey 作为额外数据传递给下一个 Activity
                                            intent.putExtra("topic", key);
                                            intent.putExtra("speakQ", subKey);
                                            intent.putExtra("times", subSubKey);
                                            // 启动新的 Activity
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }
                        }
                    } else {
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    @NonNull
    public void getAPPchat(){
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference targetRef = databaseReference
                    .child("app_conversation")
                    .child(userId)
                    .child("Conversation");

            targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // 清空布局中的按钮
                    reviewLayout.removeAllViews();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String key = childSnapshot.getKey();

                            // 获取子节点下的所有子节点名称
                            for (DataSnapshot subChildSnapshot : childSnapshot.getChildren()) {
                                String subKey = subChildSnapshot.getKey();
                                // 这里可以处理子节点名称，例如打印输出或者其他操作
                                Log.d("SubKey", subKey);

                                // 创建一个按钮
                                Button button = setbutton();
                                // 设置按钮的文本为子节点的键名
                                button.setText("Conversation \n--- " + key);
                                // 将按钮添加到布局中
                                reviewLayout.addView(button);

                                // 为按钮设置点击事件
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // 创建一个新的 Intent
                                        Intent intent = new Intent(getContext(), ReviewShow_appConversation.class);
                                        //将 key 和 subKey 作为额外数据传递给下一个 Activity
                                        intent.putExtra("topic", key);
                                        intent.putExtra("radom", subKey);
                                        // 启动新的 Activity
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    } else {
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    @NonNull
    public void getVRjudge(){
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference targetRef = databaseReference
                    .child("Conversation")
                    .child(userId)
                    ;

            targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // 清空布局中的按钮
                    reviewLayout.removeAllViews();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String key = childSnapshot.getKey();

                            // 获取子节点下的所有子节点名称
                            //for (DataSnapshot subChildSnapshot : childSnapshot.getChildren()) {
                                // 检查子节点是否包含时间节点
                                if (childSnapshot.hasChild("time")) {
                                    String time = childSnapshot.child("time").getValue(String.class);
                                    // 这里可以处理子节点名称，例如打印输出或者其他操作
                                    Log.d("SubKey", time);

                                    // 创建一个按钮
                                    Button button = setbutton();
                                    // 设置按钮的文本为子节点的键名
                                    button.setText("VR speaking test records"+ " \n" + time);
                                    // 将按钮添加到布局中
                                    reviewLayout.addView(button);

                                    // 为按钮设置点击事件
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // 创建一个新的 Intent
                                            Intent intent = new Intent(getContext(), ReviewShow_VR.class);
                                            //将 key 和 subKey 作为额外数据传递给下一个 Activity
                                            intent.putExtra("subkey", key);
                                            //intent.putExtra("time", time);
                                            // 启动新的 Activity
                                            startActivity(intent);
                                        }
                                    });
                                }
                            //}
                        }
                    } else {
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
//選擇器呈現
    @NonNull
    public void GetSelectR(){
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference targetRef = databaseReference
                    .child("R_Review")
                    .child(userId)
                    .child(selectedTestKey);

            targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // 清空布局中的按钮
                    reviewLayout.removeAllViews();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String subKey = childSnapshot.getKey();
                            Log.d("SubKey", subKey);

                            // 获取子节点下的所有子节点名称
                            for (DataSnapshot subChildSnapshot : childSnapshot.getChildren()) {
                                String subSubKey = subChildSnapshot.getKey();
                                // 这里可以处理子节点名称，例如打印输出或者其他操作
                                Log.d("SubSubKey", subSubKey);
                                    // 创建一个按钮
                                    Button button = setbutton();
                                    // 设置按钮的文本为子节点的键名
                                    button.setText("Reading \n" + selectedTestKey + "    question " + subKey + "\n" +subSubKey);
                                    // 将按钮添加到布局中
                                    reviewLayout.addView(button);
                                    // 为按钮设置点击事件
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (selectedTestKey.equals("R_blank")) {
                                                // 创建一个新的 Intent
                                                Intent intent = new Intent(getContext(), Re_blank.class);
                                                //将 key 和 subKey 作为额外数据传递给下一个 Activity
                                                intent.putExtra("docID", subKey);
                                                intent.putExtra("saveT", subSubKey);
                                                Log.d("r_blank", subKey + "," + subSubKey);
                                                // 启动新的 Activity
                                                startActivity(intent);
                                            } else if (selectedTestKey.equals("R_chose")) {
                                                // 创建一个新的 Intent
                                                Intent intent = new Intent(getContext(), Re_chose.class);
                                                //将 key 和 subKey 作为额外数据传递给下一个 Activity
                                                intent.putExtra("docID", subKey);
                                                intent.putExtra("saveT", subSubKey);
                                                Log.d("r_chose", subKey + "," + subSubKey);
                                                // 启动新的 Activity
                                                startActivity(intent);
                                            } else if (selectedTestKey.equals("R_judge")) {
                                                // 创建一个新的 Intent
                                                Intent intent = new Intent(getContext(), Re_judge.class);
                                                //将 key 和 subKey 作为额外数据传递给下一个 Activity
                                                intent.putExtra("docID", subKey);
                                                intent.putExtra("saveT", subSubKey);
                                                Log.d("r_judge", subKey + "," + subSubKey);
                                                // 启动新的 Activity
                                                startActivity(intent);
                                            } else if (selectedTestKey.equals("R_match")) {
                                                // 创建一个新的 Intent
                                                Intent intent = new Intent(getContext(), Re_match.class);
                                                //将 key 和 subKey 作为额外数据传递给下一个 Activity
                                                intent.putExtra("docID", subKey);
                                                intent.putExtra("saveT", subSubKey);
                                                Log.d("r_match", subKey + "," + subSubKey);
                                                // 启动新的 Activity
                                                startActivity(intent);
                                            } else {
                                                // 创建一个新的 Intent
                                                Intent intent = new Intent(getContext(), Re_multiple.class);
                                                //将 key 和 subKey 作为额外数据传递给下一个 Activity
                                                intent.putExtra("docID", subKey);
                                                intent.putExtra("saveT", subSubKey);
                                                Log.d("r_mutlipe", subKey + "," + subSubKey);
                                                // 启动新的 Activity
                                                startActivity(intent);
                                            }
                                        }
                                    });
                            }
                        }
                    } else {
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    @NonNull
    public void GetSelectL(){
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference targetRef = databaseReference
                    .child("Listen")
                    .child(userId)
                    .child(selectedTestKey);

            targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // 清空布局中的按钮
                    reviewLayout.removeAllViews();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String subKey = childSnapshot.getKey(); //儲存時間
                            // 获取子节点下的所有子节点名称
                            // 创建一个按钮
                            Button button = setbutton();
                            // 设置按钮的文本为子节点的键名
                            button.setText("Listening test"+selectedTestKey+" \n"+subKey);
                            // 将按钮添加到布局中
                            reviewLayout.addView(button);

                            // 为按钮设置点击事件
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // 创建一个新的 Intent
                                    Intent intent = new Intent(getContext(), RReview.class);
                                    //将 key 和 subKey 作为额外数据传递给下一个 Activity
                                    intent.putExtra("test", selectedTestKey);
                                    intent.putExtra("Ldate", subKey);
                                    Log.d("SubKey", selectedTestKey+","+subKey);
                                    // 启动新的 Activity
                                    startActivity(intent);
                                }
                            });
                        }
                    } else {
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    @NonNull
    public void GetSelectW(){
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference targetRef = databaseReference
                    .child("users")
                    .child(userId)
                    .child("Writing")
                    .child(selectedTestKey);

            targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // 清空布局中的按钮
                    reviewLayout.removeAllViews();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String subKey = childSnapshot.getKey();
                            Log.d("SubKey", subKey);

                            // 获取子节点下的所有子节点名称
                            for (DataSnapshot subChildSnapshot : childSnapshot.getChildren()) {
                                String subSubKey = subChildSnapshot.getKey();
                                Log.d("SubSubKey", subSubKey);

                                // 创建一个按钮
                                Button button = setbutton();
                                // 设置按钮的文本为子节点的键名
                                button.setText("Writing task" + selectedTestKey + " \nquestion " + subKey);
                                // 将按钮添加到布局中
                                reviewLayout.addView(button);

                                // 为按钮设置点击事件
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // 创建一个新的 Intent
                                        Intent intent = new Intent(getContext(), ReviewShow_Writing.class);
                                        //将 key 和 subKey 作为额外数据传递给下一个 Activity
                                        intent.putExtra("task", selectedTestKey);
                                        intent.putExtra("question", subKey);
                                        intent.putExtra("times", subSubKey);
                                        // 启动新的 Activity
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    } else {
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    @NonNull
    public void GetselectS(){
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference targetRef = databaseReference
                    .child("users")
                    .child(userId)
                    .child("Speaking")
                    .child(selectedTestKey);

            targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // 清空布局中的按钮
                    reviewLayout.removeAllViews();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String subKey = childSnapshot.getKey();
                            Log.d("SubKey", subKey);

                            // 获取子节点下的所有子节点名称
                            for (DataSnapshot subChildSnapshot : childSnapshot.getChildren()) {
                                String subSubKey = subChildSnapshot.getKey();
                                Log.d("SubSubKey", subSubKey);
                                // 创建一个按钮
                                Button button = setbutton();
                                // 设置按钮的文本为子节点的键名
                                button.setText("Speaking --- " + selectedTestKey+" \nquestion "+subKey);
                                // 将按钮添加到布局中
                                reviewLayout.addView(button);

                                // 为按钮设置点击事件
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // 创建一个新的 Intent
                                        Intent intent = new Intent(getContext(), ReviewShow_Speaking.class);
                                        //将 key 和 subKey 作为额外数据传递给下一个 Activity
                                        intent.putExtra("topic", selectedTestKey);
                                        intent.putExtra("speakQ", subKey);
                                        intent.putExtra("times", subSubKey);
                                        // 启动新的 Activity
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    } else {
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    @NonNull
    public void GetselectAPPchat(){
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference targetRef = databaseReference
                    .child("app_conversation")
                    .child(userId)
                    .child("Conversation")
                    .child(selectedTestKey);

            targetRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // 清空布局中的按钮
                    reviewLayout.removeAllViews();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String subKey = childSnapshot.getKey();
                            Log.d("SubKey", subKey);
                            // 创建一个按钮
                            Button button = setbutton();
                            // 设置按钮的文本为子节点的键名
                            button.setText("Conversation \n--- " + selectedTestKey);
                            // 将按钮添加到布局中
                            reviewLayout.addView(button);

                            // 为按钮设置点击事件
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // 创建一个新的 Intent
                                    Intent intent = new Intent(getContext(), ReviewShow_appConversation.class);
                                    //将 key 和 subKey 作为额外数据传递给下一个 Activity
                                    intent.putExtra("topic", selectedTestKey);
                                    intent.putExtra("radom", subKey);
                                    // 启动新的 Activity
                                    startActivity(intent);
                                }
                            });

                        }
                    } else {
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
}