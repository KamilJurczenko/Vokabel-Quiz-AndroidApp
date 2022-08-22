package com.example.vokabelquizapp.classes.VocabData;

import java.util.ArrayList;

public class VocabList {

    private ArrayList<VocabTuple> vocabList;
    private String name;

    public VocabList() {
    }

    public VocabList(String name) {
        this.vocabList = new ArrayList<>();
        this.name = name;
    }

    public ArrayList<VocabTuple> getVocabList() {
        return vocabList;
    }

    public boolean addTupleToVocabList(VocabTuple tuple){
        boolean alreadyInList = false;
        for(VocabTuple v : vocabList){
            if (v.getLearningVocab().equals(tuple.getLearningVocab())) {
                alreadyInList = true;
                break;
            }
        }
        if(!alreadyInList)
            this.vocabList.add(tuple);
        return alreadyInList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
