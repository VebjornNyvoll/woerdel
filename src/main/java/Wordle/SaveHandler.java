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
    private String secretWordFilename = "Wordle/secretWord.txt";

    @Override
	public void writeToFile(String text, String file) {
		try {
			FileWriter fileWriter = new FileWriter(getFile(file));
			fileWriter.write(text);
			fileWriter.close();
			System.out.println("Lagret " + text + " i " + file);
		}
		catch(IOException ie){
			System.out.println("Feil under skriving til " + file);
			ie.printStackTrace();
		}

        
	}
    
    @Override
    public String readSecretWord() throws FileNotFoundException{
        try(Scanner scanner = new Scanner(getFile(secretWordFilename))){
            while(scanner.hasNext()){
                String word = scanner.nextLine();
                return word;
            }
        }
        return null;
  
    }

    public String readStarWarsMode() throws FileNotFoundException{
        try(Scanner scanner = new Scanner(getFile("Wordle/starWarsMode.txt"))){
            while(scanner.hasNext()){
                String word = scanner.nextLine();
                return word;
            }
        }
        return null;
  
    }

    @Override
    public void writeSave(String text, String filename) throws IOException{
        try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(getFile(filename), true)))){
            writer.append(text + "\n");
        }
    }

    @Override
    public ArrayList<String> readSave(String filename) throws FileNotFoundException {
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
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename);

        if (is == null) {
            throw new IllegalArgumentException("File not found! Filename: " + filename);
        } else {
            try (InputStreamReader isReader = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(isReader)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    list.add(line);
                }
            } catch (IOException ie) {
                ie.printStackTrace();
                System.out.println("Failure during reading of file: " + filename);
            }
        }
        return list;
      }

    private File getFile(String filename){
        System.out.println(this.getClass().getClassLoader().getResource(filename).toExternalForm().replace("file:/", ""));
        return new File(this.getClass().getClassLoader().getResource(filename).toExternalForm().replace("file:/", ""));
    }

    @Override
    public boolean hasData(String filename) throws FileNotFoundException{
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
