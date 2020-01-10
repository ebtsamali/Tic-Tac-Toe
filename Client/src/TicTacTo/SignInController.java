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
import javafx.scene.text.Text;

public class SignInController implements Initializable {
    
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
    private void signInButton(javafx.event.ActionEvent actionEvent) 
    {
        try{
        Player newPlayer = new Player(usernameField.getText(), passwordField.getText());
        if(CheckLogin(newPlayer))
        {
            // to player dashboard
            sceneLoader("fxml/signUp.fxml", actionEvent);
        }
        else
        {
            errorMsg.setVisible(true);
            tempMsg("*Invalid username or password!");
        }
        

        }
        catch(Exception e)
        {
            tempMsg("* Connection error");
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



    private void tempMsg(String msg)
    {
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
    
    private void popMsg(String msg)
    {
        
    }

    @FXML
    private void redirect(ActionEvent event) {
        try 
        {
            sceneLoader("fxml/signUp.fxml",event);
        } 
        catch (Exception ex) 
        {
            //System.out.println(ex);
        }

    }

}
