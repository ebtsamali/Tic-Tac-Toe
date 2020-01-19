/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TicTacTo;

import static TicTacTo.SignInController.player;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
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
import javafx.stage.Stage;
import java.util.ArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jfoenix.controls.JFXButton;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author mena
 */
public class DashboardController implements Initializable {

    @FXML
    private VBox onlineUserPane;
    public static VBox sonlineUserPane;
    public static String otherPlayrName = "";
    ServerReciver reciver;
    @FXML
    private VBox onlineUserPane1;
    public static VBox sonlineUserPane1;
    @FXML
    private Label scoreLabel;
    @FXML
    private Button Uname;
    @FXML
    private VBox onlineUserPane11;
    public static VBox sonlineUserPane11;

    int mybuttonIndex;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sonlineUserPane = onlineUserPane;
        sonlineUserPane1 = onlineUserPane1;
        sonlineUserPane11 = onlineUserPane11;
        reciver = new ServerReciver();
        SignInController.player.setPlayerthread(reciver);
        try {
            String players = new BufferedReader(new InputStreamReader(SignInController.player.getPlayerSocket().getInputStream())).readLine();
            JsonArray playerArray = new JsonParser().parse(players).getAsJsonArray();

            List<Button> buttonlist = new ArrayList<>();
            List<Button> buttonlist1 = new ArrayList<>();
            List<Button> buttonlist11 = new ArrayList<>();
            for (int i = 0; i < playerArray.size(); i++) {
                JsonObject otherPlayer = new JsonParser().parse(playerArray.get(i).toString()).getAsJsonObject();
                if (otherPlayer.get("username").getAsString().equals(player.getPlayerUserName())) {
                    player.setPlayerScore(otherPlayer.get("score").getAsInt());
                }else{
                JFXButton playerButton = new JFXButton(otherPlayer.get("username").getAsString());
                playerButton.setStyle("-fx-background-color : darkblue;" + "-fx-opacity: 0.6;" + "-fx-text-fill: #ffffff;" + "-fx-font-family: Verdana");
                playerButton.setMaxWidth(163);
                playerButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        try {
                            String username = otherPlayer.get("username").getAsString();
                            otherPlayrName=username;
                            new PrintStream(SignInController.player.getPlayerSocket().getOutputStream()).println(createInvite(SignInController.player.getPlayerUserName(), username));
                            System.out.println("invitation sent");
                        } catch (IOException ex) {
                            System.out.println("error in sendding invitation");
                        }
                    }
                });
                if (otherPlayer.get("score").getAsInt() >= 10000) {
                    buttonlist.add(playerButton);
                } else if (otherPlayer.get("score").getAsInt() >= 1000) {
                    buttonlist1.add(playerButton);
                } else {
                    buttonlist11.add(playerButton);
                }
            }
            }
            onlineUserPane.getChildren().addAll(buttonlist);
            onlineUserPane1.getChildren().addAll(buttonlist1);
            onlineUserPane11.getChildren().addAll(buttonlist11);
            onlineUserPane.setAlignment(Pos.TOP_CENTER);
        } catch (Exception ex) {
            System.out.println("error in getting all users");;
        }
        scoreLabel.setText(player.getPlayerScore() + "");
        Uname.setText(player.getPlayerUserName());
        reciver.start();
    }

    @FXML
    private void loadSinglePlayerWindow(ActionEvent event
    ) {
        Parent newParent;
        try {
            newParent = FXMLLoader.load(getClass().getResource("fxml/singlePlayerGameGUI.fxml"));
            Scene newScene = new Scene(newParent, 800, 550);
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
        System.exit(0);
    }

    String createInvite(String sender, String reciver) {
        JsonObject invite = new JsonObject();
        invite.addProperty("type", "invite");
        invite.addProperty("senderUsername", sender);
        invite.addProperty("reciverUsername", reciver);
        return invite.toString();
    }
}
