package Wordle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SaveHandler implements ISaveHandler{

    @Override
	public void writeToFile(String text, String filename) {
        // Used to write & wipe secretWord.txt and starWarsmode.txt and for wiping guessedWords.txt
		try {
			FileWriter fileWriter = new FileWriter(getFile(filename));
			fileWriter.write(text);
			fileWriter.close();
			System.out.println("Saved " + text + " to " + filename);
		}
		catch(IOException ie){
			System.out.println("Failure when writing to " + filename);
			ie.printStackTrace();
		}
	}
    
    @Override
    public String readOneLiner(String filename) throws FileNotFoundException{
        // Used to read starWarsMode.txt and secretWord.txt
        try(Scanner scanner = new Scanner(getFile(filename))){
            while(scanner.hasNext()){
                String word = scanner.nextLine();
                return word;
            }
        } catch (FileNotFoundException fnfe) {
            throw new FileNotFoundException("File not found in readOneLiner " + filename);
        }
        
        return null;
    
    }

    @Override
    public void writeSave(String text, String filename) throws IOException{
        // Used to save valid guesses to guessedWords.txt
        try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(getFile(filename), true)))){
            writer.append(text + "\n");
        } catch (IOException ie){
            throw new IOException("IOException in writeSave " + filename);
        }
    }

    @Override
    public ArrayList<String> readSave(String filename) throws FileNotFoundException {
        // Loads previously guessed words.
        ArrayList<String> arrayList = new ArrayList<>();
        try(Scanner scanner = new Scanner(getFile(filename))){
            while(scanner.hasNext()){
                String guessedWord = scanner.nextLine();
                arrayList.add(guessedWord);
            }
        }
        return arrayList;
    }


    @Override
    public List<String> readPossibleWords(String filename, List<String> list){
        // Loads possible, valid words from filename. 
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename);

        if (is == null) {
            throw new IllegalArgumentException("File not found! Filename: " + filename);
        } else {
            try (InputStreamReader isReader = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(isReader)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if(line.length() == 5 && !line.contains("*") && !line.contains("^") && !line.contains("?")){
                        list.add(line.toLowerCase());
                    }
                    
                }
            } catch (IOException ie) {
                ie.printStackTrace();
                System.out.println("Failure during reading of file: " + filename);
            }
        }
        return list;
      }

    private File getFile(String filename){
        // Gets file from target by converting relative path to absolute path.
        return new File(this.getClass().getClassLoader().getResource(filename).toExternalForm().replace("file:/", ""));
    }

    @Override
    public boolean hasData(String filename) throws FileNotFoundException{
        // Checks if a file has data
        try(Scanner scanner = new Scanner(getFile(filename))){
            if(scanner.hasNext()){
                return true;
            }
            else {
                return false;
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("FILE NOT FOUND IN hasData()" + filename);
            fnfe.printStackTrace();
            return false;
        }
    }

    
}
