package MainMenuPackage;

import gamePackage.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame{
    public JPanel mainPanel;
    JFrame logInFrame;
    private Client player;
    private JButton signInButton;
    private JButton registerButton;
    private JButton leaderBoard;
    private JButton exit;
    private JLabel gameJL;
    private JLabel orJL;
    private JLabel welcomeJL;
    private JPanel bottomPanel;
    boolean isNew;

        public MainMenu() {

            ActionListener checkUser = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if ( e.getSource() == signInButton ) {
                        isNew = true;
                    }
                    if ( e.getSource() == registerButton ) {
                        isNew = false;
                    }

                    Thread thread = new Thread(){
                        @Override
                        public void run() {
                            logInFrame = new NameAndPassword(isNew);
                            logInFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                            logInFrame.pack();
                            logInFrame.setVisible(true);
                        }
                    };

                    thread.start();
                }
            };

            signInButton.addActionListener(checkUser);
            registerButton.addActionListener(checkUser);

            exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

            leaderBoard.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    Server server = Server.getInstance();
                    server.showLeaderBoard();
                    };
            });
        }

    /*
        private void sendUserName(MenuCall callback, String name, char[] password, boolean isNew) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    callback.menuCommunication(name,password, isNew);
                }
            };
          thread.start();
        }


        /*
        @Override
        public void sendLogIn(String name, char[] password, boolean isNew) {
                sendUserName(player, name, password, isNew);
        }

    */
    private void createUIComponents() {
        // TODO: place custom component creation code here


    }
}
