/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameserver;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import static gameserver.Server.players;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mena
 */
public class ClientReceiver extends Thread {

    Player player;

    public ClientReceiver(Player newPlayer) {
        player = newPlayer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String message = new DataInputStream(player.getPlayerSocket().getInputStream()).readLine();
                makeAction(message);
            } catch (IOException ex) {
                System.out.println("error in reciving data in client thread");
                try {
                    player.getPlayerSocket().close();
                } catch (IOException ex1) {
                    System.out.println("cannot close the client socket");
                }
                for (int i = 0; i < players.size(); i++) {
                    if (players.get(i).getPlayerUserName() == player.getPlayerUserName()) {
                        players.remove(i);
                    }
                }
                System.out.println("the thread stopped of palyer : " + player.getPlayerUserName());
                Server.refreshTable();
                this.stop();
            }
        }
    }

    void makeAction(String msg) {
        JsonObject message = new JsonParser().parse((msg)).getAsJsonObject();
        switch (message.get("type").getAsString()) {
            case "invite":
                message.get("senderUsername").getAsString();
                System.out.println("invite sent from " + message.get("senderUsername").getAsString() + " to " + message.get("reciverUsername").getAsString());
                send(message);
                break;
            case "accept":
                if (message.get("result").getAsBoolean()) {
                    for (int i = 0; i < players.size(); i++) {
                        if (players.get(i).getPlayerUserName().equals(message.get("senderUsername").getAsString()) || players.get(i).getPlayerUserName().equals(message.get("reciverUsername").getAsString())) {
                            try {
                                new PrintStream(Server.players.get(i).getPlayerSocket().getOutputStream()).println("{type:openGame,senderUsername:"+message.get("senderUsername")+",reciverUsername:"+message.get("reciverUsername")+"}");
                            } catch (IOException ex) {
                                System.out.println("error in sendding game start");
                            }
                        }
                    }
                }
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
