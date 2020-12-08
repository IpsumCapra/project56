import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResetPage extends JFrame{
    private JPanel panel1;
    private JTextField textField1;
    private JButton opvragenButton;
    private JButton terugButton;

    ResetPage(){
        opvragenButton.setFocusable(false);
        terugButton.setFocusable(false);

        terugButton.addActionListener(new ActionListener() {
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
        setTitle("naam hier doen dingen enzo");
        setSize(600, 600);
    }
}
