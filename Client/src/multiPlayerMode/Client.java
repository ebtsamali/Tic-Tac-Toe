/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multiPlayerMode;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Client extends Application {

    private Button XOplayer;
    private Button Messages;
    private int column = 0;
    private int row = 0;
    private Socket clientSocket;
    private static BufferedReader dis;
    private static PrintStream ps;
    private int recievedButtonNumber;
    private int ButtonNumberToSend;
    private int XOcounter = 0;
    private String myXOsign;
    private static int myTurn;
    private int playCounter = 1;
    Color red = Color.RED;
    String GameMessage;

    @Override
    public void init() {
        try {
            clientSocket = new Socket("127.0.0.1", 5005);
            dis = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ps = new PrintStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    myTurn = dis.read();
                    myXOsign = dis.readLine();
                    String message = dis.readLine();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            GameGUIController.sXOplayer.setText(myXOsign);
                            GameGUIController.sMessages.setText(message);
                        }
                    });

                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                while (true) {

                    try {
                        recievedButtonNumber = dis.read();
                        System.out.println("recievedButtonNumber = " + recievedButtonNumber);
                        GameMessage = dis.readLine();
                        playCounter++;
                        if (playCounter > 2) {
                            playCounter = 1;
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    Platform.runLater(new Runnable() {
                        public void run() {
                            setButtonText(recievedButtonNumber,GameGUIController.buttons);
                            GameGUIController.sMessages.setText(GameMessage);

                        }
                    });

                }

            }

        }).start();
    }

    @Override
    public void start(Stage primaryStage) {

        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("multiPlayerGameGUI.fxml"));
            primaryStage.setScene(new Scene(root, 800, 550));
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

/////////////////////////////////////////////////////////////////////////////////////////////
    public void sendButtonNumber(Button b,Button[] buttons) {
        for (int i = 0; i < buttons.length; i++) {
            if (b == buttons[i]) {
                ButtonNumberToSend = i;
            }
        }
        if (playCounter == myTurn) {
            ps.write(ButtonNumberToSend);
        } else {
        }

    }

    public void setButtonText(int x,Button[] buttons) {
        if (recievedButtonNumber >= 0 && recievedButtonNumber < 9) {
            XOcounter++;
            if (XOcounter == 3) {
                XOcounter = 1;
            }

            if (XOcounter == 1) {
                buttons[x].setText("X");
                if (myTurn == 2) {
                    buttons[x].setTextFill(red);
                }

            } else {
                buttons[x].setText("O");
                if (myTurn == 1) {
                    buttons[x].setTextFill(red);
                }

            }
            buttons[x].setOnAction(null);
        } else {

            for (Button button : buttons) {
                button.setOnAction(null);
            }
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

}
