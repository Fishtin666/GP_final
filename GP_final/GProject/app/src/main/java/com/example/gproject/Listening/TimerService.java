package com.example.gproject.Listening;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import java.util.Locale;

public class TimerService extends Service {
    private CountDownTimer countDownTimer;
    private  long totalTime = 30 * 60 * 1000; // 30 minutes in milliseconds
    private boolean timerRunning = false;
    //timer function
    // 這個方法用來啟動或停止計時器
    public void onCreate() {
        super.onCreate();
        // 如果計時器正在運行，則停止計時器，否則啟動計時器
        if (timerRunning) {
            StopTimer();
        } else {
            StartTimer();
        }
    }

    // 這個方法用來停止計時器
    public void StopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }// 取消計時器
        timerRunning = false;    // 設定計時器狀態為停止
    }

    // 這個方法用來啟動計時器
    public void StartTimer() {
        // 使用 CountDownTimer 類別來實現計時器，totalTime 是總倒數時間，1000 是倒數時間的間隔（1秒）
        countDownTimer = new CountDownTimer(totalTime, 1000) {
            @Override
            public void onTick(long l) {
                totalTime = l;      // 更新剩餘的總倒數時間
                updateTimer();      // 更新計時器顯示
            }

            @Override
            public void onFinish() {
                // 在倒數計時完成時執行的操作（這裡沒有指定任何操作）
                Log.d("TimerService", "Timer finished");
            }
        }.start();                // 啟動計時器
        timerRunning = true;      // 設定計時器狀態為運行中
    }

    // 這個方法用來更新計時器的顯示，你可以在這裡處理更新 UI 的邏輯
    private void updateTimer() {
        // 在這裡更新計時器的顯示，例如將剩餘時間顯示在 TextView 中
        // 在這裡更新計時器文本，例如將時間轉換為分鐘和秒的格式
        long minutes = totalTime / 60000;
        long seconds = (totalTime % 60000) / 1000;
        String timerText = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        Intent intent= new Intent();
        intent.putExtra("time", (int) totalTime/1000);
        intent.setAction("com.demo.timer");
        sendBroadcast(intent);
        // 你需要將這個 timerText 傳遞給你的 Activity 或其他 UI 元素顯示
        // 例如可以使用廣播、EventBus、LiveData 等機制將資訊發送到 Activity
        Log.d("TimerService", "Timer text: " + timerText+"\n"+"totaltime: "+ totalTime);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onDestroy() {
        super.onDestroy();
        // 在 Service 銷毀時停止計時器
        StopTimer();
    }
}