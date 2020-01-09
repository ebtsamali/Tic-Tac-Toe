package gameserver;

import com.sun.javaws.util.JfxHelper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class Client 
{
    Socket mySocket;
    DataInputStream dataInputStream;
    PrintStream printStream;
    
    public Client()
    {
        try
        {
            mySocket = new Socket("127.0.0.1", 8090);
            dataInputStream = new DataInputStream(mySocket.getInputStream());
            printStream = new PrintStream(mySocket.getOutputStream());
            printStream.println("Hello form Client");
            
        }
        catch (IOException e) {
            System.out.println("aaaaaaa");
        }

    }
    
    public static void main(String[] args)
    {
        Client c1 = new Client();

    }

}
