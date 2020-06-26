package gamePackage;

import java.io.ObjectOutputStream;
import java.io.Serializable;

public class LogOut implements Serializable, SentFromUser {
    String name;
    boolean toPass;

    public LogOut(boolean toPass, String name){
        this.toPass = toPass;
        this.name = name;
    }


    @Override
    public void passObject(UsersManager manager, ObjectOutputStream[] outputStreams, int playerNum) {
        System.out.println("LOG OUT");
        if ( toPass ) {
            try {
                outputStreams[2 - playerNum].writeObject(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        manager.userLogOut(name);
        System.out.println("[ " + name + " ]  logged out.");
    }

    @Override
    public void receive(GameGUI game) {
        game.competitorLeft();
    }

    @Override
    public String toString() {
        return "received log out";
    }
}
