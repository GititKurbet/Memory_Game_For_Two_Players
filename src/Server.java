import javax.naming.ldap.SortKey;
import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) {
        boolean listening = true;
        int size = 0;
        while ( (size != 4) && (size != 6) ) {
            System.out.println("Enter size of board : 4 or 6");
            Scanner scan = new Scanner(System.in);
            size = scan.nextInt();
        }

        int numberOfPlayers = 0;

        ServerSocket serverSocket = null;

        //create severSocket
        try {
            serverSocket = new ServerSocket(7777);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        //Ready for clients
        System.out.println("Server ready");
        Socket socket1 = null;
        Socket socket2 = null;

        while (listening) {
            try {
                numberOfPlayers += 1;
                if ( (numberOfPlayers % 2) != 0 ) {
                    socket1 = serverSocket.accept();
                    System.out.println("Client arrived");
                }
                //For each two players - open a new game room
                if ( (numberOfPlayers % 2) == 0 ) {
                    socket2 = serverSocket.accept();
                    System.out.println("Client arrived");
                    new GameThread(size, socket1, socket2).start();


                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }



        //close streams
        try {
            socket1.close();

            socket2.close();
            serverSocket.close();
        }
        catch ( IOException e) {
            System.err.println("Close streams failed");
            System.exit(1);
        }

    }


}
