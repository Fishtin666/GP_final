package com.example.gproject.WordCard;

import java.util.List;

public class Meaning {
    private String partOfSpeech;
    private List<Definition> definitions;
    private List<String> synonyms;
    private List<String> antonyms;
    private String definitionsText;

    public Meaning(String partOfSpeech, List<Definition> definitions, List<String> synonyms, List<String> antonyms, String definitionsText) {
        this.partOfSpeech = partOfSpeech;
        this.definitions = definitions;
        this.synonyms = synonyms;
        this.antonyms = antonyms;
        this.definitionsText = definitionsText;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public List<Definition> getDefinitions() {
        return definitions;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public List<String> getAntonyms() {
        return antonyms;
    }

    public String getDefinitionsText() {
        return definitionsText;
    }
}
