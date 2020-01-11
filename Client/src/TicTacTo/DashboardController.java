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
import jdk.nashorn.internal.parser.JSONParser;
/**
 * FXML Controller class
 *
 * @author mena
 */
public class DashboardController implements Initializable {

    @FXML
    private FlowPane onlineUserPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            String players = new DataInputStream(SignInController.player.playerSocket.getInputStream()).readLine();
            JsonArray playerArray = new JsonParser().parse(players).getAsJsonArray();
            
            List<Button> buttonlist = new ArrayList<>();
            for (int i = 0; i < playerArray.size(); i++) {
                JsonObject otherPlayer= new JsonParser().parse(playerArray.get(i).toString()).getAsJsonObject();
                buttonlist.add(new Button(otherPlayer.get("username").getAsString()));
            }
            onlineUserPane.getChildren().addAll(buttonlist);
        } catch (Exception ex) {
            System.out.println("error in getting all users");;
        }
    }

    @FXML
    private void loadSinglePlayerWindow(ActionEvent event) {
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

}
