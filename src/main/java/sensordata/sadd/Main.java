package main.java.sensordata.sadd;

import main.java.sensordata.sadd.database.QueryDemo;
import main.java.sensordata.sadd.database.UserInfo;
import main.java.sensordata.sadd.pages.HomePage;
import main.java.sensordata.sadd.pages.LoginPage;

import javax.swing.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        QueryDemo queryDemo = new QueryDemo();
        //UserInfo userInfo = new UserInfo();

        //LoginPage loginPage = new LoginPage(userInfo.getLoginInfo());
        HomePage homePage = new HomePage();

        JFrame frame = new JFrame("Home Page");
        frame.setContentPane(homePage.homePage);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        System.out.println("rdy.");
        System.out.println("Query de database:");

        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("> ");
            String query = scanner.nextLine();
            if (query.equals("quit")) {
                System.out.println("Gestopt. Fijne dag.");
                return;
            }
            try {
                System.out.println(queryDemo.query(query));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

