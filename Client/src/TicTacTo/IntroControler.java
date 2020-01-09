package TicTacTo;

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
import static javafx.application.Application.launch;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Window;

public class IntroControler implements Initializable {
    
    @FXML Button loginButton;
    @FXML Button newPlayerButton;
    @FXML AnchorPane anchorHome;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @FXML
    private void signInButton(javafx.event.ActionEvent event) throws Exception
    {
        Parent newParent =  FXMLLoader.load(getClass().getResource("signIn.fxml"));
        Scene newScene = new Scene(newParent,730,500);
        Stage windowStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        windowStage.setScene(newScene);
        windowStage.show();
    }
}
