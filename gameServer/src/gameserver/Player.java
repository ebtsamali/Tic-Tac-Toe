package gameserver;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.net.Socket;
import java.util.Vector;

public class Player {

    public String playerUserName;
    public String playerPassword;
    public String hashedPass;
    public String playerEmail;
    public String userFullname;
    public String securityQuestion;
    public Socket playerSocket;
    public Thread playerthread;

    public Player(String name, String pass) {
        playerUserName = name;
        playerPassword = pass;
    }

    public Player(String name, String password, String fullname, String email, String securityQ) {
        playerUserName = name;
        playerPassword = password;
        userFullname = fullname;
        playerEmail = email;
        securityQuestion = securityQ;
    }

    public Player(JsonObject player) {
        try {
            playerUserName = player.get("username").getAsString();
            playerPassword = player.get("password").getAsString();
            try {
                userFullname = player.get("fullname").getAsString();
                playerEmail = player.get("email").getAsString();
                securityQuestion = player.get("question").getAsString();
            } catch (Exception ex) {
                System.out.println("small object");
            }
        } catch (Exception ex) {
            System.out.println("error in fitching the player object");
        }
    }

    public JsonObject toJsonObject() {
        JsonObject player = new JsonObject();
        player.addProperty("username", playerUserName);
        player.addProperty("password", playerPassword);
        player.addProperty("fullname", userFullname);
        player.addProperty("email", playerEmail);
        player.addProperty("question", securityQuestion);
        return player;
    }

    static public JsonArray toJsonArray(Vector<Player> players) {
        JsonArray playerArray = new JsonArray();
        for (int i = 0; i < players.size(); i++) {
            playerArray.add(players.get(i).toJsonObject());
        }
        return playerArray;
    }
}
