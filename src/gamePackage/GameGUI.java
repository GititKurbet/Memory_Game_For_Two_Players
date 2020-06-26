package gamePackage;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

interface GameCall {
    void gameCommunications( int index, int openCards, boolean toPass);
    void sendChatMessage(String message, boolean toPass);
    void sendEndOfGame(boolean win, boolean draw, boolean toPass, boolean logOut);
    void logOut(boolean toPass);
    void showLeaderBoard();
}

public class GameGUI extends JFrame implements ActionListener{
    private JPanel board;
    private JPanel scoring;
    private JPanel chatPanel;
    private JLabel player1;
    private JLabel player2;
    private JLabel moves1JL;
    private JLabel moves2JL;
    private JLabel turn;
    private JLabel chatTitle;
    private JTextField writeField;
    private JTextArea messages;
    private JButton firstButton, secondButton;
    private String firstPhoto, secPhoto;
    private File file;
    private int size;
    private int scoring1, scoring2, openCards, moves1, moves2;
    private ArrayList<JButton> buttons;
    private ArrayList<Integer> photosForGame;
    private ArrayList<String> usedPhotos;
    private Client player;
    public boolean currentTurn;
    private boolean draw, competitorHere;
    private String name1, name2;
    private User myAccount;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem logOut, leaderBoard , myDetails;

    final Color PRIMARY_COLOR = new Color(190, 215, 156);
    final Font MESSAGE = new Font("message", Font.TRUETYPE_FONT,16);

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

        if ( firstPhoto.equals(secPhoto)) {
            if (currentTurn) {
                scoring1 += 1;
                player1.setText(name1 + " : " + scoring1);
            } else {
                scoring2 += 1;
                player2.setText( name2 + " : " + scoring2);
            }
            firstButton.setIcon(blank);
            secondButton.setIcon(blank);

            usedPhotos.add(firstPhoto);

            if ( (scoring1 + scoring2) == (size*size / 2) ){
                boolean victory = false;
                if ( scoring1 > scoring2 ) {
                    JOptionPane.showMessageDialog(null, "Bravo! " + "\n" + name1 + " WON!! ");
                    victory = true;
                    addMessage("YOU WON!");
                    myAccount.addVictory();
                }
                if ( scoring1 < scoring2 ) {
                    JOptionPane.showMessageDialog(null, name2 + " won .... ");
                    addMessage("Opponent won ...");
                    myAccount.addLose();
                }
                if ( scoring2 == scoring1 ) {
                    JOptionPane.showMessageDialog(null, "- It's a draw  -\n" +
                            "Each player get 0.5 point for victories\nand 0.5 point for loses.");
                    addMessage("It's a draw\nEach player get 0.5 point for victories\nand 0.5 point for loses.");
                    myAccount.addDraw();
                    draw = true;
                }
                competitorHere = false;

                //send call to server : update scores
                sendEnd(player, name1,victory,draw, false);

                //no need to update scores again
                addMessage("** Log out to start a new game **");
            }
        }
        else {
            firstButton.setIcon(def);
            secondButton.setIcon(def);
        }
        if ( currentTurn ) {
            currentTurn = false;
            turn.setText("It's " + name2 + " turn");
            turn.setForeground(Color.BLACK);
        }
        else if (!currentTurn) {
            currentTurn = true;
            turn.setText("It's your turn !");
            turn.setForeground(Color.RED);
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
                        //openCards = 0;
                        moves1JL.setText("moves : " + moves1);
                    }

