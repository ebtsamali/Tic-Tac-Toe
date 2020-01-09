package gameserver;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerManager extends Application {
    ServerSocket myServerSocket;   // to establish connection
    Socket socket;

    @Override
    public void init() throws ClassNotFoundException  {
        
    }
                
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        runServer();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public void runServer()
    {
//        (new Thread(new InitiatingServer())).start();
        (new Thread(() -> {
            try
            {
                myServerSocket = new ServerSocket(8090);
                System.out.println("running");
            }
            catch (IOException ex)
            {
                System.out.println(ex);
            } catch (Exception ex) {
                Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            while (true)
            {
                try
                {
                    socket = myServerSocket.accept();
                    System.out.println("ok");
                    
                    Thread userThread = new Thread(new UserHandler(socket));
                    userThread.start();
                }
                catch (IOException ex)
                {
                    System.out.println("error in connection server");
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
                }       
            }       })).start();


    }
}
