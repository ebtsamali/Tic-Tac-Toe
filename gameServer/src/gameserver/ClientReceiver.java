/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameserver;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import static gameserver.Server.games;
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
                gameInviteResult(message);
                break;
            case "move":
                playMove(message);
                break;
            case "online":
                sendOnlinePlayers();
                break;
            case "message":
                sendChat(message);
                break;
        }

    }

    void send(JsonObject invitation) {
        for (int i = 0; i < Server.players.size(); i++) {
            if (Server.players.get(i).getPlayerUserName().equals(invitation.get("reciverUsername").getAsString())) {
                try {
                    new PrintStream(Server.players.get(i).getPlayerSocket().getOutputStream()).println(invitation.toString());
                } catch (Exception ex) {
                    System.out.println("Cannot send the invitation");
                }
            }
        }
    }

    void gameInviteResult(JsonObject message) {
        if (message.get("result").getAsBoolean()) {
            int x = 0;
            int gameIndex = 0;
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getPlayerUserName().equals(message.get("senderUsername").getAsString()) || players.get(i).getPlayerUserName().equals(message.get("reciverUsername").getAsString())) {
                    if (x == 0) {
                        x++;
                        gameIndex = games.size();
                        games.add(new Game(players.get(i)));
                        try {
                            new PrintStream(Server.players.get(i).getPlayerSocket().getOutputStream()).println("{type:openGame,senderUsername:" + message.get("senderUsername") + ",reciverUsername:" + message.get("reciverUsername") + ",gameId:" + gameIndex + ",sign:X}");
                        } catch (IOException ex) {
                            System.out.println("error in sendding game start");
                        }
                    } else {
                        games.get(gameIndex).player2 = players.get(i);
                        try {
                            new PrintStream(Server.players.get(i).getPlayerSocket().getOutputStream()).println("{type:openGame,senderUsername:" + message.get("senderUsername") + ",reciverUsername:" + message.get("reciverUsername") + ",gameId:" + gameIndex + ",sign:O}");
                        } catch (IOException ex) {
                            System.out.println("error in sendding game start");
                        }
                    }

                }
            }
        }
    }

    private void playMove(JsonObject message) {
        try {
            String result = games.get(message.get("gameId").getAsInt()).makeMove(message.get("place").getAsInt());
            if (result.equals("X") || result.equals("O")) {
                new PrintStream(games.get(message.get("gameId").getAsInt()).player1.getPlayerSocket().getOutputStream()).println("{type:move,result:" + result + ",place:" + message.get("place").getAsString() + "}");
                new PrintStream(games.get(message.get("gameId").getAsInt()).player2.getPlayerSocket().getOutputStream()).println("{type:move,result:" + result + ",place:" + message.get("place").getAsString() + "}");
            } else {
                if (!games.get(message.get("gameId").getAsInt()).turn) {
                    new PrintStream(games.get(message.get("gameId").getAsInt()).player2.getPlayerSocket().getOutputStream()).println("{type:move,result:" + result + ",place:" + message.get("place").getAsString() + "}");
                } else {
                    new PrintStream(games.get(message.get("gameId").getAsInt()).player1.getPlayerSocket().getOutputStream()).println("{type:move,result:" + result + ",place:" + message.get("place").getAsString() + "}");
                }
            }
        } catch (Exception ex) {
            System.out.println("error in sendding game move");
        }
    }

    private void sendOnlinePlayers() {
        try {
            String playersJsonArr ="["+Player.toJsonArray(players).toString();
            new PrintStream(player.getPlayerSocket().getOutputStream()).println(playersJsonArr);
        } catch (Exception ex) {
            System.out.println("error in sendding online players");
        }
    }

    private void sendChat(JsonObject message) {
       if(player.getPlayerUserName().equals(games.get(message.get("gameId").getAsInt()).player1.getPlayerUserName())){
           try {
               new PrintStream(games.get(message.get("gameId").getAsInt()).player2.getPlayerSocket().getOutputStream()).println("{type:message,message:\""+player.getPlayerUserName()+":"+message.get("message").getAsString()+"\"}");
           } catch (Exception ex) {
               System.out.println("error in sending chat");;
           }
       }else{
           try {
               new PrintStream(games.get(message.get("gameId").getAsInt()).player1.getPlayerSocket().getOutputStream()).println("{type:message,message:\""+player.getPlayerUserName()+":"+message.get("message").getAsString()+"\"}");
           } catch (Exception ex) {
               System.out.println("error in sending chat");;
           }
       }
    }
}
