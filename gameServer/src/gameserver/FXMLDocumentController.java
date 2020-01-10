/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameserver;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
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
    }

}
