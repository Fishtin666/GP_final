package com.example.gproject.Models;

public class QuestionNumberModel {
    String num;
    int task;

    public int getTask() {
        return task;
    }

    public void setTask(int task) {
        this.task = task;
    }

    public QuestionNumberModel(String num, int task) {
        this.num = num;
        this.task = task;
    }



    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
