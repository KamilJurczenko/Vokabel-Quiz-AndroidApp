package com.example.vokabelquizapp.classes.VocabData;

import java.util.ArrayList;

public class LanguageVocabs {

    private ArrayList<VocabList> lanVocabList;
    //private String languageCombination;

    public LanguageVocabs() {
        lanVocabList = new ArrayList<>();
    }

    public LanguageVocabs(ArrayList<VocabList> lanVocabList, String languageCombination) {
        this.lanVocabList = lanVocabList;
        //this.languageCombination = languageCombination;
    }

    public void addToList(VocabList list){
        lanVocabList.add(list);
    }

    public ArrayList<VocabList> getLanguageList(){
        return lanVocabList;
    }
}
