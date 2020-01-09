/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserHandler implements Runnable{
    
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    DataBaseHandler dataBaseHandler = new DataBaseHandler();

    
    public UserHandler(Socket socket) throws ClassNotFoundException
    {
        try 
        {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
//            objectInputStream = new ObjectInputStream(socket.getInputStream());
//            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(UserHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() 
    {
        try 
        {
            String username = dataInputStream.readUTF();
            Player newPlayer = new Player();
            newPlayer.playerUserName = username;
            Boolean isValidUsername = dataBaseHandler.checkUsername(newPlayer);
            dataOutputStream.writeBoolean(isValidUsername);
            
        } 
        catch (IOException ex) 
        {
            System.out.println("saaaaa");
        } catch (Exception ex) {
            Logger.getLogger(UserHandler.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
}
