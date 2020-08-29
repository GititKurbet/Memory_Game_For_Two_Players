package clientPackage;
import communication.*;
import serverPackage.SentFromUser;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client implements GameCall , MenuCall{
    static Socket gameSocket;
    static JFrame mainMenu;
    private GameGUI myGameGui;
    static WaitingGUI waiting;
    static GameInfo myGame = null;
    static ObjectOutputStream out = null;
    static ObjectInputStream in = null;
    static String name, host;
    boolean endOfGame;

    public void newLogIn(){

        Thread newLogIn = new Thread(){

            public void run() {
                startMenu();
            }
        };
        newLogIn.start();
    }

    //Player exit and want to log in with another user name
    public void logOut(boolean toPass) {
        endOfGame = true;

        System.out.println(name + " LOG OUT");
        try {
            out.writeObject(new LogOut(toPass, name));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        newLogIn();
    }

    //Player exit game
    public void sendEndOfGame( boolean win, boolean draw, boolean toPass, boolean logOut) {
        EndOfGame end = new EndOfGame(name ,  win, draw, toPass, logOut);
        endOfGame = true;

        try {
            out.writeObject(end);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        if (logOut){
            newLogIn();
        }
    }

    public void sendChatMessage(String message, boolean toPass) {

        try{
            out.writeObject(new ChatLine(name + " : " + message, toPass));
        }catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void showLeaderBoard() {
        try{
            out.writeObject(new ShowTable());
        }catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void sendShowTable() {
        connectToServer();

        showLeaderBoard();
        try{
            out.writeObject(new ShowTable());
        }catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        try{
            UsersTable table = (UsersTable) in.readObject();
            showLeaderBoard(table);
        }catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        closeStreams();

    }

    public void menuCommunication(String name, char[] password , boolean isNew) {
        this.name = name;
        System.out.println("Name is " + this.name);

        if (password.length == 0) {
            JOptionPane.showMessageDialog(null, " You must submit password");
            System.out.println("Login is invalid");
            return;
        }

        try {

            connectToServer();

            LogIn logIn = new LogIn(name, password, isNew);
            out.writeObject(logIn);
            boolean success = (boolean) in.readObject();
            if ( !success ) {
                mainMenu.dispose();
                startMenu();
                JOptionPane.showMessageDialog(null ," Incorrect password or username\nplease try again");
                System.out.println("Login is invalid");
                closeStreams();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error with writeObject !");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error with readObject !");
        }

        createGame();
    }

    public void showLeaderBoard(final UsersTable table){
        Thread thread = new Thread() {
            public void run() {
                LeaderBoard leaders = new LeaderBoard(table);
                leaders.pack();
                leaders.setVisible(true);
                leaders.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        };
        thread.start();
    }

    //client sent move to server
    public void gameCommunications(int index, int clickNum, boolean toPass) {
        Click current = new Click(index, clickNum, toPass);
        try {
            out.writeObject(current);
            System.out.println("Player number " + myGame.playerNumber + " send a click");
        }
        catch (Exception e) { e.printStackTrace();}
    }

    public void createGame(){
        endOfGame = false;

        mainMenu.dispose();

        try {

            waiting = new WaitingGUI(name);
            waiting.setSize(600,250);
            waiting.setVisible(true);
            waiting.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            myGame = (GameInfo) in.readObject();

            System.out.println("New game has began !\nGame size : " + myGame.size );
            System.out.println("I am player number " + myGame.playerNumber );

            User[] users = myGame.users;

            waiting.dispose();

            myGameGui = new GameGUI(users, myGame.size, myGame.playerNumber, myGame.cardsOrder, myGame.firstTurn);
            myGameGui.pack();
            myGameGui.setVisible(true);
            myGameGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            while ( !endOfGame ) {
                try {
                    Object received = in.readObject();
                    System.out.println(name + " receive object");

                    if ( received instanceof UsersTable ) {
                        showLeaderBoard((UsersTable) received);
                    } else {
                        ((SentFromUser) received).receive(myGameGui);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("@ " + users[myGame.playerNumber - 1] + " : End of input @" );
                    break;
                }
            }


        }catch ( Exception e ) {
            e.printStackTrace();
            System.exit(1);
        }


    }

    public static void main(String[] args) {

        if (args.length == 0)
            host = "localhost";
        else host = args[0];

        startMenu();

    }

    //start main menu
    public static void startMenu(){

        mainMenu = new JFrame("menu");
        mainMenu.setContentPane(new MainMenu().mainPanel);
        mainMenu.pack();
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu.setVisible(true);

    }

    //Client send request for server
    public static void connectToServer(){
        gameSocket = null;

        try{
            gameSocket = new Socket(host,7777);

            out = new ObjectOutputStream(gameSocket.getOutputStream());
            in = new ObjectInputStream(gameSocket.getInputStream());
        } catch (Exception e){
            e.printStackTrace();
            System.exit(0);
        }

        System.out.println("After connection");

        System.out.println("Waiting...");
    }

    //end of current communication
    public void closeStreams(){

        try {
            gameSocket.close();

            in.close();
            out.close();
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }

}
