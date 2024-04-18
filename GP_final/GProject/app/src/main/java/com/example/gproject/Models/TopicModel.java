package com.example.gproject.Models;

public class TopicModel {
    String name;
    int image;

    int part;
    //0:question卡 1:ai_teacher


    public TopicModel(String name, int image, int part) {
        this.name = name;
        this.image = image;
        this.part = part;
    }

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
