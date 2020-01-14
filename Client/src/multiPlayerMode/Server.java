/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multiPlayerMode;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private ServerSocket serverSocket;
    public Vector<Player> players;
    private int playerNumber = 1;
    public String[] gameBoardArr;
    
    public int counter = 1;

    
    
    public Server() {
        gameBoardArr = new String[9];
        for(int i = 0; i<9; i++){
    gameBoardArr[i] = String.valueOf(i);}
        try {
            serverSocket = new ServerSocket(5005);
            players = new Vector<>();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (true) {
            try {
                Socket s = serverSocket.accept();
                new Player(s, playerNumber, this);
                playerNumber++;
                if (playerNumber > 2) {
                    playerNumber = 1;
                }
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void checkPlayerMove(int buttonNumberforArr) {
        counter++;
            gameBoardArr[buttonNumberforArr] = (counter%2 == 0 ? "X" : "O");
    }

    public String gameOver() {
        String winner = "no winner";
        if (gameBoardArr[0] == gameBoardArr[1] && gameBoardArr[0] == gameBoardArr[2]) {
            winner = gameBoardArr[0];
        } else if (gameBoardArr[3] == gameBoardArr[4] && gameBoardArr[3] == gameBoardArr[5]) {
            winner = gameBoardArr[3];
        } else if (gameBoardArr[6] == gameBoardArr[7] && gameBoardArr[6] == gameBoardArr[8]) {
            winner = gameBoardArr[6];
        } else if (gameBoardArr[0] == gameBoardArr[3] && gameBoardArr[0] == gameBoardArr[6]) {
            winner = gameBoardArr[0];
        } else if (gameBoardArr[1] == gameBoardArr[4] && gameBoardArr[1] == gameBoardArr[7]) {
            winner = gameBoardArr[1];
        } else if (gameBoardArr[2] == gameBoardArr[5] && gameBoardArr[2] == gameBoardArr[8]) {
            winner = gameBoardArr[2];
        } else if (gameBoardArr[0] == gameBoardArr[4] && gameBoardArr[0] == gameBoardArr[8]) {
            winner = gameBoardArr[0];
        } else if (gameBoardArr[2] == gameBoardArr[4] && gameBoardArr[2] == gameBoardArr[6]) {
            winner = gameBoardArr[2];
        }
            return winner;

        }


    public static void main(String[] args) {
        new Server();

    }

}

