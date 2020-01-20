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
import java.time.LocalDateTime;
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
                FXMLDocumentController.logString+=LocalDateTime.now()+": "+"error in reciving data in client thread"+"\n";
                try {
                    player.getPlayerSocket().close();
                } catch (IOException ex1) {
                    FXMLDocumentController.logString+=LocalDateTime.now()+": "+"cannot close the client socket"+"\n";
                }
                for (int i = 0; i < players.size(); i++) {
                    if (players.get(i).getPlayerUserName() == player.getPlayerUserName()) {
                        players.remove(i);
                        i--;
                    } else {
                        try {
                            new PrintStream(players.get(i).getPlayerSocket().getOutputStream()).println("{type:offlineUser,user:\"" + player.getPlayerUserName() + "\"}");
                        } catch (IOException ex1) {
                            FXMLDocumentController.logString+=LocalDateTime.now()+": "+"error in send offline user"+"\n";
                        }
                    }
                }
                FXMLDocumentController.logString+=LocalDateTime.now()+": "+"the thread stopped of palyer : " + player.getPlayerUserName() + "\n";
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
                FXMLDocumentController.logString+=LocalDateTime.now()+": "+"invite sent from " + message.get("senderUsername").getAsString() + " to " + message.get("reciverUsername").getAsString()+"\n";
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
            case "gameSave":
                saveGame(message);
                break;
            case "addscore":
                addScore(message);
                break;
        }

    }

    void send(JsonObject invitation) {
        for (int i = 0; i < Server.players.size(); i++) {
            if (Server.players.get(i).getPlayerUserName().equals(invitation.get("reciverUsername").getAsString())) {
                try {
                    new PrintStream(Server.players.get(i).getPlayerSocket().getOutputStream()).println(invitation.toString());
                } catch (Exception ex) {
                    FXMLDocumentController.logString+=LocalDateTime.now()+": "+"Cannot send the invitation"+"\n";
                }
            }
        }
    }

    void gameInviteResult(JsonObject message) {
        if (message.get("result").getAsBoolean()) {
            int x = 0;
            int gameIndex = 0;
            JsonObject oldGame = new DataBaseHandler().retrieveGame(message.get("senderUsername").getAsString(), message.get("reciverUsername").getAsString());
            new DataBaseHandler().delete(message.get("senderUsername").getAsString(), message.get("reciverUsername").getAsString());
            if (oldGame != null) {
                gameIndex = games.size();
                games.add(new Game());
                for (int i = 0; i < 9; i++) {
                    if (oldGame.get("btn" + i).getAsString().equals("X") || oldGame.get("btn" + i).getAsString().equals("O")) {
                        games.get(gameIndex).gameBoardArr[i] = oldGame.get("btn" + i).getAsString();
                    }
                }
                if (oldGame.get("turn").getAsString().equalsIgnoreCase("true")) {
                    games.get(gameIndex).turn = true;
                } else {
                    games.get(gameIndex).turn = false;
                }

                for (int i = 0; i < players.size(); i++) {
                    if (players.get(i).getPlayerUserName().equals(oldGame.get("user1").getAsString())) {
                        try {
                            games.get(gameIndex).player1 = Server.players.get(i);
                            JsonObject newMessage = new JsonParser().parse(oldGame.toString()).getAsJsonObject();
                            newMessage.addProperty("sign", "X");
                            newMessage.addProperty("type", "retriveGame");
                            newMessage.addProperty("gameId", gameIndex);
                            new PrintStream(Server.players.get(i).getPlayerSocket().getOutputStream()).println(newMessage.toString());
                        } catch (IOException ex) {
                            FXMLDocumentController.logString+=LocalDateTime.now()+": "+"error in sending game retrive"+"\n";
                        }
                    } else if (players.get(i).getPlayerUserName().equals(oldGame.get("user2").getAsString())) {
                        try {
                            games.get(gameIndex).player2 = Server.players.get(i);
                            JsonObject newMessage = new JsonParser().parse(oldGame.toString()).getAsJsonObject();
                            newMessage.addProperty("sign", "O");
                            newMessage.addProperty("type", "retriveGame");
                            newMessage.addProperty("gameId", gameIndex);
                            new PrintStream(Server.players.get(i).getPlayerSocket().getOutputStream()).println(newMessage.toString());
                        } catch (IOException ex) {
                            FXMLDocumentController.logString+=LocalDateTime.now()+": "+"error in sendding game retrive"+"\n";
                        }
                    }
                }
            } else {
                for (int i = 0; i < players.size(); i++) {
                    if (players.get(i).getPlayerUserName().equals(message.get("senderUsername").getAsString()) || players.get(i).getPlayerUserName().equals(message.get("reciverUsername").getAsString())) {

                        if (x == 0) {
                            x++;
                            gameIndex = games.size();
                            games.add(new Game(players.get(i)));
                            try {
                                new PrintStream(Server.players.get(i).getPlayerSocket().getOutputStream()).println("{type:openGame,senderUsername:" + message.get("senderUsername") + ",reciverUsername:" + message.get("reciverUsername") + ",gameId:" + gameIndex + ",sign:X}");
                            } catch (IOException ex) {
                                FXMLDocumentController.logString+=LocalDateTime.now()+": "+"error in sendding game start"+"\n";
                            }
                        } else {
                            games.get(gameIndex).player2 = players.get(i);
                            try {
                                new PrintStream(Server.players.get(i).getPlayerSocket().getOutputStream()).println("{type:openGame,senderUsername:" + message.get("senderUsername") + ",reciverUsername:" + message.get("reciverUsername") + ",gameId:" + gameIndex + ",sign:O}");
                            } catch (IOException ex) {
                                FXMLDocumentController.logString+=LocalDateTime.now()+": "+"error in sendding game start"+"\n";
                            }
                        }
                    }
                }
            }
        }
    }

    private void playMove(JsonObject message) {
        try {
            String result = games.get(message.get("gameId").getAsInt()).makeMove(message.get("place").getAsInt());
            if (result.equals("X") || result.equals("O") || result.equals("end")) {
                new PrintStream(games.get(message.get("gameId").getAsInt()).player1.getPlayerSocket().getOutputStream()).println("{type:move,result:" + result + ",place:" + message.get("place").getAsString() + "}");
                new PrintStream(games.get(message.get("gameId").getAsInt()).player2.getPlayerSocket().getOutputStream()).println("{type:move,result:" + result + ",place:" + message.get("place").getAsString() + "}");
                if (result.equals("X")) {
                    new DataBaseHandler().addScore(games.get(message.get("gameId").getAsInt()).player1.getPlayerUserName(), 50);
                    games.get(message.get("gameId").getAsInt()).player1.setPlayerScore(player.getPlayerScore() + 50);
                    for (int i = 0; i < players.size(); i++) {
                        if (players.get(i).getPlayerUserName().equals(games.get(message.get("gameId").getAsInt()).player1.getPlayerUserName())) {
                            players.get(i).setPlayerScore(games.get(message.get("gameId").getAsInt()).player1.getPlayerScore());
                        }
                    }
                    Server.refreshTable();
                } else if (result.equals("O")) {
                    new DataBaseHandler().addScore(games.get(message.get("gameId").getAsInt()).player2.getPlayerUserName(), 50);
                    games.get(message.get("gameId").getAsInt()).player2.setPlayerScore(player.getPlayerScore() + 50);
                    for (int i = 0; i < players.size(); i++) {
                        if (players.get(i).getPlayerUserName().equals(games.get(message.get("gameId").getAsInt()).player2.getPlayerUserName())) {
                            players.get(i).setPlayerScore(games.get(message.get("gameId").getAsInt()).player2.getPlayerScore());
                        }
                    }
                    Server.refreshTable();
                }
            } else {
                if (!games.get(message.get("gameId").getAsInt()).turn) {
                    new PrintStream(games.get(message.get("gameId").getAsInt()).player2.getPlayerSocket().getOutputStream()).println("{type:move,result:" + result + ",place:" + message.get("place").getAsString() + "}");
                } else {
                    new PrintStream(games.get(message.get("gameId").getAsInt()).player1.getPlayerSocket().getOutputStream()).println("{type:move,result:" + result + ",place:" + message.get("place").getAsString() + "}");
                }
            }
        } catch (Exception ex) {
            FXMLDocumentController.logString+=LocalDateTime.now()+": "+"error in sendding game move"+"\n";
        }
    }

    private void sendOnlinePlayers() {
        try {
            String playersJsonArr = "[" + Player.toJsonArray(players).toString();
            new PrintStream(player.getPlayerSocket().getOutputStream()).println(playersJsonArr);
        } catch (Exception ex) {
            FXMLDocumentController.logString+=LocalDateTime.now()+": "+"error in sendding online players"+"\n";
        }
    }

    private void sendChat(JsonObject message) {
        if (player.getPlayerUserName().equals(games.get(message.get("gameId").getAsInt()).player1.getPlayerUserName())) {
            try {
                new PrintStream(games.get(message.get("gameId").getAsInt()).player2.getPlayerSocket().getOutputStream()).println("{type:message,message:\"" + player.getPlayerUserName() + ":" + message.get("message").getAsString() + "\"}");
            } catch (Exception ex) {
                FXMLDocumentController.logString+=LocalDateTime.now()+": "+"error in sending chat"+"\n";
            }
        } else {
            try {
                new PrintStream(games.get(message.get("gameId").getAsInt()).player1.getPlayerSocket().getOutputStream()).println("{type:message,message:\"" + player.getPlayerUserName() + ":" + message.get("message").getAsString() + "\"}");
            } catch (Exception ex) {
                FXMLDocumentController.logString+=LocalDateTime.now()+": "+"error in sending chat"+"\n";
            }
        }
    }

    private void saveGame(JsonObject message) {
        message.addProperty("turn", games.get(message.get("gameId").getAsInt()).turn);
        new DataBaseHandler().addGame(message);
        if (player.getPlayerUserName().equals(games.get(message.get("gameId").getAsInt()).player1.getPlayerUserName())) {
            try {
                new PrintStream(games.get(message.get("gameId").getAsInt()).player2.getPlayerSocket().getOutputStream()).println("{type:returnTodashboard}");
            } catch (Exception ex) {
                FXMLDocumentController.logString+=LocalDateTime.now()+": "+"error in sending chat"+"\n";
            }
        } else {
            try {
                new PrintStream(games.get(message.get("gameId").getAsInt()).player1.getPlayerSocket().getOutputStream()).println("{type:returnTodashboard}");
            } catch (Exception ex) {
                FXMLDocumentController.logString+=LocalDateTime.now()+": "+"error in sending chat"+"\n";
            }
        }
    }

    private void addScore(JsonObject message) {
        new DataBaseHandler().addScore(player.getPlayerUserName(), message.get("add").getAsInt());
        player.setPlayerScore(player.getPlayerScore() + message.get("add").getAsInt());
        for (int i = 0; i < players.size(); i++) {
            if (player.getPlayerUserName().equals(players.get(i).getPlayerUserName())) {
                players.get(i).setPlayerScore(player.getPlayerScore());
            }
        }
        Server.refreshTable();
    }
}
