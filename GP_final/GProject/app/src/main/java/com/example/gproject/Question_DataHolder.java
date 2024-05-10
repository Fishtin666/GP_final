package com.example.gproject;

public class Question_DataHolder {
    String topic;
    String question;
    int part;
    int position;

    public Question_DataHolder(String topic, String question, int part, int position) {
        this.topic = topic;
        this.question = question;
        this.part = part;
        this.position = position;
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

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
