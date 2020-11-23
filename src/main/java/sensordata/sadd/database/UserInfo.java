package main.java.sensordata.sadd.database;

import java.util.HashMap;

public class UserInfo {

    HashMap<String, String> loginInfo = new HashMap<String, String>();

    public UserInfo(){
        loginInfo.put("Owen", "wachtwoord");
        loginInfo.put("Klaas", "morghe");
        loginInfo.put("Naam", "woord");
    }

    public HashMap getLoginInfo() {
        return loginInfo;
    }
}
