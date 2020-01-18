/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TicTacTo;

import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Islam Hanafi
 */
public class SignUpController implements Initializable {

    @FXML
    private JFXTextField userFullname;
    @FXML
    private JFXTextField userName;
    @FXML
    private JFXTextField userEmail;
    @FXML
    private JFXPasswordField userPassword;
    @FXML
    private JFXPasswordField userPasswordConfig;
    @FXML
    private JFXTextField userSecurityQ;
    @FXML
    private JFXButton signUpbutton;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXButton cancelBtn;
    @FXML
    private Text errorMsg;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    private void alertMsg(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearForm() {
        userFullname.setText("");
        userName.setText("");
        userPassword.setText("");
        userPasswordConfig.setText("");
        userEmail.setText("");
        userSecurityQ.setText("");
    }

    @FXML
    private void minimizeWindow(MouseEvent event) {
        Stage windowStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        windowStage.setIconified(true);

    }

    private void fullScreenWindow(MouseEvent event) {
        Stage windowStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        windowStage.setFullScreen(true);

    }

    @FXML
    private void closeWindow(MouseEvent event) {
        Stage windowStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        windowStage.close();

    }

    private void sceneLoader(String fxmlFileName, javafx.event.ActionEvent actionEvent) throws Exception {
        Parent newParent = FXMLLoader.load(getClass().getResource(fxmlFileName));
        Scene newScene = new Scene(newParent, 800, 550);
        Stage windowStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        windowStage.setScene(newScene);
        windowStage.show();

    }

    @FXML
    private void signUpbutton(ActionEvent event) {
        Player newPlayer = new Player(userName.getText(), userPassword.getText(), userFullname.getText(), userEmail.getText(), userSecurityQ.getText());
        // check if the name or email is already in our database
        // check if the password is correct 
        // check if the user entered all values
        // save data in database
        boolean isValidUser = false;
        boolean isValidPassword = (userPassword.getText()).equals(userPasswordConfig.getText());
        boolean isEmpty = (userName.getText().isEmpty() || userPassword.getText().isEmpty() || userFullname.getText().isEmpty());

        if (isValidPassword && !isValidUser && !isEmpty) {
            // add data 

            Socket socket;
            try {
                System.out.println("sending the new player");
                socket = new Socket("127.0.0.1", 8090);

                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                // sending data to server for checking
                JsonObject jsonPlayer = newPlayer.toJsonObject();
                jsonPlayer.addProperty("type", 2);
                new PrintStream(socket.getOutputStream()).println(jsonPlayer.toString());
                socket.close();
            } catch (IOException ex) {
                System.out.println("error in connection with servr to send the new player");
            }
        }
    }

    @FXML
    private void CancelHndeler(ActionEvent event) {
        try {
            sceneLoader("fxml/signIn.fxml", event);
        } catch (Exception ex) {
            System.out.println("canot Redirect");
        }
    }

    @FXML
    private void checker(KeyEvent event) {
        if (userFullname.getText().isEmpty()) {
            errorMsg.setText("please enter your Fullname");
            errorMsg.setVisible(true);
            signUpbutton.setDisable(true);
        } else if (userName.getText().isEmpty()) {
            errorMsg.setText("please enter your username");
            errorMsg.setVisible(true);
            signUpbutton.setDisable(true);
        } else if (userEmail.getText().isEmpty()) {
            errorMsg.setText("please enter your Email");
            errorMsg.setVisible(true);
            signUpbutton.setDisable(true);
        } else if (userPassword.getText().isEmpty()) {
            errorMsg.setText("please enter your Password");
            errorMsg.setVisible(true);
            signUpbutton.setDisable(true);
        } else if (userPasswordConfig.getText().isEmpty()) {
            errorMsg.setText("please enter your PasswordConfig");
            errorMsg.setVisible(true);
            signUpbutton.setDisable(true);
        } else if (userSecurityQ.getText().isEmpty()) {
            errorMsg.setText("please enter your SecurityQ");
            errorMsg.setVisible(true);
            signUpbutton.setDisable(true);
        } else if (!userPassword.getText().equals(userPasswordConfig.getText())) {
            errorMsg.setText("the password and it's Config don't match each other ");
            errorMsg.setVisible(true);
            signUpbutton.setDisable(true);
        } else {
            errorMsg.setVisible(false);
            signUpbutton.setDisable(false);
        }
    }

}
