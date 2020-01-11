/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TicTacTo;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

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
     List<Button> buttonlist = new ArrayList<>();
     for(int i=0;i<50;i++)
        buttonlist.add(new Button("111111"));
        onlineUserPane.getChildren().addAll(buttonlist); 
    }    
    
}
