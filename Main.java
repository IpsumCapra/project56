public class Main {

    public static void main(String[] args) {
        UserInfo userInfo = new UserInfo();

        LoginPage loginPage = new LoginPage(userInfo.getLoginInfo());
    }
}

