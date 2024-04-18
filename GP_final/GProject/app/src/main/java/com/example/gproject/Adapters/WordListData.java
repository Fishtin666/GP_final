package com.example.gproject.Adapters;

public class WordListData {
    String word, phonetic;
    private boolean isExpanded;

    public WordListData(String word, String phonetic) {
        this.word = word;
        this.phonetic = phonetic;

    }
    public boolean isExpanded() {
        return isExpanded;
    }
    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }
}
