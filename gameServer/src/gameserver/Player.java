package gameserver;


public class Player {

    
    public String playerUserName;
    public String playerPassword;
    public String hashedPass;
    public String email;
    public String userFullname;
    public String securityQuestion;
    
    
    public  Player(){}

    public Player(String name ,String pass)
    {
        this.playerUserName = name;
        this.playerPassword = pass;
    }
    public  Player(String name , String password,String fullname,String email , String securityQ){
        
        this.playerUserName = name;
        this.playerPassword = password;
        this.userFullname = fullname;
        this.email = email;
        this.securityQuestion = securityQ;
    }

}
