package serverPackage;

import communication.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static int size, numberOfPlayers;
    public static UsersManager manager;
    static ObjectInputStream[] inputStreams;
    static ObjectOutputStream[] outputStreams;
    static User[] users ;

    public static void main(String[] args) {

        size = 6;

        manager = new UsersManager(size);
        numberOfPlayers = 0;

        ServerSocket serverSocket = null;
        Socket socket1 = null;
        Socket socket2 = null;

        //create severSocket
        try {
            serverSocket = new ServerSocket(7777);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        //Ready for clients
        System.out.println("Server ready");

        inputStreams = new ObjectInputStream[2];
        outputStreams = new ObjectOutputStream[2];

        users = new User[2];
        boolean success;
        boolean listening = true;

        while (listening) {
                try {
                    numberOfPlayers += 1;
                    if ((numberOfPlayers % 2) != 0) {
                        socket1 = serverSocket.accept();
                        System.out.println("Request accepted");

                        outputStreams[0] = new ObjectOutputStream(socket1.getOutputStream());
                        inputStreams[0] =  new ObjectInputStream(socket1.getInputStream());

                        success = accept(inputStreams[0], outputStreams[0], socket1, 0);
                        if ( ! success ) {
                            socket1.close();
                            numberOfPlayers -= 1;
                            continue;
                        }

                    }
                    if ((numberOfPlayers % 2) == 0) {
                        socket2 = serverSocket.accept();
                        System.out.println("Request accepted");

                        outputStreams[1] = new ObjectOutputStream(socket2.getOutputStream());
                        inputStreams[1] = new ObjectInputStream(socket2.getInputStream());

                        success = accept(inputStreams[1], outputStreams[1], socket2, 1);
                        if ( ! success ) {
                            socket2.close();
                            numberOfPlayers -= 1;
                            continue;
                        }

                        try {
                            System.out.println("number of players now : " + numberOfPlayers);

                            System.out.println("$$$$ START NEW GAME $$$$");
                            new GameThread(size, inputStreams, outputStreams, users, manager).start();

                            inputStreams = new ObjectInputStream[2];
                            outputStreams = new ObjectOutputStream[2];
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("First player quit before game began");
                            outputStreams[0] = outputStreams[1];
                            inputStreams[0] = inputStreams[1];
                            users[0] = users[1];
                            numberOfPlayers -= 1;
                        }
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

    private static boolean accept(ObjectInputStream input, ObjectOutputStream output,Socket socket, int playerNum) {

        boolean success = false;

        try {
            Object received = input.readObject();

            if (received instanceof SentFromUser) {
                ( (SentFromUser)received).passObject(manager,outputStreams, playerNum+1);
                return success;
            }

            users[playerNum] = manager.insertUser((LogIn) received);
            if (users[playerNum] != null)
                success = true;

            output.writeObject(success);
            if ( success )
                System.out.println("Successful log in : " + users[playerNum].getName());

        } catch (Exception e) {
            System.out.println("Error with log in");
            return false;
        }

        return success;
    }
}
