import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client implements CallBack{
    static GameInfo myGame = null;
    static ObjectOutputStream out = null;
    static ObjectInputStream in = null;

    public void methodToCallBack(int player, int index, boolean end, boolean draw, int playerNum) {
//        System.out.println("Player number " + myGame.playerNumber + " : I've been called back");
        if (end){
            if (draw)
                myGame.winning = 3;
            else
                myGame.winning = playerNum;
            return;
        }
        Click current = new Click(index);
        try {
            out.writeObject(current);
            System.out.println("Player number " + myGame.playerNumber + " send a click");
        }
        catch (Exception e) { e.printStackTrace();}
    }

    public static void main(String[] args) {
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
            System.out.println("I am player number " + myGame.playerNumber );

            GameGUI myGameGui = new GameGUI(myGame.size, myGame.playerNumber, myGame.cardsOrder, myGame.firstTurn);
            myGameGui.pack();
            myGameGui.setVisible(true);
            myGameGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            while (myGame.winning == 0) {
                System.out.println("it is " + (myGameGui.currentTurn ? "" : "not") + " my turn");
//                if (!myGameGui.currentTurn) {
//                    System.out.println("Not my turn");
                Click newClick = (Click) in.readObject();
                System.out.println("Player number " + myGame.playerNumber + " receive a click");

                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        myGameGui.addMessage("Competitor clicked");
                        myGameGui.clickOnCard(newClick.index);
                    }
                });
//                }
            }

            out.close();
            in.close();
            gameSocket.close();

        }catch ( Exception e ) {
            e.printStackTrace();
            System.exit(1);
        }


    }

}
