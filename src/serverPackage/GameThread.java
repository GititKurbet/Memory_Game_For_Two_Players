package serverPackage;

import communication.*;
import java.io.*;
import java.net.SocketException;

public class GameThread extends Thread{
    private final GameInfo newGame;
    private final ObjectInputStream[] inputStreams;
    private final ObjectOutputStream[] outputStreams;
    private final UsersManager manager;


    public GameThread(int size, ObjectInputStream[] inputStreams , ObjectOutputStream[] outputStreams,
                      User[] users , UsersManager manager) throws Exception {
        System.out.println("@@@ Game thread created @@@");

        this.inputStreams = inputStreams;
        this.outputStreams = outputStreams;
        this.manager = manager;

        newGame = new GameInfo(size, users);
        newGame.currentPlayer(1); //Player 1
        outputStreams[0].writeObject(newGame);
        newGame.currentPlayer(2); // Player 2
        outputStreams[1].writeObject(newGame);

    }

    public void run() {
        System.out.println("@@@ Game thread start running @@@");

        Thread listenToFirst = new Thread(){
            public void run() {
                try {
                    receive(newGame,1);
                } catch (EndOfInputException e) {
                    System.out.println("Player number 1 is out ");
                    return;
                }
            }
        };

        Thread listenToSecond = new Thread() {
            public void run() {
                try {
                    receive(newGame,2);
                } catch (EndOfInputException e) {
                    System.out.println("Player number 2 is out ");
                    return;
                }

            }
        };

        listenToFirst.start();
        listenToSecond.start();


    }

    public void receive(GameInfo newGame, int playerNum) throws EndOfInputException {
        while ( true ) {
            try {
                SentFromUser received = (SentFromUser) inputStreams[playerNum - 1].readObject();
                System.out.println("...Object received...");
                received.passObject(manager, outputStreams, playerNum );

            } catch (SocketException e) {
                //if player logout
                throw new EndOfInputException();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

    }

}
