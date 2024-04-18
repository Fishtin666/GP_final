package com.example.gproject.Adapters;

public class WordQuizData {
    private String definition;
    private String correctWord;
    private String incorrectWord;
    private int selectedOption;
    private String opt1;
    private String opt2;
    private String PartOfSpeech, word;
    private String documentId;

    public WordQuizData(String definition, String PartOfSpeech, String correctWord, String incorrectWord,String documentId) {
        this.definition = definition;
//        this.word = Word;
        this.correctWord = correctWord;
        this.incorrectWord = incorrectWord;
        this.selectedOption = -1;
        this.PartOfSpeech = PartOfSpeech;
        this.documentId = documentId;

    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getPartOfSpeech() {
        return PartOfSpeech;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        PartOfSpeech = partOfSpeech;
    }

    public void setOpt1(String opt1) {
        this.opt1 = opt1;
    }

    public void setOpt2(String opt2) {
        this.opt2 = opt2;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getCorrectWord() {
        return correctWord;
    }

    public void setCorrectWord(String correctWord) {
        this.correctWord = correctWord;
    }

    public String getIncorrectWord() {
        return incorrectWord;
    }

    public void setIncorrectWord(String incorrectWord) {
        this.incorrectWord = incorrectWord;
    }

    public int getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(int selectedOption) {
        this.selectedOption = selectedOption;
    }
    public String getOpt1() {
        return correctWord; // 或者返回相應的內容
    }

    public String getOpt2() {
        return incorrectWord; // 或者返回相應的內容
    }

}
