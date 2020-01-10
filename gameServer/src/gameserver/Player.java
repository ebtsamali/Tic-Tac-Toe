package gameserver;

import com.google.gson.JsonObject;
import java.net.Socket;

public class Player {

    public String playerUserName;
    public String playerPassword;
    public String hashedPass;
    public String playerEmail;
    public String userFullname;
    public String securityQuestion;
    public Socket playerSocket;

    public Player(String name, String pass, Socket socket) {
        playerUserName = name;
        playerPassword = pass;
        playerSocket = socket;
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
        } catch (Exception ex) {
            System.out.println("error in fitching the player object");
        }
    }

}
