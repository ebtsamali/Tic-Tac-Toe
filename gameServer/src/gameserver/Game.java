/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameserver;

/**
 *
 * @author mena
 */
public class Game {

    Player player1;
    Player player2;
    boolean turn = true;
    public String[] gameBoardArr = new String[9];

    public Game(Player p1) {
        player1 = p1;
        for (int i = 0; i < gameBoardArr.length; i++) {
            gameBoardArr[i] = new String();
            gameBoardArr[i] = i + "";
        }
    }

    public Game() {
        for (int i = 0; i < gameBoardArr.length; i++) {
            gameBoardArr[i] = new String();
            gameBoardArr[i] = i + "";
        }
    }

    public String makeMove(int index) {
        if (turn) {
            gameBoardArr[index] = "X";
        } else {
            gameBoardArr[index] = "O";
        }
        turn = !turn;
        return gameOver();
    }

    public String gameOver() {
        String winner = "noWin";
        if (gameBoardArr[0].equals(gameBoardArr[1]) && gameBoardArr[0].equals(gameBoardArr[2])) {
            winner = gameBoardArr[0];
        } else if (gameBoardArr[3].equals(gameBoardArr[4]) && gameBoardArr[3].equals(gameBoardArr[5])) {
            winner = gameBoardArr[3];
        } else if (gameBoardArr[6].equals(gameBoardArr[7]) && gameBoardArr[6].equals(gameBoardArr[8])) {
            winner = gameBoardArr[6];
        } else if (gameBoardArr[0].equals(gameBoardArr[3]) && gameBoardArr[0].equals(gameBoardArr[6])) {
            winner = gameBoardArr[0];
        } else if (gameBoardArr[1].equals(gameBoardArr[4]) && gameBoardArr[1].equals(gameBoardArr[7])) {
            winner = gameBoardArr[1];
        } else if (gameBoardArr[2].equals(gameBoardArr[5]) && gameBoardArr[2].equals(gameBoardArr[8])) {
            winner = gameBoardArr[2];
        } else if (gameBoardArr[0].equals(gameBoardArr[4]) && gameBoardArr[0].equals(gameBoardArr[8])) {
            winner = gameBoardArr[0];
        } else if (gameBoardArr[2].equals(gameBoardArr[4]) && gameBoardArr[2].equals(gameBoardArr[6])) {
            winner = gameBoardArr[2];
        }
        if (winner.equals("noWin")) {
            boolean thereIsEmptyCell = false;
            for (int i = 0; i < gameBoardArr.length; i++) {
                if (!((gameBoardArr[i].equals("X")) || (gameBoardArr[i].equals("O")))) {
                    thereIsEmptyCell = true;
                }
            }
            if (!thereIsEmptyCell) {
                winner = "end";
            }
        }
        return winner;

    }
}
