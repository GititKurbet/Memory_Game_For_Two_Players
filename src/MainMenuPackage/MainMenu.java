package MainMenuPackage;

import gamePackage.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

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

            player = new Client();

            ActionListener checkUser = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if ( e.getSource() == signInButton ) {
                        isNew = false;
                    }
                    if ( e.getSource() == registerButton ) {
                        isNew = true;
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
                    sendShowTable(player);
                    };
            });
        }

    private void sendShowTable(MenuCall player) {
        player.sendShowTable();
    }

    private void createUIComponents() { }
}
