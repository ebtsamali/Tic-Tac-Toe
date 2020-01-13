/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameserver;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;

/**
 *
 * @author Islam Hanafi
 */
public class FXMLDocumentController implements Initializable {

    public Server server = new Server();
    @FXML
    private ToggleButton serverBtn;
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

    /*
    @FXML
    private TableColumn<Player,String> score;
    @FXML
    private TableColumn<Player,String> statuse;
     */
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (serverBtn.isSelected()) {
            JOptionPane.showMessageDialog(null, "starting");
            server.runServer();
        } else {
            JOptionPane.showMessageDialog(null, "stopping");
            server.stopServer();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        userName.setCellValueFactory(new PropertyValueFactory<>("playerUserName"));
        password.setCellValueFactory(new PropertyValueFactory<>("playerPassword"));
        score.setCellValueFactory(new PropertyValueFactory<>("score"));
        email.setCellValueFactory(new PropertyValueFactory<>("playerEmail"));
        state.setCellValueFactory(new PropertyValueFactory<>("State"));
        sOnlineUsersTable=onlineUsersTable;
        ObservableList<Player> data = FXCollections.observableArrayList(new DataBaseHandler().getAllPlayers());
        sOnlineUsersTable.setItems(data);
    }

}
