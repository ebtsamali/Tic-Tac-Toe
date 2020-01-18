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
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Mahmoud
 */
public class GameServer {

    public String conString = "jdbc:mysql://localhost:3306/test?zeroDateTimeBehavior=convertToNull";
    public Connection con;
    public Statement stmt;
    public String queryString;
    public ResultSet resset;
    public DataInputStream dis;
    public PrintStream ps;
    public String sGame;

    public ServerSocket serverSocket;

    public GameServer() throws IOException, SQLException {
        serverSocket = new ServerSocket(6000);
        while (true) {
//            System.out.println("jhyfdytugl");
            Socket s = serverSocket.accept();
            dis = new DataInputStream(s.getInputStream());
            ps = new PrintStream(s.getOutputStream());
            sGame = dis.readLine();
            setConnection();
            //addGame(new JsonParser().parse(sGame).getAsJsonObject());
            retrieveGame("Mahmoud", "Ahmed");
        }
    }

    public void setConnection() {

        try {

            //load driver
            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection(conString, "root", "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void retrieveGame(String user1, String user2) throws SQLException {

//        System.out.println("srgdghb");
        PreparedStatement retrieve = con.prepareStatement("select * from tgame where userone = ? and usertwo= ?");
        retrieve.setString(1, user1);
        retrieve.setString(2, user2);

        ResultSet resultSet = retrieve.executeQuery();

        JsonObject ret = new JsonObject();

        if (resultSet.next()) {
//            System.out.println("hgkughk");
            ret.addProperty("btn1", resultSet.getString("btnone"));
            ret.addProperty("btn2", resultSet.getString("btntwo"));
            ret.addProperty("btn3", resultSet.getString("btnthree"));
            ret.addProperty("btn4", resultSet.getString("btnfour"));
            ret.addProperty("btn5", resultSet.getString("btnfive"));
            ret.addProperty("btn6", resultSet.getString("btnsix"));
            ret.addProperty("btn7", resultSet.getString("btnseven"));
            ret.addProperty("btn8", resultSet.getString("btneight"));
            ret.addProperty("btn9", resultSet.getString("btnnine"));

//            System.out.println(ret.toString());
            ps.println(ret.toString());

        }
    }

//    public void deleteGame(int id) {
//        try {
//            PreparedStatement delGame = con.prepareStatement("delete from tgame where g_id = ?");
//            delGame.setInt(1, id);
//            delGame.executeUpdate();
//        } catch (SQLException ex) {
//            Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SQLException {
        // TODO code application logic here
        new GameServer();
    }
}
