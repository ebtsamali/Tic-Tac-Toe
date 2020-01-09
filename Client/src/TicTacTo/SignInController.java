package TicTacTo;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class SignInController implements Initializable {
    
    private Stage window;
    private Parent root;
    Thread checkThread;
    @FXML
    private FontAwesomeIconView minIcon;
    @FXML
    private FontAwesomeIconView maxIcon;
    @FXML
    private FontAwesomeIconView closeIcone;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Hyperlink registerLink;
    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private JFXButton signInbutton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkThread = (new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if(!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty())
                    {
                        signInbutton.setDisable(false);                    
                    }
                }
                
            }
        }));
        checkThread.start(); // disable submmiting while no input
    
    }

    
    @FXML
    private void signInButton(javafx.event.ActionEvent actionEvent) throws Exception
    {
        Player newPlayer = new Player(usernameField.getText(), passwordField.getText());
        if(CheckLogin(newPlayer))
        {
            sceneLoader("fxml/signUp.fxml", actionEvent);
        }
           
    }
    
    public boolean CheckLogin(Player currentPlayer) throws IOException
    {
        Boolean isValidUsername;
        Boolean isValidPassword;
        Socket socket = new Socket("127.0.0.1", 8090);
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        // sending data to server for checking
        dataOutputStream.writeUTF(currentPlayer.playerUserName);
        dataOutputStream.writeUTF(currentPlayer.playerPassword);

         isValidUsername = dataInputStream.readBoolean();
         isValidPassword = dataInputStream.readBoolean();

        return isValidPassword && isValidUsername;
    }

    @FXML
    private void minimizeWindow(MouseEvent event) {
        Stage windowStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        windowStage.setIconified(true);
    }

    @FXML
    private void fullScreenWindow(MouseEvent event) {  
        Stage windowStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        windowStage.setFullScreen(true);
    }

    @FXML
    private void closeWindow(MouseEvent event) {
//        Stage windowStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
//        windowStage.close();
        checkThread.stop();
        Platform.exit();
        
    }
    
    private void sceneLoader(String fxmlFileName,javafx.event.ActionEvent actionEvent) throws Exception
    {
        Parent newParent =  FXMLLoader.load(getClass().getResource(fxmlFileName));
        Scene newScene = new Scene(newParent,730,500);
        Stage windowStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        windowStage.setScene(newScene);
        windowStage.show();
    }

    @FXML
    private void redirect(ActionEvent actionEvent) {
        
        try 
        {
            sceneLoader("fxml/signUp.fxml",actionEvent);
        } 
        catch (Exception ex) 
        {
            System.out.println(ex);
        }

    }

    @FXML
    private void lockLogin(KeyEvent event) {
    }


}
