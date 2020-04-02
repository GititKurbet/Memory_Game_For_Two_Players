import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

interface CallBack {
    void methodToCallBack(int player, int index, boolean end, boolean draw, int playerNum);
}

public class GameGUI extends JFrame implements ActionListener{
    private int playerNum;
    private JPanel board;
    private JPanel scoring;
    private JLabel player1;
    private JLabel player2;
    private JLabel moves1JL;
    private JLabel moves2JL;
    private JLabel turn;
    private JTextArea info;
    private JButton firstButton, secondButton, startOver;
    private String firstPhoto, secPhoto;
    private File file;
    private int size;
    private int scoring1, scoring2, openCards, moves1, moves2;
    private ArrayList<JButton> buttons;
    private ArrayList<Integer> photosForGame;
    private ArrayList<String> usedPhotos;
    private Client player;
    public boolean currentTurn;
    private boolean endOfGame, draw;

    public void clickOnCard(int index){

        System.out.println("New click received");
        Timer timer = new Timer(1500, this);
        Icon icon = new ImageIcon( getClass().getResource(photosForGame.get(index)+".png"));

        openCards += 1;
        if (openCards == 1) {
            System.out.println("index clicked is " + index);
            firstPhoto = Integer.toString(photosForGame.get(index)) + ".png";
            firstButton = buttons.get(index);

            firstButton.setIcon( icon) ;
        }
        else if (openCards == 2) {
            System.out.println("index clicked is " + index);
            secPhoto = Integer.toString(photosForGame.get(index)) + ".png";
            secondButton = buttons.get(index);

            secondButton.setIcon( icon );
            //clicking the same card is unavailable
            if ( firstButton == secondButton ) {
                openCards -= 1;
                return;
            }

            timer.start();

            moves2 += 1;
            openCards = 0;
            moves2JL.setText("moves : " + moves2);
        }

    }

    public void actionPerformed(ActionEvent e) {
        Icon def = new ImageIcon(getClass().getResource("default.png"));
        Icon blank = new ImageIcon( getClass().getResource("blank.png"));

        //clickOnCard(buttons.indexOf(e.getSource()));

        if ( firstPhoto.equals(secPhoto)) {
            if (currentTurn) {
                scoring1 += 1;
                player1.setText("Player 1 : " + scoring1);
            } else {
                scoring2 += 1;
                player2.setText( "Player 2 : " + scoring2);
            }
            firstButton.setIcon(blank);
            secondButton.setIcon(blank);

            usedPhotos.add(firstPhoto);

            if ( (scoring1 + scoring2) == (size*size / 2) ){
                if ( scoring1 > scoring2 )
                    JOptionPane.showMessageDialog(null, "Bravo! \nYOU WON!! ");
                if ( scoring1 < scoring2 )
                    JOptionPane.showMessageDialog(null, "competitor won .... ");
                if ( scoring2 == scoring1 ) {
                    JOptionPane.showMessageDialog(null, "- It's a draw  -");
                    draw = true;
                }
                endOfGame = true;
                register(player,0);
            }
        }
        else {
            firstButton.setIcon(def);
            secondButton.setIcon(def);
        }
        if ( currentTurn ) {
            currentTurn = false;
            addMessage("It's competitor turn");
            turn.setText(" ");
        }
        else if (!currentTurn) {
            currentTurn = true;
            turn.setText("It's your turn !");
        }
        ((Timer)e.getSource()).stop();

    }

