package com.example.gproject.Listening;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.gproject.R;

public class listen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listen);


    }

    // 将 testClick 方法用于启动新的 Activity，并传递按钮标识作为参数
    public void testClick(int buttonIdentifier, Class<?> activityClass) {
        Bundle bundle = new Bundle();
        bundle.putInt("L_test",buttonIdentifier);
        bundle.putInt("L_section",1);
        bundle.putInt("Qcount",1);

        Log.d("TAG", "choose L_test value: " + buttonIdentifier);
        // 创建一个 Intent 对象，用于启动新的 Activity
        Intent intent = new Intent(this, activityClass);
        intent.putExtras(bundle);
        // 启动新的 Activity
        startActivity(intent);
    }
    // 当点击 test1 按钮时调用该方法，启动指定的 Activity
    public void test1Click(View view) {
        // 调用 testClick 方法，传递按钮标识为 1 和目标 Activity 的类
        testClick(1, listen_ftest1.class);
    }
    public void test2Click(View view) {
        testClick(2, listen_ftest1.class);
    }
    public void test3Click(View view) {
        testClick(3, listen_ftest1.class);
    }
    public void test4Click(View view) {
        testClick(4, listen_ftest1.class);
    }

}