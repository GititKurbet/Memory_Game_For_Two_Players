package communication;
import clientPackage.GameGUI;
import serverPackage.SentFromUser;
import serverPackage.UsersManager;

import javax.swing.*;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Click implements Serializable, SentFromUser {
    int index;
    int clicked;
    boolean toPass;


    public Click(int index, int clicked, boolean toPass) {
        this.index = index;
        this.clicked = clicked;
        this.toPass = toPass;
    }

    @Override
    public void passObject(UsersManager manager, ObjectOutputStream[] outputStreams, int playerNum) {
        System.out.println("Number of clicks : " + clicked +"\nCurrent turn : " + playerNum );

        System.out.println("Player number " + playerNum + " send a click");
        if ( toPass ) {
            try {
                outputStreams[2 - playerNum].writeObject(this);
            } catch (Exception e) {
                System.out.println("@@@@@@@@@@@@@@@ END OF GAME @@@@@@@@@@@@@@@");
            }
        }
        System.out.println("Sent to player number " + (3-playerNum));
        if (this != null)
            clicked +=1;

        /*
        //After two legal clicks - moving to next turn
        if ( clicked == 2 ) {
            turn = 3 - turn;
            clicked = 0;
        }
        */
    }

    @Override
    public void receive(final GameGUI game) {
        System.out.println("Click received");

        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    game.clickOnCard(index);
                }
            });
        } catch ( Exception e ) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public String toString() {
        return "received click";
    }
}
