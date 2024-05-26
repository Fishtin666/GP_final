package com.example.gproject.Review;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.gproject.MainActivity;
import com.example.gproject.R;
import com.example.gproject.fragment.WrongFragment;

public class Review_choose extends AppCompatActivity {

    private FragmentManager manager;
    private FragmentTransaction transaction;
    String selectedTestKey;
    String spinner;

    //speakin變數
    String selectpart;
    String selecttopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_choose);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        Spinner spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this,R.array.Review_array, android.R.layout.simple_dropdown_item_1line);
        spinner2.setAdapter(adapter1);

        manager = getSupportFragmentManager();

        ImageButton pencil = findViewById(R.id.pencil2);
        ImageButton backButton = findViewById(R.id.back);
        Button check = findViewById(R.id.button5);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Review_choose.this, WrongFragment.class);
//                intent.putExtra("testKey", selectedTestKey);
//                Log.d("testKey", selectedTestKey);
//                startActivity(intent);
                finish();
            }
        });




        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        loadFragment(new Fragment_Review_reading(), "FRAGMENT_READING");
                        break;
                    case 1:
                        loadFragment(new Fragment_Review_listening(), "FRAGMENT_LISTENING");
                        // 假设你通过FragmentTransaction添加了你的Fragment
//                        Fragment_Review_listening myFragment = (Fragment_Review_listening) getSupportFragmentManager().findFragmentById(R.id.center);
//                        manager.executePendingTransactions();  // Ensure the transaction is complete before proceeding
//                        String selectedTestKey=myFragment.getLreviewTestKey();
//                        Log.d("testKey", selectedTestKey);
                        break;
                    case 2:
                        loadFragment(new Fragment_Review_writing(), "FRAGMENT_WRITING");
                        break;
                    case 3:
                        loadFragment(new Fragment_Review_speaking(), "FRAGMENT_SPEAKING");
                        break;
                    case 4:
                        loadFragment(new Fragment_Review_conversation(), "FRAGMENT_CONVERSATION");
                        break;
                    case  5:
                        loadFragment(new Fragment_Review_VR(), "FRAGMENT_VR");
                        break;
                    default:
