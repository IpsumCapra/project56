import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.zip.CheckedOutputStream;

public class LoginPage extends JFrame{
    private JPanel panel1;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JButton loginButton;
    private JLabel resetLabel;
    private JLabel registerLabel;
    private JLabel messageLabel;

    //private ImageIcon image = new ImageIcon(getClass().getResource("image.png"));

    HashMap<String, String> loginInfo = new HashMap<String, String>();

    LoginPage(HashMap<String, String> loginInf){
        loginInfo = loginInf;

        loginButton.setFocusable(false);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());
                if(loginInfo.containsKey(name)){
                    if(loginInfo.get(name).equals(password)){
                        WelcomePage welcomePage = new WelcomePage();
                        welcomePage.setVisible(true);
                        setVisible(false);
                    } else {
                        wrongPasswordOrUsername();
                    }
                } else {
                    wrongPasswordOrUsername();
                }
            }
        });

        resetLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ResetPage resetPage = new ResetPage();
                resetPage.setVisible(true);
                setVisible(false);
            }
        });

        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                RegisterPage registerPage = new RegisterPage();
                registerPage.setVisible(true);
                setVisible(false);
            }
        });

        add(panel1);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("login scherm");
        setSize(600, 600);
    }

    public void wrongPasswordOrUsername(){
        messageLabel.setForeground(Color.red);
        messageLabel.setText("Gebruikersnaam en/of wachtwoord incorrect");
        usernameField.setText("");
        passwordField.setText("");
    }
}
