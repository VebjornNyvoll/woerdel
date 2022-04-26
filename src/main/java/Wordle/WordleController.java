package Wordle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class WordleController {
    Wordle wordle = new Wordle();

    @FXML
    private TextField guessField;

    @FXML
    private Button startButton;

    
    @FXML
    private void handleGuessField(){
        if(wordle.checkWord(guessField.getText())){
            wordle.submitWord(guessField.getText());
        }
        
    }
    
    @FXML
    private void initialize(){
        wordle = new Wordle();
        wordle.readFile();
    }

}
