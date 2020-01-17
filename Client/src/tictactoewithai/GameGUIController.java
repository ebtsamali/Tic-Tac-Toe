package tictactoewithai;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GameGUIController extends AI implements Initializable {
    public Button[][] buttons = new Button [3][3];
    @FXML
    private Button button_0_0,button_0_1,button_0_2,button_1_1,button_1_0,button_2_2,button_2_1,button_1_2,button_2_0,resetButton,difficultyButton;
    @FXML
    private Label playerScoreLabel,compScoreLabel;
   
    public Button[] guiButtons = new Button[2];
    
    public static int gameTurn = 0;
  

public void setButtonsArray(){
    buttons[0][0] = button_0_0;
    buttons[0][1] = button_0_1;
    buttons[0][2] = button_0_2;
    buttons[1][0] = button_1_0;
    buttons[1][1] = button_1_1;
    buttons[1][2] = button_1_2;
    buttons[2][0] = button_2_0;
    buttons[2][1] = button_2_1;
    buttons[2][2] = button_2_2;
    guiButtons[0] = resetButton;
    guiButtons[1] = difficultyButton;
}
@FXML
    private void minimizeWindow(ActionEvent event) {
        Stage windowStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        windowStage.setIconified(true);
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        System.exit(0);
    }
    
    @FXML 
    public void resetGameButtonAction(ActionEvent e){
        resetGame(buttons,board,guiButtons);
    }
    @FXML
    public void playerClick(javafx.event.ActionEvent event){
         Button btn = (Button) event.getSource();
         btn.setText("X");
         btn.setFocusTraversable(false);
         setBoardValue(btn,buttons);
         gameTurn++;
         compTurn(buttons, board, guiButtons, playerScoreLabel, compScoreLabel);
    }
    
   
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setButtonsArray();
        resetBoard(board);
    }    
}
