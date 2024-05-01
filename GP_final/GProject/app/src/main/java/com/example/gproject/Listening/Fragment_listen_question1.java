package com.example.gproject.Listening;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gproject.JustifyTextView2;
import com.example.gproject.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_listen_question1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_listen_question1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_BUTTON_IDENTIFIER = "L_test";
    // Key for the button identifier

    // TODO: Rename and change types of parameters
    private String test;
    // Button identifier
    private String mParam1;
    private String mParam2;


    public Fragment_listen_question1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_listen_question1.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_listen_question1 newInstance(String param1, String param2, String L_test) {
        Fragment_listen_question1 fragment = new Fragment_listen_question1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_BUTTON_IDENTIFIER, L_test);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            test = getArguments().getString(ARG_BUTTON_IDENTIFIER);

        }
    }

    //布局
    private JustifyTextView2 question;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_listen_question1, container, false);
        // 在Fragment的根視圖上查找視圖元素
        question = rootView.findViewById(R.id.Lquestion);

        // 获取传递给 Fragment 的 Bundle
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("L_test")) {
            // 从 Bundle 中获取按钮标识的值
            int test = bundle.getInt("L_test");

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            int d = 1;
            DocumentReference Q = db.collection("Listen_"+test).document("S"+d);
            Log.d("TAG", "L_test value: " + test);
            Log.d("TAG", "d value: " + d);
            Q.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        // 获取文档中字段的数量
                        int numberOfFields = 0;
                        // 假设 document 是您从 Firestore 中获取的文档
                        for (Map.Entry<String, Object> entry : document.getData().entrySet()) {
                            String fieldName = entry.getKey();
                            // 检查字段名称是否以 "Q" 开头
                            if (fieldName.startsWith("Q")) {
                                numberOfFields++;
                            }
                        }
                        // 根据字段的数量来处理文档中的数据
                        for ( int i=1; i <= numberOfFields ; i++) {
                            String fieldName = "Q" + i;
                            if (document.contains(fieldName)) {
                                // 根据字段名称获取字段的值并设置到相应的视图上
                                String fieldValue = document.getString(fieldName);
                                String questionText = fieldValue.replace("\\n", "\n");
                                question.setText(questionText);
                            }
                        }
                    }else {
                        // 文件不存在的處理
                        Log.d("TAG", "Document does not exist");
                    }
                }else {
                    // 讀取失敗的處理
                    Log.e( "TAG","Error getting document: " + task.getException());
                }
            });
        }
        return rootView;
    }
}