package main.java.sensordata.sadd.pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class LoginPage extends Page implements ActionListener {


    public JPanel frame = new JPanel();

    // create a login button
    JButton loginButton = new JButton("Login");

    // create a label and text field for the userID
    JLabel userIDLabel = new JLabel("Gebruikersnaam:");
    JTextField userIDField = new JTextField();

    // create a label and text field for the userPassword
    JLabel userPasswordLabel = new JLabel("Wachtwoord:");
    JPasswordField userPasswordField = new JPasswordField();

    JLabel messageLabel = new JLabel();

    ImageIcon image = new ImageIcon(getClass().getClassLoader().getResource("images/background.png").getFile());

    JLabel image_label = new JLabel(image);

    HashMap<String, String> loginInfo;


    public LoginPage(HashMap<String, String> loginInf, CardLayout cards, Container parent){
        super(cards, parent);

        loginInfo = loginInf;

        userIDLabel.setBounds(500, 320, 110, 25);
        userIDLabel.setFont(new Font(null, Font.BOLD, 13));
        userIDLabel.setForeground(Color.gray);

        userPasswordLabel.setBounds(500, 350, 85, 25);
        userPasswordLabel.setFont(new Font(null, Font.BOLD, 13));
        userPasswordLabel.setForeground(Color.gray);

        messageLabel.setBounds(554, 450, 400, 35);
        messageLabel.setFont(new Font(null, Font.ITALIC, 20));

        userIDField.setBounds(625, 320, 200, 25);
        userIDField.setBackground(Color.darkGray);
        userIDField.setForeground(Color.lightGray);
        userIDField.setBorder(BorderFactory.createLineBorder(Color.darkGray));

        userPasswordField.setBounds(625, 350, 200, 25);
        userPasswordField.setBackground(Color.darkGray);
        userPasswordField.setForeground(Color.lightGray);
        userPasswordField.setBorder(BorderFactory.createLineBorder(Color.darkGray));

        loginButton.setBounds(625, 380, 100, 25);
        loginButton.setForeground(Color.darkGray);
        loginButton.setBackground(Color.gray);
        loginButton.setFocusable(false);
        loginButton.addActionListener(this);

        frame.add(userIDLabel);
        frame.add(userPasswordLabel);
        frame.add(messageLabel);
        frame.add(userIDField);
        frame.add(userPasswordField);
        frame.add(loginButton);
        frame.add(image_label);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==loginButton){
            String userID = userIDField.getText();
            String password = String.valueOf(userPasswordField.getPassword());

            if(loginInfo.containsKey(userID)){
                if(loginInfo.get(userID).equals(password)){
                    messageLabel.setForeground(Color.green);
                    messageLabel.setText("succesvol ingelogd!");
                } else {
                    messageLabel.setForeground(Color.red);
                    messageLabel.setText("Gebruikersnaam en/of wachtwoord incorrect");
                    userPasswordField.setText("");
                }
            } else {
                messageLabel.setForeground(Color.red);
                messageLabel.setText("Gebruikersnaam en/of wachtwoord incorrect");
                userIDField.setText("");
                userPasswordField.setText("");
            }
        }
    }
}
