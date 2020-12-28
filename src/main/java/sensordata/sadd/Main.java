package main.java.sensordata.sadd;


import main.java.sensordata.sadd.pages.HomePage;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        CardLayout layout = new CardLayout();
        JPanel container = new JPanel(layout);

        HomePage homePage = new HomePage(layout, container);

        JFrame frame = new JFrame("Home Page");

        frame.setContentPane(homePage.homePage);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Voor full screen, gebruik dit:
        // frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // frame.setUndecorated(true);

        frame.setPreferredSize(new Dimension(800, 600));
        frame.pack();
        frame.setVisible(true);
    }
}

