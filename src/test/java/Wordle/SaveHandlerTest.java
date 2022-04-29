package Wordle;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SaveHandlerTest {
    SaveHandler saveHandler;

    private String testGuessedWordsFilename = "Wordle/testfiles/testGuessedWords.txt";
    private String testSecretWordFilename = "Wordle/testfiles/testSecretWord.txt";
    private String testPossibleWordsFilename = "Wordle/testfiles/testPossibleWords.txt";
    private String testStarWarsModeFilename = "Wordle/testfiles/testStarWarsMode.txt";

    @BeforeEach
    public void setup(){
        saveHandler = new SaveHandler();
    }

    @AfterAll
    public void teardown(){
        saveHandler.writeToFile("", testSecretWordFilename);
        saveHandler.writeToFile("", testGuessedWordsFilename);
        saveHandler.writeToFile("false", testStarWarsModeFilename);
        saveHandler.writeToFile("death\npains\nhurts\nsheet\nblank\nheist\nhoist\nflags\n", testPossibleWordsFilename);
    }


    @Test
    public void testReadOneLiner() throws FileNotFoundException{
        String expectedString = "hello";
        saveHandler.writeToFile("hello", testSecretWordFilename);
        assertEquals(expectedString, saveHandler.readOneLiner(testSecretWordFilename), "Sjekker om readOneLiner leser " + expectedString + " riktig.");

        saveHandler.writeToFile("two different lines\nthat's crazy", testSecretWordFilename);
        expectedString = "two different lines";
        assertEquals("two different lines", saveHandler.readOneLiner(testSecretWordFilename), "Sjekker at readOneLiner kun leser første linje");

        assertThrows(
                NullPointerException.class,
                () -> saveHandler.readOneLiner("Wordle/testfiles/invalidFile.txt"),
                "NullPointerException should be thrown if file is null!");
    }

    @Test
    public void testWriteToFile() throws FileNotFoundException{
        saveHandler.writeToFile("banan", testSecretWordFilename);
        assertEquals(saveHandler.readOneLiner(testSecretWordFilename), "banan");

        assertFalse(Boolean.parseBoolean(saveHandler.readOneLiner(testStarWarsModeFilename)));
        saveHandler.writeToFile("true", testStarWarsModeFilename);
        assertTrue(Boolean.parseBoolean(saveHandler.readOneLiner(testStarWarsModeFilename)));

        assertThrows(
                NullPointerException.class,
                () -> saveHandler.writeToFile("test","Wordle/testfiles/invalidFile.txt"),
                "NullPointerException should be thrown if file is null!");
    }

    @Test
    public void testWriteReadSave() throws IOException{
        ArrayList<String> expectedResult = new ArrayList<>();
        expectedResult.add("cakes");
        expectedResult.add("basil");
        expectedResult.add("phone");

        saveHandler.writeSave("cakes", testGuessedWordsFilename);
        saveHandler.writeSave("basil", testGuessedWordsFilename);
        saveHandler.writeSave("phone", testGuessedWordsFilename);

        ArrayList<String> actualResult = saveHandler.readSave(testGuessedWordsFilename);

        assertEquals(expectedResult, actualResult, "Sjekker at writeSave og readSave fungerer som forventet.");

        assertThrows(
                NullPointerException.class,
                () -> saveHandler.readSave("Wordle/testfiles/invalidFile.txt"),
                "NullPointerException should be thrown if file is null!");

        assertThrows(
                NullPointerException.class,
                () -> saveHandler.writeSave("test","Wordle/testfiles/invalidFile.txt"),
                "NullPointerException should be thrown if file is null!");

    }

    @Test
    public void testReadPossibleWords() throws IOException{
        List<String> expectedPossibleWords = new ArrayList<>();
        expectedPossibleWords.add("death");
        expectedPossibleWords.add("pains");
        expectedPossibleWords.add("hurts");
        expectedPossibleWords.add("sheet");
        expectedPossibleWords.add("blank");
        expectedPossibleWords.add("heist");
        expectedPossibleWords.add("hoist");
        expectedPossibleWords.add("flags");
    
        List<String> actualPossibleWords = new ArrayList<>();
        actualPossibleWords = saveHandler.readPossibleWords(testPossibleWordsFilename, actualPossibleWords);

        assertEquals(expectedPossibleWords, actualPossibleWords, "Sjekker at readPossibleWords leser inn possibleWords riktig.");

        saveHandler.writeSave("test", testPossibleWordsFilename);
        List<String> actualPossibleWordsShortWord = new ArrayList<>();
        actualPossibleWordsShortWord = saveHandler.readPossibleWords(testPossibleWordsFilename, actualPossibleWordsShortWord);
        assertEquals(expectedPossibleWords, actualPossibleWordsShortWord, "Tester at ikke ugyldige ord leses inn");

        assertThrows(
                IllegalArgumentException.class,
                () -> saveHandler.readPossibleWords("Wordle/testfiles/invalidFile.txt", new ArrayList<>()),
                "IlleglArgumentException should be thrown if file is not a real file!");
    }

    @Test
    public void testHasData() throws FileNotFoundException{
        assertTrue(saveHandler.hasData(testPossibleWordsFilename), "File with text should return true");
        saveHandler.writeToFile("", testSecretWordFilename);
        assertFalse(saveHandler.hasData(testSecretWordFilename), "Empty file should return false");


        assertThrows(
                NullPointerException.class,
                () -> saveHandler.hasData("Wordle/testfiles/otherFile.txt"),
                "NullPointerException should be thrown if file doesn't exist");
    }

}
