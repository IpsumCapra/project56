package main.java.sensordata.sadd;


import main.java.sensordata.sadd.pages.HomePage;

import javax.swing.*;
import java.awt.*;


public class Main {

    static CardLayout layout = new CardLayout();
    static JPanel container = new JPanel(layout);

    public static void main(String[] args) {
        UserInfo userInfo = new UserInfo();

        LoginPage loginPage = new LoginPage(userInfo.getLoginInfo(), layout, container);
        ResetPage resetPage = new ResetPage(layout, container);
        RegisterPage registerPage = new RegisterPage(layout, container);


        container.add("login", loginPage.loginPanel);
        container.add("register", registerPage.registerPanel);
        container.add("reset", resetPage.resetPagePanel);


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
    public void updateNewPassword(String email)
    {
        NewPassword password = new NewPassword(email, layout, container);
        container.add("newPassword", password.newPasswordPanel);

    }

    public void updateHomePage(String email,String username)
    {
        HomePage homePage = new HomePage(email,username, layout, container);
        container.add("home", homePage.homePage);

    }
}

