package Wordle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
            if(wordle.getGuessedWordCount() == 5){
                System.out.println("is 5 now");
                guessField.setEditable(false);
            }
            wordle.submitWord(guessField.getText().toLowerCase());
            fillGrid(guessField.getText().toUpperCase());
            guessField.setText("");
        }
        
    }

    private void fillGrid(String guessedWord){
        String lowercase = guessedWord.toLowerCase();
        char[] lowerArray = lowercase.toCharArray();
        char[] cArray = guessedWord.toCharArray();

        for (int i = 0; i<5; i++ ) {
            Character c = cArray[i];
            CharBox cbox = new CharBox(c, wordle.generateCharBoxColor(lowercase, i, lowerArray));
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
