package com.example.vokabelquizapp.classes.VocabData;

import java.io.Serializable;

// Ein VokabelTuple besteht aus zwei Strings (zwei Vokabeln unterschiedlicher Sprache)
public class VocabTuple implements Serializable {

    private String learningVocab;
    private String mainVocab;
    private boolean notFoundInDict;

    public VocabTuple(String learningVocab, String mainVocab, boolean notFoundInDict) {
        this.learningVocab = learningVocab;
        this.mainVocab = mainVocab;
        this.notFoundInDict = notFoundInDict;
    }

    public boolean isNotFoundInDict() {
        return notFoundInDict;
    }

    public void setNotFoundInDict(boolean notFoundInDict) {
        this.notFoundInDict = notFoundInDict;
    }

    public String getLearningVocab() {
        return learningVocab;
    }

    public void setLearningVocab(String learningVocab) {
        this.learningVocab = learningVocab;
    }

    public String getMainVocab() {
        return mainVocab;
    }

    public void setMainVocab(String mainVocab) {
        this.mainVocab = mainVocab;
    }
}
