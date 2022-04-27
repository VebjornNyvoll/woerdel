package Wordle;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class WordleController {
    Wordle wordle = new Wordle();

    @FXML
    private TextField guessField;

    @FXML
    private Button startButton;

    @FXML
    private GridPane charGrid;

    @FXML
    private void handleGuessField(){
        if(wordle.checkWord(guessField.getText().toLowerCase())){
          
            wordle.submitWord(guessField.getText().toLowerCase());
            fillGrid(guessField.getText().toUpperCase());
            guessField.setText("");

            if(wordle.getGuessedWordCount() == 6){
                guessField.setEditable(false);
                lossAlert();
            }
        }
        
    }

    private void victoryAlert(){
        Alert victoryAlert = new Alert(AlertType.INFORMATION);
        victoryAlert.setTitle("CONGRATULATIONS!");
        victoryAlert.setHeaderText(null);
        victoryAlert.setContentText("You won in " + wordle.getGuessedWordCount() + " guesses! Great job!");
        victoryAlert.getButtonTypes().setAll(new ButtonType("WOOOHOOO!!! LET'S PLAY SOME MORE!"));
        victoryAlert.showAndWait();
    }

    private void lossAlert(){
        Alert lossAlert = new Alert(AlertType.INFORMATION);
        lossAlert.setTitle("My condolences");
        lossAlert.setHeaderText(null);
        lossAlert.setContentText("Sadly, you were not able to guess the word within your 6 guesses. The secret word was: " + wordle.getSecretWord() + ". Better luck next time!");
        lossAlert.getButtonTypes().setAll(new ButtonType("I can do better! Let me try again!"));
        lossAlert.showAndWait();
    }

    private void fillGrid(String guessedWord){
        String lowercase = guessedWord.toLowerCase();
        char[] lowerArray = lowercase.toCharArray();
        char[] cArray = guessedWord.toCharArray();

         wordle.generateColorArrays(guessedWord, lowerArray);

        for (int i = 0; i<5; i++ ) {
            Character c = cArray[i];
            CharBox cbox = new CharBox(c, wordle.generateCharBoxColor(lowerArray, i));
            charGrid.add(createPane(cbox), (i + wordle.getGuessedWordCount() * 5 - 5) % 5, (i + wordle.getGuessedWordCount() * 5 - 5) / 5);
        }
    }
    
    @FXML
    private void initialize(){
        wordle = new Wordle();
        wordle.readFile();
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
