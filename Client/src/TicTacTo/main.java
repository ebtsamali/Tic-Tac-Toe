package TicTacTo;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.Socket;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class main extends Application {

    Socket mySocket;
    DataInputStream dataInputStream;
    PrintStream printStream;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("fxml/signIn.fxml"));
        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.setScene(new Scene(root, 800,550));
        try{
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("images/icon.jpg")));
        }catch(Exception e){
            System.out.println("Can't load icon" + e);
        }
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    
}
