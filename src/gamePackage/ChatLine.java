package gamePackage;

import javax.swing.*;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ChatLine implements Serializable, MyInterface {
    String line;
    boolean toPass;

    public ChatLine(String line, boolean toPass){
        this.line = line;
        this.toPass = toPass;
    }


    @Override
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

    @Override
    public void receive(GameGUI game) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
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
