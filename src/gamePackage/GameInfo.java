package gamePackage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/*
This class is the communication between the server and the client
include information about each game : cards order , current turn and scoring
 */
public class GameInfo  implements Serializable{
    ArrayList<Integer> cardsOrder;
    int playerNumber;
    int size;
    int scoring1, scoring2, moves1, moves2;
    User[] users;
    int firstTurn;

    //constructor : create a new game room
    public GameInfo(int size, User[] users) {
        this.size = size;
        this.users = users;


        cardsOrder = new ArrayList<>();
        scoring1 = scoring2 = 0;
        moves1 = moves2 = 0;
        playerNumber = 1;

        //Randomly choose who get the first turn
        Random random = new Random();
        firstTurn = random.nextInt(2) + 1;

        for (int i = 1; i <= (size*size)/2 ; i++ ) {
            cardsOrder.add(i);
            cardsOrder.add(i);
        }
        System.out.println(cardsOrder.toString());
        Collections.shuffle(cardsOrder);

    }

    public void currentPlayer(int num) {
        playerNumber = num;
    }

}
