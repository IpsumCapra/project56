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
    private JLabel inserionLabel;
    private JLabel requiredFieldLabel;
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
                getCards().show(getParent(), "login");
                break;

            case CREATE:
                String firstName = firstNameField.getText();
                String insertion = insertionField.getText();
                String lastName = lastNameField.getText();
                String email = emailField.getText();

                checkForEmptyFields(firstName, firstNameField);
                checkForEmptyFields(lastName, lastNameField);
                checkForEmptyFields(email, emailField);

                if (firstName.equals("") || lastName.equals("") || email.equals("") ) {
                    messageLabel.setForeground(Color.red);
                    messageLabel.setText("Niet alle verplichte velden zijn ingevuld");

                } else  {
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
                            final String command = "INSERT INTO sadd.users(username,email,insertion,lastname) VALUES ('" + firstName + "', '" + email + "','" + insertion + "','" + lastName + "')";
                            new_account.post(command);
                            getCards().show(getParent(), "reset");
                            firstNameField.setText("");
                            insertionField.setText("");
                            lastNameField.setText("");
                            emailField.setText("");
                        }
                    } catch (Exception E) {
                        System.out.print(E);
                    }
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


