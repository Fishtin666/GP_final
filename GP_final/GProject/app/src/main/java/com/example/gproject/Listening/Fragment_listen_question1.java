package com.example.gproject.Listening;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gproject.JustifyTextView2;
import com.example.gproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private int docCount=1;
    // Key for the button identifier

    // TODO: Rename and change types of parameters
    private String test;
    // Button identifier
    private String mParam1;
    private String mParam2;

    private HashMap<Integer, String> editTextMap = new HashMap<>();
    private View rootView;
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

//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            parentActivity = (listen_ftest1) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString() + " must implement listen_ftest1");
//        }
//    }

    // 添加一个方法用于接收 filecount 的值
    public void setDocCount(int count) {
        // 在此处处理传递过来的 filecount 值
        // 您可以在这里对 filecount 进行任何操作
        //this.docCount = count;
        //Log.d("TAG", "Received file count in Fragment: " + count);
    }
    public void setQuestionCount(int Qcount){
        this.docCount = Qcount;
        //Log.d("TAG", "Received Question count in Fragment: " + docCount);
    }


    // 添加一个方法用于将 Fragment 中编辑的文本内容存储到 HashMap 中
    public void setEditTextValue(int questionNumber, String answer) {
        editTextMap.put(questionNumber, answer);
        // 添加日志以检查存储的键值对
        Log.d("HashMap", "键: " + questionNumber + ", 值: " + answer);
        for (Map.Entry<Integer, String> entry : editTextMap.entrySet()) {
            Log.d("HashMap", "问题 " + entry.getKey() + ": " + entry.getValue());
        }
        // 获取对 listen_ftest1 类的引用并传递 HashMap
        if (getActivity() instanceof listen_ftest1) {
            ((listen_ftest1) getActivity()).setEditTextValueFromFG(editTextMap);
        }
    }
    // 在需要获取 HashMap 的地方，可以添加一个方法来获取该 HashMap
    public HashMap<Integer, String> getEditTextValues() {
        return editTextMap;
    }


    //布局
    private JustifyTextView2 question;
    private ImageView pic;
    private TextView title;
    private listen_ftest1 parentActivity;
//    private HashMap<Integer, String> editTextMap = new HashMap<>();
//    private EditText ans;
//    private String savedText=" ";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_listen_question1, container, false);
        // 在Fragment的根視圖上查找視圖元素
        question = rootView.findViewById(R.id.Lquestion);
        pic = rootView.findViewById(R.id.pic);
        title = rootView.findViewById(R.id.title2);
