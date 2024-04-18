package com.example.gproject.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
public class regist extends AppCompatActivity {

    private EditText emailEditText, passEditText;
    private Button registButton;

    // Firebase Authentication
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist);

        // 初始化 Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // 获取布局元素
        emailEditText = findViewById(R.id.registEmail);
        passEditText = findViewById(R.id.registPass);
        registButton = findViewById(R.id.registButton);

        // 注册按钮点击事件
        registButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取用户输入的邮箱和密码
                String email = emailEditText.getText().toString();
                String password = passEditText.getText().toString();

                // 检查邮箱格式
                if (!isEmailValid(email)) {
                    Toast.makeText(regist.this, "Email 格式错误", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 检查密码长度
                if (!isPasswordValid(password)) {
                    Toast.makeText(regist.this, "密码长度需至少六位", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 使用 Firebase Authentication 进行注册
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(regist.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // 注册成功
                                    Toast.makeText(regist.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    // 跳转到登录页面
                                    Intent intent = new Intent(regist.this, login.class);
                                    startActivity(intent);
                                    //
                                } else {
                                    // 注册失败
                                    Toast.makeText(regist.this, "注册失败：" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
    public void goLogin(View view){
        Intent intent =new Intent(regist.this, login.class);
        startActivity(intent);
    }
    // 检查邮箱格式是否有效
    private boolean isEmailValid(String email) {
        // 在实际应用中，你可能需要更复杂的邮箱格式验证逻辑
        return email.contains("@");
    }

    // 检查密码长度是否有效
    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }
}
