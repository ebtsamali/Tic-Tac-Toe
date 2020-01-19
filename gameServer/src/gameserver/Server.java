/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameserver;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;
/**
 *
 * @author mena
 */
public class Server {

    ServerSocket myServerSocket;   // to establish connection
    Socket socket;
    static Vector<Player> players = new Vector<Player>();
    static Vector<Game> games = new Vector<Game>();
    Thread serverThread;

    public void runServer() {
        serverThread = new Thread(() -> {
            try {
                myServerSocket = new ServerSocket(DataBaseHandler.serverSocket);
                FXMLDocumentController.logString+=LocalDateTime.now()+": "+"Server ready for new clients"+"\n";
            } catch (IOException ex) {
                FXMLDocumentController.logString+=LocalDateTime.now()+": "+"canot open the socket"+"\n";
            }

            while (true) {
                refreshTable();
                try {
                    socket = myServerSocket.accept();
                    String data = new DataInputStream(socket.getInputStream()).readLine();
                    JsonObject message = new JsonParser().parse((data)).getAsJsonObject();
                    if (message.get("type").getAsInt() == 1) {
                        if (new DataBaseHandler().isUserExist(new Player(message)) && (!isOnline(new Player(message).getPlayerUserName()))) {
                            FXMLDocumentController.logString+=LocalDateTime.now()+": "+"user connected"+"\n";
                            new PrintStream(socket.getOutputStream()).println("true");
                            Player newPlayer = new Player(message);
                            newPlayer.setPlayerSocket(socket);
                            players.add(newPlayer);
                            new PrintStream(socket.getOutputStream()).println(Player.toJsonArray(players).toString());
                            ClientReceiver userThread = new ClientReceiver(newPlayer);
                            userThread.start();
                            sentToAllPlayers(newPlayer);
                        } else {
                            FXMLDocumentController.logString+=LocalDateTime.now()+": "+"login failed"+"\n";
                            new DataOutputStream(socket.getOutputStream()).writeBytes("false");
                            socket.close();
                        }
                    } else {
                        if (!(new DataBaseHandler().isUserNameExist(new Player(message)))) {
                            new DataBaseHandler().addNewUser(new Player(message));
                            FXMLDocumentController.logString+=LocalDateTime.now()+": "+"user added"+"\n";
                            new DataOutputStream(socket.getOutputStream()).writeBytes("true");
                        } else {
                            FXMLDocumentController.logString+=LocalDateTime.now()+": "+"user is already exist"+"\n";
                            new DataOutputStream(socket.getOutputStream()).writeBytes("user is already exist");
                        }
                        socket.close();
                    }
                } catch (Exception ex) {
                    FXMLDocumentController.logString+=LocalDateTime.now()+": "+"error in connection with client"+"\n";
                }
            }
        });
        serverThread.start();
    }

    public void stopServer() {
        try {
            serverThread.stop();
            myServerSocket.close();
            for (int i = 0; i < players.size(); i++) {
                players.get(i).getPlayerthread().stop();
            }
            players.clear();
            FXMLDocumentController.logString+=LocalDateTime.now()+": "+"server turned off"+"\n";
        } catch (IOException ex) {
            FXMLDocumentController.logString+=LocalDateTime.now()+": "+"cannot stop the server"+"\n";
        }
    }

    public static void refreshTable() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ObservableList<Player> data = FXCollections.observableArrayList(new DataBaseHandler().getAllPlayers());
                for (int i = 0; i < data.size(); i++) {
                    for (int j = 0; j < players.size(); j++) {
                        if (players.get(j).getPlayerUserName().equals(data.get(i).getPlayerUserName())) {
                            data.get(i).setOnline();
                            data.get(i).setPlayerSocket(players.get(j).getPlayerSocket());
                        }
                    }
                }
                FXMLDocumentController.sOnlineUsersTable.setItems(data);
            }
        });
    }

    private boolean isOnline(String username) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getPlayerUserName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    private void sentToAllPlayers(Player p) {
    new Thread(new Runnable() {
        @Override
        public void run() {
            JsonObject playerJson = p.toJsonObject();
            playerJson.addProperty("type", "newPlayer");
            for(int i=0;i<players.size()-1;i++){
                try {
                    new PrintStream(players.get(i).getPlayerSocket().getOutputStream()).println(playerJson.toString());
                } catch (IOException ex) {
                    FXMLDocumentController.logString+=LocalDateTime.now()+": "+"error adding new player"+"\n";
                }
            }
            }
    }).start();
    }
}
