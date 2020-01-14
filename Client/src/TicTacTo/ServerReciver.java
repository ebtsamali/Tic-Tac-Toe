/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TicTacTo;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

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
                if (askForGame(message.get("senderUsername").getAsString())) {
                    
                }else{
                
                }
                break;
        }
    }

    boolean askForGame(String senderUsername) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("game Requet");
                alert.setHeaderText(null);
                alert.setContentText(senderUsername + " wants to play with you");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    choise = true;
                } else {
                    choise = false;
                }
            }
        });
        return choise;
    }
}
