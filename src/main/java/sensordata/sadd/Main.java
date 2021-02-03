package main.java.sensordata.sadd;


import main.java.sensordata.sadd.database.UserInfo;
import main.java.sensordata.sadd.pages.*;
import javax.swing.*;
import java.awt.*;


public class Main {
    //create a new jPanel and layout
    static CardLayout layout = new CardLayout();
    static JPanel container = new JPanel(layout);

    public static void main(String[] args) {

        //create pages
        UserInfo userInfo = new UserInfo();
        LoginPage loginPage = new LoginPage(userInfo.getLoginInfo(), layout, container);
        ResetPage resetPage = new ResetPage(layout, container);
        RegisterPage registerPage = new RegisterPage(layout, container);

        //add the pages to a jPanel
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

    //create a new page for adding password
    public void updateNewPassword(String email)
    {
        NewPassword password = new NewPassword(email, layout, container);
        container.add("newPassword", password.newPasswordPanel);

    }

    //create a new HomePage
    public void updateHomePage(String email,String username)
    {
        HomePage homePage = new HomePage(email,username, layout, container);
        container.add("home", homePage.homePage);

    }
}

