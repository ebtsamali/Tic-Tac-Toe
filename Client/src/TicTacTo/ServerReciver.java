/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TicTacTo;

import static TicTacTo.SignInController.mainStage;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 *
 * @author mena
 */
public class ServerReciver extends Thread {

    boolean choise = false;

    @Override
    public void run() {
        while (true) {
            try {
                String message = new DataInputStream(SignInController.player.getPlayerSocket().getInputStream()).readLine();
                System.out.println(message);
                makeAction(message);
            } catch (Exception ex) {
                System.out.println("error in reciving data in client thread");;
            }
        }
    }

    void makeAction(String msg) {
        JsonObject message = new JsonParser().parse((msg)).getAsJsonObject();
        switch (message.get("type").getAsString()) {
            case "invite":
                askForGame(message);
                break;
            case "openGame":
                switchToMultiPlayer();
                break;
        }
    }

    void askForGame(JsonObject message) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("game Requet");
                alert.setHeaderText(null);
                alert.setContentText(message.get("senderUsername").getAsString() + " wants to play with you");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    try {
                        new PrintStream(SignInController.player.getPlayerSocket().getOutputStream()).println("{type:accept,result:true,senderUsername:" + message.get("senderUsername") + ",reciverUsername:" + message.get("reciverUsername") + "}");
                    } catch (IOException ex) {
                        System.out.println("error in accept invitation");
                    }
                } else {
                    try {
                        new PrintStream(SignInController.player.getPlayerSocket().getOutputStream()).println("{type:accept,result:false,senderUsername:" + message.get("senderUsername") + ",reciverUsername:" + message.get("reciverUsername") + "}");
                    } catch (IOException ex) {
                        System.out.println("error in accept invitation");
                    }
                }
            }
        });
    }

    private void switchToMultiPlayer() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Parent newParent;
                try {
                    newParent = FXMLLoader.load(getClass().getResource("fxml/multiPlayerGameGUI.fxml"));
                    Scene newScene = new Scene(newParent, 800, 550);
                    Stage windowStage = (Stage) mainStage;
                    windowStage.setScene(newScene);
                    windowStage.show();
                } catch (Exception ex) {
                    System.out.println("cant switch to multiplayer "+ ex);;
                }
            }
        });
    }
}
