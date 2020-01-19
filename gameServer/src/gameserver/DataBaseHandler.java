package gameserver;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
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
            System.out.println("error in openning connection with database"+e);
        }
    }

    @Override
    protected void finalize() {
        try {
            conn.close();
        } catch (SQLException ex) {
            System.out.println("error in close connection with data base");
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
            System.out.println("error in finding users");
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
            System.out.println("error in adding user");
        }
    }
    
    public List<Player> getAllPlayers(){
        List<Player> allPlayers = new ArrayList<>();
        try {
            Statement stmt=conn.createStatement();
            ResultSet rs=stmt.executeQuery("select * from userdata");
            while(rs.next()){
                Player newPlayer=new Player(rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6));
                allPlayers.add(newPlayer);
            }
        } catch (SQLException ex) {
            System.out.println("cannot retrive all players");
        }
    return allPlayers;
    }
    
}