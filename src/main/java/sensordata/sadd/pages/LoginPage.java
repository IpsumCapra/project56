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
    //<editor-fold desc="Swing elements">
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
    //</editor-fold>

    // Action commands.
    private static final String LOGIN = "login";
    private static final String REGISTER = "register";

    // create a HashMap
    HashMap<String, String> loginInfo = new HashMap<String, String>();

    // Create the LoginPage
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

        // Switch to ResetPage when resetLabel button is pressed
        resetLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                getCards().show(getParent(), "reset");
                messageLabel.setText("");

            }
        });
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            // login case
            case LOGIN:
                // login variables
                String username_input = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());
                UserInfo email = new UserInfo();

                try {
                    // checks if the email and password are correct
                    email.get("email", username_input);
                    email.salting("email", username_input);
                    if(email.get_count("email",username_input).equals("1")) {
                        byte[] salt = Base64.getDecoder().decode(email.getSalt());
                        if (email.getSecurePassword(password, salt).equals(email.getpassword())) {

                            Main main = new Main();
                            String username = email.getUsername(username_input);
                            main.updateHomePage(username_input, username);
                            getCards().show(getParent(), "home");
                            usernameField.setText("");
                            passwordField.setText("");
                            messageLabel.setText("");
                        } else {
                            wrongPasswordOrUsername();
                        }
                    }
                    else {
                            wrongPasswordOrUsername();
                        }
                }
                catch (Exception E) { }
                break;

            // Register case
            case REGISTER:
                // Switch to RegisterPage
                getCards().show(getParent(), "register");
                messageLabel.setText("");
                break;
        }
    }

    // if the password or email is wrong print message and reset the usernameField and passwordField
    public void wrongPasswordOrUsername() {
        messageLabel.setForeground(Color.red);
        messageLabel.setText("Gebruikersnaam en/of wachtwoord incorrect");
        usernameField.setText("");
        passwordField.setText("");
    }
}
