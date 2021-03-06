package main.java.sensordata.sadd.pages;

import main.java.sensordata.sadd.MailUtil;
import main.java.sensordata.sadd.Main;
import main.java.sensordata.sadd.pages.Page;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResetPage extends Page implements ActionListener {
    //<editor-fold desc="Swing elements">
    public JPanel resetPagePanel;
    private JTextField textField1;
    private JButton opvragenButton;
    private JButton terugButton;
    private JTextField textField2;
    private JButton verifieerButton;
    private JLabel warning;
    //</editor-fold>

    // reset variables
    private String number;
    private String email;

    // Action commands
    private static final String LOGIN = "login";
    private static final String VERIFIEER = "verifieer";
    private static final String REQUEST = "request";

    // Create ResetPage
    public ResetPage(CardLayout cards, Container parent) {
        super(cards, parent);

        terugButton.setActionCommand(LOGIN);
        terugButton.addActionListener(this);

        opvragenButton.setActionCommand(REQUEST);
        opvragenButton.addActionListener(this);

        verifieerButton.setActionCommand(VERIFIEER);
        verifieerButton.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            // Switch to LoginPage
            case LOGIN:
                getCards().show(getParent(), "login");
                break;
            case REQUEST:
                // sends email with random generated code
                email = textField1.getText();
                MailUtil random_generator = new MailUtil();
                number = random_generator.random_numbers();
                try {
                    MailUtil.sendMail(email, number);

                } catch (Exception E) {
                }
                break;
            case VERIFIEER:
                // checks if code is valid
                if(number.equals(textField2.getText())) {
                    Main main = new Main();
                    main.updateNewPassword(email);
                    // switch to NewPasswordPage and reset fields
                    getCards().show(getParent(), "newPassword");
                    number="";
                    textField1.setText("");
                    textField2.setText("");
                    warning.setText("");
                }
                else {
                    warning.setForeground(Color.red);
                    warning.setText("De code is niet juist");
                    }
                break;
        }
    }

    // getter
    public String getTextField2() {
        String mail = textField2.getText();
        return mail;
    }
}