    private void toStartOver(int size) {
        Timer timer = new Timer(1500, this);
        Icon def = new ImageIcon(getClass().getResource("default.png"));

        //add cards to board JPanel
        for( int i = 0; i < (size*size) ; i++ ) {
            Icon icon = new ImageIcon( getClass().getResource(photosForGame.get(i)+".png"));
            JButton photo = new JButton();
            photo.setBorder(BorderFactory.createLineBorder(Color.black));
            photo.setOpaque(true);
            photo.setIcon(def);
            buttons.add(photo);

            photo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //if not this player turn
                    if ( !currentTurn )
                        return;

                    //cant view any other card while view two other cards
                    if ( timer.isRunning() )
                        return;

                    //cant view card that we already find his couple
                    if ( usedPhotos.contains( new File(icon.toString()).getName()) )
                        return;

                    photo.setBackground(Color.WHITE);
                    photo.setIcon(icon);
                    file = new File( photo.getIcon().toString());
                    openCards += 1;

                    if (openCards == 1) {
                        firstPhoto = file.getName();
                        firstButton = photo;
                    }
                    else if (openCards == 2) {
                        secPhoto = file.getName();
                        secondButton = photo;


                        //clicking the same card is unavailable
                        if ( firstButton == secondButton ) {
                            openCards -= 1;
                            return;
                        }

                        timer.start();

                        moves1 += 1;
                        openCards = 0;
                        moves1JL.setText("moves : " + moves1);
                    }

                    //Send the click only if it's legal
                    register(player,buttons.indexOf(photo));

                }
            });

            board.add(photo);
        }
    }

    //set and add the scoring of players
    private void createScoringJPanel() {
        scoringSetText();

        scoring.add(player1);
        scoring.add(moves1JL);
        scoring.add(player2);
        scoring.add(moves2JL);
        scoring.add(turn);
    }

    //Set scoring of two players and number of moves
    private void scoringSetText() {
        if (currentTurn)
            turn.setText("It's your turn");
        else turn.setText(" ");
        player1.setText("Player 1 : " + scoring1);
        moves1JL.setText("moves : " + moves1);
        moves2JL.setText("moves : " + moves2);
        player2.setText("Player 2 : " + scoring2);
    }

    public void addMessage(String message) {
        info.append("*" + "\n");
        info.append(message + "\n");
    }

    private void register(CallBack callback,int index) {
        callback.methodToCallBack(playerNum ,index, endOfGame, draw, playerNum);
    }

    public GameGUI(int size,int playerNum, ArrayList<Integer> cardsOrder, int firstTurn) {
        buttons = new ArrayList<>();
        usedPhotos = new ArrayList<>();
        photosForGame = new ArrayList<>();
        this.size = size;
        this.playerNum = playerNum;
        openCards = 0;
        scoring1 = scoring2 = 0;
        moves1 = moves2 = 0;
        player = new Client();

        currentTurn = (firstTurn == playerNum);

        photosForGame.addAll(cardsOrder);

        JPanel information = new JPanel();
        info = new JTextArea("Welcome                 \n");
        addMessage("You are player 1,\ncompetitor is player 2");
        if (!currentTurn)
            addMessage("It's competitor turn");
        addMessage("I am player number " + playerNum);
        info.setBackground(new Color(190, 215, 156));
        Border lowered = BorderFactory.createLoweredBevelBorder();
        TitledBorder title = BorderFactory.createTitledBorder(
                lowered, "Massages");
        title.setTitlePosition(TitledBorder.ABOVE_TOP);
        info.setBorder(title);
        info.setEditable(false);
        information.add(info);

        //initialize components
        player1 = new JLabel();
        player2 = new JLabel();
        moves1JL = new JLabel();
        moves2JL = new JLabel();
        turn = new JLabel();
        startOver = new JButton("Start over");

        //these are the two JPanels that are in the frame
        board = new JPanel();
        board.setLayout(new GridLayout(size, size, 4,4));
        board.setBackground(new Color(190, 215, 156));
        scoring = new JPanel();
        scoring.setLayout(new GridLayout(3, 2));
        scoring.setBackground(new Color(190, 215, 156));

        //Start the game
        toStartOver(size);

        createScoringJPanel();

        //add the panels to the frame
        add(board, BorderLayout.CENTER);
        add(scoring, BorderLayout.SOUTH);
        add(information, BorderLayout.EAST);
    }


}
