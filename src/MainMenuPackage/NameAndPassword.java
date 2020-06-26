package MainMenuPackage;

import gamePackage.Client;
import gamePackage.MenuCall;
import javafx.geometry.VerticalDirection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class NameAndPassword extends JFrame {
    JLabel please, nameJL, passwordJL;
    JPanel mainPanel, bottomPanel;
    JTextField userName;
    JPasswordField password;
    JButton go, back;
    Client player;
    boolean isNew;
    final Color BG = new Color(229,247,252);
    final Font MESSAGE = new Font("message", Font.TRUETYPE_FONT,16);

    public NameAndPassword(boolean isNew) {

        this.isNew = isNew;

        player = new Client();

        if (isNew) {
            please = new JLabel("   Please insert new username & password   ");
            passwordJL = new JLabel("Password : (Up to 6 characters)");
        }
        else {
            please = new JLabel("   Please insert your username & password   ");
            passwordJL = new JLabel("Password : ");
        }



        please.setFont(new Font("title", Font.BOLD,30));

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0,2,2,15));
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(0,1));

        nameJL = new JLabel("User name : ");
        nameJL.setFont(MESSAGE);

        passwordJL.setFont(MESSAGE);
        userName = new JTextField();
        password = new JPasswordField();

        mainPanel.add(nameJL);
        mainPanel.add(userName);
        mainPanel.add(passwordJL);
        mainPanel.add(password);

        go = new JButton("OK");
        go.setFont(MESSAGE);
        go.setAlignmentX(Component.CENTER_ALIGNMENT);

        back = new JButton("Back to main menu");
        back.setFont(MESSAGE);
        back.setAlignmentX(Component.CENTER_ALIGNMENT);

        bottomPanel.add(go);
        bottomPanel.add(back);

        bottomPanel.setBackground(BG);
        mainPanel.setBackground(BG);
        please.setBackground(BG);
        please.setOpaque(true);

        setLayout(new BorderLayout(0,10));
        setBackground(BG);
        add(please, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        go.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = userName.getText();
                char[] pass = password.getPassword();

                if (pass.length > 6) {
                    JOptionPane.showMessageDialog(null, "Password is too long");
                    return;
                }
                sendLogIn(player, name, pass, isNew);

            }
        });

        //activate go button when pressing enter
        getRootPane().setDefaultButton(go);
    }

    private void sendLogIn(MenuCall player, String name, char[] password, boolean isNew) {
        Thread thread = new Thread() {
            @Override
            public void run() {
               player.menuCommunication(name,password, isNew);
            }
        };
        thread.start();
        dispose();
    }
}
