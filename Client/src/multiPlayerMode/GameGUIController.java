/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multiPlayerMode;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

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
    @FXML
    private Button btnSaveGame;
    
    
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
    
     @FXML
    private void notificationPopup(ActionEvent event){
        //Image img = new Image("../images/hello.png");
        Notifications notificationBuilder = Notifications.create()
                .title("HELLO")
                .text("TIC TAC TOE")
                .graphic(null)
                .hideAfter(Duration.seconds(5))
                .position(Pos.BOTTOM_RIGHT);
                
        notificationBuilder.darkStyle();
        notificationBuilder.show();   
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
    

   

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sXOplayer=XOplayer;
        sMessages=Messages;
        setButtonsArray();

    }

    @FXML
    private void saveGame(ActionEvent event) {
        try {
            JsonObject obj = new JsonObject();
            obj.addProperty("btn0", btn0.getText());
            obj.addProperty("btn1", btn1.getText());
            obj.addProperty("btn2", btn2.getText());
            obj.addProperty("btn3", btn3.getText());
            obj.addProperty("btn4", btn4.getText());
            obj.addProperty("btn5", btn5.getText());
            obj.addProperty("btn6", btn6.getText());
            obj.addProperty("btn7", btn7.getText());
            obj.addProperty("btn8", btn8.getText());

            //ps.println(obj.toString()); //will managed

        } catch (Exception ex) {
            System.out.println("error in sending saved game to server");
        }
    }
   /*
    private void handleretrieveGame(ActionEvent event) throws IOException {
        rGame = dis.readLine();
        JsonObject retrieveGame = new JsonParser().parse(rGame).getAsJsonObject();

//            obj.addProperty("type", 2);
        btn1.setText(retrieveGame.get("btn1").getAsString());
        btn2.setText(retrieveGame.get("btn2").getAsString());
        btn3.setText(retrieveGame.get("btn3").getAsString());
        btn4.setText(retrieveGame.get("btn4").getAsString());
        btn5.setText(retrieveGame.get("btn5").getAsString());
        btn6.setText(retrieveGame.get("btn6").getAsString());
        btn7.setText(retrieveGame.get("btn7").getAsString());
        btn8.setText(retrieveGame.get("btn8").getAsString());
        btn9.setText(retrieveGame.get("btn9").getAsString());
    }
*/ //will managed

}
