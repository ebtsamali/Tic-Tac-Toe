package gameserver;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;

public class DataBaseHandler {

    Connection conn;
    BufferedReader configFile;
    public static int serverSocket;

    public DataBaseHandler() {
        try {
            configFile = new BufferedReader(new FileReader("ServerConfig.txt"));
                String json ="";
                StringBuilder sb = new StringBuilder();
                String line = configFile.readLine();
                while (line != null) {
                    sb.append(line);
                    sb.append("\n");
                    line = configFile.readLine();
                }
                json = sb.toString();
                configFile.close();
                JsonObject Jconfig = (JsonObject) new JsonParser().parse(json);
                String Jip = Jconfig.get("DataBase_ip").getAsString();
                String Jport = Jconfig.get("DataBase_port").getAsString();
                String Jusername = Jconfig.get("DataBase_username").getAsString();
                String Jpassword = Jconfig.get("Database_password").getAsString();
                String Jdatabase_name = Jconfig.get("DataBase_name").getAsString();
                serverSocket = Jconfig.get("Server_socket").getAsInt();
            String driver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://"+Jip+":"+Jport+"/"+Jdatabase_name;
            String username = Jusername;
            String password = Jpassword;
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            FXMLDocumentController.logString+=LocalDateTime.now()+": "+"error in openning connection with database" + e+"\n";
        }
    }

    @Override
    protected void finalize() {
        try {
            conn.close();
        } catch (SQLException ex) {
            FXMLDocumentController.logString+=LocalDateTime.now()+": "+"error in close connection with data base"+"\n";
        }
    }

    public Boolean isUserExist(Player currentPlayer) {
        PreparedStatement statement1;
        try {
            statement1 = conn.prepareStatement("SELECT * FROM userdata WHERE username = ? and password=?;");
            statement1.setString(1, currentPlayer.getPlayerUserName());
            statement1.setString(2, currentPlayer.getPlayerPassword());
            ResultSet resultSet = statement1.executeQuery();
            return resultSet.next();
        } catch (SQLException ex) {
            FXMLDocumentController.logString+=LocalDateTime.now()+": "+"error in finding users"+"\n";
            return false;
        }
    }

    public Boolean isUserNameExist(Player currentPlayer) {
        PreparedStatement statement1;
        try {
            statement1 = conn.prepareStatement("SELECT * FROM userdata WHERE username = ?;");
            statement1.setString(1, currentPlayer.getPlayerUserName());
            ResultSet resultSet = statement1.executeQuery();
            return resultSet.next();
        } catch (SQLException ex) {
            FXMLDocumentController.logString+=LocalDateTime.now()+": "+"error in finding users"+"\n";
            return false;
        }
    }

    public Player retrieveUserData(String username) throws Exception {
        /*
            retreving data as result set and transfer it to array of objects (users or players)
         */
        Player currentUserData = null;

        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM userdata WHERE username = ?");
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            currentUserData = new Player(resultSet.getString("username"), resultSet.getString("password"));
        }

        return currentUserData;
    }

    public void addNewUser(Player newPlayer) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = conn.prepareStatement("INSERT INTO userdata (username, password, fullname, email, securityQuestion) VALUES (?, ?, ?, ?, ?);");
            preparedStatement.setString(1, newPlayer.getPlayerUserName());
            preparedStatement.setString(2, newPlayer.getPlayerPassword());
            preparedStatement.setString(3, newPlayer.getUserFullname());
            preparedStatement.setString(4, newPlayer.getPlayerEmail());
            preparedStatement.setString(5, newPlayer.getSecurityQuestion());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            FXMLDocumentController.logString+=LocalDateTime.now()+": "+"error in adding user"+"\n";
        }
    }

    public List<Player> getAllPlayers() {
        List<Player> allPlayers = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from userdata");
            while (rs.next()) {
                Player newPlayer = new Player(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7));
                allPlayers.add(newPlayer);
            }
        } catch (SQLException ex) {
            FXMLDocumentController.logString+=LocalDateTime.now()+": "+"cannot retrive all players"+"\n";
        }
        return allPlayers;
    }

    int getScore(String playerUserName) {
        try {
            PreparedStatement statement1;
            statement1 = conn.prepareStatement("SELECT score FROM userdata WHERE username = ?;");
            statement1.setString(1, playerUserName);
            ResultSet resultSet = statement1.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (Exception ex) {
            FXMLDocumentController.logString+=LocalDateTime.now()+": "+"error in get score from data base"+"\n";
        }
        return 0;
    }

    public void addGame(JsonObject saveGame) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO savegame (username1,username2,place0,place1,place2,place3,place4,place5,place6,place7,place8,turn)VALUES(?,?,?,?,?,?,?,?,?,?,?,?);");
            ps.setString(1, saveGame.get("user1").getAsString());
            ps.setString(2, saveGame.get("user2").getAsString());
            ps.setString(3, saveGame.get("btn0").getAsString());
            ps.setString(4, saveGame.get("btn1").getAsString());
            ps.setString(5, saveGame.get("btn2").getAsString());
            ps.setString(6, saveGame.get("btn3").getAsString());
            ps.setString(7, saveGame.get("btn4").getAsString());
            ps.setString(8, saveGame.get("btn5").getAsString());
            ps.setString(9, saveGame.get("btn6").getAsString());
            ps.setString(10, saveGame.get("btn7").getAsString());
            ps.setString(11, saveGame.get("btn8").getAsString());
            ps.setString(12, saveGame.get("turn").getAsString());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void delete(String user1, String user2) {
        try {
            PreparedStatement delete = conn.prepareStatement("DELETE FROM savegame where (username1 = ? and username2= ?) or (username1 = ? and username2= ?);");
            delete.setString(1, user1);
            delete.setString(2, user2);
            delete.setString(3, user2);
            delete.setString(4, user1);
            delete.executeUpdate();
        } catch (SQLException ex) {
            FXMLDocumentController.logString+=LocalDateTime.now()+": "+"error in delete old game"+ex+"\n";
        }
    }

    public JsonObject retrieveGame(String user1, String user2) {
        JsonObject ret = new JsonObject();
        try {
            PreparedStatement retrieve = conn.prepareStatement("select * from savegame where (username1 = ? and username2= ?) or (username1 = ? and username2= ?);");
            retrieve.setString(1, user1);
            retrieve.setString(2, user2);
            retrieve.setString(3, user2);
            retrieve.setString(4, user1);

            ResultSet resultSet = retrieve.executeQuery();

            if (resultSet.next()) {
                ret.addProperty("user1", resultSet.getString("username1"));
                ret.addProperty("user2", resultSet.getString("username2"));
                ret.addProperty("btn0", resultSet.getString("place0"));
                ret.addProperty("btn1", resultSet.getString("place1"));
                ret.addProperty("btn2", resultSet.getString("place2"));
                ret.addProperty("btn3", resultSet.getString("place3"));
                ret.addProperty("btn4", resultSet.getString("place4"));
                ret.addProperty("btn5", resultSet.getString("place5"));
                ret.addProperty("btn6", resultSet.getString("place6"));
                ret.addProperty("btn7", resultSet.getString("place7"));
                ret.addProperty("btn8", resultSet.getString("place8"));
                ret.addProperty("turn", resultSet.getString("turn"));
                return ret;
            }
        } catch (SQLException ex) {
            FXMLDocumentController.logString+=LocalDateTime.now()+": "+"error in retriving game"+"\n";
        }
        return null;
    }

}
