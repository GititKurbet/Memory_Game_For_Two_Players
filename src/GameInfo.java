
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
    int firstTurn;
    int winning; //0 for if the game is still on, 1 for player 1 won, 2 for player 2 won, 3 for equal score

    //constructor : create a new game room
    public GameInfo(int size) {
        this.size = size;
        cardsOrder = new ArrayList<>();
        scoring1 = scoring2 = 0;
        moves1 = moves2 = 0;
        winning = 0;
        playerNumber = 1;

        //Randomly choose who get the first turn
        Random random = new Random();
        firstTurn = random.nextInt(2)+1;

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
