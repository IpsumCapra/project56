package main.java.sensordata.sadd;

import main.java.sensordata.sadd.database.UserInfo;
import main.java.sensordata.sadd.pages.LoginPage;

public class Main {

    public static void main(String[] args) {
        UserInfo userInfo = new UserInfo();

        LoginPage loginPage = new LoginPage(userInfo.getLoginInfo());
    }
}

