/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multiPlayerMode;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author Dell
 */
public class GameGUIController extends Client implements Initializable {
//    Client client = new Client();
    
    @FXML
    private Button btn0;
    @FXML
    private Button btn1;
    @FXML
    private Button btn2;
    @FXML
    private Button btn3;
    @FXML
    private Button btn4;
    @FXML
    private Button btn5;
    @FXML
    private Button btn6;
    @FXML
    private Button btn7;
    @FXML
    private Button btn8;
     public static Button[] buttons = new Button[9];
    @FXML
    private Button XOplayer;
    public static Button sXOplayer;
    
    @FXML
    private Button Messages;
    public static Button sMessages;
    
    
    public void setButtonsArray(){
        buttons[0] = btn0;
        buttons[1] = btn1;
        buttons[2] = btn2;
        buttons[3] = btn3;
        buttons[4] = btn4;
        buttons[5] = btn5;
        buttons[6] = btn6;
        buttons[7] = btn7;
        buttons[8] = btn8;
      
    }
    @FXML
    public void playerClick(javafx.event.ActionEvent e){
        Button clickedButton = (Button) e.getSource();
        sendButtonNumber(clickedButton,buttons);
        
    }
    

   

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sXOplayer=XOplayer;
        sMessages=Messages;
        setButtonsArray();

    }

}
