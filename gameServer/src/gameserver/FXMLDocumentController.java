/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameserver;

import com.jfoenix.controls.JFXToggleButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Islam Hanafi
 */
public class FXMLDocumentController implements Initializable {

    public Server server = new Server();
    @FXML
    private JFXToggleButton serverBtn;
    @FXML
    public TableView<Player> onlineUsersTable;
 
    public static TableView<Player> sOnlineUsersTable;
    @FXML
    private TableColumn<Player, String> userName;
    @FXML
    private TableColumn<Player, String> email;
    @FXML
    private TableColumn<Player, String> password;
    @FXML
    private TableColumn<Player, String> score;
    @FXML
    private TableColumn<Player, String> state;
    @FXML
    private Button logButton;
    public static String logString = "";
    /*
    @FXML
    private TableColumn<Player,String> score;
    @FXML
    private TableColumn<Player,String> statuse;
     */
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (serverBtn.isSelected()) {
            server.runServer();
        } else if(serverBtn.isArmed()){
            server.stopServer();
        }
    }
    @FXML
    public void displayLogHistory(ActionEvent event){
        Platform.runLater(new Runnable() {
        @Override
        public void run() {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Server Log");
            alert.setHeaderText(null);
            TextArea area = new TextArea();
            area.setText(logString);
            area.setWrapText(true);
            area.setEditable(false);
            alert.getDialogPane().setContent(area);
            alert.setResizable(true);
            alert.show();
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        userName.setCellValueFactory(new PropertyValueFactory<>("playerUserName"));
        password.setCellValueFactory(new PropertyValueFactory<>("playerPassword"));
        score.setCellValueFactory(new PropertyValueFactory<>("playerScore"));
        email.setCellValueFactory(new PropertyValueFactory<>("playerEmail"));
        state.setCellValueFactory(new PropertyValueFactory<>("State"));
        sOnlineUsersTable=onlineUsersTable;
        ObservableList<Player> data = FXCollections.observableArrayList(new DataBaseHandler().getAllPlayers());
        sOnlineUsersTable.setItems(data);
    }

}
