package com.example.gproject.Models;

public class QuestionModel {
    String question;
    int part;

    String topic;
    boolean isSelect;


    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    public QuestionModel(String question,int part,String topic,Boolean isSelect) {
        this.part =part;
        this.question = question;
        this.topic = topic;
        this.isSelect = isSelect;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
    public Boolean getStart(){return isSelect;}
}
