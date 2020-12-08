import java.util.HashMap;

public class UserInfo {

    HashMap<String, String> loginInfo = new HashMap<String, String>();

    UserInfo(){
        loginInfo.put("Owen", "wachtwoord");
        loginInfo.put("Klaas", "morghe");
        loginInfo.put("Naam", "woord");
    }

    protected HashMap getLoginInfo() {
        return loginInfo;
    }
}
