import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomePage extends JFrame{
    private JPanel panel1;
    private JButton uitloggenButton;

    WelcomePage(){

        uitloggenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserInfo userInfo = new UserInfo();
                LoginPage loginPage = new LoginPage(userInfo.getLoginInfo());
                loginPage.setVisible(true);
                setVisible(false);
            }
        });

        add(panel1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("HomePage");
        setSize(600, 600);
    }
}
