package TicTacTo;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.Socket;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class main extends Application {

    Socket mySocket;
    DataInputStream dataInputStream;
    PrintStream printStream;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("fxml/signIn.fxml"));
        primaryStage.setTitle("Home");
        primaryStage.setScene(new Scene(root, 730, 500));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    
}
