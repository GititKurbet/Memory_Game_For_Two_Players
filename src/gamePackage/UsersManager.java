package gamePackage;

import leaderBoard.LeaderBoard;

import javax.swing.*;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class UsersManager implements Serializable {
    static UsersTable usersTable;
    static ArrayList<User> loggedIn; // TODO: change to set
    protected static int size;

    public UsersManager(int size){
        usersTable = new UsersTable(size);
        loggedIn = new ArrayList<>();
        this.size = size;
    }


    /*
    public void showLeaderBoard(){

        Thread thread = new Thread() {
            @Override
            public void run() {
                LeaderBoard leaders = new LeaderBoard(usersTable);
                leaders.pack();
                leaders.setVisible(true);
                leaders.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        };
        thread.start();
    }
    */


    public static User insertUser(LogIn logIn) {

        String name = logIn.name;
        char[] password = logIn.password;
        boolean isNew = logIn.isNew;

        if (name == null)
            return null;

        User user;

        if (!isNew) {
            user = usersTable.getUser(name);
            if (user == null) {
                return null;
            }

            if ( (!user.checkPassword(password)) || (loggedIn.contains(user)) ) {
                return null;
            }

        }
        else{
            user = new User(name, password);

            if (usersTable.exist(name)) {
                return null;
            }
            usersTable.add(user, true);
        }

        loggedIn.add(user);
        System.out.println("Players in : " + loggedIn.toString());
        return user;
    }

    public synchronized void updateUser(String name, boolean victory) {

        //Update current user
        User user = usersTable.getUser(name);
        if (victory)
            user.addVictory();
        else
            user.addLose();
        updateTable();
    }

    public void updateDraw(String name) {
        usersTable.getUser(name).addDraw();
        updateTable();
    }

    public void updateTable(){
        System.out.println("Server : update table . . .");
        usersTable.updateOnFile();
    }

    public synchronized void userLogOut(String name) {

        Iterator<User> itr = loggedIn.iterator();
        while (itr.hasNext()) {
            User user = itr.next();
            if (user.getName().equals(name)) {
                itr.remove();
            }
        }

        System.out.println(name + " has left the game");
        System.out.println("Players in : " + loggedIn.toString());
    }

    /*
    @Override
    public void passObject(UsersManager manager, ObjectOutputStream[] outputStreams, int playerNum) {
        //this object only sent by game thread, no need to pass it between two players
    }

    @Override
    public void receive(GameGUI game) {
        System.out.println("****** UsersManager currently : " + usersTable.myTable.size() + " Users , " +
        loggedIn.size() + " logged in now");
        showLeaderBoard();
    }
    */
}
