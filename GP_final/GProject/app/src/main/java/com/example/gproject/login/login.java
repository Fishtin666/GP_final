package com.example.gproject.login;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gproject.ChoseTestActivity;
import com.example.gproject.MainActivity;
import com.example.gproject.R;
import com.example.gproject.WordQuiz.LevelAQuizActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private FirebaseAuth mAuth;
    private EditText usernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // 初始化 Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // 獲取布局元素
        emailEditText = findViewById(R.id.inputEmail);
        passwordEditText = findViewById(R.id.inputPass);
        loginButton = findViewById(R.id.loginButton);
        usernameEditText = findViewById(R.id.inputEmail);
        passwordEditText = findViewById(R.id.inputPass);
        loginButton = findViewById(R.id.loginButton);

        // 检查是否有保存的账号信息，如果有则自动填充到输入框中
        if (RememberMeManager.hasSavedCredentials(this)) {
            String[] credentials = RememberMeManager.getSavedCredentials(this);
            usernameEditText.setText(credentials[0]);
            passwordEditText.setText(credentials[1]);
        }

        // 設置按鈕點擊監聽器
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 在此處調用登錄方法
                loginUser();

            }
        });

    }

    public void goRegist(View view) {
        Intent intent = new Intent(login.this, regist.class);
        startActivity(intent);
        finish();
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // 使用 Firebase Authentication 進行登錄
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 登录成功
                            Toast.makeText(login.this, "Login successful", Toast.LENGTH_SHORT).show();
                            // 获取当前登录用户的用户 ID
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
                            DatabaseReference levelRef = FirebaseDatabase.getInstance().getReference().child("word_Level").child(userId);
                            levelRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {

                                        Intent intent = new Intent(login.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
//
                                    } else {

                                        Log.e("login", "User node or word_Level does not exist");
                                        CheckWordLevel();

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // 读取数据时出现错误
                                    Log.e(TAG, "Error reading user data: " + error.getMessage());
                                }
                            });
                        } else {
                            // 登录失败
                            Log.e("LoginActivity", "Login failed: " + task.getException().getMessage());
                            Toast.makeText(login.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void CheckWordLevel() {
        // 启动 LevelAQuizActivity
        Intent intent = new Intent(this, LevelAQuizActivity.class);
        startActivity(intent);
        finish();
        //获取SharedPreferences实例
        SharedPreferences sharedPreferences = getSharedPreferences("WordLevel", MODE_PRIVATE);

        // 从SharedPreferences中获取当前单词级别，默认值为"未知"
        String wordLevel = sharedPreferences.getString("word_level", "unKnow");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        // 将单词级别设置为"Login"
        editor.putString("word_level", "Login");
        // 提交编辑
        editor.apply();
    }
}
