package main.java.sensordata.sadd.pages;

import main.java.sensordata.sadd.database.UserInfo;
import main.java.sensordata.sadd.pages.Page;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterPage extends Page implements ActionListener {
    //<editor-fold desc="Swing elements">
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
    //</editor-fold>

    // Action commands
    private static final String LOGIN = "login";
    private static final String CREATE = "createAccount";

    // Create RegisterPage
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
                // Resets the field and switches to the loginPage
                messageLabel.setText("");
                firstNameField.setText("");
                insertionField.setText("");
                lastNameField.setText("");
                emailField.setText("");
                getCards().show(getParent(), "login");
                break;

            case CREATE:
                // create variables
                String firstName = firstNameField.getText();
                String insertion = insertionField.getText();
                String lastName = lastNameField.getText();
                String email = emailField.getText();

                checkForEmptyFields(firstName, firstNameField);
                checkForEmptyFields(lastName, lastNameField);
                checkForEmptyFields(email, emailField);

                // checks if all the fields are filled in
                if (firstName.equals("") || lastName.equals("") || email.equals("") ) {
                    messageLabel.setForeground(Color.red);
                    messageLabel.setText("Niet alle verplichte velden zijn ingevuld");

                } else  {
                    UserInfo new_account = new UserInfo();
                    try {
                        String email_check = new_account.get_count("email", email);
                        // checks if the email is usable
                       if (email_check.equals("1")) {
                            messageLabel.setForeground(Color.red);
                            messageLabel.setText("De email is al in gebruik");
                        }
                       else {
                           // putt the user information into the databases en switch to ResetPage
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

    // when fields are empty the border color will be set red
    public void checkForEmptyFields(String string, JTextField field) {
        if (string.equals("")) {
            field.setBorder(BorderFactory.createLineBorder(Color.red));
        } else {
            field.setBorder(BorderFactory.createLineBorder(Color.gray));
        }
    }
}


