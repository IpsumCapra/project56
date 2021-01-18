package main.java.sensordata.sadd;


import main.java.sensordata.sadd.pages.HomePage;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        CardLayout layout = new CardLayout();
        JPanel container = new JPanel(layout);

        UserInfo userInfo = new UserInfo();

        HomePage homePage = new HomePage(layout, container);
        LoginPage loginPage = new LoginPage(userInfo.getLoginInfo(), layout, container);
        ResetPage resetPage = new ResetPage(layout, container);
        RegisterPage registerPage = new RegisterPage(layout, container);
        NewPassword password = new NewPassword(resetPage.getTextField2(), layout, container);

        container.add("login", loginPage.loginPanel);
        container.add("register", registerPage.registerPanel);
        container.add("reset", resetPage.resetPagePanel);
        container.add("newPassword", password.newPasswordPanel);
        container.add("home", homePage.homePage);

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

