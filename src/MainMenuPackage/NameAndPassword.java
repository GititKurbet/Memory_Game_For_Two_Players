package MainMenuPackage;

import gamePackage.Client;
import gamePackage.MenuCall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

public class NameAndPassword extends JFrame {
    JLabel please, nameJL, passwordJL;
    JPanel mainPanel;
    JTextField userName;
    JPasswordField password;
    JButton go;
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

        nameJL = new JLabel("User name : ");
        nameJL.setFont(MESSAGE);

        passwordJL.setFont(MESSAGE);
        userName = new JTextField();
        password = new JPasswordField();

        mainPanel.add(nameJL);
        mainPanel.add(userName);
        mainPanel.add(passwordJL);
        mainPanel.add(password);

        go = new JButton("go");
        go.setFont(MESSAGE);

        mainPanel.setBackground(BG);
        please.setBackground(BG);
        please.setOpaque(true);

        setLayout(new BorderLayout(0,10));
        setBackground(BG);
        add(please, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(go, BorderLayout.SOUTH);


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
