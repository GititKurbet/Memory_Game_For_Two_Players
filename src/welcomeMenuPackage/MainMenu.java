package welcomeMenuPackage;

import gamePackage.*;
import leaderBoard.LeaderBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class MainMenu extends JFrame{
    public JPanel mainPanel;
    private Client player;
    private JButton signInButton;
    private JButton register;
    private JButton leaderBoard;
    private JButton exit;
    private JLabel gameJL;
    private JLabel orJL;
    private JLabel welcomeJL;
    private JPanel bottomPanel;

        public MainMenu() {
            player = new Client();

            signInButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String name = JOptionPane.showInputDialog(null, "Enter your username (must be unique)");
                    register(player, name, true);
                }
            });
            register.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    String name = JOptionPane.showInputDialog(null, "Enter your username");
                    register(player, name, false);
                }
            });
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

    private void register(MenuCall callback, String name, boolean isNew) {
            // TODO: close menu
        Thread thread = new Thread() {
            @Override
            public void run() {
                callback.menuCommunication(name, isNew);
            }
        };
        thread.start();
    }

        private void createUIComponents() {
        // TODO: place custom component creation code here


    }
}
