package Wordle;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WordleTest {
    private Wordle wordle;

    @BeforeEach
    public void setup(){
        wordle = new Wordle();
    }

    @Test
    public void testSwapLanguage() throws FileNotFoundException{
        assertEquals("Wordle/possibleWords.txt", wordle.getPossibleWordsFilename());

        wordle.readPossibleWords();
        wordle.generateSecretWord();
       
        List<String> possibleWords = wordle.getPossibleWords();
        
        assertEquals(12947, possibleWords.size(), "Sjekker antallet i possibleWords før swap");
        wordle.swapLanguage();
        assertEquals(76, possibleWords.size(), "Sjekker antallet i possibleWords etter swap");
        assertEquals("Wordle/starWarsWords.txt", wordle.getPossibleWordsFilename());

        wordle.swapLanguage();
        assertEquals(12947, possibleWords.size(), "Sjekker antallet i possibleWords før swap");
    }



    @Test
   public void testGenerateSecretWord() throws FileNotFoundException{
        ISaveHandler saveHandler = new SaveHandler();
        wordle.wipeSecretAndGuessedWordFile();
        wordle.wipeGuessedWords();
        wordle.readPossibleWords();
        assertEquals(null, saveHandler.readOneLiner("Wordle/secretWord.txt"));
        wordle.generateSecretWord();
        assertTrue(saveHandler.hasData("Wordle/secretWord.txt"));
        assertEquals(wordle.getSecretWord(), saveHandler.readOneLiner("Wordle/secretWord.txt"));
   }

   @Test
   public void testCheckWord() throws FileNotFoundException{
       wordle.readPossibleWords();
       assertFalse(wordle.checkWord(null));
       assertFalse(wordle.checkWord("-1"));
       assertFalse(wordle.checkWord("howdys"));
       assertFalse(wordle.checkWord("ca rs"));
       assertFalse(wordle.checkWord(""));
       assertFalse(wordle.checkWord("."));
       assertTrue(wordle.checkWord("death"));
   }

   @Test
   public void testGenerateColorArray() throws FileNotFoundException{
       wordle.readPossibleWords();
       String guessedWord = "hello";
       char[] cArray = guessedWord.toLowerCase().toCharArray();
       wordle.wipeSecretAndGuessedWordFile();
       wordle.setSecretWord("freak");
       wordle.generateColorArrays("hello", cArray);
       char[] expectedGreen = {'?','?','?','?','?'};
       char[] expectedYellow = {'?','e','?','?','?'};

       assertArrayEquals(expectedGreen, wordle.getGreenLetters());
       assertArrayEquals(expectedYellow, wordle.getYellowLetters());

       guessedWord = "frail";
       cArray = guessedWord.toLowerCase().toCharArray();
       wordle.generateColorArrays(guessedWord, cArray);

       expectedGreen[0] = 'f';
       expectedGreen[1] = 'r';

       expectedYellow[1] = '?';
       expectedYellow[2] = 'a';
       assertArrayEquals(expectedGreen, wordle.getGreenLetters());
       assertArrayEquals(expectedYellow, wordle.getYellowLetters());
   }

   @Test
   public void testGenerateCharBoxColor() throws FileNotFoundException{
       wordle.readPossibleWords();
       String guessedWord = "frail";
       char[] lowerArray = guessedWord.toLowerCase().toCharArray();
       wordle.wipeSecretAndGuessedWordFile();
       wordle.setSecretWord("freak");
       wordle.generateColorArrays("frail", lowerArray);

       assertEquals('g', wordle.generateCharBoxColor(lowerArray, 0));
       assertEquals('g', wordle.generateCharBoxColor(lowerArray, 1));
       assertEquals('y', wordle.generateCharBoxColor(lowerArray, 2));
       assertEquals('b', wordle.generateCharBoxColor(lowerArray, 3));
       assertEquals('b', wordle.generateCharBoxColor(lowerArray, 4));
   }

   @Test
   public void testWipeSecretGuessedWordFile() throws FileNotFoundException{
       ISaveHandler saveHandler = new SaveHandler();
       saveHandler.writeToFile("helps\nplead\ndeath\n", "Wordle/guessedWords.txt");

        ArrayList<String> expectedGuessedWords = new ArrayList<>();
        expectedGuessedWords.add("helps");
        expectedGuessedWords.add("plead");
        expectedGuessedWords.add("death");

        assertEquals(expectedGuessedWords, saveHandler.readSave("Wordle/guessedWords.txt"));

        wordle.wipeSecretAndGuessedWordFile();
        expectedGuessedWords.clear();

        assertEquals(expectedGuessedWords, saveHandler.readSave("Wordle/guessedWords.txt"));
   }

   @AfterAll
   public void teardown(){
       wordle.wipeSecretAndGuessedWordFile();
   }

}
