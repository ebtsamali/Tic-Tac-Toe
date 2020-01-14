/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TicTacTo;

import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import jdk.nashorn.internal.parser.JSONParser;

/**
 * FXML Controller class
 *
 * @author mena
 */
public class DashboardController implements Initializable {

    @FXML
    private VBox onlineUserPane;
    ServerReciver reciver ;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
            reciver = new ServerReciver();

            reciver.start();
            try {
                String players = new DataInputStream(SignInController.player.getPlayerSocket().getInputStream()).readLine();
                JsonArray playerArray = new JsonParser().parse(players).getAsJsonArray();

                List<Button> buttonlist = new ArrayList<>();
                for (int i = 0; i < playerArray.size(); i++) {
                    JsonObject otherPlayer = new JsonParser().parse(playerArray.get(i).toString()).getAsJsonObject();
                    buttonlist.add(new Button(otherPlayer.get("username").getAsString()));
                    buttonlist.get(i).setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            try {
                                String username = otherPlayer.get("username").getAsString();
                                reciver.suspend();
                                new PrintStream(SignInController.player.getPlayerSocket().getOutputStream()).println(createInvite(SignInController.player.getPlayerUserName(), username));
                                reciver.resume();
                                System.out.println("invitation sent");
                            } catch (IOException ex) {
                                System.out.println("error in sendding invitation");
                            }
                        }
                    });
                }
                onlineUserPane.getChildren().addAll(buttonlist);
            } catch (Exception ex) {
                System.out.println("error in getting all users");;
            }
    }

    @FXML
    private void loadSinglePlayerWindow(ActionEvent event
    ) {
        Parent newParent;
        try {
            newParent = FXMLLoader.load(getClass().getResource("fxml/GameGUI.fxml"));
            Scene newScene = new Scene(newParent, 730, 500);
            Stage windowStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            windowStage.setScene(newScene);
            windowStage.show();
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void minimizeWindow(MouseEvent event) {
        Stage windowStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        windowStage.setIconified(true);
    }

    @FXML
    private void closeWindow(MouseEvent event) {
        Platform.exit();
    }

    String createInvite(String sender, String reciver) {
        JsonObject invite = new JsonObject();
        invite.addProperty("type", "invite");
        invite.addProperty("senderUsername", sender);
        invite.addProperty("reciverUsername", reciver);
        return invite.toString();
    }

    @FXML
    public void loadMultiPlayerWindow(ActionEvent event){
        Parent newParent;
        try {
            newParent = FXMLLoader.load(getClass().getResource("fxml/multiPlayerGameGUI.fxml"));
            Scene newScene = new Scene(newParent, 800, 550);
            Stage windowStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            windowStage.setScene(newScene);
            windowStage.show();
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
