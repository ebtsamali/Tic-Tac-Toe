package multiPlayerMode;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

class Player extends Thread {

    private BufferedReader dis;
    private PrintStream ps;
    private String XOsign;
    private int playerNumber;
    private int recievedButtonNumber;
    Server server;
    public static int counter = 1;
    String playerTurn;

    public Player(Socket cs, int playerNo, Server s) {
        playerNumber = playerNo;
        XOsign = (playerNumber == 1 ? "X" : "O");
        playerTurn = (playerNumber == 1 ? "YOUR TURN!" : "WAIT FOR OPPONENT MOVE!");
        try {
            dis = new BufferedReader(new InputStreamReader(cs.getInputStream()));
            ps = new PrintStream(cs.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
        server = s;
        server.players.add(this);
        start();

    }

    @Override
    public void run() {
        ps.write(playerNumber);
        ps.println(XOsign);
        ps.println(playerTurn);

        while (true) {
            try {
                recievedButtonNumber = dis.read();
                System.out.println(recievedButtonNumber);
                System.out.println("recievedButtonNumber = " + recievedButtonNumber);
                server.checkPlayerMove(recievedButtonNumber);
                sendMessageToPlayers(recievedButtonNumber);
                gameOverMessage();
            } catch (IOException ex) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void sendMessageToPlayers(int ButtonNumber) {
        for (Player p : server.players) {
            p.ps.write(ButtonNumber);
        }

    }

    public void gameOverMessage() {
        counter = (counter + 1) % 2;
        if (server.gameOver() == "X") {
            server.players.elementAt(0).ps.println("GAME OVER! YOU WON");
            server.players.elementAt(1).ps.println("GAME OVER! TRY AGAIN");
            ps.write(9);

        } else if (server.gameOver() == "O") {
            server.players.elementAt(0).ps.println("GAME OVER! TRY AGAIN");
            server.players.elementAt(1).ps.println("GAME OVER! YOU WON");
            ps.write(9);

        } else if (server.counter == 10) {
            server.players.elementAt(0).ps.println("OPPS!NO WINNER");
            server.players.elementAt(1).ps.println("OPPS!NO WINNER");
        } else {
            if (counter == 0) {
                server.players.elementAt(0).ps.println("WAIT FOR OPPONENT MOVE!");
                server.players.elementAt(1).ps.println("YOUR TURN!");
            } else {
                server.players.elementAt(0).ps.println("YOUR TURN!");
                server.players.elementAt(1).ps.println("WAIT FOR OPPONENT MOVE!");
            }

        }
    }
}
