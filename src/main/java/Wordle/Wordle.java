package Wordle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
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

    private void generateSecretWord(){
        secretWord = possibleWords.get(new Random().nextInt(possibleWords.size()));
    }

    public void readFile(){
      InputStream is = this.getClass().getClassLoader().getResourceAsStream(possibleWordsFilename);
      if (is == null) {
          throw new IllegalArgumentException("fant ikke fila sry " + possibleWordsFilename);
      } else {
          try (InputStreamReader isReader = new InputStreamReader(is, StandardCharsets.UTF_8);
          BufferedReader reader = new BufferedReader(isReader)) {
              String line;
              while ((line = reader.readLine()) != null) {
                  possibleWords.add(line);
              }
          } catch (IOException ie) {
              ie.printStackTrace();
              System.out.println("dammit");
          }
      }
      //System.out.println(possibleWords);
      generateSecretWord();
      System.out.println(secretWord);
    }


    public void submitWord(String word){
        if(checkWord(word)){
            guessedWords.add(word);
            // Controller creates panes
                
            // LAGE NOE CHARACTERBOKSER MED CHARACTERS OG WHAT NOT
            //Word guessedWord = new Word();
        }
        else {
            System.out.println("nein nein, ikke godkjent ord");
        }
    }

    public boolean checkWord(String word){
        if(possibleWords.contains(word)){
            return true;
        }
        else{
            return false;
        }
    }

    public int getGuessedWordCount(){
        return guessedWords.size();
    }

    public void generateColorArrays(String guessedWord, char[] cArray){
        System.out.println("GENERATE COLOR ARRAYS CALLED");
        char[] secretArray = secretWord.toCharArray();
        String secretWordCopy = secretWord;
        System.out.println(secretWordCopy);
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
                // System.out.println("green " + String.valueOf(l));
            }
        }

        for (int j = 0; j<5; j++ ){
            char l = cArray[j];
            if(secretWordCopy.contains(String.valueOf(l)) && l != greenLetters[j]){
                yellowLetters[j] = l;
                secretWordCopy = secretWordCopy.replaceFirst(String.valueOf(l), "^");
                // System.out.println("secretWordCopy: " + secretWordCopy);
                secretArray = secretWordCopy.toCharArray();
                // System.out.println("secretWordCopy: " + secretWordCopy);
                // System.out.println("secretArray: " + secretArray.toString());
                // System.out.println("yellow " + String.valueOf(l));
            }
        }
        // System.out.println(greenLetters);
        // System.out.println(yellowLetters);
        // System.out.println(secretWordCopy);
        

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

}
