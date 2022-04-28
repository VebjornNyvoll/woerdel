package Wordle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

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
    Wordle wordle = new Wordle();

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
        boolean victory = false;
        ISaveHandler saveHandler = new SaveHandler();
        if(wordle.checkWord(guessField.getText().toLowerCase())){
          
            wordle.submitWord(guessField.getText().toLowerCase());
            fillGrid(guessField.getText().toUpperCase());
            saveHandler.writeSave(guessField.getText().toLowerCase(), "Wordle/guessedWords.txt");
            
            if(guessField.getText().equalsIgnoreCase(wordle.getSecretWord())){
                victoryAlert();
                victory = true;
                // SUBMIT WIN TIL FIL
            }

            if(wordle.getGuessedWordCount() == 6){
                guessField.setEditable(false);
                if(!victory){
                    lossAlert();
                }
            }
            guessField.setText("");
            helpText.setText("");
        }
        else {
            if(guessField.getText().toLowerCase().length() != 5){
                helpText.setText("Ugyldig ord! Ordet ditt må være nøyaktig 5 bokstaver langt!");
            }
            else if(starWarsTitle.isVisible()==true){
                helpText.setText("Ugyldig ord! Ordet ditt må være et gyldig ord i Star Wars! (Og selvfølgelig bare fra mitt personlige utvalg)");
            } 
            else {
                helpText.setText("Ugyldig ord! Ordet ditt må være et gyldig engelsk ord!");
            }
        }
        
    }

    private void victoryAlert(){
        Alert victoryAlert = new Alert(AlertType.INFORMATION);
        victoryAlert.setTitle("GRATULERER!");
        victoryAlert.setHeaderText(null);
        victoryAlert.setContentText("Du vant på " + wordle.getGuessedWordCount() + " gjett! Sykt bra jobba!");
        victoryAlert.getButtonTypes().setAll(new ButtonType("WOOOHOOO!!! JEG VIL SPILLE MER!"));
        victoryAlert.showAndWait();
    }

    private void lossAlert(){
        Alert lossAlert = new Alert(AlertType.INFORMATION);
        lossAlert.setTitle("Jeg kondolerer");
        lossAlert.setHeaderText("Du tapte :(");
        lossAlert.setContentText("Dessverre greide du ikke å gjette riktig svar i løpet av dine 6 forsøk.\nDet hemmelige ordet var : " + wordle.getSecretWord() + ".\nBedre lykke neste gang!");
        lossAlert.getButtonTypes().setAll(new ButtonType("Jeg lover jeg kan gjøre det bedre! LA MEG SPILLE IGJEN!"));
        lossAlert.showAndWait();
    }

    private void fillGrid(String guessedWord){
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
        System.out.println(wordle.getPossibleWordCount());
    }

    @FXML
    private void restart() throws FileNotFoundException{
        clearCharGrid();
        wordle.wipeSecretAndGuessedWordFile();
        wordle.wipeGuessedWords();
        wordle.readPossibleWords();
        wordle.generateSecretWord();
        guessField.setEditable(true);
    }

    private void clearCharGrid(){
        charGrid.setGridLinesVisible(false);
        charGrid.getChildren().clear();
        charGrid.setGridLinesVisible(true);
    }

    private BorderPane createPane(CharBox cBox){
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
