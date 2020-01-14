/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameserver;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mena
 */
public class ClientReceiver extends Thread {

    Socket socket;

    public ClientReceiver(Socket s) {
        socket = s;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String message = new DataInputStream(socket.getInputStream()).readLine();
                makeAction(message);
            } catch (IOException ex) {
                System.out.println("error in reciving data in client thread");;
            }
        }
    }

    void makeAction(String msg) {
        JsonObject message = new JsonParser().parse((msg)).getAsJsonObject();
        switch (message.get("type").getAsString()) {
            case "invite":
                message.get("senderUsername").getAsString();
                System.out.println("invite sent from " + message.get("senderUsername").getAsString()+"to " + message.get("reciverUsername").getAsString());
                send(message);
                break;
        }
    }

    void send(JsonObject invitation) {
        for (int i = 0; i < Server.players.size(); i++) {
            if (Server.players.get(i).getPlayerUserName().equals(invitation.get("reciverUsername").getAsString())) {
                try {
                    new PrintStream(Server.players.get(i).getPlayerSocket().getOutputStream()).println(invitation.toString());
                } catch (IOException ex) {
                    System.out.println("Cannot send the invitation");;
                }
            }
        }
    }
}
