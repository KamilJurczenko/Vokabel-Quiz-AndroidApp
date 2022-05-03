package com.example.vokabelquizapp.classes;

// Ein VokabelTuple besteht aus zwei Strings (zwei Vokabeln unterschiedlicher Sprache)
public class VokabelTuple {

    private String firstVocab;
    private String secondVocab;

    public String getFirstVocab() {
        return firstVocab;
    }

    public void setFirstVocab(String firstVocab) {
        this.firstVocab = firstVocab;
    }

    public String getSecondVocab() {
        return secondVocab;
    }

    public void setSecondVocab(String secondVocab) {
        this.secondVocab = secondVocab;
    }
}
