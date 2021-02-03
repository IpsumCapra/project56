package main.java.sensordata.sadd;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MailUtil {

    //field
    private static String verify_code;

    public static void sendMail(String recepient, String code) throws Exception {
        verify_code = code;
        System.out.println("Preparing to send email");
        Properties properties = new Properties();

        //Enable authentication
        properties.put("mail.smtp.auth", "true");
        //Set TLS encryption enabled
        properties.put("mail.smtp.starttls.enable", "true");
        //Set SMTP host
        properties.put("mail.smtp.host", "smtp.gmail.com");
        //Set smtp port
        properties.put("mail.smtp.port", "587");


        //Your gmail address
        String myAccountEmail = "STC.SADD@gmail.com";
        //Your gmail password
        String password = "sadd2020";

        //Create a session with account credentials
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });

        //Prepare email message
        Message message = prepareMessage(session, myAccountEmail, recepient);

        //Send mail
        Transport.send(message);
        System.out.println("Message sent successfully");
    }

    //context of the email
    private static Message prepareMessage(Session session, String myAccountEmail, String recepient) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("Verify code"); //title of mail
            String htmlCode = "<h1> " + verify_code+ " </h1> <br/> <h2><b> </b></h2>"; // contex
            message.setContent(htmlCode, "text/html");
            return message;
        } catch (Exception ex) {
            Logger.getLogger(MailUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //generate a 4 random numbers (verify code)
    public String random_numbers() {
        Random random = new Random();
        String[] number = new String[4];

        for (int i = 0; i <= 3; i++) {
            number[i] = Integer.toString(random.nextInt(10));
        }
        return number[0] + number[1] + number[2] + number[3];
    }
}
