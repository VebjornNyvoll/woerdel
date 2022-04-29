package Wordle;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.List;

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
   }
}
