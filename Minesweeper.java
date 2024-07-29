import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Minesweeper {
    
//create window for the program
    //set the dimensions of the elements in the window.
    int tile_size = 60;//65
    int num_rows = 14;//12
    int num_collumns = 18 /*num_rows*/;
    int board_width = num_collumns * tile_size;
    int board_height = num_rows * tile_size;

    int mine_count = 26;

    //creates frame
    JFrame window_frame = new JFrame ("Minesweeper");

    //creates text label and panel for displaying messages to the player
    JLabel text_label = new JLabel();
    //creates a panel that will be used for the text label
    JPanel text_panel =  new JPanel();

    //creates a panel for the board
    JPanel board_panel = new JPanel();

    //creates a text field that will be used to chance the mines i the board
    JTextField t_field = new JTextField(Integer.toString(mine_count));
    
    //array of tiles, the board
    Mine_tile[][] board = new Mine_tile [num_rows][num_collumns];

    //this list stores every tile that has a mine placed in them
    ArrayList<Mine_tile> mine_list;

    //random object to get random numbers
    Random random_number =  new Random();
    //ammount of mines
    //int mine_count = random_number.nextInt(9,13);
    //int mine_count = 12;
    //tracks ammount of tiles clicked
    int tiles_clicked = 0;
    //game over control variable
    boolean gameover = false;

    //creates text label and panel for placing text
    JLabel text_label2 = new JLabel();
    //creates a panel that will be used for the text label
    JPanel text_panel2 =  new JPanel();

//constructor
    Minesweeper(){
        //set the frame of the window visible
        //window_frame.setVisible(true);
        //set the size of the window
        window_frame.setSize(board_width, board_height);
        //where the window will open in the screen
        window_frame.setLocationRelativeTo(null);
        //window cant be resized
        window_frame.setResizable(false);
        //terminates the program
        window_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //lays out using the standart provided by BorderLayout
        window_frame.setLayout(new BorderLayout());

        //placing text above the board
        //set font for the text
        text_label.setFont(new Font("Arial", Font.BOLD, 30/*fontsize*/));
        //centers the text
        text_label.setHorizontalAlignment(JLabel.CENTER);
        //text that will be displayed
        text_label.setText("Minesweeper"/*+ Integer.toString(mine_count)*/);
        //transparency
        text_label.setOpaque(true);
        //----text_label.setBackground(Color.black);----

        //lays out the text panel
        text_panel.setLayout(new BorderLayout());
        //displays the text in the panel created
        text_panel.add(text_label);

        //add the panel to the window, to the top of the screen
        window_frame.add(text_panel, BorderLayout.NORTH);

        //lays out the board according to the number of rows and collumns
        board_panel.setLayout(new GridLayout(num_rows, num_collumns));
        //adds a color to the background
        //-----board_panel.setBackground(Color.gray);----
        //adds board to the window
        window_frame.add(board_panel);

        //adding the tiles to the window
        for(int r = 0; r < num_rows; r++){
            for(int c = 0; c < num_collumns; c++){
                Mine_tile tile = new Mine_tile(r, c);
                //reference to the array of tiles
                board[r][c] = tile;
                
                //give cacacteristics/properties to to the object
                //focus
                tile.setFocusable(false);
                //margin
                tile.setMargin(new Insets(0, 0, 0, 0));
                //set font for the text in the tiles
                tile.setFont(new Font("Arial Unicode MS", Font.PLAIN, 45));
                //tile.setText("1");
                //listens for user mouse click. Its too indented, so it is inside this function
                user_click(tile);
                //add the tiles to the board panel
                board_panel.add(tile);
            }
        }
        //set the frame of the window visible
        //window_frame.setVisible(true);

        //set mines function
        set_mines();
        
        //placing text below the board
        //set font for the text
        text_label2.setFont(new Font("Arial", Font.PLAIN, 25/*fontsize*/));
        //text that will be displayed
        text_label2.setText(" Mines: "+ Integer.toString(mine_count));
        //text_label2.setText("1 ");
        //transparency
        text_label2.setOpaque(true);
        //----text_label.setBackground(Color.black);----
        text_panel2.setLayout(new BorderLayout());
        
        //JTextField t_field = new JTextField(""+ Integer.toString(mine_count));
        t_field.setFont(new Font("Arial", Font.PLAIN, 20/*fontsize*/));
        t_field.setBounds(board_width-173, 0, 29, 26);
        text_panel2.add(t_field);

        JButton restart_button = new JButton ("Restart game");
        restart_button.setBounds(board_width-139,0,120,25);
        //restart_button.setBounds(1,0,120,25);
        //action
        restart_button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    mine_count = Integer.parseInt(t_field.getText());
                } catch (NumberFormatException nfe) {
                    t_field.setText("");
                    JOptionPane.showMessageDialog(window_frame, "Invalid text. Insert only numbers", "Error",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                mine_count = Integer.parseInt(t_field.getText());
                if(mine_count <= 0 || mine_count > (num_rows*num_collumns)){
                    //text_label.setText("There can't be any mines. Insert a valid value");
                    JOptionPane.showMessageDialog(window_frame, "There can't be any mines. You can insert 1 to "+ num_rows*num_collumns +" mines", "Error",JOptionPane.ERROR_MESSAGE);
                    //gameover = true;
                    return;
                }
                //in case game finished and user wants to restart
                text_label.setText("Minesweeper"/*+ Integer.toString(mine_count)*/);
                gameover = false;
                //text_label.setText("Restarted");
                //lay out the board again to reset the tiles
                board_panel.setLayout(new GridLayout(num_rows, num_collumns));
                //reset tiles
                for(int r = 0; r < num_rows; r++){
                    for(int c = 0; c < num_collumns; c++){
                        //Mine_tile tile = new Mine_tile(r, c);
                        //reference to the array of tiles
                        board[r][c].setText("");
                        board[r][c].setEnabled(true);
                    }
                }
                //reset tiles clicked
                tiles_clicked = 0;
                //set mines
                set_mines();
                text_label2.remove(t_field);
                text_label2.add(t_field);
                text_label2.setText(" Mines: " + Integer.toString(mine_count));
            }
        });
        
        text_panel2.add(text_label2);
        text_label2.add(restart_button);
        
        window_frame.add(text_panel2, BorderLayout.SOUTH);

        //set the frame of the window visible
        window_frame.setVisible(true);
    }

    //set mines in the tiles
    void set_mines(){
        mine_list = new ArrayList<Mine_tile>();
        //mine_list.add(board[1][1]);
        //mine_list.add (board[2][2]);
        int mines_left = mine_count;
        //while there are mines left, do:
        while(mines_left > 0){
            //get random numbers from 0-7
            int r = random_number.nextInt(num_rows);
            int c = random_number.nextInt(num_collumns);
            
            //use them as indexes for placing mines randomly
            Mine_tile tile = board[r][c];

            //if that mine list does not contain a mine already, do:
            if(!mine_list.contains(tile)){
                //add to the list of tiles that contains a mine
                mine_list.add(tile);
                //decrease ammount of mines left to be placed
                mines_left--;
            }
        }
    }

    //reveals all mines in the game
    void reveal_mines(){
        for(int i = 0; i < mine_list.size(); i++){
            Mine_tile tile = mine_list.get(i);
            tile.setText("B");//💣
            //ends game
            gameover = true;
            text_label.setText("Kabum!");
        }
        JOptionPane.showMessageDialog(window_frame, "You Lost!", "Gameover",JOptionPane.ERROR_MESSAGE);
    }

    //check for mines in the tiles, r and c are row and number of the tile
    void check_mine(int r, int c){
        //this and the next if and elses are to make this function call perform the recursive action call on the surrounding tiles correctly
        //check if the tile is out of bounds so it doesn't check recursively
        if(r < 0 || r >= num_rows || c < 0 || c >= num_collumns){
            return;
        }
        //get the tile that has been clicked
        Mine_tile tile = board[r][c];
        //if the tile has been clicked on, nothing will happen, the function will exit
        if(!tile.isEnabled()){
            return;
        }
        //disable the button so it is not pressed again
        tile.setEnabled(false);
        //count the tile
        tiles_clicked++;

        //set mines foud to 0
        int mines_found = 0;

        //check the surrouding tiles
        //the 3 tiles at the top
        mines_found += count_mine(r-1, c-1);//top left
        mines_found += count_mine(r-1, c);//top
        mines_found += count_mine(r-1, c+1);//top right

        //the two tiles on the side
        mines_found += count_mine(r, c-1);//left
        mines_found += count_mine(r, c+1);//right

        //the 3 tiles at the bottom
        mines_found += count_mine(r+1, c-1);//bottom left
        mines_found += count_mine(r+1, c);//bottom
        mines_found += count_mine(r+1, c+1);//bottom right

        //if mines have been found, return how many
        if(mines_found > 0){
            tile.setText(Integer.toString(mines_found));
        }
        //else, return nothing + check recursively the surrounding tiles that have not yet been clicked by the user
        else{
            tile.setText("");
            //check the surrouding tiles
            //the 3 tiles at the top
            check_mine(r-1, c);//top
            check_mine(r-1, c+1);//top right
            check_mine(r-1, c-1);//top left
            
            //the two tiles on the side
            check_mine(r, c-1);//left
            check_mine(r, c+1);//right

            //the 3 tiles at the bottom
            check_mine(r+1, c-1);//bottom left
            check_mine(r+1, c);//bottom
            check_mine(r+1, c+1);//bottom right
            }
        //if every tile except the ones conteining mines have been cleared, the user wins
        if(tiles_clicked == num_rows * num_collumns - mine_list.size()){
            gameover = true;
            text_label.setText("Mines cleared!");
            for(int i = 0; i < mine_list.size(); i++){
                Mine_tile tile2 = mine_list.get(i);
                tile2.setText("B"); //💣
            }
            JOptionPane.showMessageDialog(window_frame, "You Won!", "Gameover",JOptionPane.INFORMATION_MESSAGE);
            tiles_clicked = 0;
        }
    }

    //function to count the mines at a given row(r) and collumn(c)
    int count_mine(int r, int c){
        //check if there is rows out of bounds, return 0 if so
        if(r < 0 || r >= num_rows || c < 0 || c >= num_collumns){
            return 0;
        }
        //tile contains mine, in other words, the tile is in the list of tiles that has mine
        if(mine_list.contains(board[r][c])){
            return 1;
        }
        //otherwise, it does not have mines
        return 0;
    }

    //listens for user mouse click. Its too indented, so it is inside this function
    void user_click(Mine_tile tile){
        tile.addMouseListener(new MouseAdapter(){
            //action
            @Override
            public void mousePressed(MouseEvent e){
                //checks gameover control variable
                if(gameover){
                    return;
                }
                if_source(e);
            }
        });
    }
    void if_source(MouseEvent e){
        //get the source, what clicked on the tile
        Mine_tile tile = (Mine_tile) e.getSource();

        //if source is left click:
        if(e.getButton()== MouseEvent.BUTTON1){
            if(tile.getText() == ""){
                //gameover, a mine was in that tile
                if(mine_list.contains(tile)){
                    reveal_mines();
                }
                //else, check for mines
                else{
                    check_mine(tile.r, tile.c);
                }
            }
        }
        //if source is right click
        else if(e.getButton()== MouseEvent.BUTTON3){
            if(tile.getText() == "" && tile.isEnabled()){
                tile.setText("F"); //🚩
            }
            else if(tile.getText() == "F"){ //🚩
                tile.setText("");
            }
        }
    }
}

