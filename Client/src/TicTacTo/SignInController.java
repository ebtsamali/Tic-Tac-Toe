package TicTacTo;

import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class SignInController implements Initializable {
    public static Player player;
    private Stage window;
    private Parent root;
    Thread checkThread;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private JFXButton signInbutton;
    @FXML
    private Text errorMsg;
    @FXML
    private Hyperlink registerLink2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkThread = (new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty()) {
                        signInbutton.setDisable(false);
                    }
                }

            }
        }));
        checkThread.start(); // disable submmiting while no input

    }

    @FXML
    private void signInButton(javafx.event.ActionEvent actionEvent) {
        try {
            player = new Player(usernameField.getText(), passwordField.getText());
            if (CheckLogin(player)) {
                // to player dashboard
                sceneLoader("fxml/dashboard.fxml", actionEvent);
                checkThread.stop();
            } else {
                errorMsg.setVisible(true);
                tempMsg("*Invalid username or password!");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            tempMsg("* Connection error");
        }
    }

    public boolean CheckLogin(Player currentPlayer) throws IOException {
        Boolean isValidUser = false;
        Socket socket = new Socket("127.0.0.1", 8090);
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

        // sending data to server for checking
        JsonObject jsonPlayer = currentPlayer.toJsonObject();
        jsonPlayer.addProperty("type", 1);
        new PrintStream(socket.getOutputStream()).println(jsonPlayer.toString());
        isValidUser = Boolean.parseBoolean(dataInputStream.readLine());
        if (!isValidUser) {
            socket.close();
        }else{
        player.playerSocket=socket;
        }
        return isValidUser;
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
//        Stage windowStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
//        windowStage.close();
        checkThread.stop();
        Platform.exit();

    }

    private void sceneLoader(String fxmlFileName, javafx.event.ActionEvent actionEvent) throws Exception {
        Parent newParent = FXMLLoader.load(getClass().getResource(fxmlFileName));
        Scene newScene = new Scene(newParent, 800, 550);
        Stage windowStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        windowStage.setScene(newScene);
        windowStage.show();
    }

    private void tempMsg(String msg) {
        errorMsg.setText(msg);
        errorMsg.setVisible(true);
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
            @Override
            public void run() {

                errorMsg.setVisible(false);
            }
        },
                5000
        );
    }

    private void popMsg(String msg) {

    }


    @FXML
    private void testabc(ActionEvent event) {
        try 
        {
            sceneLoader("fxml/signUp.fxml", event);
            checkThread.stop();
        } catch (Exception ex) 
        {
            //System.out.println(ex);
        }

    }

}