//                        // 默认加载第一个 Fragment
//                        loadFragment(new Fragment_Review_reading());
                        break;
                }

                check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Retrieve the key only if the selected fragment is Fragment_Review_listening
                        if (position == 0) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Fragment_Review_reading myFragment = (Fragment_Review_reading) getSupportFragmentManager().findFragmentByTag("FRAGMENT_READING");
                                    if (myFragment != null) {
                                        selectedTestKey = myFragment.getLreviewTestKey();
                                        if (selectedTestKey != null) {
                                            Log.d("1testKey", selectedTestKey);

                                            // 将选择的信息传递到 MainActivity
                                            Intent intent = new Intent(Review_choose.this, MainActivity.class);
                                            intent.putExtra("selectedTab", 3); // 假设 3 是你想要打开的 Tab 的位置
                                            intent.putExtra("testKey", selectedTestKey);
                                            intent.putExtra("spinner", "reading");
                                            startActivity(intent);

                                            finish();
                                        } else {
                                            Log.d("testKey", "fragment null");
                                            Toast.makeText(Review_choose.this,"請選擇選項", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }, 100); // 延迟100毫秒
                        }
                        else if (position == 1) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Fragment_Review_listening myFragment = (Fragment_Review_listening) getSupportFragmentManager().findFragmentByTag("FRAGMENT_LISTENING");
                                    if (myFragment != null) {
                                        selectedTestKey = myFragment.getLreviewTestKey();
                                        Log.d("testKey", selectedTestKey);
                                        if (selectedTestKey != null) {
                                            Log.d("testKey", selectedTestKey);
                                            // 将选择的信息传递到 MainActivity
                                            Intent intent = new Intent(Review_choose.this, MainActivity.class);
                                            intent.putExtra("selectedTab", 3); // 假设 3 是你想要打开的 Tab 的位置
                                            intent.putExtra("testKey", selectedTestKey);
                                            intent.putExtra("spinner", "listening");
                                            startActivity(intent);

                                            finish();
                                        } else {
                                            Log.d("testKey", "fragment null");
                                            Toast.makeText(Review_choose.this, "請選擇選項", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }, 100); // 延迟100毫秒
                        }
                        else if (position == 2) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Fragment_Review_writing myFragment = (Fragment_Review_writing) getSupportFragmentManager().findFragmentByTag("FRAGMENT_WRITING");
                                    if (myFragment != null) {
                                        selectedTestKey = myFragment.getLreviewTestKey();
                                        Log.d("testKey", selectedTestKey);
                                        if (selectedTestKey != null) {
                                            Log.d("testKey", selectedTestKey);
                                            // 将选择的信息传递到 MainActivity
                                            Intent intent = new Intent(Review_choose.this, MainActivity.class);
                                            intent.putExtra("selectedTab", 3); // 假设 3 是你想要打开的 Tab 的位置
                                            intent.putExtra("testKey", selectedTestKey);
                                            intent.putExtra("spinner", "writing");
                                            startActivity(intent);

                                            finish();
                                        } else {
                                            Log.d("testKey", "fragment null");
                                            Toast.makeText(Review_choose.this, "請選擇選項", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }, 100); // 延迟100毫秒
                        }
                        else if (position == 3) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Fragment_Review_speaking myFragment = (Fragment_Review_speaking) getSupportFragmentManager().findFragmentByTag("FRAGMENT_SPEAKING");
                                    if (myFragment != null) {
                                        selectpart = myFragment.getSreviewPartkey();
                                        selecttopic = myFragment.getSreviewTopickey();
                                        Log.d("partKey", selectpart);
                                        Log.d("topicKey", ":"+selecttopic);
//                                        if (selectpart != null && selecttopic == null) {
//                                            Log.d("判斷partKey", selectpart);
//                                            // 将选择的信息传递到 MainActivity
//                                            Intent intent = new Intent(Review_choose.this, MainActivity.class);
//                                            intent.putExtra("selectedTab", 3); // 假设 3 是你想要打开的 Tab 的位置
//                                            intent.putExtra("testKey", selectpart);
//                                            intent.putExtra("spinner", "speaking");
//                                            startActivity(intent);
//
//                                            finish();
//                                        } else
                                        if (selectpart != null && selecttopic != null) {
                                            Log.d("判斷partKey", selectpart);
                                            Log.d("判斷topicKey", ":"+selecttopic);
                                            // 将选择的信息传递到 MainActivity
                                            Intent intent = new Intent(Review_choose.this, MainActivity.class);
                                            intent.putExtra("selectedTab", 3); // 假设 3 是你想要打开的 Tab 的位置
                                            intent.putExtra("testKey", selecttopic);
                                            //intent.putExtra("testSubKey",selecttopic);
                                            intent.putExtra("spinner", "speaking");
                                            startActivity(intent);

                                            finish();
                                        } else {
                                            Log.d("testKey", "fragment null");
                                            Toast.makeText(Review_choose.this, "請選擇選項", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }, 100); // 延迟100毫秒
                        }
                        else if (position == 4) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Fragment_Review_conversation myFragment = (Fragment_Review_conversation) getSupportFragmentManager().findFragmentByTag("FRAGMENT_CONVERSATION");
                                    if (myFragment != null) {
                                        selectedTestKey = myFragment.getLreviewTestKey();
                                        Log.d("testKey", selectedTestKey);
                                        if (selectedTestKey != null) {
                                            Log.d("testKey", selectedTestKey);
                                            // 将选择的信息传递到 MainActivity
                                            Intent intent = new Intent(Review_choose.this, MainActivity.class);
                                            intent.putExtra("selectedTab", 3); // 假设 3 是你想要打开的 Tab 的位置
                                            intent.putExtra("testKey", selectedTestKey);
                                            intent.putExtra("spinner", "conversation");
                                            startActivity(intent);

                                            finish();
                                        } else {
                                            Log.d("testKey", "fragment null");
                                            Toast.makeText(Review_choose.this, "請選擇選項", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }, 100); // 延迟100毫秒
                        }
                        else if (position == 5) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Fragment_Review_VR myFragment = (Fragment_Review_VR) getSupportFragmentManager().findFragmentByTag("FRAGMENT_VR");
                                    if (myFragment != null) {
                                        selectedTestKey = myFragment.getLreviewTestKey();
                                        Log.d("testKey", ":"+selectedTestKey);
                                        if (selectedTestKey != null) {
                                            Log.d("testKey", selectedTestKey);
                                             //将选择的信息传递到 MainActivity
                                            Intent intent = new Intent(Review_choose.this, MainActivity.class);
                                            intent.putExtra("selectedTab", 3); // 假设 3 是你想要打开的 Tab 的位置
                                            intent.putExtra("testKey", selectedTestKey);
                                            intent.putExtra("spinner", "VR");
                                            startActivity(intent);

                                            finish();
                                        }
                                         else {
                                            Log.d("testKey", "fragment null");
                                            Toast.makeText(Review_choose.this, "請選擇選項", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }, 100); // 延迟100毫秒
                        }
                    }
                });


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    private void loadFragment(Fragment fragment,String tag) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.center, fragment, tag);
        transaction.commit();
       // manager.executePendingTransactions();  // Ensure the transaction is complete before proceeding
    }
    public String setSelection(){
        return selectedTestKey;
    }
    public String setSelectionspin(){
        return spinner;
    }
}