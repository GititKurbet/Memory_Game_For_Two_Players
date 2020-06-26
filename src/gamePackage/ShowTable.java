package gamePackage;

import java.io.ObjectOutputStream;
import java.io.Serializable;

/*
Object of communication
When user wants to see the high scores table he send this object to the server
the server send back to the user the table details
 */
public class ShowTable implements Serializable, SentFromUser{
    UsersManager manager;

    @Override
    public void passObject(UsersManager manager, ObjectOutputStream[] outputStreams, int playerNum) {
        this.manager = manager;
        System.out.println("Show leaderboard");

        UsersTable currentTable = new UsersTable(manager.size);

        try {
            outputStreams[playerNum - 1].writeObject(currentTable);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error with write object of ShowTable");
        }
    }

    @Override
    public void receive(GameGUI game) { }
}
