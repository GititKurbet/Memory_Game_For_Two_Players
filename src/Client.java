import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements CallBack{
    static GameInfo myGame = null;
    static ObjectOutputStream out = null;
    static ObjectInputStream in = null;

    public void methodToCallBack(int player, int phtIndex, int btnIndex) {
        System.out.println("Player number " + myGame.playerNumber + " : I've been called back");
        Click current = new Click(phtIndex, btnIndex);
        try {out.writeObject(current);}
        catch (Exception e) { e.printStackTrace();}
    }

    public static void main (String[] args) {
        Socket gameSocket = null;
        String host = "localhost";


        System.out.println("Waiting...");

        try {
            gameSocket = new Socket(host,7777);

            out = new ObjectOutputStream(gameSocket.getOutputStream());
            in = new ObjectInputStream(gameSocket.getInputStream());

            System.out.println("After connection");

            myGame = (GameInfo) in.readObject();

            System.out.println("New game has began !\nGame size : " + myGame.size );


            GameGUI myGameGui = new GameGUI(myGame.size, myGame.playerNumber, myGame.cardsOrder);
            if ( myGame.playerNumber == myGame.turn )
                myGameGui.currentTurn = true;
            else myGameGui.currentTurn = false;
            myGameGui.pack();
            myGameGui.setVisible(true);
            myGameGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            myGameGui.addMassage("You are player " + myGame.playerNumber);

            while ( myGame.winning == 0 ){
                if ( myGameGui.currentTurn == false) {
                    Click newClick = (Click) in.readObject();
                    myGameGui.addMassage("Competitor clicked");
                    System.out.println("Competitor clicked");
                }
            }

            out.close();
            in.close();
            gameSocket.close();

        }catch ( Exception e ) {
            e.printStackTrace();
            System.exit(1);
        }


    }

    public interface onClick{

    }

}
