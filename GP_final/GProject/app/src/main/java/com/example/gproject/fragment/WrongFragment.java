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

import com.example.gproject.Listening.listen_Ans1;
import com.example.gproject.R;
import com.example.gproject.Review.RReview;
import com.example.gproject.Review.Review_choose;
import com.example.gproject.ReviewShow_Speaking;
import com.example.gproject.ReviewShow_Writing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WrongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WrongFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WrongFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WrongFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WrongFragment newInstance(String param1, String param2) {
        WrongFragment fragment = new WrongFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private LinearLayout reviewLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_wrong, container, false);

        ImageButton pencil = rootview.findViewById(R.id.pencil1);
        //Button button = rootview.findViewById(R.id.button);
        reviewLayout = rootview.findViewById(R.id.reviewLayout);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Spinner spinner1 = rootview.findViewById(R.id.spinner1);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(getContext(),R.array.Review_array, android.R.layout.simple_dropdown_item_1line);
        spinner1.setAdapter(adapter1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
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
                    case 4: //VR
                        break;
                    default: //R
                        break;
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Review_choose.class);
                startActivity(intent);
            }
        });
//        button.setOnClickListener(new View.OnClickListener(){
//            public  void onClick(View v){
//                Intent intent = new Intent(getContext(), RReview.class);
//                startActivity(intent);
//            }
//        });

    return rootview;
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
                                // 创建一个按钮
                                Button button = setbutton();
                                // 设置按钮的文本为子节点的键名
                                button.setText("Reading " + key + " question" + subKey);
                                // 将按钮添加到布局中
                                reviewLayout.addView(button);
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
                                    button.setText("Writing task" + key + " question" + subKey+" times:"+subSubKey);
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
                                    button.setText("Speaking --" + key+" question"+subKey+" SubSubKey:"+subSubKey);
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
}