package Wordle;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Wordle{
    
    private ArrayList<String> guessedWords = new ArrayList<>();
    private List<String> possibleWords = new ArrayList<>();
    private String possibleWordsFilename = "Wordle/possibleWords.txt";
    private String secretWord;

    private char[] greenLetters = {'?','?','?','?','?'};
    private char[] yellowLetters = {'?','?','?','?','?'};

    public String getSecretWord(){
        return secretWord;
    }

    public int getPossibleWordCount(){
        return possibleWords.size();
    }

    public void generateSecretWord() throws FileNotFoundException{
        SaveHandler saveHandler = new SaveHandler();
        String previousSecret = saveHandler.readSecretWord();
        if(previousSecret == null){
            System.out.println("Ingen tidligere save funnet. Genererer nytt ord.");
            secretWord = possibleWords.get(new Random().nextInt(possibleWords.size()));
        }else{
            secretWord = previousSecret;
            System.out.println("Tidligere save funnet. Hemmelig ord lastet.");
        }
        
    }

    public void readPossibleWords(){
        SaveHandler saveHandler = new SaveHandler();
        possibleWords = saveHandler.readPossibleWords(possibleWordsFilename, possibleWords);
        System.out.println("Reading possibleWords!");
        System.out.println(possibleWords);
    }

    public void initiateReadSave(String filename) throws FileNotFoundException{
    //   InputStream is = this.getClass().getClassLoader().getResourceAsStream(possibleWordsFilename);
    //   if (is == null) {
    //       throw new IllegalArgumentException("fant ikke fila sry " + possibleWordsFilename);
    //   } else {
    //       try (InputStreamReader isReader = new InputStreamReader(is, StandardCharsets.UTF_8);
    //       BufferedReader reader = new BufferedReader(isReader)) {
    //           String line;
    //           while ((line = reader.readLine()) != null) {
    //               possibleWords.add(line);
    //           }
    //       } catch (IOException ie) {
    //           ie.printStackTrace();
    //           System.out.println("dammit");
    //       }
    //   }
        SaveHandler saveHandler = new SaveHandler();
        if(saveHandler.hasData(possibleWordsFilename))
        guessedWords = saveHandler.readSave(possibleWordsFilename);

        System.out.println(guessedWords);
    }

    public ArrayList<String> getGuessedWords(){
        ArrayList<String> guessedWordsCopy = guessedWords;
        return guessedWordsCopy;
    }

    public void submitWord(String word){
        if(checkWord(word)){
            guessedWords.add(word);
            // Controller creates panes
        }
        else {
            System.out.println("Word not approved.");
        }
    }

    public boolean checkWord(String word){
        if(possibleWords.contains(word)){
            return true;
        }
        else{
            System.out.println("In checkword:");
            System.out.println(word);
            System.out.println(possibleWords);
            System.out.println("returned false.");
            return false;
        }
    }

    public int getGuessedWordCount(){
        return guessedWords.size();
    }

    public void generateColorArrays(String guessedWord, char[] cArray){
        char[] secretArray = secretWord.toCharArray();
        String secretWordCopy = secretWord;

        for (int k = 0; k<5; k++ ){
            greenLetters[k] = '?';
            yellowLetters[k] = '?';
        }
       

        for (int i = 0; i<5; i++ ){
            char l = cArray[i];
            if(l == secretArray[i]){
                greenLetters[i] = l;
                secretArray[i] = '*';
                secretWordCopy = String.valueOf(secretArray);
            }
        }

        for (int j = 0; j<5; j++ ){
            char l = cArray[j];
            if(secretWordCopy.contains(String.valueOf(l)) && l != greenLetters[j]){
                yellowLetters[j] = l;
                secretWordCopy = secretWordCopy.replaceFirst(String.valueOf(l), "^");
                secretArray = secretWordCopy.toCharArray();
            }
        }
        

    }

    public Character generateCharBoxColor(char[] lowerArray, int i){
        if(lowerArray[i] == greenLetters[i]){
            return 'g';
        }
        else if(lowerArray[i] == yellowLetters[i]){
            return 'y';
        }
        else{
            return 'b';
        }
    }

    public void wipeSecretWordFile(){
        SaveHandler saveHandler = new SaveHandler();
        saveHandler.writeToFile("", "secretWord.txt");
    }

}
