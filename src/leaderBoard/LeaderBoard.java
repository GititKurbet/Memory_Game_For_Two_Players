package leaderBoard;

import gamePackage.User;
import gamePackage.UsersTable;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.ArrayList;

public class LeaderBoard extends JFrame{
    JLabel title, rate, name, victories, loses, perWins;
    JPanel table;
    ArrayList<User> users;
    final Font BIG_TITLE = new Font("bigTitle", Font.BOLD,36);
    final Font TITLE = new Font("title", Font.TRUETYPE_FONT,24);
    final Font INFO = new Font ("info", Font.TRUETYPE_FONT,20);
    final Color BACKGROUND = new Color(255,232,232);

    public LeaderBoard(UsersTable usersTable){
        if (usersTable != null) {
            this.users = usersTable.getMyTable();
        }

        Border border = new MatteBorder(1, 0, 0, 0, Color.BLACK);

        rate = new JLabel("Rate");
        name = new JLabel("Name");
        victories = new JLabel("Victories");
        loses = new JLabel("Loses");
        perWins = new JLabel("Percent Of Wins");
        title = new JLabel("LeaderBoard", SwingConstants.CENTER);
        table = new JPanel();

        table.setLayout(new GridLayout(0, 5, 0, 2));
        title.setFont(BIG_TITLE);
        rate.setFont(TITLE);
        name.setFont(TITLE);
        victories.setFont(TITLE);
        loses.setFont(TITLE);
        perWins.setFont(TITLE);

        table.setBackground(BACKGROUND);
        title.setBackground(BACKGROUND);

        add(table, BorderLayout.CENTER);
        add(title, BorderLayout.NORTH);

        table.add(rate);
        table.add(name);
        table.add(victories);
        table.add(loses);
        table.add(perWins);

        if ( usersTable == null ) {
            JLabel message = new JLabel("No rating to view");
            message.setFont(INFO);
            add(message, BorderLayout.SOUTH);
            return;
        }


        //Show top 5
        for ( int i = 1; i < 6 ; i ++ ) {
            if (users.size() < i)
                return;
            User current = users.get(i-1);
            JLabel number = new JLabel(String.valueOf(i));
            JLabel newName = new JLabel(current.getName());
            JLabel vic = new JLabel(String.valueOf(current.getVictories()));
            JLabel lo = new JLabel(String.valueOf(current.getLoses()));
            JLabel per = new JLabel(String.valueOf(current.getPerWins()) +"%");

            number.setFont(INFO);
            newName.setFont(INFO);
            vic.setFont(INFO);
            lo.setFont(INFO);
            per.setFont(INFO);

            number.setBorder(border);
            newName.setBorder(border);
            vic.setBorder(border);
            lo.setBorder(border);
            per.setBorder(border);

            table.add(number);
            table.add(newName);
            table.add(vic);
            table.add(lo);
            table.add(per);


        }



    }

    public static void main(String[] args) {
        UsersTable myTable = new UsersTable(4);

        // TODO: no GUI from server
        LeaderBoard frame = new LeaderBoard(myTable);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
