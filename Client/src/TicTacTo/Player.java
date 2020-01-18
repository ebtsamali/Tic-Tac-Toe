package TicTacTo;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.net.Socket;
import java.util.Vector;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class Player {

    private String playerUserName;
    private String playerPassword;
    private String hashedPass;
    private String playerEmail;
    private String userFullname;
    private String securityQuestion;
    private Socket playerSocket;
    private Thread playerthread;
    private int playerScore;
    private Button state =new Button("⚫ Offline");

    public Player(String name, String pass) {
        playerUserName = name;
        playerPassword = pass;
        state.setDisable(true);
        state.setTextFill(Color.GREY);
        state.setStyle("-fx-background-color: transparent;");
    }

    public Player(String name, String password, String fullname, String email, String securityQ,int score) {
        playerUserName = name;
        playerPassword = password;
        userFullname = fullname;
        playerEmail = email;
        securityQuestion = securityQ;
        playerScore=score;
        state.setDisable(true);
        state.setTextFill(Color.GREY);
        state.setStyle("-fx-background-color: transparent;");
    }
    public Player(String name, String password, String fullname, String email, String securityQ) {
        playerUserName = name;
        playerPassword = password;
        userFullname = fullname;
        playerEmail = email;
        securityQuestion = securityQ;
        state.setDisable(true);
        state.setTextFill(Color.GREY);
        state.setStyle("-fx-background-color: transparent;");
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
        player.addProperty("username", getPlayerUserName());
        player.addProperty("password", getPlayerPassword());
        player.addProperty("fullname", getUserFullname());
        player.addProperty("email", getPlayerEmail());
        player.addProperty("question", getSecurityQuestion());
        player.addProperty("score", getPlayerScore());
        return player;
    }

    static public JsonArray toJsonArray(Vector<Player> players) {
        JsonArray playerArray = new JsonArray();
        for (int i = 0; i < players.size(); i++) {
            playerArray.add(players.get(i).toJsonObject());
        }
        return playerArray;
    }

    /**
     * @return the playerUserName
     */
    public String getPlayerUserName() {
        return playerUserName;
    }

    /**
     * @param playerUserName the playerUserName to set
     */
    public void setPlayerUserName(String playerUserName) {
        this.playerUserName = playerUserName;
    }

    /**
     * @return the playerPassword
     */
    public String getPlayerPassword() {
        return playerPassword;
    }

    /**
     * @param playerPassword the playerPassword to set
     */
    public void setPlayerPassword(String playerPassword) {
        this.playerPassword = playerPassword;
    }

    /**
     * @return the hashedPass
     */
    public String getHashedPass() {
        return hashedPass;
    }

    /**
     * @param hashedPass the hashedPass to set
     */
    public void setHashedPass(String hashedPass) {
        this.hashedPass = hashedPass;
    }

    /**
     * @return the playerEmail
     */
    public String getPlayerEmail() {
        return playerEmail;
    }

    /**
     * @param playerEmail the playerEmail to set
     */
    public void setPlayerEmail(String playerEmail) {
        this.playerEmail = playerEmail;
    }

    /**
     * @return the userFullname
     */
    public String getUserFullname() {
        return userFullname;
    }

    /**
     * @param userFullname the userFullname to set
     */
    public void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
    }

    /**
     * @return the securityQuestion
     */
    public String getSecurityQuestion() {
        return securityQuestion;
    }

    /**
     * @param securityQuestion the securityQuestion to set
     */
    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    /**
     * @return the playerSocket
     */
    public Socket getPlayerSocket() {
        return playerSocket;
    }

    /**
     * @param playerSocket the playerSocket to set
     */
    public void setPlayerSocket(Socket playerSocket) {
        this.playerSocket = playerSocket;
    }

    /**
     * @return the playerthread
     */
    public Thread getPlayerthread() {
        return playerthread;
    }

    /**
     * @param playerthread the playerthread to set
     */
    public void setPlayerthread(Thread playerthread) {
        this.playerthread = playerthread;
    }

    /**
     * @return the playerScore
     */
    public int getPlayerScore() {
        return playerScore;
    }

    /**
     * @param playerScore the playerScore to set
     */
    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    /**
     * @return the state
     */
    public Button getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(Button state) {
        this.state = state;
    }
    public void setOnline() {
        state.setDisable(false);
        state.setText("⚫ Online");
        state.setTextFill(Color.GREEN);
    }
    public void setOffline() {
        state.setDisable(true);
        state.setText("⚫ Offline");
        state.setTextFill(Color.GREY);
    }
}
