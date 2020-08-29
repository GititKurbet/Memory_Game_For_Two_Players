package communication;

import clientPackage.GameGUI;
import serverPackage.SentFromUser;
import serverPackage.UsersManager;

import javax.swing.*;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ChatLine implements Serializable, SentFromUser {
    String line;
    boolean toPass;

    public ChatLine(String line, boolean toPass){
        this.line = line;
        this.toPass = toPass;
    }

    public void passObject(UsersManager manager, ObjectOutputStream[] outputStreams, int playerNum) {
        if ( toPass ) {
            try {
                System.out.println("Got chat line [[ " + line + " ]]");
                outputStreams[2 - playerNum].writeObject(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Chat line successfully sent");
        }
    }

    public void receive(final GameGUI game) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    game.addMessage(line);
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public String toString() {
        return "CHAT LINE received =" + line;
    }
}
