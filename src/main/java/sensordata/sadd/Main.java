package main.java.sensordata.sadd;

import main.java.sensordata.sadd.database.UserInfo;
import main.java.sensordata.sadd.pages.HomePage;
import main.java.sensordata.sadd.pages.LoginPage;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        //UserInfo userInfo = new UserInfo();

        //LoginPage loginPage = new LoginPage(userInfo.getLoginInfo());
        HomePage homePage = new HomePage();

        JFrame frame = new JFrame("Home Page");
        frame.setContentPane(homePage.homePage);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

