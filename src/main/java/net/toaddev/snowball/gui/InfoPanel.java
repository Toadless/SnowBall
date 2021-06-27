package net.toaddev.snowball.gui;

import net.toaddev.snowball.main.BotController;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {

    public InfoPanel()
    {
        super();
        JTextArea text = new JTextArea();
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setEditable(false);

        text.append("Snowball\n");
        text.append("by Toadless");

        JScrollPane pane = new JScrollPane();
        pane.setViewportView(text);

        super.setLayout(new GridLayout(1,1));
        super.add(pane);

        super.setPreferredSize(new Dimension(400,300));
    }
}
