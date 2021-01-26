package main.java.sensordata.sadd.pages;

import main.java.sensordata.sadd.Main;
import main.java.sensordata.sadd.database.UserInfo;
import main.java.sensordata.sadd.pages.Page;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Base64;
import java.util.HashMap;

public class LoginPage extends Page implements ActionListener {
    public JPanel loginPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JButton loginButton;
    private JLabel resetLabel;
    private JLabel registerLabel;
    private JLabel messageLabel;
    private JButton registerButton;

    private static final String LOGIN = "login";
    private static final String REGISTER = "register";

    HashMap<String, String> loginInfo = new HashMap<String, String>();

    public LoginPage(HashMap loginInf, CardLayout cards, Container parent) {
        super(cards, parent);
        loginInfo = loginInf;

        loginButton.setActionCommand(LOGIN);
        loginButton.addActionListener(this);
        usernameField.setActionCommand(LOGIN);
        usernameField.addActionListener(this);
        passwordField.setActionCommand(LOGIN);
        passwordField.addActionListener(this);

        registerButton.setActionCommand(REGISTER);
        registerButton.addActionListener(this);

        resetLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                getCards().show(getParent(), "reset");
            }
        });
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case LOGIN:
                String username_input = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());
                UserInfo email = new UserInfo();

                try {
                    //If the user has inserted username, then get the email from this username
                    if(email.get("email", username_input)==null)
                    {
                        username_input = email.getEmail("username", username_input);
                        System.out.print(username_input);
                    }
                    email.get("email", username_input);
                    email.salting("email", username_input);


                    byte[] salt = Base64.getDecoder().decode(email.getSalt());
                    if (email.getSecurePassword(password, salt).equals(email.getpassword())) {

                        Main main = new Main();
                        String username= email.getUsername("email", username_input);
                        System.out.print(username);
                        main.updateHomePage(username_input,username);
                        getCards().show(getParent(), "home");
                        usernameField.setText("");
                        passwordField.setText("");
                        messageLabel.setText("");
                    } else {
                        wrongPasswordOrUsername();

                    }
                } catch (Exception E) {
                }
                break;

            case REGISTER:
                getCards().show(getParent(), "register");
                break;
        }
    }

    public void wrongPasswordOrUsername() {
        messageLabel.setForeground(Color.red);
        messageLabel.setText("Gebruikersnaam en/of wachtwoord incorrect");
        usernameField.setText("");
        passwordField.setText("");
    }
}
