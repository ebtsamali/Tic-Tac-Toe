/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TicTacTo;

import static TicTacTo.DashboardController.otherPlayrName;
import static TicTacTo.ServerReciver.sign;
import static TicTacTo.SignInController.mainStage;
import static TicTacTo.SignInController.player;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author Dell
 */
public class MultiPlayerGUIController implements Initializable {
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

    public static int gameID = 0;

    @FXML
    private JFXTextArea chatTa;
    public static JFXTextArea schatTa;
    @FXML
    private JFXTextField chatTf;
    @FXML
    private TextField scoreLbl;
    @FXML
    private Label myUsernameLbl;
    @FXML
    private Label otherUsernameLbl;

    public void setButtonsArray() {
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
    public void playerClick(javafx.event.ActionEvent e) {
        try {
            Button clickedButton = (Button) e.getSource();
            int pressedButton = sendButtonNumber(clickedButton, buttons);
            new PrintStream(player.getPlayerSocket().getOutputStream()).println("{type:move,gameId:" + gameID + ",place:" + pressedButton + "}");
            clickedButton.setText(XOplayer.getText());
            clickedButton.setTextFill(Color.WHITE);
            disableall();
        } catch (Exception ex) {
            System.out.println("error in sending move to server");
        }
    }

    @FXML
    private void minimizeWindow(ActionEvent event) {
        Stage windowStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        windowStage.setIconified(true);
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        try {
            player.getPlayerthread().stop();
            new PrintStream(player.getPlayerSocket().getOutputStream()).println("{type:online}");
            Parent newParent = FXMLLoader.load(getClass().getResource("fxml/dashboard.fxml"));
            Scene newScene = new Scene(newParent, 800, 550);
            mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Stage windowStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            windowStage.setScene(newScene);
            windowStage.show();
        } catch (Exception ex) {
            System.out.println("error in switching to dash board" + ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sXOplayer = XOplayer;
        sMessages = Messages;
        schatTa = chatTa;
        setButtonsArray();
        System.out.println(sign);
        if (sign.equals("O")) {
            disableall();
        }
        myUsernameLbl.setText(player.getPlayerUserName());
        otherUsernameLbl.setText(otherPlayrName);
        scoreLbl.setText(player.getPlayerScore() + "");
    }

    @FXML
    private void saveGame(ActionEvent event) {
        try {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "gameSave");
            if (sXOplayer.equals("X")) {
                obj.addProperty("user1", player.getPlayerUserName());
                obj.addProperty("user2", otherPlayrName);
            } else {
                obj.addProperty("user1", otherPlayrName);
                obj.addProperty("user2", player.getPlayerUserName());
            }
            obj.addProperty("btn0", btn0.getText());
            obj.addProperty("btn1", btn1.getText());
            obj.addProperty("btn2", btn2.getText());
            obj.addProperty("btn3", btn3.getText());
            obj.addProperty("btn4", btn4.getText());
            obj.addProperty("btn5", btn5.getText());
            obj.addProperty("btn6", btn6.getText());
            obj.addProperty("btn7", btn7.getText());
            obj.addProperty("btn8", btn8.getText());
            obj.addProperty("gameId", gameID);
            new PrintStream(player.getPlayerSocket().getOutputStream()).println(obj.toString());
            closeWindow(event);
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
    public int sendButtonNumber(Button b, Button[] buttons) {
        for (int i = 0; i < buttons.length; i++) {
            if (b == buttons[i]) {
                return i;
            }
        }
        return 0;
    }

    public static void disableall() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setDisable(true);
            sMessages.setText("waitting for other player");
        }
    }

    public static void enableall() {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].getText().isEmpty()) {
                buttons[i].setDisable(false);
            } else {
                buttons[i].setDisable(true);
            }
            sMessages.setText("your turn");
        }
    }

    @FXML
    private void SendChat(ActionEvent event) {
        try {
            if (chatTf.getText().length() != 0) {
                chatTa.setText(chatTa.getText() + "you: " + chatTf.getText() + "\n");
                new PrintStream(player.getPlayerSocket().getOutputStream()).println("{type:message,message:\" " + chatTf.getText() + "\",gameId:" + gameID + "}");
                chatTf.setText("");
            }
        } catch (IOException ex) {
            System.out.println("error in send chat");
        }
    }
}
