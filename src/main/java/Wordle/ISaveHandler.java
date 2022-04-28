package Wordle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public interface ISaveHandler {

    List<String> readPossibleWords(String filename, List<String> arrayList) throws FileNotFoundException;

    String readSecretWord() throws FileNotFoundException;

    void writeSave(String text, String filename) throws FileNotFoundException;

    ArrayList<String> readSave(String filename) throws FileNotFoundException;

    boolean hasData(String filename) throws FileNotFoundException;
}
