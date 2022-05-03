package Wordle;

import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class WordleController {
    private Wordle wordle = new Wordle();

    @FXML
    private ImageView titlePicture, starWarsTitle;

    @FXML
    private RadioButton setEnglish, setStarWars;

    @FXML
    private TextField guessField;

    @FXML 
    private Text helpText;

    @FXML
    private Button startButton;

    @FXML
    private GridPane charGrid;

    @FXML
    private void handleSetStarWars() throws FileNotFoundException{
        // Called when the Star Wars RadioButton is selected. Enables starWarsMode.
        setStarWars.setDisable(true);
        setEnglish.setDisable(false);
        setEnglish.setSelected(false);
        setStarWars.setSelected(true);
        wordle.setStarWarsMode(true);
        wordle.swapLanguage();
        wordle.readPossibleWords();
        restart();
        starWarsTitle.setVisible(true);
    }

    @FXML
    private void handleSetEnglish() throws FileNotFoundException{
        // Called when the English RadioButton is selected. Disables starWarsMode.
        setStarWars.setDisable(false);
        setEnglish.setDisable(true);
        setEnglish.setSelected(true);
        setStarWars.setSelected(false);
        wordle.setStarWarsMode(false);
        wordle.swapLanguage();
        restart();
        starWarsTitle.setVisible(false);
    }

    @FXML
    private void handleGuessField() throws IOException{
        // Called when the user hits enter in the GuessField.
        boolean victory = false;
        ISaveHandler saveHandler = new SaveHandler();
        String lowerGuessField = guessField.getText().toLowerCase();

        if(wordle.checkWord(lowerGuessField)){
            // Submits valid word.
            wordle.submitWord(lowerGuessField);
            fillGrid(guessField.getText().toUpperCase());
            saveHandler.writeSave(lowerGuessField, "Wordle/guessedWords.txt");
            
            if(guessField.getText().equalsIgnoreCase(wordle.getSecretWord())){
                victoryAlert();
                victory = true;
                guessField.setEditable(false);
                // User won!
            }

            if(wordle.getGuessedWordCount() == 6){
                guessField.setEditable(false);
                if(!victory){
                    lossAlert();
                }
                // User lost!
            }
            guessField.setText("");
            helpText.setText("");
        }
        else {
            if(lowerGuessField.length() != 5){
                helpText.setText("Ugyldig ord! Ordet ditt må være nøyaktig 5 bokstaver langt!");
            }
            else if(wordle.isStarWarsMode()==true){
                helpText.setText("Ugyldig ord! Ordet ditt må være et gyldig ord i Star Wars! (Og selvfølgelig bare fra mitt personlige utvalg)");
            } 
            else {
                helpText.setText("Ugyldig ord! Ordet ditt må være et gyldig engelsk ord!");
            }
        }
        
    }

    private void victoryAlert(){
        // Called when the user wins.
        Alert victoryAlert = new Alert(AlertType.INFORMATION);
        victoryAlert.setTitle("GRATULERER!");
        victoryAlert.setHeaderText(null);
        victoryAlert.setContentText("Du vant på " + wordle.getGuessedWordCount() + " gjett! Sykt bra jobba!");
        victoryAlert.getButtonTypes().setAll(new ButtonType("WOOOHOOO!!! JEG VIL SPILLE MER!"));
        victoryAlert.showAndWait();
    }

    private void lossAlert(){
        // Called when the user loses.
        Alert lossAlert = new Alert(AlertType.INFORMATION);
        lossAlert.setTitle("Jeg kondolerer");
        lossAlert.setHeaderText("Du tapte :(");
        lossAlert.setContentText("Dessverre greide du ikke å gjette riktig svar i løpet av dine 6 forsøk.\nDet hemmelige ordet var : " + wordle.getSecretWord() + ".\nBedre lykke neste gang!");
        lossAlert.getButtonTypes().setAll(new ButtonType("Jeg lover jeg kan gjøre det bedre! LA MEG SPILLE IGJEN!"));
        lossAlert.showAndWait();
    }

    private void fillGrid(String guessedWord){
        // Fills the guessed word into the gridpane. Creates CharBox objects from each letter in the guessed word.
        String lowercase = guessedWord.toLowerCase();
        char[] lowerArray = lowercase.toCharArray();
        char[] cArray = guessedWord.toCharArray();
        
        wordle.addToWordsInGrid(guessedWord);
        wordle.generateColorArrays(guessedWord, lowerArray);

        for (int i = 0; i<5; i++ ) {
            Character c = cArray[i];
            CharBox cbox = new CharBox(c, wordle.generateCharBoxColor(lowerArray, i));
            charGrid.add(createPane(cbox), (i + wordle.getWordsInGridCount() * 5 - 5) % 5, (i + wordle.getWordsInGridCount() * 5 - 5) / 5);
        }
    }

    private void initializeInStarWarsMode() throws FileNotFoundException{
        // Used when starWarsMode.txt is true on initialize.
        setStarWars.setDisable(true);
        setEnglish.setDisable(false);
        setEnglish.setSelected(false);
        setStarWars.setSelected(true);
        wordle.setStarWarsMode(true);
        wordle.swapLanguage();
        starWarsTitle.setVisible(true);
    }
    
    @FXML
    private void initialize() throws FileNotFoundException{
        // Intiializes the application. Checks if the game is in starWarsMode and then loads previous save if there is any and fills the grid with loaded guesses.
        wordle.loadStarWarsMode();
        if(wordle.isStarWarsMode()){
            initializeInStarWarsMode();
        }
        wordle.readPossibleWords();
        wordle.initiateReadSave("guessedWords.txt");
        wordle.generateSecretWord();
        System.out.println(wordle.getGuessedWords());
        for (String word : wordle.getGuessedWords()) {
            fillGrid(word.toUpperCase());
        }
    }

    @FXML
    private void restart() throws FileNotFoundException{
        // Restarts the game by wiping saves and generating new words.
        clearCharGrid();
        wordle.wipeSecretAndGuessedWordFile();
        wordle.wipeGuessedWords();
        wordle.readPossibleWords();
        wordle.generateSecretWord();
        guessField.setEditable(true);
    }

    private void clearCharGrid(){
        // Clears the grid of CharBox objects. 
        // setGridLinesVisible(false) and then (true) is called because the GridLinesVisible is stored within the first node in the grid, which is also wiped when calling clear.
        charGrid.setGridLinesVisible(false);
        charGrid.getChildren().clear();
        charGrid.setGridLinesVisible(true);
    }

    private BorderPane createPane(CharBox cBox){
        // Creates panes from CharBox to fill the gridpane. Mostly just styling.
        BorderPane bpane = new BorderPane();
        Label label = new Label(cBox.getLetter().toString());

        label.wrapTextProperty().setValue(true);
        label.setStyle("-fx-text-fill:WHITE; -fx-font-size: 50;");

        bpane.setStyle("-fx-text-alignment: center;");
        bpane.setStyle("-fx-background-color: " + cBox.getColorAsString() + "; -fx-border-width: 1; -fx-border-color: grey");
        bpane.setMaxWidth(Double.MAX_VALUE);
        bpane.setMaxHeight(Double.MAX_VALUE);
        bpane.setCenter(label);

        return bpane;
    }


}
