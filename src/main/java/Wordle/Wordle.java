package Wordle;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Wordle{
    
    private ArrayList<String> guessedWords = new ArrayList<>();
    private ArrayList<String> wordsInGrid = new ArrayList<>();
    private List<String> possibleWords = new ArrayList<>();
    private String possibleWordsFilename = "Wordle/possibleWords.txt";
    private String secretWord;
    private boolean starWarsMode = false;

    private char[] greenLetters = {'?','?','?','?','?'};
    private char[] yellowLetters = {'?','?','?','?','?'};

    public void setStarWarsMode(boolean bool){
        starWarsMode = bool;
        ISaveHandler saveHandler = new SaveHandler();
        saveHandler.writeToFile(String.valueOf(bool), "Wordle/starWarsMode.txt");
        System.out.println("starWarsMode set to " + bool);
    }

    public void loadStarWarsMode() throws FileNotFoundException{
        ISaveHandler saveHandler = new SaveHandler();
        starWarsMode = Boolean.parseBoolean(saveHandler.readStarWarsMode());
    }

    public boolean isStarWarsMode(){
        return starWarsMode;
    }

    public void swapLanguage() throws FileNotFoundException{
        if(possibleWordsFilename == "Wordle/possibleWords.txt"){
            possibleWordsFilename = "Wordle/starWarsWords.txt";
            System.out.println("SWAPPING TO STAR WARS");
            possibleWords.clear();
            readPossibleWords();
            generateSecretWord();
        } else {
            possibleWordsFilename = "Wordle/possibleWords.txt";
            possibleWords.clear();
            readPossibleWords();
            generateSecretWord();
        }
    }

    public int getWordsInGridCount(){
        return wordsInGrid.size();
    }

    public void addToWordsInGrid(String word){
        wordsInGrid.add(word);
    }

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
            saveHandler.writeToFile(secretWord, "Wordle/secretWord.txt");
            System.out.println(secretWord);
        }else{
            secretWord = previousSecret;
            System.out.println("Tidligere save funnet. Hemmelig ord lastet. " + secretWord);
        }
        
    }

    public void readPossibleWords(){
        SaveHandler saveHandler = new SaveHandler();
        possibleWords = saveHandler.readPossibleWords(possibleWordsFilename, possibleWords);
        System.out.println("Reading possibleWords!");
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
        if(saveHandler.hasData("Wordle/guessedWords.txt"))
        guessedWords = saveHandler.readSave("Wordle/guessedWords.txt");

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

    public void wipeSecretAndGuessedWordFile(){
        SaveHandler saveHandler = new SaveHandler();
        saveHandler.writeToFile("", "Wordle/secretWord.txt");
        saveHandler.writeToFile("", "Wordle/guessedWords.txt");
    }

    public void wipeGuessedWords(){
        guessedWords.clear();
        wordsInGrid.clear();

    }
}
