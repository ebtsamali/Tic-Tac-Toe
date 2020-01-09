package gameserver;
import java.sql.*;  
import java.util.ArrayList;


public class DataBaseHandler {
    
    
    public DataBaseHandler() {}
    
    public Connection dbConnection()throws Exception
    {
        try 
        {
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/tic_tac";
            String username = "root";
            String password = "1894";
            Class.forName(driver);

            Connection conn = DriverManager.getConnection(url,username,password);
            return conn;
        }
        catch(Exception e){System.out.println(e);}
        
        return null;
    }
    
    public Boolean checkUsername(Player currentPlayer) throws Exception
    {
        Connection conn = dbConnection();        
        PreparedStatement statement1 = conn.prepareStatement("SELECT * FROM userdata WHERE username = ?;");
        statement1.setString(1, currentPlayer.playerUserName);
        ResultSet resultSet = statement1.executeQuery();
        return resultSet.next();       
    }
    
    public boolean checkPassword(Player currentPlayer) throws Exception
    {
        Player checkPlayer = retrieveUserData(currentPlayer.playerUserName);
        
        return  (checkPlayer.playerPassword).equals(currentPlayer.playerPassword);
    }

    public ArrayList<Player> retrieveALLData() throws Exception
    {
        /*
            retreving data as result set and transfer it to array of objects (users or players)
        */
        ArrayList<Player> playersList = new ArrayList<>();

        Connection conn = dbConnection();        
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM userdata");

        while(resultSet.next())
        {
            Player newPlayer = new Player(resultSet.getString("username"),resultSet.getString("password"));
            playersList.add(newPlayer);           
        }
        
        return playersList;
    }
    
    public Player retrieveUserData(String username) throws Exception
    {
        /*
            retreving data as result set and transfer it to array of objects (users or players)
        */
        Player currentUserData = new Player();
        
        Connection conn = dbConnection();        
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM userdata WHERE username = ?");
        preparedStatement.setString(1,username);
        ResultSet resultSet = preparedStatement.executeQuery();

        while(resultSet.next())
        {
            currentUserData = new Player(resultSet.getString("username"),resultSet.getString("password"));
        }
        
        return currentUserData;
    }

    public void signNewUser(Player newPlayer) throws Exception
    {
        Connection conn = dbConnection();
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO userdata (username, password, fullname, email, securityQuestion) VALUES (?, ?, ?, ?, ?);");
        preparedStatement.setString(1, newPlayer.playerUserName);
        preparedStatement.setString(2, newPlayer.playerPassword);
        preparedStatement.setString(3, newPlayer.userFullname);
        preparedStatement.setString(4, newPlayer.email);
        preparedStatement.setString(5, newPlayer.securityQuestion);
        
        preparedStatement.executeUpdate();
        
        // TO DO
        // redirect to your home page and start playing


    }
}  

