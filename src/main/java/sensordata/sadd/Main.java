package main.java.sensordata.sadd;


import main.java.sensordata.sadd.pages.HomePage;
import main.java.sensordata.sadd.pages.LoginPage;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        CardLayout layout = new CardLayout();
        JPanel container = new JPanel(layout);

        HomePage homePage = new HomePage(layout, container);
        LoginPage loginPage = new LoginPage(new HashMap<String, String>(), layout, container);

        container.add("homepage", homePage.homePage);
        container.add("loginpage", loginPage.frame);

        JFrame frame = new JFrame("Home Page");

        frame.setContentPane(container);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Voor full screen, gebruik dit:
        // frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // frame.setUndecorated(true);

        frame.setPreferredSize(new Dimension(800, 600));
        frame.pack();
        frame.setVisible(true);
    }
}

