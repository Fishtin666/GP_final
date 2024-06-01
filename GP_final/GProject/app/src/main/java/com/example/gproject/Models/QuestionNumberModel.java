package com.example.gproject.Models;

public class QuestionNumberModel {
    String num;
    int task;
    boolean isSelect;
    String topic;

    public int getTask() {
        return task;
    }

    public void setTask(int task) {
        this.task = task;
    }

    public QuestionNumberModel(String num, int task,boolean isSeclect) {
        this.num = num;
        this.task = task;
        this.isSelect = isSeclect;

    }



    public String getNum() {
        return num;
    }
    public Boolean getStart(){return isSelect;}

    public void setNum(String num) {
        this.num = num;
    }
}
