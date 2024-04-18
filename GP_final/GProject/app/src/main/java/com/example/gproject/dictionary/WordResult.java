package com.example.gproject.dictionary;

import java.util.ArrayList;
import java.util.List;

public class WordResult {

    private String word;
    private String phonetic;
    private List<Meaning> meanings;

    //public ArrayList<Phonetic> phonetics;


    public WordResult(String word, String phonetic, List<Meaning> meanings) {
        this.word = word;
        this.phonetic = phonetic;
        this.meanings = meanings;

    }
    public class Definition{
        public String definition;
        public ArrayList<Object> synonyms;
        public ArrayList<Object> antonyms;

    }



    public class Meaning{
        public String partOfSpeech;
        public ArrayList<Definition> definitions;
        public ArrayList<String> synonyms;
        public ArrayList<String> antonyms;
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

    public List<Meaning> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<Meaning> meanings) {
        this.meanings = meanings;
    }
}


