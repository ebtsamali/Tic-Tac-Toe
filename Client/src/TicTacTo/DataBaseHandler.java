package TicTacTo;
import java.sql.*;  
import java.util.ArrayList;


public class DataBaseHandler {
    
    
    public DataBaseHandler() {}
    
    public  Connection dbConnection()throws Exception
    {
        try 
        {
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/tictac";
            String username = "root";
            String password = "12351235";
            Class.forName(driver);

            Connection conn = DriverManager.getConnection(url,username,password);
            return conn;
        }
        catch(Exception e){System.out.println(e);}
  
  
        return null;
    }
    
    public boolean checkUsername(Player currentPlayer) throws Exception
    {
        Connection conn = dbConnection();        
        PreparedStatement statement1 = conn.prepareStatement("SELECT * FROM userdata WHERE username = ?;");
        statement1.setString(1, currentPlayer.getPlayerUserName());
        ResultSet resultSet = statement1.executeQuery();
        return resultSet.next();       
    }
    
    public boolean checkPassword(Player currentPlayer) throws Exception
    {
        Player checkPlayer = retrieveUserData(currentPlayer.getPlayerUserName());
        return  (checkPlayer.getPlayerPassword()).equals(currentPlayer.getPlayerPassword());
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
        Player currentUserData = null;
        
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
        preparedStatement.setString(1, newPlayer.getPlayerUserName());
        preparedStatement.setString(2, newPlayer.getPlayerPassword());
        preparedStatement.setString(3, newPlayer.getUserFullname());
        preparedStatement.setString(4, newPlayer.getPlayerEmail());
        preparedStatement.setString(5, newPlayer.getSecurityQuestion());
        
        preparedStatement.executeUpdate();
        
        // TO DO
        // redirect to your home page and start playing


    }
}  

