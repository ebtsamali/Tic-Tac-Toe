
package tictactoewithai;

import java.io.PrintStream;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class AI {
    public String[][] board = new String [3][3];
    int playerScore = 0;
    int compScore = 0;
    String Difficulty = "easy";
    
    public void setBoardValue(Button b, Button[][] buttons){
         for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if (b == (buttons[i][j])){
                board[i][j] = b.getText();
                }
            }
         }
    }
    public void resetGame(Button[][] buttons,String[][] board,Button[] guiButtons){
    for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                buttons[i][j].setDisable(false);
                buttons[i][j].setText(" ");
                buttons[i][j].setStyle("-fx-text-fill:blue");
                board[i][j] = null;
            }
            }
    resetBoard(board);
    enableGuiButtons(guiButtons);
    GameGUIController.gameTurn = 0;
}
     public void resetBoard(String[][] board){
        int x = 0;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if (board[i][j] == null)
                {
                    board[i][j] = " "+x;
                    x++;
                }
            }
        }
    }
     public void disableAllButtons(Button[][] buttons){
         for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                buttons[i][j].setDisable(true);
            }
            }
         }
    public void disableGuiButtons(Button[] guiButtons){
       guiButtons[0].setDisable(true);
       guiButtons[1].setDisable(true);
    }
    public void enableGuiButtons(Button[] guiButtons){
       guiButtons[0].setDisable(false);
       guiButtons[1].setDisable(false);
    }
    
    public void saveScoreInDatabase(PrintStream out, int score){
        out.println(String.valueOf(score));
    }
    
    public void changeDifficulty(){
    ButtonType easyOption = new ButtonType("Easy");
    ButtonType mediumOption = new ButtonType("Medium");
    ButtonType hardOption = new ButtonType("Hard");
    Alert diff = new Alert(Alert.AlertType.CONFIRMATION, "Please choose game difficulty", easyOption, mediumOption, hardOption);
    diff.setHeaderText(null);
    diff.setTitle("Difficulty");
    Optional<ButtonType> result = diff.showAndWait();
                if (result.get() == easyOption){
                    Difficulty = "easy";
                }else if(result.get() == mediumOption){
                    Difficulty = "medium";
                }else if(result.get() == hardOption){
                    Difficulty = "hard";
                }
    }
    public void compTurn(Button[][] buttons,String[][] board,Button[] guiButtons, Label playerScoreLabel, Label compScoreLabel){
        
         if(GameGUIController.gameTurn == 9){
             enableGuiButtons(guiButtons);
         Alert draw = new Alert(Alert.AlertType.INFORMATION, "Game Draw! \n Do you want to paly again?", ButtonType.YES,ButtonType.NO);
         draw.setHeaderText(null);
         draw.setTitle("You were Close!");
         Optional<ButtonType> result = draw.showAndWait();
                if (result.get() == ButtonType.YES){
                    resetGame(buttons,board,guiButtons);
                }
                else{
                    disableAllButtons(buttons);
                }
         }
         else if(hasWon(board)){
             playerScore+=50;
             playerScoreLabel.setText(String.valueOf(playerScore));
             //saveScoreInDatabase(out, playerScore);
             enableGuiButtons(guiButtons);
            Alert youWon = new Alert(Alert.AlertType.CONFIRMATION, "you Won! \n Do you want to paly again?", ButtonType.YES,ButtonType.NO);
              youWon.setHeaderText(null);
              youWon.setTitle("Congratulations!");
            Optional<ButtonType> result = youWon.showAndWait();
                if (result.get() == ButtonType.YES){
                    resetGame(buttons,board,guiButtons);
                }
                else{
                    disableAllButtons(buttons);
                }
         }
         else if(!hasWon(board)){
             switch(Difficulty){
                 case "easy":
                     easyMode(board,buttons);
                     break;
                 case "medium":
                     mediumMode(board,buttons);
                     break;
                 case "hard":
                     hardMode(board,buttons);
                     break;
             }
             GameGUIController.gameTurn++;
             if(hasWon(board)){
                 compScore+=50;
                 compScoreLabel.setText(String.valueOf(compScore));
                 //saveScoreInDatabase(out, compScore);
                 enableGuiButtons(guiButtons);
                Alert computerWon = new Alert(Alert.AlertType.CONFIRMATION, "Computer  Won!  \n Do you want to paly again?", ButtonType.YES,ButtonType.NO);
                computerWon.setHeaderText(null);
                computerWon.setTitle("Hard Luck!");
                Optional<ButtonType> result = computerWon.showAndWait();
                if (result.get() == ButtonType.YES){
                    resetGame(buttons,board,guiButtons);
                }
                else{
                    disableAllButtons(buttons);
                }
             }
         } 
    }
    public boolean hasWon (String[][] board){
        //horizontal 
        for(int i = 0; i<3; i++){
            if(board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2])){
                return true;
            }
        }
        //vertical
        for(int i = 0; i<3; i++){
            if(board [0][i].equals(board[1][i]) && board[1][i].equals(board[2][i])){
                return true;
            }
        }
        //diagonal
        if(board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2]) || board[2][0].equals(board[1][1]) && board[1][1].equals(board[0][2]))
            return true;
        return false;
    }
    public void hardMode(String[][] board,Button[][] buttons) {

    boolean mademove = false;

    // check if you can take a win horizontally
    for(int i = 0; i<3; i++){

        if(board[0][i].equals(board[1][i]) && board[0][i].equals("O")){

            if(board[2][i] != "X" && board[2][i] != "O"){
                board[2][i] = "O";
                buttons[2][i].setStyle("-fx-text-fill:red;");
                buttons[2][i].setText("O");
                buttons[2][i].setDisable(true);
                mademove = true;
                return;
            }

        }

    }

    for(int i = 0; i<3; i++){

        if(board[2][i].equals(board[1][i]) && board[2][i].equals("O")){

            if(board[0][i] != "X" && board[0][i] != "O"){
                board[0][i] = "O";
                buttons[0][i].setStyle("-fx-text-fill:red;");
                buttons[0][i].setText("O");
                buttons[0][i].setDisable(true);
                mademove = true;
                return;
            }

        }


    }



    // check if you can take a win horizontally
    for(int i = 0; i<3; i++){

        if(board[i][0].equals(board[i][1]) && board[i][0].equals("O")){

            if(board[i][2] != "X" && board[i][2] != "O"){
                board[i][2] = "O";
                buttons[i][2].setStyle("-fx-text-fill:red;");
                buttons[i][2].setText("O");
                buttons[i][2].setDisable(true);
                mademove = true;
                return;
            }

        }

    }

    for(int i = 0; i<3; i++){

        if(board[i][2].equals(board[i][1]) && board[i][2].equals("O")){

            if(board[i][0] != "X" && board[i][0] != "O"){
                board[i][0] = "O";
                buttons[i][0].setStyle("-fx-text-fill:red;");
                buttons[i][0].setText("O");
                buttons[i][0].setDisable(true);
                mademove = true;
                return;
            }

        }

    }


    // check if you can take a win diagonally bottom


    if(board[0][0].equals(board[1][1]) && board[0][0].equals("O")){

        if(board[2][2] != "X" && board[2][2] != "O"){
            board[2][2] = "O";
            buttons[2][2].setStyle("-fx-text-fill:red;");
            buttons[2][2].setText("O");
            buttons[2][2].setDisable(true);
            mademove = true;
            return;
        }
    }

    if(board[2][2].equals(board[1][1]) && board[2][2].equals("O")){

        if(board[0][0] != "X" && board[0][0] != "O"){
            board[0][0] = "O";
            buttons[0][0].setStyle("-fx-text-fill:red;");
            buttons[0][0].setText("O");
            buttons[0][0].setDisable(true);
            mademove = true;
            return;
        }
    }

    if(board[0][0].equals(board[1][1]) && board[0][0].equals("O")){

        if(board[2][2] != "X" && board[2][2] != "O"){
            board[2][2] = "O";
            buttons[2][2].setStyle("-fx-text-fill:red;");
            buttons[2][2].setText("O");
            buttons[2][2].setDisable(true);
            mademove = true;
            return;
        }
    }

    if(board[0][2].equals(board[1][1]) && board[0][2].equals("O")){

        if(board[2][0] != "X" && board[2][0] != "O"){
            board[2][0] = "O";
            buttons[2][0].setStyle("-fx-text-fill:red;");
            buttons[2][0].setText("O");
            buttons[2][0].setDisable(true);
            mademove = true;
            return;
        }
    }

    if(board[2][0].equals(board[1][1]) && board[2][0].equals("O")){

        if(board[0][2] != "X" && board[0][2] != "O"){
            board[0][2] = "O";
            buttons[0][2].setStyle("-fx-text-fill:red;");
            buttons[0][2].setText("O");
            buttons[0][2].setDisable(true);
            mademove = true;
            return;
        }
    }


    // BLOCKS!!!! //

    mediumMode(board,buttons);

}  
    public void easyMode(String[][] board,Button[][] buttons){
      boolean mademove = false;
        while(!mademove){

        int rand1 = (int) (Math.random() * 3);
        int rand2 = (int) (Math.random() * 3);

        if(board[rand1][rand2] != "X" && board[rand1][rand2] != "O"){
            board[rand1][rand2] = "O";
            buttons[rand1][rand2].setStyle("-fx-text-fill:red;");
            buttons[rand1][rand2].setText("O");
            buttons[rand1][rand2].setDisable(true);
            mademove = true;        

        }
      }
    }
    public void mediumMode(String[][] board,Button[][] buttons){
        boolean mademove = false;
        // check if you can block a win horizontally
    for(int i = 0; i<3; i++){

        if(board[0][i].equals(board[1][i]) && board[0][i].equals("X")){
            if(board[2][i] != "O" && board[2][i] != "X"){
                board[2][i] = "O";
                buttons[2][i].setStyle("-fx-text-fill:red;");
                buttons[2][i].setText("O");
                buttons[2][i].setDisable(true);
                mademove = true;
                return;
            }

        }

    }

    for(int i = 0; i<3; i++){

        if(board[2][i].equals(board[1][i]) && board[0][i].equals("X")){

            if(board[0][i] != "O" && board[0][i] != "X"){
                board[0][i] = "O";
                buttons[0][i].setStyle("-fx-text-fill:red;");
                buttons[0][i].setText("O");
                buttons[0][i].setDisable(true);
                mademove = true;
                return;
            }

        }


    }

    // check if you can block a win vertically


    for(int i = 0; i<3; i++){

        if(board[i][0].equals(board[i][1]) && board[i][0].equals("X")){

            if(board[i][2] != "O" && board[i][2] != "X"){
                board[i][2] = "O";
                buttons[i][2].setStyle("-fx-text-fill:red;");
                buttons[i][2].setText("O");
                buttons[i][2].setDisable(true);
                mademove = true;
                return;
            }

        }

    }

    for(int i = 0; i<3; i++){

        if(board[i][2].equals(board[i][1]) && board[i][2].equals("X")){

            if(board[i][0] != "O" && board[i][0] != "X"){
                board[i][0] = "O";
                buttons[i][0].setStyle("-fx-text-fill:red;");
                buttons[i][0].setText("O");
                buttons[i][0].setDisable(true);
                mademove = true;
                return;
            }

        }

    }

    for(int i = 0; i<3; i++){

        if(board[2][i].equals(board[1][i]) && board[2][i].equals("X")){

            if(board[0][i] != "O" && board[0][i] != "X"){
                board[0][i] = "O";
                buttons[0][i].setStyle("-fx-text-fill:red;");
                buttons[0][i].setText("O");
                buttons[0][i].setDisable(true);
                mademove = true;
                return;
            }

        }

    }



    // check if you can block a win diagonally 


    if(board[0][0].equals(board[1][1]) && board[0][0].equals("X")){

        if(board[2][2] != "O" && board[2][2] != "X"){
            board[2][2] = "O";
            buttons[2][2].setStyle("-fx-text-fill:red;");
            buttons[2][2].setText("O");
            buttons[2][2].setDisable(true);
            mademove = true;
            return;
        }
    }

    if(board[2][2].equals(board[1][1]) && board[2][2].equals("X")){

        if(board[0][0] != "O" && board[0][0] != "X"){
            board[0][0] = "O";
            buttons[0][0].setStyle("-fx-text-fill:red;");
            buttons[0][0].setText("O");
            buttons[0][0].setDisable(true);
            mademove = true;
            return;
        }
    }

    if(board[0][0].equals(board[1][1]) && board[0][0].equals("X")){
        if(board[2][2] != "O" && board[2][2] != "X"){
            board[2][2] = "O";
            buttons[2][2].setStyle("-fx-text-fill:red;");
            buttons[2][2].setText("O");
            buttons[2][2].setDisable(true);
            mademove = true;
            return;
        }
    }

    if(board[0][2].equals(board[1][1]) && board[0][2].equals("X")){

        if(board[2][0] != "O" && board[2][0] != "X"){
            board[2][0] = "O";
            buttons[2][0].setStyle("-fx-text-fill:red;");
            buttons[2][0].setText("O");
            buttons[2][0].setDisable(true);
            mademove = true;
            return;
        }
    }

    if(board[2][0].equals(board[1][1]) && board[2][0].equals("X")){

        if(board[0][2] != "O" && board[0][2] != "X"){
            board[0][2] = "O";
            buttons[0][2].setStyle("-fx-text-fill:red;");
            buttons[0][2].setText("O");
            buttons[0][2].setDisable(true);
            mademove = true;
            return;
        }
    }




    // make random move if above rules dont apply
        easyMode(board,buttons);

    }
}
