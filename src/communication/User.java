package communication;

import java.io.Serializable;

public class User implements Comparable, Serializable{
    private String name;
    private char[] password;
    private double victories, loses, perWins;

    public User(String name, char[] password){
        this.name = name;
        this.password = password;
        victories = 0;
        loses = 0;
        perWins = 0;
    }

    public User(String name, double victories, double loses, char[] password){
        this.name = name;
        this.password = password;
        this.victories = victories;
        this.loses = loses;
        calculatePerWins();
    }

    public char[] getPassword() {
        return password;
    }

    public double getPerWins() {
        return perWins;
    }

    public double getVictories(){
        return victories;
    }

    public double getLoses(){
        return loses;
    }

    public String getName() {
        return name;
    }

    public boolean checkPassword(char[] newPass) {
        if ( password.length != newPass.length )
            return false;
        for ( int i = 0 ; i < password.length ; i ++ ) {
            if ( password[i] != newPass[i] )
                return false;
        }
        return true;
    }

    public void calculatePerWins(){
        double sumOfGames = victories + loses;
        double sum = sumOfGames;
        if (sum == 0) {
            perWins = 0;
            return;
        }
        perWins = (victories*  100) / sum ;
        perWins = Math.floor(perWins * 100) / 100;
    }


    public void addVictory(){
        victories += 1;
        calculatePerWins();
    }

    public void addLose(){
        loses += 1;
        calculatePerWins();
    }

    public void addDraw(){
        loses += 0.5;
        victories += 0.5;
        calculatePerWins();
    }

    @Override
    public int compareTo(Object o) {
        User other = (User) o;
        if ( this.perWins == other.perWins)
            return 0;
        if ( this.perWins < other.perWins)
            return 1;
        else return -1;
    }

    public String toString() {
        return name;
    }
}
