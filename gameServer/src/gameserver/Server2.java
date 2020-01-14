package gameserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server2{
    private ServerSocket serverSocket;
    private BufferedReader buffer;
    private PrintStream printStream;
    private Vector<PlayerInfo> connectors = new Vector<PlayerInfo>();
    private Vector<String> players = new Vector<String>();

    public int i = 0;  //will be removed
    //public String userName = "nour";  //will be removed
    public String[] userName = {"nour","ebtsam","heba"};//will be removed

    public Server2() {
        try {
            serverSocket = new ServerSocket(5000);
            while (true) {
                new PlayerHandler(serverSocket.accept());
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }

    private class PlayerHandler extends Thread {
        PlayerInfo playerInfo = new PlayerInfo();

        public PlayerHandler(Socket socket) {
            playerInfo.playerSocket = socket;
            playerInfo.playerId = i; // id of user, will be changed
            playerInfo.name = userName[i]; // name of user, will be changed
            try {
                buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                printStream = new PrintStream(socket.getOutputStream());
                connectors.add(playerInfo);
                start();
                i++;  //will be removed
                //userName = "ebtsam"; //will be removed
            }
            catch (IOException e) {
                e.getMessage();
            }
        }

        @Override
        public void run(){
            while (true) {
                try {

                    String input = buffer.readLine();
                    System.out.println("input : "+input);  // input == (message, sender, receiver)
                    printStream.flush();

                    String[] inputArr = input.split(",");
                    System.out.println(inputArr[0]);

                    // if input request to play with someone (1 mean request)
                    if(inputArr[0].equals("1")){
                        // function check if sender and receiver is not already playing
                        // 1 mean one of them or both is already playing, 0 mean the two are not playing
                        int checkResult = inSessionCheck(inputArr[1], inputArr[2]);

                        if (checkResult == 0){
                            players.add(inputArr[1]);
                            players.add(inputArr[2]);
                            ivitationRequest(inputArr[1], inputArr[2]);  // inputArr[1] >> request sender, inputArr[2] >> receiver
                        }
                        else{
                            // complete if they are already playing
                            notAllowed(inputArr[1]);
                        }
                    }

                    if (inputArr[0].equals("true") || inputArr[0].equals("false")){
                        response(inputArr[0], inputArr[1], inputArr[2]);
                    }
                }
                catch (IOException e) {
                    e.getMessage();
                }
            }
        }
    }



    public class PlayerInfo {
        private Socket playerSocket = new Socket();
        private int playerId;
        private  String name;

        public PlayerInfo() {
        }
    }

    // function check if sender and receiver is not already playing
    // 1 mean one of them or both is already playing, 0 mean the two are not playing
    public int inSessionCheck(String sender, String receiver){
        int result = 0;
        for(String player : players){
            if (player.equals(sender) || player.equals(receiver)){
                result = 1;
                break;
            }
        }
        return result;
    }

    public void ivitationRequest(String sender, String receiver){
        int senderIndex;
        int receiverIndex;
        for(int index=0; index<connectors.size(); index++){
            if (connectors.get(index).name.equals(sender)){
                senderIndex = index;
                try {
                    PrintStream printP0 = new PrintStream(connectors.get(senderIndex).playerSocket.getOutputStream());
                    printP0.println("invitation sent");
                    printP0.flush();
                }
                catch (IOException e){
                    e.getMessage();
                }
            }

            if (connectors.get(index).name.equals(receiver)){
                receiverIndex = index;
                try {
                    PrintStream printP1 = new PrintStream(connectors.get(receiverIndex).playerSocket.getOutputStream());
                    printP1.println("invitation,"+sender);
                    printP1.flush();
                }
                catch (IOException e){
                    e.getMessage();
                }
            }
        }
    }

    public void notAllowed(String sender){
        int senderIndex;
        for(int index=0; index<connectors.size(); index++) {
            if (connectors.get(index).name.equals(sender)) {
                senderIndex = index;
                try {
                    PrintStream printP0 = new PrintStream(connectors.get(senderIndex).playerSocket.getOutputStream());
                    printP0.println("Not Allowed");
                    printP0.flush();
                } catch (IOException e) {
                    e.getMessage();
                }
            }
        }
    }

    public void response(String reply, String sender, String receiver){
        int senderIndex = 0;
        int receiverIndex = 0;
        for(int index=0; index<connectors.size(); index++) {
            if (connectors.get(index).name.equals(sender)) {
                senderIndex = index;
            }

            if (connectors.get(index).name.equals(receiver)) {
                receiverIndex = index;
            }
        }
        if (reply.equals("true")){
            try {
                PrintStream printP0 = new PrintStream(connectors.get(receiverIndex).playerSocket.getOutputStream());
                PrintStream printP1 = new PrintStream(connectors.get(senderIndex).playerSocket.getOutputStream());
                printP0.println("game will start");
                printP1.println("game will start");
                printP0.flush();
                printP1.flush();
            }
            catch (IOException e){
                e.getMessage();
            }

        }
        else if(reply.equals("false")){
            try {
                PrintStream printP0 = new PrintStream(connectors.get(receiverIndex).playerSocket.getOutputStream());
                printP0.println("refuse,"+sender);
                printP0.flush();
            }
            catch (IOException e){
                e.getMessage();
            }
        }
    }
}
