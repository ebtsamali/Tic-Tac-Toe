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
import jdk.nashorn.internal.parser.JSONParser;

/**
 * FXML Controller class
 *
 * @author mena
 */
public class DashboardController implements Initializable {

    @FXML
    private FlowPane onlineUserPane;
    Invite invite;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            String players = new DataInputStream(SignInController.player.playerSocket.getInputStream()).readLine();
            JsonArray playerArray = new JsonParser().parse(players).getAsJsonArray();

            List<Button> buttonlist = new ArrayList<>();
            for (int i = 0; i < playerArray.size(); i++) {
                JsonObject otherPlayer = new JsonParser().parse(playerArray.get(i).toString()).getAsJsonObject();
                buttonlist.add(new Button(otherPlayer.get("username").getAsString()));
                buttonlist.get(i).setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        System.out.println("nour clicked");
                        String pl1 = otherPlayer.get("username").getAsString();
                        System.out.println(pl1);
                        invite.printStream.println("1,"+SignInController.player.playerUserName+","+pl1);   // request("1,senderName(userName),receiverName(who wanted play with)"), nour will be changed
                        invite.printStream.flush();
                    }
                });
            }
                onlineUserPane.getChildren().addAll(buttonlist);
            }catch (Exception ex) {
            System.out.println("error in getting all users");;
        }
        }

        @FXML
        private void loadSinglePlayerWindow
        (ActionEvent event
        
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
        
        
        public class Invite{
        public Socket playerSocket;
        public BufferedReader buffer;
        public PrintStream printStream;
        public Thread thread;
        public String serverRespond;

        public Invite() {
            try {
                playerSocket = new Socket("127.0.0.1", 5000);
                buffer = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
                printStream = new PrintStream(playerSocket.getOutputStream());

                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (true){
                                serverRespond = buffer.readLine();
                                System.out.println(serverRespond);
                                String[] serverRespondArr = serverRespond.split(",");
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (serverRespondArr[0].equals("invitation sent")){
                                            confirmSendingInvitation();
                                        }

                                        else if(serverRespondArr[0].equals("invitation")){
                                            Optional<ButtonType> response = invitationAlert(serverRespondArr[1]);
                                            if (response.get() == ButtonType.OK){
                                                agree(serverRespondArr[1]);
                                            }
                                            else if (response.get() == ButtonType.CANCEL){
                                                refuse(serverRespondArr[1]);
                                            }
                                        }

                                        else if (serverRespondArr[0].equals("Not Allowed")){
                                            notAllowed();
                                        }

                                        else if(serverRespondArr[0].equals("game will start")){
                                            acceptInvitation();
                                            System.out.println("accept");
                                        }

                                        else if (serverRespondArr[0].equals("refuse")){
                                            refuseInvitation(serverRespondArr[1]);
                                            System.out.println("refuse");
                                        }
                                    }
                                });
                            }
                        }
                        catch (IOException e){
                            e.getMessage();
                        }
                    }
                });
                thread.start();
            }
            catch (IOException e){
                e.getMessage();
            }
        }

        public void notAllowed(){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invitation did not be Sent");
            alert.setHeaderText(null);
            alert.setContentText("Sorry, Not Available to play");
            alert.showAndWait();
        }

        public Optional<ButtonType> invitationAlert(String invitationSender){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Invitation");
            alert.setHeaderText(invitationSender + " want to play with you ");
            alert.setContentText("Are you accept playing with "+ invitationSender +"?");
            Optional<ButtonType> response = alert.showAndWait();
            return response;
        }

        public void confirmSendingInvitation(){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invitation Sent");
            alert.setHeaderText(null);
            alert.setContentText("Wait Reply....");
            alert.showAndWait();
        }

        public void agree(String invitationSender){
            printStream.println("true,"+SignInController.player.playerUserName+"," + invitationSender);  // heba>>replay sender(userName), invitationSender(ebtsam)>>receiver(message sent to her) , will be changed
            System.out.println("ok true");
        }

        public void refuse(String invitationSender){
            printStream.println("false,"+SignInController.player.playerUserName+"," + invitationSender);  // heba>>replay sender(userName), invitationSender(ebtsam)>>receiver(message sent to her) , will be changed
            System.out.println("no false");
        }

        public void acceptInvitation(){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Tic Tac Toe");
            alert.setHeaderText(null);
            alert.setContentText("wait game will start");
            alert.showAndWait();
        }

        public void refuseInvitation(String replySender){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Refuse");
            alert.setHeaderText(null);
            alert.setContentText(replySender+" Refuse playing with you");
            alert.showAndWait();
        }


    }


    }
