package Wordle;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CharBoxTest {

    @Test
    public void testGetColorAsString(){
        CharBox greenBox = new CharBox('c', 'g');
        CharBox yellowBox = new CharBox('h', 'y');
        CharBox blackBox = new CharBox('x', 'b');
        CharBox otherBox = new CharBox('r', 'x');

        String actualGreenColor = greenBox.getColorAsString();
        String expectedGreenColor = "green";
        assertEquals(expectedGreenColor, actualGreenColor, "Tester om boks med color 'g' er grønn");

        String actualYellowColor = yellowBox.getColorAsString();
        String expectedYellowColor = "yellow";
        assertEquals(expectedYellowColor, actualYellowColor, "Tester om boks med color 'y' er gul");

        String actualBlackColor = blackBox.getColorAsString();
        String expectedBlackColor = "black";
        assertEquals(expectedBlackColor, actualBlackColor, "Tester om boks med color 'b' er svart");

        String actualOtherColor = otherBox.getColorAsString();
        assertEquals(expectedBlackColor, actualOtherColor, "Tester om boks med color annet enn g,y eller b er svart.");
    }
}   
