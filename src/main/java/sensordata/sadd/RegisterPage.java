package main.java.sensordata.sadd;

import main.java.sensordata.sadd.pages.Page;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Base64;

public class RegisterPage extends Page implements ActionListener {
    private JTextField firstNameField;
    private JTextField insertionField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JButton createAccountButton;
    public JPanel registerPanel;
    private JLabel firstNameLabel;
    private JLabel lastNameLabel;
    private JLabel emailLabel;
    private JLabel passwordLabel;
    private JLabel repeatPasswordLabel;
    private JLabel inserionLabel;
    private JLabel requiredFieldLabel;
    private JPasswordField passwordField;
    private JPasswordField repeatPasswordField;
    private JLabel goBackLabel;
    private JLabel messageLabel;
    private JButton backToLogin;

    private static final String LOGIN = "login";
    private static final String CREATE = "createAccount";

    public RegisterPage(CardLayout cards, Container parent) {
        super(cards, parent);

        backToLogin.setActionCommand(LOGIN);
        backToLogin.addActionListener(this);

        createAccountButton.setActionCommand(CREATE);
        createAccountButton.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case LOGIN:
                messageLabel.setText("");
                firstNameField.setText("");
                insertionField.setText("");
                lastNameField.setText("");
                emailField.setText("");
                passwordField.setText("");
                repeatPasswordField.setText("");
                getCards().show(getParent(), "login");
                break;

            case CREATE:
                String firstName = firstNameField.getText();
                String insertion = insertionField.getText();
                String lastName = lastNameField.getText();
                String email = emailField.getText();
                String password1 = String.valueOf(passwordField.getPassword());
                String password2 = String.valueOf(repeatPasswordField.getPassword());

                checkForEmptyFields(firstName, firstNameField);
                checkForEmptyFields(lastName, lastNameField);
                checkForEmptyFields(email, emailField);
                checkForEmptyFields(password1, passwordField);
                checkForEmptyFields(password2, repeatPasswordField);

                if (firstName.equals("") || lastName.equals("") || email.equals("") || password1.equals("") || password2.equals("")) {
                    messageLabel.setForeground(Color.red);
                    messageLabel.setText("Niet alle verplichte velden zijn ingevuld");

                } else if (!password1.equals(password2)) {
                    messageLabel.setForeground(Color.red);
                    passwordField.setText("");
                    repeatPasswordField.setText("");
                    messageLabel.setText("Wachtwoord komt niet overeen");
                } else if (password1.length() >= 8) {
                    UserInfo new_account = new UserInfo();
                    try {
                        String username_check = new_account.get_count("username", firstName);
                        String email_check = new_account.get_count("email", email);

                        if (username_check.equals("1") & email_check.equals("1")) {
                            messageLabel.setForeground(Color.red);
                            messageLabel.setText("Gebruikersnaam en Email zijn beide  al in gebruik");
                        } else if (username_check.equals("1")) {
                            messageLabel.setForeground(Color.red);
                            messageLabel.setText("De  gebruikersnaam is al in gebruik");
                        } else if (email_check.equals("1")) {
                            messageLabel.setForeground(Color.red);
                            messageLabel.setText("De Email is al in gebruik");
                        } else {
                            byte[] salt = new_account.generateSalt();
                            String char_salt = Base64.getEncoder().encodeToString(salt);
                            final String command = "INSERT INTO users.security(username, password,email,insertion,lastname,salt) VALUES ('" + firstName + "', '" + new_account.getSecurePassword(password1, salt) + "','" + email + "','" + insertion + "','" + lastName + "','" + char_salt + "')";
                            new_account.post(command);
                            // TODO
                            // naar home scherm
                        }
                    } catch (Exception E) {
                        System.out.print(E);
                    }
                } else {
                    messageLabel.setForeground(Color.red);
                    messageLabel.setText("Wachtwoord moet meer dan 8 letters/cijfers hebben");
                }
                break;
        }
    }

    public void checkForEmptyFields(String string, JTextField field) {
        if (string.equals("")) {
            field.setBorder(BorderFactory.createLineBorder(Color.red));
        } else {
            field.setBorder(BorderFactory.createLineBorder(Color.gray));
        }
    }
}


