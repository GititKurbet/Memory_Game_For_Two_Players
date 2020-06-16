package gamePackage;

import java.io.ObjectOutputStream;
import java.io.Serializable;

public class EndOfGame implements Serializable, MyInterface {
    String name;
    boolean win;
    boolean draw;
    boolean logOut;
    boolean toPass;


    public EndOfGame(String name , boolean win, boolean draw, boolean toPass, boolean logOut){
        this.name = name;
        this.win = win;
        this.draw = draw;
        this.toPass = toPass;
        this.logOut = logOut;
    }

    @Override
    public void passObject(UsersManager manager, ObjectOutputStream[] outputStreams, int playerNum) {
        System.out.println("Processing end of game ...");

        if (draw){
            manager.updateDraw(name);
            return;
        }

        //Player exit while game is not completed - should inform the other player
        if ( toPass ) {
            try {
                outputStreams[2 - playerNum].writeObject(this);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error with write object of endGame");
            }
            manager.userLogOut(name);
        }

        manager.updateUser(name, win);

    }

    @Override
    public void receive(GameGUI game) {
        if ( logOut ) {
            game.competitorLeft();
        }
    }

    @Override
    public String toString() {
        return name + "send end of game";
    }
}