//        ans = rootView.findViewById(R.id.ans1);
//        ans.setText(savedText);
//
//        // 获取并恢复 EditText 中的文本内容
//        restoreEditTextValues(rootView);
        // 监听用户输入完成的事件，比如在 EditText 中输入完成后


        // 获取传递给 Fragment 的 Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            // 获取 Bundle 中所有键的集合
            Set<String> keys = bundle.keySet();

            // 遍历集合并打印出每个键
            for (String key : keys) {
                //Log.d("TAG", "Key in bundle: " + key);
            }

        }
        if (bundle != null && bundle.containsKey("L_test") && bundle.containsKey("L_section")&& bundle.containsKey("Qcount")) {
            // 从 Bundle 中获取按钮标识的值
            int test = bundle.getInt("L_test");
            int section = bundle.getInt("L_section");
            int Qcount = bundle.getInt("Qcount");

            title.setText("Section"+section);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            //int section = 1;
            DocumentReference Q = db.collection("Listen_" + test).document("S" + section + "_" + Qcount);
            //Log.d("TAG", "fragment L_test value: " + test);
            //Log.d("TAG", "fragment L_section value: " + section);
            //Log.d("TAG", "fragment QCount value: " + Qcount);
            Log.d("Ref", "Listen_" + test+"/S"+section+"_"+Qcount);
            Q.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    //查找此題組有幾題
                    if (document.exists()) {
                        // 获取文档中字段的数量
                        //int numberOfAnswer = 0;
                        // 假设 document 是您从 Firestore 中获取的文档
                        for (Map.Entry<String, Object> entry : document.getData().entrySet()) {
                            String fieldName = entry.getKey();
                            // 检查字段名称是否以 "" 开头
                            if (fieldName.startsWith("A")) {
                                //numberOfAnswer++;
                                // 使用正则表达式从字段名称中提取数字部分
                                Pattern pattern = Pattern.compile("\\d+");
                                Matcher matcher = pattern.matcher(fieldName);
                                if (matcher.find()) {
                                    String number = matcher.group(); // 提取到的数字部分
                                    int Anskey = Integer.parseInt(number);
                                    // 在TextView中显示数字部分
                                    // 根据提取到的数字动态添加 TextView 和 EditText 到 LinearLayout
                                    TextView textView = new TextView(getContext());
                                    textView.setText(number + " ."); // 设置 TextView 文本内容为提取到的数字

                                    // 创建一个新的水平 LinearLayout
                                    LinearLayout horizonLayout = new LinearLayout(getContext());
                                    horizonLayout.setOrientation(LinearLayout.HORIZONTAL);

                                    // 检查是否有对应的键值在 Activity 的 HashMap 中
                                    // 获取对 Activity 的引用并将其转换为 listen_ftest1 类型
                                    listen_ftest1 activity = (listen_ftest1) getActivity();
                                    if (activity != null) {
                                        HashMap<Integer, String> editTextValues = activity.getEditTextValues();
                                        for (Map.Entry<Integer, String> mapEntry : editTextValues.entrySet()) {
                                            Integer key = mapEntry.getKey();
                                            String value = mapEntry.getValue();
                                            editTextMap.put(key,value);
                                            Log.d("TAG", "editTextValues Key: " + key + ", Value: " + value);
                                            Log.d("tag","number: "+number);
                                        }

                                        //Log.d("TAG", "editText  Value: " + editTextValues.get(Anskey));

                                        if (editTextValues.containsKey(Anskey)) {
                                            // 如果有对应的键值，则将其显示在 EditText 中
                                            String value = editTextValues.get(Anskey);
                                            Log.d("TAG", "editText Key: " + Anskey + ", Value: " + value); // 打印键和对应的值

                                            EditText editText = getEditText(value, number);


                                            // 将 TextView 和 EditText 添加到水平 LinearLayout
                                            horizonLayout.addView(textView);
                                            horizonLayout.addView(editText);
                                        }else {
                                            EditText editText = getEditText(number);


                                            // 将 TextView 和 EditText 添加到水平 LinearLayout
                                            horizonLayout.addView(textView);
                                            horizonLayout.addView(editText);
                                        }

                                        // 将水平 LinearLayout 添加到父 LinearLayout
                                        LinearLayout answer = rootView.findViewById(R.id.listenContent);
                                        answer.addView(horizonLayout);
                                    }

                                }
                            }
                            //Log.d("TaG","numberOfAnswer: "+numberOfAnswer);
                        }
                    }

                    //檢查這題是否需要圖片
                    boolean haspicture = false;
                    //int[] p = {1}; //圖片名稱: 1_s1_p2.png
                    for (Map.Entry<String, Object> entry : document.getData().entrySet()) {
                        String fieldName = entry.getKey();
                        // 檢查是否有picture欄位
                        if (fieldName.equals("picture")) {
                            haspicture = true;
                            //final int pValue = p[0]; // 保存循环开始时的 p[0] 的值
                            StorageReference storageRef = FirebaseStorage.getInstance().getReference("Listen/" + test + "_s" + section +"_p" +Qcount+".png");
                            //Log.d("TAG", "pic ref: " + test + "_p" + pValue);
                            try {
                                File localfile = File.createTempFile("tempfile", ".png");
                                storageRef.getFile(localfile)
                                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                                pic.setImageBitmap(bitmap);
                                                // 成功下载图片后，递增 p 变量
                                                //p[0]++;
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    if (!haspicture) {
                        pic.setVisibility(View.GONE);
                    }


                    //顯示題目到textview
                    if (document.exists()) {
                        // 根据字段的数量来处理文档中的数据
                        String fieldName = "Q";
                        if (document.contains(fieldName)) {
                            // 根据字段名称获取字段的值并设置到相应的视图上
                            String fieldValue = document.getString(fieldName);
                            String questionText = fieldValue.replace("\\n", "\n");
                            question.setText(questionText);
                        }
                    } else {
                        // 文件不存在的處理
                        Log.d("TAG", "Document does not exist");
                    }
                } else {
                    // 讀取失敗的處理
                    Log.e("TAG", "Error getting document: " + task.getException());
                }

            });


        }
        return rootView;
    }

    @NonNull
    private EditText getEditText(String number) {
        EditText editText = new EditText(getContext());

        // 创建并设置 EditText 的属性
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        // 添加 TextWatcher 监听器
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                // 获取用户输入的文本
                String userAnswer = s.toString();
                // 调用方法保存编辑文本内容
                setEditTextValue(Integer.parseInt(number), userAnswer);
                //Log.d("TAG","edittext change");
            }
        });
        return editText;
    }

    @NonNull
    private EditText getEditText(String value, String number) {
        EditText editText = new EditText(getContext());
        if (value != null) {
            // 确保值不为空再设置到 EditText 中
            editText.setText(value);
        } else {
            // 如果值为空，则清空 EditText
            editText.setText("");
        }

        // 创建并设置 EditText 的属性
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        // 添加 TextWatcher 监听器
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                // 获取用户输入的文本
                String userAnswer = s.toString();
                // 调用方法保存编辑文本内容
                setEditTextValue(Integer.parseInt(number), userAnswer);
                //Log.d("TAG","edittext change");
            }
        });
        return editText;
    }
}