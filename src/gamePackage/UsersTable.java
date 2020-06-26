package gamePackage;
import javax.swing.*;
import java.io.File;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

/*
This class responsible of organized the users information
read end write from file
 */
public class UsersTable implements Serializable {
    ArrayList<User> myTable;
    transient Path path;
    transient File dataFile;
    transient String level;

    public ArrayList<User> getMyTable() {
        return myTable;
    }

    public UsersTable(int size){
        myTable = new ArrayList<>();
        if (size == 4)
            level = "Easy";
        else if (size == 6)
            level = "Hard";

        readPrevScoring();
    }

    //Reads best scoring from file
    public void readPrevScoring(){
        boolean exists = false;
        path = Paths.get("Scoring_Table_" + level + ".txt");
        dataFile = new File(path.toString());

        //read from file
        try {
            if ( dataFile.exists() ) {
                exists = true;

                Scanner reader = new Scanner( path );
                String line;
                while ( reader.hasNext() ) {
                    line = reader.nextLine();
                    parseLine(line);
                }
            }
            else exists = false;
        } catch (Exception e) { e.printStackTrace();}

        //if file does not exists - create one
        try {
            if ( !exists ) {
                Formatter scoringTable = new Formatter("Scoring_Table_" + level + ".txt");
            }
        }catch (Exception e) { e.printStackTrace();}
    }


    public void parseLine(String line) {
        String[] elements = line.split(":");
        try {
            if ( exist(elements[0]) ) {
                JOptionPane.showConfirmDialog(null,"Error in data file : duplicate user\n", "Error", JOptionPane.ERROR_MESSAGE);
                throw new InvalidUserNameException();
            }
            add( new User(elements[0], Double.parseDouble(elements[1]), Double.parseDouble(elements[2]), elements[3].toCharArray()), false);
        }catch (InvalidUserNameException e){
            System.out.println("Duplicate users in data file");
        }
    }

    public User getUser(String name){
        for (User user : myTable ){
            if (user.getName().equals(name))
                return user;
        }
        //if user does not exists in current table
        return null;
    }

    public void addToFile(User current) {
        String toAppend = current.getName() + ":" + current.getVictories() + ":" + current.getLoses() + ":" + String.valueOf(current.getPassword()) +"\n";
        try {
            Files.write(path,toAppend.getBytes() , StandardOpenOption.APPEND);
        } catch (Exception e) { e.printStackTrace();}
    }

    public void add(User current, boolean addToFile){

        myTable.add(current);
        if (addToFile) {
            addToFile(current);
        }
        Collections.sort(myTable);


        return;
    }

    public boolean exist(String name) {
        User user = getUser(name);

        if (user == null)
            return false;
        else return true;
    }

    public synchronized void updateOnFile() {
        deleteFileContent();

        for (User user : myTable)
            addToFile(user);

    }

    public void deleteFileContent() {
        PrintWriter writer = null;
        try {
             writer = new PrintWriter(new File(path.toString()));
        }catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        writer.print("");
        writer.close();
    }
}
