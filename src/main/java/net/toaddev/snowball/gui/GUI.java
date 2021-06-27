package net.toaddev.snowball.gui;

import net.toaddev.snowball.main.BotController;
import net.toaddev.snowball.main.SnowBall;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class GUI {
    private final ConsolePanel console;
    private final InfoPanel info;
    private final JFrame frame;

    public GUI()
    {
        System.setProperty("java.awt.headless", "false");
        console = new ConsolePanel();
        info = new InfoPanel();
        frame = new JFrame();
    }

    public void init()
    {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("Snowball");

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Console", console);
        tabs.add("Info", info);

        frame.getContentPane().add(tabs);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addWindowListener(new WindowListener()
        {
            @Override public void windowOpened(WindowEvent e) { /* unused */ }
            @Override public void windowClosing(WindowEvent e)
            {
                try
                {
                    BotController.shutdown(0);
                }
                catch(Exception ex)
                {
                    System.exit(0);
                }
            }
            @Override public void windowClosed(WindowEvent e) { /* unused */ }
            @Override public void windowIconified(WindowEvent e) { /* unused */ }
            @Override public void windowDeiconified(WindowEvent e) { /* unused */ }
            @Override public void windowActivated(WindowEvent e) { /* unused */ }
            @Override public void windowDeactivated(WindowEvent e) { /* unused */ }
        });
    }
}