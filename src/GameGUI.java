import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

interface CallBack {
    void methodToCallBack(int player, int phtIndex, int btnIndex);
}

public class GameGUI extends JFrame implements ActionListener{
    int playerNum;
    JPanel board;
    JPanel scoring;
    JLabel player1;
    JLabel player2;
    JLabel moves1JL;
    JLabel moves2JL;
    JLabel turn;
    JTextArea info;
    JButton firstButton, secondButton, startOver;
    String firstPhoto;
    File file;
    int size;
    int scoring1, scoring2, openCards, moves1, moves2;
    ArrayList<Integer> photosForGame;
    ArrayList<String> usedPhotos;
    Client player;
    ArrayList<JButton> buttons;
    boolean currentTurn;

    public void actionPerformed(ActionEvent e) {
        Icon def = new ImageIcon(getClass().getResource("default.png"));
        Icon blank = new ImageIcon( getClass().getResource("blank.png"));

        if ( firstPhoto.equals(file.getName())) {
            scoring1 += 1;
            player1.setText("Player 1 : " + scoring1);
            firstButton.setIcon(blank);
            secondButton.setIcon(blank);

            usedPhotos.add(firstPhoto);

            if ( (scoring1 + scoring2) == (size*size / 2) ){
                JOptionPane.showMessageDialog(null, "Bravo!");
            }
        }
        else {
            firstButton.setIcon(def);
            secondButton.setIcon(def);
        }
        ((Timer)e.getSource()).stop();

    }

    public void toStartOver(int size) {
        Timer timer = new Timer(1500, this::actionPerformed);
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
                    if ( currentTurn == false )
                        return;

                    //cant view any other card while view two other cards
                    if ( timer.isRunning() )
                        return;

                    //cant view card that we already find his couple
                    if ( usedPhotos.contains( new File(icon.toString()).getName()) )
                        return;

                    register(player, photosForGame.indexOf(icon.toString()), buttons.indexOf(photo));
                    photo.setBackground(Color.WHITE);
                    photo.setIcon(icon);
                    file = new File( photo.getIcon().toString());
                    openCards += 1;

                    if (openCards == 1) {
                        firstPhoto = file.getName();
                        firstButton = photo;
                    }
                    else if (openCards == 2) {
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

                }
            });

            board.add(photo);
        }
    }

    //set and add the scoring of players
    public void createScoringJPanel() {
        scoringSetText();

        turn.setText("It's your turn");
        scoring.add(player1);
        scoring.add(moves1JL);
        scoring.add(player2);
        scoring.add(moves2JL);
        scoring.add(turn);
    }

    //Set scoring of two players and number of moves
    public void scoringSetText() {
        player1.setText("Player 1 : " + scoring1);
        moves1JL.setText("moves : " + moves1);
        moves2JL.setText("moves : " + moves2);
        player2.setText("Player 2 : " + scoring2);
    }

    public void addMassage(String massage) {
        info.append(massage + "\n");
    }

    public void register(CallBack callback,int phtIndex,int btnIndex) {
        callback.methodToCallBack(playerNum , phtIndex ,btnIndex);
    }

    public GameGUI(int size,int playerNum, ArrayList<Integer> cardsOrder) {
        buttons = new ArrayList<>();
        usedPhotos = new ArrayList<>();
        photosForGame = new ArrayList<>();
        this.size = size;
        this.playerNum = playerNum;
        openCards = 0;
        scoring1 = scoring2 = 0;
        moves1 = moves2 = 0;
        player = new Client();

        for( int i = 0 ; i < cardsOrder.size() ; i++ )
            photosForGame.add( cardsOrder.get(i) );

        //Create massages JPanel
        JPanel information = new JPanel();
        info = new JTextArea("Welcome                 \n");
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
