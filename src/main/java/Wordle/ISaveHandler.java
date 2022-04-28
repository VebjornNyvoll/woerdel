package Wordle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface ISaveHandler {
    
    void writeToFile(String text, String file);

    List<String> readPossibleWords(String filename, List<String> arrayList) throws FileNotFoundException;

    String readSecretWord() throws FileNotFoundException;

    void writeSave(String text, String filename) throws FileNotFoundException, IOException;

    ArrayList<String> readSave(String filename) throws FileNotFoundException;

    boolean hasData(String filename) throws FileNotFoundException;

    String readStarWarsMode() throws FileNotFoundException;
}
