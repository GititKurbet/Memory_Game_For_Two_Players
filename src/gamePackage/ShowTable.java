package gamePackage;

import leaderBoard.LeaderBoard;

import javax.swing.*;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ShowTable implements Serializable, MyInterface, ServerConnections {
    UsersManager manager;

    @Override
    public void passObject(UsersManager manager, ObjectOutputStream[] outputStreams, int playerNum) {
        this.manager = manager;
        System.out.println("Show leaderboard");

        UsersTable currentTable = new UsersTable(4);

        try {
            outputStreams[playerNum - 1].writeObject(currentTable);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error with write object of ShowTable");
        }
    }

    @Override
    public void receive(GameGUI game) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                LeaderBoard leaders = new LeaderBoard(manager.usersTable);
                leaders.pack();
                leaders.setVisible(true);
                leaders.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        };
        thread.start();
    }

    @Override
    public void respond(ObjectOutputStream out, UsersManager manager) throws IllegalArgumentException {
        this.manager = manager;
        System.out.println("Show leaderboard");

        UsersTable currentTable = new UsersTable(4);

        try {
            out.writeObject(currentTable);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error with write object of ShowTable");
        }
    }
}
