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
import java.util.Vector;

/**
 *
 * @author mena
 */
public class Server {

    ServerSocket myServerSocket;   // to establish connection
    Socket socket;
    Vector<Thread> userThreads = new Vector<Thread>();
    Thread serverThread;

    public void runServer() {
        serverThread = new Thread(() -> {
            try {
                myServerSocket = new ServerSocket(8090);
                System.out.println("Server ready for new clients");
            } catch (IOException ex) {
                System.out.println("canot open the socket");
            }

            while (true) {
                try {
                    socket = myServerSocket.accept();
   
                    String data=new DataInputStream(socket.getInputStream()).readLine();
                    JsonObject message = new JsonParser().parse((data)).getAsJsonObject();
                    if (message.get("type").getAsInt() == 1) {
                        if (new DataBaseHandler().isUserExist(new Player(message))) {
                            System.out.println("user connected");
                            new PrintStream(socket.getOutputStream()).println("true");
                            //Thread userThread = new Thread(new UserHandler(socket));
                            //userThread.start();
                            //userThreads.add(userThread);
                        } else {
                            System.out.println("login failed");
                            new DataOutputStream(socket.getOutputStream()).writeBytes("false");
                            socket.close();
                        }
                    }else{
                        if (!(new DataBaseHandler().isUserExist(new Player(message)))){
                            new DataBaseHandler().addNewUser(new Player(message));
                            System.out.println("user added");
                        }else{
                            System.out.println("user is already exist");
                        }
                        socket.close();
                    }
                } catch (Exception ex) {
                    System.out.println("error in connection with client");
                }
            }
        });
        serverThread.start();
    }

    public void stopServer() {
        try {
            serverThread.stop();
            myServerSocket.close();
            for (int i = 0; i < userThreads.size(); i++) {
                userThreads.get(i).stop();
            }
            System.out.println("server turned off");
        } catch (IOException ex) {
            System.out.println("cannot stop the server");
        }
    }
}
