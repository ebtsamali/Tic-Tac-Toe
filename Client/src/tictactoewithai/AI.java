
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
                     char[] board1D = new char[9];
                     board1D[0] = board[0][0].charAt(0);
                     board1D[1] = board[0][1].charAt(0);
                     board1D[2] = board[0][2].charAt(0);
                     board1D[3] = board[1][0].charAt(0);
                     board1D[4] = board[1][1].charAt(0);
                     board1D[5] = board[1][2].charAt(0);
                     board1D[6] = board[2][0].charAt(0);
                     board1D[7] = board[2][1].charAt(0);
                     board1D[8] = board[2][2].charAt(0);
                     hardMode(board1D,board,buttons);
                     break;
             }
             GameGUIController.gameTurn++;
             if(hasWon(board)){
                 compScore+=50;
                 compScoreLabel.setText(String.valueOf(compScore));
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
    
    public int minimax(char[] board,int depth,char input){
         int score = evaluateBoard(board); 
  
        // If AI has won the game  
        // return evaluated score 
        if (score == 10) 
            return score-depth; 

        // If user has won the game  
        // return his/her evaluated score 
        if (score == -10) 
            return score+depth; 

        // If there are no more moves and  
        // no winner then it is a tie 
        if (isMovesLeft(board) == false) 
            return 0; 

        // If this AI's move 
         if (input == 'O'){ 
            int bestValue = 99999999;
		for(int i = 0 ; i < board.length; i++)
                {
                    if(board[i] != 'X' && board[i] != 'O'){
					char before = board[i];
					board[i] = 'O';
					int value = minimax(board,depth++,'X');
					bestValue = Math.min(bestValue, value);

					board[i] = before;
				}
			}
			return bestValue;
		}else{
			int bestValue = -99999999;
			for(int i = 0 ; i < board.length; i++){
				if(board[i] != 'X' && board[i] != 'O'){
					char before = board[i];
					board[i] = 'X';
					
					int value = minimax(board,depth++,'O');
					bestValue = Math.max(bestValue, value);

					board[i] = before;
				}
			}
			return bestValue;
		}
} 
  
    public void hardMode(char[] board,String[][] board2,Button[][] btn) {

   int bestMoveValues =  999999999;
		int bestMove = -1;
		
		for(int i = 0 ; i < board.length; i++){
			if(board[i] != 'X' && board[i] != 'O'){
				char before = board[i];
				board[i] = 'O';
				
				int bestValue = minimax(board,3,'X');
				
				board[i] = before;

				if(bestValue < bestMoveValues){
					bestMoveValues = bestValue;
					bestMove = i;
				}
			}
		}
        
	
                board2[bestMove/3][bestMove%3] = "O";
                btn[bestMove/3][bestMove%3].setStyle("-fx-text-fill:red;");
                btn[bestMove/3][bestMove%3].setText("O");
                btn[bestMove/3][bestMove%3].setDisable(true);
            
}  
    public void mediumMode(String[][] board,Button[][] buttons) {


    // check if you can take a win horizontally
    for(int i = 0; i<3; i++){

        if(board[0][i].equals(board[1][i]) && board[0][i].equals("O")){

            if(board[2][i] != "X" && board[2][i] != "O"){
                board[2][i] = "O";
                buttons[2][i].setStyle("-fx-text-fill:red;");
                buttons[2][i].setText("O");
                buttons[2][i].setDisable(true);
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
            return;
        }
    }

    if(board[2][2].equals(board[1][1]) && board[2][2].equals("O")){

        if(board[0][0] != "X" && board[0][0] != "O"){
            board[0][0] = "O";
            buttons[0][0].setStyle("-fx-text-fill:red;");
            buttons[0][0].setText("O");
            buttons[0][0].setDisable(true);
            return;
        }
    }

    if(board[0][0].equals(board[1][1]) && board[0][0].equals("O")){

        if(board[2][2] != "X" && board[2][2] != "O"){
            board[2][2] = "O";
            buttons[2][2].setStyle("-fx-text-fill:red;");
            buttons[2][2].setText("O");
            buttons[2][2].setDisable(true);
            return;
        }
    }

    if(board[0][2].equals(board[1][1]) && board[0][2].equals("O")){

        if(board[2][0] != "X" && board[2][0] != "O"){
            board[2][0] = "O";
            buttons[2][0].setStyle("-fx-text-fill:red;");
            buttons[2][0].setText("O");
            buttons[2][0].setDisable(true);
            return;
        }
    }

    if(board[2][0].equals(board[1][1]) && board[2][0].equals("O")){

        if(board[0][2] != "X" && board[0][2] != "O"){
            board[0][2] = "O";
            buttons[0][2].setStyle("-fx-text-fill:red;");
            buttons[0][2].setText("O");
            buttons[0][2].setDisable(true);
            return;
        }
    }


    // BLOCKS!!!! //

    easyMode(board,buttons);

}  
    
    public void easyMode(String[][] board,Button[][] buttons){
        // check if you can block a win horizontally
    for(int i = 0; i<3; i++){

        if(board[0][i].equals(board[1][i]) && board[0][i].equals("X")){
            if(board[2][i] != "O" && board[2][i] != "X"){
                board[2][i] = "O";
                buttons[2][i].setStyle("-fx-text-fill:red;");
                buttons[2][i].setText("O");
                buttons[2][i].setDisable(true);
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
            return;
        }
    }

    if(board[2][2].equals(board[1][1]) && board[2][2].equals("X")){

        if(board[0][0] != "O" && board[0][0] != "X"){
            board[0][0] = "O";
            buttons[0][0].setStyle("-fx-text-fill:red;");
            buttons[0][0].setText("O");
            buttons[0][0].setDisable(true);
            return;
        }
    }

    if(board[0][0].equals(board[1][1]) && board[0][0].equals("X")){
        if(board[2][2] != "O" && board[2][2] != "X"){
            board[2][2] = "O";
            buttons[2][2].setStyle("-fx-text-fill:red;");
            buttons[2][2].setText("O");
            buttons[2][2].setDisable(true);
            return;
        }
    }

    if(board[0][2].equals(board[1][1]) && board[0][2].equals("X")){

        if(board[2][0] != "O" && board[2][0] != "X"){
            board[2][0] = "O";
            buttons[2][0].setStyle("-fx-text-fill:red;");
            buttons[2][0].setText("O");
            buttons[2][0].setDisable(true);
            return;
        }
    }

    if(board[2][0].equals(board[1][1]) && board[2][0].equals("X")){

        if(board[0][2] != "O" && board[0][2] != "X"){
            board[0][2] = "O";
            buttons[0][2].setStyle("-fx-text-fill:red;");
            buttons[0][2].setText("O");
            buttons[0][2].setDisable(true);
            return;
        }
    }
  }
     static int evaluateBoard(char[] board) 
{ 
   if((board[0] == 'O' && board[1] == 'O' && board[2] == 'O') ||
		 	(board[3] == 'O' && board[4] == 'O' && board[5] == 'O') ||
		 	(board[6] == 'O' && board[7] == 'O' && board[8] == 'O') ||
		 	(board[0] == 'O' && board[3] == 'O' && board[6] == 'O') ||
		 	(board[1] == 'O' && board[4] == 'O' && board[7] == 'O') ||
		 	(board[2] == 'O' && board[5] == 'O' && board[8] == 'O') ||
		 	(board[0] == 'O' && board[4] == 'O' && board[8] == 'O') ||
		 	(board[2] == 'O' && board[4] == 'O' && board[6] == 'O')){
			return -(10);
		 }else if((board[0] == 'X' && board[1] == 'X' && board[2] == 'X') ||
			 	(board[3] == 'X' && board[4] == 'X' && board[5] == 'X') ||
			 	(board[6] == 'X' && board[7] == 'X' && board[8] == 'X') ||
			 	(board[0] == 'X' && board[3] == 'X' && board[6] == 'X') ||
			 	(board[1] == 'X' && board[4] == 'X' && board[7] == 'X') ||
			 	(board[2] == 'X' && board[5] == 'X' && board[8] == 'X') ||
			 	(board[0] == 'X' && board[4] == 'X' && board[8] == 'X') ||
			 	(board[2] == 'X' && board[4] == 'X' && board[6] == 'X')){
			return 10;
		 }{
			 return 0;
		 }
} 
    static Boolean isMovesLeft(char[] board) 
{ 
    for (int i = 0; i < board.length; i++) {
        
            if (board[i] != 'X' && board[i] != 'O') 
                return true; 
    }
    return false; 

}
}
