package main.java.sensordata.sadd;

import main.java.sensordata.sadd.pages.Page;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Base64;

public class NewPassword extends Page implements ActionListener {
    public JPanel newPasswordPanel;
    private JLabel label1;
    private JPasswordField repeat_password;
    private JPasswordField new_password;
    private JButton wachtwoordVeranderenButton;
    private JLabel label2;
    private String email;

    private static final String CHANGEPASSWORD = "changePassword";

    public NewPassword(String email, CardLayout cards, Container parent) {
        super(cards, parent);
        this.email = email;

        wachtwoordVeranderenButton.setActionCommand(CHANGEPASSWORD);
        wachtwoordVeranderenButton.addActionListener(this);

        label1.setText("Email: " + email);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case CHANGEPASSWORD:
                UserInfo post_pass = new UserInfo();

                String input_password = new_password.getText();
                if (input_password.equals(repeat_password.getText())) {
                    if (input_password.length() >= 8) {
                        label2.setForeground(Color.RED);
                        label2.setText("Approved");
                        try {
                            byte[] salt = post_pass.generateSalt();
                            String char_salt = Base64.getEncoder().encodeToString(salt);
                            final String command = "UPDATE users.security SET password = \"" + post_pass.getSecurePassword(input_password, salt) + "\" , Salt = " + "\"" + char_salt + "\"" + " WHERE email = \"" + email + "\";";
                            System.out.print(char_salt);
                            post_pass.post(command);

                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException E) {
                                System.out.println(e);
                            }
                            getCards().show(getParent(), "login");
                        } catch (Exception E) {
                        }
                    } else {
                        label2.setForeground(Color.RED);
                        label2.setText("Wachtwoord moet meer dan 8 letters/cijfers hebben");
                    }
                } else {
                    label2.setForeground(Color.RED);
                    label2.setText("Wachtwoord komt niet overeen");

                }
                break;
        }
    }
}


