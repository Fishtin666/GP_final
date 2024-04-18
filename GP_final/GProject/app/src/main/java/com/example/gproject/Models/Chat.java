package com.example.gproject.Models;

public class Chat {
    public static String SENT_BY_ME="me";
    public static String SENT_BY_BOT="bot";


    String message;
    String SendBy;

    //返回訊息內容
    public String getMessage() {return message;}

    //設置訊息內容
    public void setMessage(String message) {
        this.message = message;
    }

    //返回訊息發送者
    public String getSendBy() {
        return SendBy;
    }

    //設置訊息發送者
    public void setSendBy(String sendBy) {
        SendBy = sendBy;
    }

    //(建構子)初始化message,SendBy (訊息內容,訊息發送者)
    public Chat(String message, String sendBy) {
        this.message = message;
        SendBy = sendBy;
    }



}


