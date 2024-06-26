package com.example.gproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gproject.Adapters.PagesAdapter;
import com.example.gproject.fragment.WrongFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    int[] originalImages = {R.drawable.learn, R.drawable.aiteacher, R.drawable.test, R.drawable.wrong};
    public static String apiKey= BuildConfig.apikey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ViewPager2 viewPager2 =findViewById(R.id.viewPager);
        viewPager2.setAdapter(new PagesAdapter(this));
        //viewPager2.registerOnPageChangeCallback(changeCallback);
        //WrongFragment fragment = WrongFragment.newInstance("testkey", "spinner");

        tabLayout = findViewById(R.id.tabLayout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }



        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                //TextView tabView = new TextView(MainActivity.this);
                // 在这里为每个Tab设置自定义布局
                View TabView = LayoutInflater.from(MainActivity.this).inflate(R.layout.tabitem, null);
                View TabView_ai = LayoutInflater.from(MainActivity.this).inflate(R.layout.tabitem_aiteacher, null);
                View TabView_test = LayoutInflater.from(MainActivity.this).inflate(R.layout.tabitem_test, null);
                View TabView_wrong = LayoutInflater.from(MainActivity.this).inflate(R.layout.tabitem_wrong, null);


                // 在自定义布局中找到TextView并设置文本
                TextView tabText_learn = TabView.findViewById(R.id.text);
                TextView tabText_ai = TabView_ai.findViewById(R.id.text);
                TextView tabText_test = TabView_test.findViewById(R.id.text);
                TextView tabText_wrong = TabView_wrong.findViewById(R.id.text);

                // 在這裡設置原始圖片資源，這裡假設您有一個對應每個位置的圖片資源陣列
                //int[] originalImages = {R.drawable.learn, R.drawable.aiteacher, R.drawable.test, R.drawable.wrong};
                //int originalImageResource = originalImages[position];

                // 在这里为每个Tab设置标记
                //tab.setTag(position);

                switch (position) {
                    case 0:
                        tab.setCustomView(TabView);
                        tabText_learn.setText("學習");
                        tab.setTag(R.drawable.learn2); // 设置learn对应的图片资源


                        break;
                    case 1:
                        tab.setCustomView(TabView_ai);
                        tabText_ai.setText("AI老師");
                        tab.setTag(R.drawable.aiteacher2); // 设置aiteacher对应的图片资源
                        break;
                    case 2:
                        tab.setCustomView(TabView_test);
                        tabText_test.setText("單字");
                        tab.setTag(R.drawable.test2); // 设置test对应的图片资源
                        break;
                    case 3:
                        tab.setCustomView(TabView_wrong);
                        tabText_wrong.setText("複習");
                        tab.setTag(R.drawable.wrong2); // 设置wrong对应的图片资源
                        break;
                }
            }

        }
        );
        tabLayoutMediator.attach();

        //R
        // 接收来自 Review_choose 的信息
        Intent intent = getIntent();
        if (intent != null) {
            int selectedTab = intent.getIntExtra("selectedTab", -1);
            String testKey = intent.getStringExtra("testKey");
            String spinner = intent.getStringExtra("spinner");
            String testSubKey = intent.getStringExtra("testSubKey");
            Log.d("MainActivity", "selectedTab: " + selectedTab);
            Log.d("MainActivity", "testKey: " + testKey);
            Log.d("MainActivity", "spinner: " + spinner);
            Log.d("MainActivity", "testSupKey: " + testSubKey);

            if (selectedTab != -1) {
                tabLayout.selectTab(tabLayout.getTabAt(selectedTab));
                updateTabItemAppearance(tabLayout.getTabAt(selectedTab),true);
                updateTabItemAppearance(tabLayout.getTabAt(0),false);
                viewPager2.setCurrentItem(selectedTab);

                if (selectedTab == 3 && testKey != null && spinner != null) {
                    // 传递参数到 PagesAdapter
                    PagesAdapter adapter = new PagesAdapter(this);
                    adapter.setWrongFragmentArgs(testKey, spinner, testSubKey);
                    viewPager2.setAdapter(adapter);
                }
                else if (selectedTab == 3 && testKey.equals("All") && spinner != null) {
                    // 传递参数到 PagesAdapter
                    PagesAdapter adapter = new PagesAdapter(this);
                    adapter.setWrongFragmentArgs(testKey, testSubKey ,spinner);
                    viewPager2.setAdapter(adapter);
                }
            }
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 获取选中的Tab的标记
                int selectedTabTag = (int) tab.getTag();


                int position=tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getPosition();
                updateTabItemAppearance(tab, true);
                /*
                runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, selectedTabTag, Toast.LENGTH_SHORT).show();

                }
                });*/

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // 获取未选中的Tab的标记
                //int unselectedTabTag = (int) tab.getTag();

                int unselectedTabTag =tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getPosition();
                updateTabItemAppearance(tab, false);

                /*
                runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, String.valueOf(unselectedTabTag), Toast.LENGTH_SHORT).show();

                }
                });*/



            }


            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void profileClick(View view){
        startActivity(new Intent(MainActivity.this,profile.class));
    }

    private void updateTabItemAppearance(TabLayout.Tab tab, boolean isSelected) {

        // 根据特定tabItem的图片资源进行更新
        //View customTabView = tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getCustomView();
        View customTabView = tab.getCustomView();
        TextView tabTextView = customTabView.findViewById(R.id.text);
        ImageView tabImageView = customTabView.findViewById(R.id.img);

        if (tabTextView != null) {
            if (isSelected) {
                // 设置选中时的颜色或样式
                tabTextView.setTextColor(ContextCompat.getColor(this, R.color.red));
                tabTextView.setTypeface(Typeface.DEFAULT_BOLD);
                tabImageView.setImageResource((int)tab.getTag());
                //tabImageView.setColorFilter(ContextCompat.getColor(this, R.color.red), PorterDuff.Mode.SRC_IN);
            } else {
                // 设置未选中时的颜色或样式
                tabTextView.setTextColor(ContextCompat.getColor(this, R.color.black));
                tabTextView.setTypeface(Typeface.DEFAULT);
                tabImageView.setImageResource(originalImages[tab.getPosition()]);

                //tabImageView.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);


            }

        }
    }
}











