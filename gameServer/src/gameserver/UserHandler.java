/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserHandler extends Thread {

    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    DataBaseHandler dataBaseHandler = new DataBaseHandler();

    public UserHandler(Socket socket) {
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
//            objectInputStream = new ObjectInputStream(socket.getInputStream());
//            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(UserHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            String username = dataInputStream.readUTF();
            String password = dataInputStream.readUTF();

            Player newPlayer = new Player(username, password);

            Boolean isValidUsername = dataBaseHandler.isUserExist(newPlayer);
            Boolean isValidPassword = false;
            if (isValidUsername) {
                isValidPassword = dataBaseHandler.isUserExist(newPlayer);
            }

            dataOutputStream.writeBoolean(isValidUsername);
            dataOutputStream.writeBoolean(isValidPassword);

        } catch (IOException ex) {
            System.out.println("error in reciving");
        }
    }

}
