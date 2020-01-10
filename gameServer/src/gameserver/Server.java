/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameserver;

import com.google.gson.JsonParser;
import java.io.DataInputStream;
import java.io.IOException;
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
                    if(new DataBaseHandler().isUserExist(new Player(new JsonParser().parse((new DataInputStream(socket.getInputStream()).readLine())).getAsJsonObject()))){
                    System.out.print("user connected");
                    }
                    Thread userThread = new Thread(new UserHandler(socket));
                    userThread.start();
                    userThreads.add(userThread);
                } catch (IOException ex) {
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
            for(int i=0;i<userThreads.size();i++){
            userThreads.get(i).stop();
            }
            System.out.println("server turned off");
        } catch (IOException ex) {
            System.out.println("canot stop the server");
        }
    }
}
