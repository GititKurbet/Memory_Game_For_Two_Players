package gamePackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

public class WaitingGUI extends JFrame {
    JLabel nameJL;
    JLabel waitingJL;
    final Color BG = new Color(229,247,252);

    public WaitingGUI(String name) {

        nameJL = new JLabel(name);
        nameJL.setFont( new Font("name", Font.TRUETYPE_FONT, 44));
        nameJL.setBackground(BG);
        nameJL.setOpaque(true);

        waitingJL = new JLabel("Waiting for another player......");
        waitingJL.setFont( new Font("waiting", Font.TRUETYPE_FONT, 34));
        waitingJL.setBackground(BG);
        waitingJL.setOpaque(true);

        add(nameJL, BorderLayout.NORTH);
        add(waitingJL, BorderLayout.CENTER);
    }
}
