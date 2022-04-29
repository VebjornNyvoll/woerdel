package Wordle;

public class CharBox {
    private Character letter;
    private Character color;

    public Character getLetter() {
        return letter;
    }

    public void setColor(Character color) {
        this.color = color;
    }

    private Character getColor() {
        return color;
    }

    public String getColorAsString(){
        // Returns the intended color for CharBox. 
        String colorString = "black";
        
        switch(this.getColor()){
            case 'g':
                colorString = "green";
                break;
            case 'y':
                colorString = "yellow";
                break;
            case 'b':
                colorString = "black";
                break;
        }

        return colorString;
    }

    public CharBox(Character letter, Character color) {
        this.letter = letter;
        this.color = color;
    }

  
    
}
