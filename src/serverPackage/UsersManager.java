package serverPackage;

import communication.LogIn;
import communication.User;
import communication.UsersTable;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;
/*
this class responsible for manage users log in and log out
and to update their scores in the file
 */
public class UsersManager implements Serializable {
    static UsersTable usersTable;
    static Set<User> loggedIn;
    public static int size;

    public UsersManager(int size){
        usersTable = new UsersTable(size);
        loggedIn = new TreeSet<>();
        this.size = size;
    }

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

        loggedIn.remove(usersTable.getUser(name));

        System.out.println(name + " has left the game");
        System.out.println("Players in : " + loggedIn.toString());
    }

}
