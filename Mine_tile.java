import javax.swing.*;

//class to inherit the buttons, but with the added functionality of telling the exact tile that was clicked
public class Mine_tile extends JButton{
    //row and collumn of the button
    int r, c;
    //constructor
    public Mine_tile(int R, int C){
        this.r = R;
        this.c = C;
    }
}