                    //Send the click only if it's legal
                    register(player,buttons.indexOf(photo), openCards);
                    if (openCards == 2){
                        openCards = 0;
                    }

                }
            });

            board.add(photo);
        }
    }

    public void addMessage(String message) {
        messages.append(message + "\n");
    }

    public void sendMessage(GameCall callback, String message){
        callback.sendChatMessage(message, competitorHere);
    }

    private void register(GameCall callback, int index, int openCards) {
        callback.gameCommunications(index, openCards, competitorHere);
    }


    public void logOut(GameCall callback){
        callback.logOut(competitorHere);
    }


    private void sendEnd(GameCall callback,String name, boolean win, boolean draw, boolean logOut){
        callback.sendEndOfGame( win, draw, competitorHere, logOut);
    }

    private void showLeaderBoard(GameCall callback){
        callback.showLeaderBoard();
    }

    public void competitorLeft() {
        competitorHere = false;
        JOptionPane.showMessageDialog(null, name2 + " has left the game\nYOU WON!");
        addMessage(name2 + " has left the game\nYOU WON!");
        addMessage("** Log out to start a new game **");
        myAccount.addVictory();
        sendEnd(player, name1, true, false, false);
    }

    public GameGUI(User[] users, int size,int playerNum, ArrayList<Integer> cardsOrder, int firstTurn){
        competitorHere = true;

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(competitorHere){
                    sendEnd(player, name1, false, false, true);
                } else logOut(player);
            }
        });

        myAccount = users[playerNum-1];

        name1 = myAccount.getName();
        name2 = users[2-playerNum].getName();

        //set icon of game window
        URL iconURL = getClass().getResource("9.png");
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage());

        //initialize components
        buttons = new ArrayList<>();
        usedPhotos = new ArrayList<>();
        photosForGame = new ArrayList<>();
        this.size = size;
        openCards = 0;
        scoring1 = scoring2 = 0;
        moves1 = moves2 = 0;
        player = new Client();

        currentTurn = (firstTurn == playerNum);

        photosForGame.addAll(cardsOrder);

        chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());

        chatTitle = new JLabel("Live Chat");
        chatTitle.setFont(new Font("message", Font.TRUETYPE_FONT,22));
        setBackground(new Color(190, 215, 156));
        chatTitle.setForeground(Color.BLACK);
        writeField = new JTextField(15);
        writeField.setEditable(true);
        writeField.setFont(MESSAGE);

        messages = new JTextArea();
        addMessage("Welcome");
        JScrollPane sp = new JScrollPane(messages);
        messages.setColumns(20);
        if (size == 4)
            messages.setRows(15);
        else if (size == 6)
            messages.setRows(27);

        messages.setBackground(new Color(190, 215, 156));
        Border lowered = BorderFactory.createLoweredBevelBorder();
        messages.setBorder(lowered);
        messages.setEditable(false);
        messages.setFont(MESSAGE);

        writeField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String line = writeField.getText();
                addMessage("me : " + line);
                sendMessage(player, line);
                writeField.setText("");
            }
        });

        chatPanel.add(chatTitle, BorderLayout.NORTH);
        chatPanel.add(sp, BorderLayout.CENTER);
        chatPanel.add(writeField, BorderLayout.SOUTH);

        //initialize components
        player1 = new JLabel();
        player2 = new JLabel();
        moves1JL = new JLabel();
        moves2JL = new JLabel();
        turn = new JLabel();
        turn.setForeground(Color.RED);

        //these are the two JPanels that are in the frame
        board = new JPanel();
        board.setLayout(new GridLayout(size, size, 4,4));
        board.setBackground(PRIMARY_COLOR);
        scoring = new JPanel();
        scoring.setLayout(new GridLayout(3, 2));
        scoring.setBackground(PRIMARY_COLOR);

        menuBar = new JMenuBar();
        menu = new JMenu("Game");
        logOut = new JMenuItem("Log out");
        leaderBoard = new JMenuItem("Show high scores");
        myDetails = new JMenuItem("Show my account");
        menu.add(logOut);
        menu.add(leaderBoard);
        menu.add(myDetails);
        menuBar.add(menu);

        logOut.addActionListener (new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(competitorHere){
                    sendEnd(player, name1, false, false, true);
                } else logOut(player);
                dispose();
            }
        });

        leaderBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLeaderBoard(player);
            }
        });

        myDetails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, name1 + "\nVictories : " + myAccount.getVictories() + "\nLoses : "
                        + myAccount.getLoses() + "\nWins percent : " + myAccount.getPerWins());
            }
        });

        //Start the game
        toStartOver(size);

        //set and add the scoring of players
        if (currentTurn)
            turn.setText("It's your turn");
        else {
            turn.setText("It's " + name2 + " turn");
            turn.setForeground(Color.BLACK);
        }
        player1.setText(name1 +" : " + scoring1);
        moves1JL.setText("moves : " + moves1);
        moves2JL.setText("moves : " + moves2);
        player2.setText(name2 + " : " + scoring2);

        player1.setFont(MESSAGE);
        moves1JL.setFont(MESSAGE);
        moves2JL.setFont(MESSAGE);
        player2.setFont(MESSAGE);

        scoring.add(player1);
        scoring.add(moves1JL);
        scoring.add(player2);
        scoring.add(moves2JL);
        scoring.add(turn);

        //add the panels to the frame
        add(menuBar, BorderLayout.NORTH);
        add(board, BorderLayout.CENTER);
        add(scoring, BorderLayout.SOUTH);
        add(chatPanel, BorderLayout.EAST);
    }


}
