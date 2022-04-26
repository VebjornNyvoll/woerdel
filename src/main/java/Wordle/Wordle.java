package Wordle;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Wordle{
    
    private ArrayList<Word> guessedWords = new ArrayList<>();
    private List<String> possibleWords = new ArrayList<>();
    private String possibleWordsFilename = "Wordle/possibleWords.txt";
    private String secretWord;
    
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
      System.out.println(possibleWords);
    }
 


    public void submitWord(String word){
        if(checkWord(word)){
            char[] cArray = word.toCharArray();
            
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

    
}
