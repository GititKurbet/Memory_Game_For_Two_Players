import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GameThread extends Thread{
    private Socket socket1;
    private Socket socket2;
    private int size;

    public GameThread(int size, Socket socket1, Socket socket2) {
        this.socket1 = socket1;
        this.socket2 = socket2;
        this.size = size;
        ObjectInputStream[] inputStreams = new ObjectInputStream[2];
        ObjectOutputStream[] outputStreams = new ObjectOutputStream[2];

        System.out.println("Thread created");


        ObjectOutputStream out1 = null;
        ObjectInputStream in1 = null;
        ObjectOutputStream out2 = null;
        ObjectInputStream in2 = null;

        try {
            out1 = new ObjectOutputStream(socket1.getOutputStream());
            in1 = new ObjectInputStream(socket1.getInputStream());
            outputStreams[0] = out1;
            inputStreams[0] = in1;

            out2 = new ObjectOutputStream(socket2.getOutputStream());
            in2 = new ObjectInputStream(socket2.getInputStream());
            outputStreams[1] = out2;
            inputStreams[1] = in2;

            GameInfo newGame = new GameInfo(size);
            newGame.currentPlayer(1); //Player 1
            out1.writeObject(newGame);
            newGame.currentPlayer(2); // Player 2
            out2.writeObject(newGame);

            int turn = newGame.turn;

            while ( newGame.winning == 0 ) {
                Click newClick = (Click) inputStreams[turn-1].readObject();
                outputStreams[2-turn].writeObject(newClick);

                //turn = 3 - turn;
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }


/*
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            GameInfo game = (GameInfo) in.readObject();


         catch(Exception e) {}
        */
    }

    @Override
    public void run() {
        System.out.println("Thread start running");
    }
}
