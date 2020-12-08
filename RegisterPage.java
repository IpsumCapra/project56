import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterPage extends JFrame{
    private JTextField firstNameField;
    private JTextField insertionField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JButton createAccountButton;
    private JPanel registerPanel;
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

    public RegisterPage(){
        createAccountButton.setFocusable(false);

        goBackLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                UserInfo userInfo = new UserInfo();
                LoginPage loginPage = new LoginPage(userInfo.getLoginInfo());
                loginPage.setVisible(true);
                setVisible(false);
            }
        });

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = firstNameField.getText();
                String insertion = insertionField.getText();
                String lastName = lastNameField.getText();
                String email = emailField.getText();
                String password1 = String.valueOf(passwordField.getPassword());
                String password2 = String.valueOf(repeatPasswordField.getPassword());

                if(firstName.equals("") || lastName.equals("") || email.equals("") || password1.equals("") || password2.equals("")) {
                    messageLabel.setForeground(Color.red);
                    messageLabel.setText("Niet alle verplichte velden zijn ingevuld");

                    checkForEmptyFields(firstName, firstNameField);
                    checkForEmptyFields(lastName, lastNameField);
                    checkForEmptyFields(email, emailField);
                    checkForEmptyFields(password1, passwordField);
                    checkForEmptyFields(password2, repeatPasswordField);
                } else {
                    messageLabel.setForeground(Color.green);
                    messageLabel.setText("alles ingevult");
                }

                if(!password1.equals(password2)){
                    messageLabel.setForeground(Color.red);
                    passwordField.setText("");
                    repeatPasswordField.setText("");
                    messageLabel.setText("Wachtwoord komt niet overeen");
                }
            }
        });

        add(registerPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Registreer pagina");
        setSize(600, 600);
    }

    public void checkForEmptyFields(String string, JTextField field){
        if (string.equals("")) {
            field.setBorder(BorderFactory.createLineBorder(Color.red));
        } else {
            field.setBorder(BorderFactory.createLineBorder(Color.gray));
        }
    }
}
