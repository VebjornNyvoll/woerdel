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
        // Sets the starWarsMode boolean and saves it to starWarsMode.txt
        starWarsMode = bool;
        ISaveHandler saveHandler = new SaveHandler();
        saveHandler.writeToFile(String.valueOf(bool), "Wordle/starWarsMode.txt");
        System.out.println("starWarsMode set to " + bool);
    }

    public void loadStarWarsMode() throws FileNotFoundException{
        // Loads the starWarsMode boolean from starWarsMode.txt
        ISaveHandler saveHandler = new SaveHandler();
        starWarsMode = Boolean.parseBoolean(saveHandler.readOneLiner("Wordle/starWarsMode.txt"));
    }

    public boolean isStarWarsMode(){
        return starWarsMode;
    }

    public void swapLanguage() throws FileNotFoundException{
        // Changes possibleWords and secretWord when changing starWarsMode.
        if(possibleWordsFilename == "Wordle/possibleWords.txt"){
            possibleWordsFilename = "Wordle/starWarsWords.txt";
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
        // Used to calculate position of upcoming words in the grid.
        return wordsInGrid.size();
    }

    public void addToWordsInGrid(String word){
        // Adds word to the wordsInGrid ArrayList.
        wordsInGrid.add(word);
    }

    public String getSecretWord(){
        return secretWord;
    }

    public void generateSecretWord() throws FileNotFoundException{
        // Attempts to load a saved secretWord and otherwise it generates a new one.
        ISaveHandler saveHandler = new SaveHandler();
        String previousSecret = saveHandler.readOneLiner("Wordle/secretWord.txt");
        if(previousSecret == null){
            secretWord = possibleWords.get(new Random().nextInt(possibleWords.size()));
            saveHandler.writeToFile(secretWord, "Wordle/secretWord.txt");
            System.out.println("No previous save found, new secretWord generated: " + secretWord);
        }else{
            secretWord = previousSecret;
            System.out.println("Previous save found. Secret word loaded: " + secretWord);
        }
        
    }

    public void readPossibleWords() throws FileNotFoundException{
        // Loads possibleWords from file into the possibleWords ArrayList.
        ISaveHandler saveHandler = new SaveHandler();
        possibleWords = saveHandler.readPossibleWords(possibleWordsFilename, possibleWords);
        System.out.println("Reading possibleWords!");
    }

    public void initiateReadSave(String filename) throws FileNotFoundException{
        // Loads previously guessed words from guessedWords.txt into the guessedWords ArrayList.
        ISaveHandler saveHandler = new SaveHandler();
        if(saveHandler.hasData("Wordle/guessedWords.txt"))
        guessedWords = saveHandler.readSave("Wordle/guessedWords.txt");

        System.out.println(guessedWords);
    }

    public ArrayList<String> getGuessedWords(){
        // Returns previously guessed words.
        ArrayList<String> guessedWordsCopy = guessedWords;
        return guessedWordsCopy;
    }

    public void submitWord(String word){
        // Adds valid guesses to the guessedWords ArrayList.
        if(checkWord(word)){
            guessedWords.add(word);
        }
        else {
            System.out.println("Word not approved by checkWord.");
        }
    }

    public boolean checkWord(String word){
        // Checks if a guessed word is valid.
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
        // Finds out which colors the letters in the guessed word should be by comparing them to the secret word.
        char[] secretArray = secretWord.toCharArray();
        String secretWordCopy = secretWord;

        // Resetter arrays
        for (int k = 0; k<5; k++ ){
            greenLetters[k] = '?';
            yellowLetters[k] = '?';
        }
       
        // Finner grønne bokstaver
        for (int i = 0; i<5; i++ ){
            char l = cArray[i];
            if(l == secretArray[i]){
                greenLetters[i] = l;
                secretArray[i] = '*';
                secretWordCopy = String.valueOf(secretArray);
            }
        }

        // Finner gule bokstaver
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
        // Compares chars from the guessed words with green/yellowLetters to find out colour of CharBox.
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
        // Wipes secretWord/guessedWords.txt for restarts.
        SaveHandler saveHandler = new SaveHandler();
        saveHandler.writeToFile("", "Wordle/secretWord.txt");
        saveHandler.writeToFile("", "Wordle/guessedWords.txt");
    }

    public void wipeGuessedWords(){
        // Wipes the guessedWords and wordsInGrid ArrayLists.
        guessedWords.clear();
        wordsInGrid.clear();

    }

    public String getPossibleWordsFilename(){
        // For tests
        return possibleWordsFilename;
    }

    public List<String> getPossibleWords(){
        // For tests
        List<String> possibleWordsCopy = possibleWords;
        return possibleWordsCopy;
    }
}
