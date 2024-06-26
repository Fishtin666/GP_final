package com.example.gproject.Listening;

import static android.os.Build.*;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.gproject.MainActivity;
import com.example.gproject.R;

public class listen extends AppCompatActivity {

    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listen);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }
        back = findViewById(R.id.back);

        // 设置点击监听器
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动新的Activity
                Intent intent = new Intent(listen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    // 将 testClick 方法用于启动新的 Activity，并传递按钮标识作为参数
    public void testClick(int buttonIdentifier, Class<?> activityClass) {
        // 显示提示对话框
        new AlertDialog.Builder(this)
                .setTitle("雅思聽力測驗規則")
                .setMessage("聽力測驗中總共分成4個Section，每個Section有10題題目，共40題。\n測驗時間為30分鐘\n按下撥放鈕即開始測驗")
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("L_test",buttonIdentifier);
                        bundle.putInt("L_section",1);
                        bundle.putInt("Qcount",1);

                        Log.d("TAG", "choose L_test value: " + buttonIdentifier);
                        // 创建一个 Intent 对象，用于启动新的 Activity
                        Intent intent = new Intent(listen.this, activityClass);
                        intent.putExtras(bundle);
                        // 启动新的 Activity
                        startActivity(intent);
                    }
                })
                .setCancelable(false) // 用户必须点击按钮才能关闭对话框
                .show();
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

    public void helpClick(View view){
        // 显示提示对话框
        new AlertDialog.Builder(listen.this)
                .setTitle("雅思聽力測驗規則提醒")
                .setMessage("聽力測驗中總共分成4個Section，每個Section有10題題目，共40題。\n測驗時間為30分鐘\n按下撥放鈕即開始測驗")
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 用户点击“確定”按钮后的操作，关闭对话框
                        dialog.dismiss(); // 可省略，因为点击按钮默认会关闭对话框
                    }
                })
                .setCancelable(false) // 用户必须点击按钮才能关闭对话框
                .show();
    }

}