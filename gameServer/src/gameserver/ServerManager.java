package gameserver;

import java.time.LocalDateTime;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ServerManager extends Application {

    @Override
    public void init() throws ClassNotFoundException {

    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Tic Tac Toe Server Manager");
        try{
        stage.getIcons().add(new Image(ServerManager.class.getResourceAsStream("icon.jpg")));
        }catch(Exception e){
            FXMLDocumentController.logString+=LocalDateTime.now()+": "+"Can't load icon"+"\n";
        }
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
