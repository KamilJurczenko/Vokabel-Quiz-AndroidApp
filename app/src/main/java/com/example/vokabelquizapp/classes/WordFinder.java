package com.example.vokabelquizapp.classes;

import android.content.Context;

import com.example.vokabelquizapp.Activities.MainActivity;
import com.example.vokabelquizapp.AppData;
import com.example.vokabelquizapp.classes.Database.DatabaseManager;
import com.example.vokabelquizapp.classes.VocabData.VocabTuple;
import com.google.mlkit.vision.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class WordFinder {

    public WordFinder() {
    }

    private final List<String> englishFilter = Arrays.asList("to","sth","sb","be","sth/sb","sb/sth");
    private final List<String> germanFilter = Arrays.asList("etw","an","jmd");
    private final List<String> frenchFilter = Arrays.asList("la","le");

    // Iterate through every Word and check for equal Words from a dictionary.
    // Mark not detected Words, So the user can manually change it.

    public ArrayList<VocabTuple> processOCRResult(Text firstOCRText, Text secondOCRText){
        ArrayList<VocabTuple> vocabList = new ArrayList<>();

        // Filter Found Words by OCR with regular expressions to increase performance by database
        List<String> learningWordList = filterWordsInLines(firstOCRText);
        List<String> mainWordList = filterWordsInLines(secondOCRText);

        /*
        available dictionaries in assets Folder as SQLite Databases
        deu-eng.tei
        deu-fra.tei
        eng-fra.tei
        ...
        */
        // TODO Use Algorithm to search for similar Words (spellfix)

        DatabaseManager database = AppData.databaseManager;
        database.open();
        for(int i = 0; i < learningWordList.size(); i++){
            String learningWord = learningWordList.get(i);
            String mainWord = "";
            boolean marked = false;
            ArrayList<String> results = database.getVocabCounterPart(learningWord);

            VocabTuple vocabTuple;
            if(results.isEmpty()){
                // Word not found in dictionary
                // Mark as not found
                marked = true;
            }
            else{
                // Word found in dictionary
                // Check if any word in results in mainWordList
                boolean foundWord = false;
                for(int j = 0; j < results.size(); j++){
                    String tmp = results.get(j);
                    String compare_tmp = tmp.toUpperCase();
                    for(int k = 0; k < mainWordList.size();k++){
                        String tmpk = mainWordList.get(k);
                        String[] splitTmpk = tmpk.split(",");
                        for (String s : splitTmpk) {
                            s = s.toUpperCase();
                            if (s.contains(compare_tmp)) {
                                mainWord = tmp;
                                foundWord = true;
                                break;
                            }
                        }
                    }
                }
                if(!foundWord){
                    marked = true;
                }
            }
            vocabTuple = new VocabTuple(learningWord, mainWord, marked);
            vocabList.add(vocabTuple);
        }

        database.close();

        return vocabList;
    }

    // One Line represents a full Vocabulary
    private List<String> filterWordsInLines(Text text){
        List<String> filteredLines = new ArrayList<>();
        for (Text.TextBlock block : text.getTextBlocks()) {
            /*
            String blockText = block.getText();
            Point[] blockCornerPoints = block.getCornerPoints();
            Rect blockFrame = block.getBoundingBox();
            */
            // Get Lines
            for (Text.Line line : block.getLines()) {
                /*
                String lineText = line.getText();
                Point[] lineCornerPoints = line.getCornerPoints();
                Rect lineFrame = line.getBoundingBox();
                 */
                StringBuilder filteredLineText = new StringBuilder();
                // Get Words
                for (int i = 0; i < line.getElements().size(); i++) {
                    String elementText = line.getElements().get(i).getText(); // represents a word or word-like entities (dates, numbers,...)
                    /*
                    Point[] elementCornerPoints = element.getCornerPoints();
                    Rect elementFrame = element.getBoundingBox();
                     */
                    String filteredWord = filterWord(elementText, AppData.learningLanguage);
                    filteredLineText.append(filteredWord);
                    if(i + 1 < line.getElements().size()){
                        filteredLineText.append(" ");
                    }
                }
                String filteredLineString = filteredLineText.toString().trim();
                // Don't allow duplicates
                if(!filteredLines.contains(filteredLineString)){
                    filteredLines.add(filteredLineString);
                }
            }
        }
        return filteredLines;
    }
    /*
    public static final Integer GERMAN = 0;
    public static final Integer ENGLISH = 1;
    public static final Integer FRENCH = 2;
    public static final Integer NOT_DEFINED = -1;
     */
    private String filterWord(String word, int LANGUAGE){

        List<String> languageFilter = null;;

        switch(LANGUAGE){
            case 0:
                languageFilter = germanFilter;
                break;
            case 1:
                languageFilter = englishFilter;
                break;
            case 2:
                languageFilter = frenchFilter;
                break;
            default:
                break;
        }

        String regex = "[^a-z,À-ÿ'\\u00F0-\\u02AFA-Z\\s]"; // Allow letters with accents
        word = word.replace("/", " ").replaceAll(regex, "");

        if(languageFilter != null) {
            String[] splitStrings = word.split(" ");
            List<String> finalLanguageFilter = languageFilter;
            word = Arrays.stream(splitStrings).filter(w -> !finalLanguageFilter.contains(w)).collect(Collectors.joining());
        }
        return word;
    }
}
