package main.java.sensordata.sadd;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class UserInfo {
    private static String password;
    private static String salt;
    private static String count_nr;

    HashMap<String, String> loginInfo = new HashMap<String, String>();

    UserInfo(){

    }

    protected HashMap getLoginInfo() {
        return loginInfo;
    }


    //JDBC connection
    public static Connection getConnection() throws Exception
    {
        try{
            String driver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://127.0.0.1:3306/users?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";// jouw eigen ding invullen
            String username = "root"; //jouw eigen ding invullen
            String password = "root";//jouw eigen ding invullen
            Class.forName(driver);

            Connection conn = DriverManager.getConnection(url,username,password);
            System.out.println("Connected");
            return conn;
        } catch(Exception e){System.out.println(e);}

        return null;
    }

    //Get a string from database
    public static String get(String input , String value) throws Exception{
        try{
            Connection con = getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT password FROM users.security WHERE "+input+" = \""+value+"\"");

            ResultSet result = statement.executeQuery();

            ArrayList<String> array = new ArrayList<String>();
            while(result.next()){
                password= result.getString("password");
                System.out.print(" ");
                salt= result.getString("salt");
                System.out.print(" ");

                array.add(result.getString("password"));
            }



        }
        catch(Exception e){

        }
        return password;
    }

    //return the password
    public String getpassword()
    {
        return password;

    }

    public static String salting(String input , String value) throws Exception{
        try{
            Connection con = getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT salt FROM users.security WHERE "+input+" = \""+value+"\"");

            ResultSet result = statement.executeQuery();

            ArrayList<String> array = new ArrayList<String>();
            while(result.next()){

                salt= result.getString("salt");
                System.out.print(" ");

                array.add(result.getString("salt"));
            }



        }
        catch(Exception e){

        }
        return salt;
    }


    public String getSalt()
    {
        return salt;

    }


    //to check if something does exist in the database
    public static String get_count(String input,String value) throws Exception{
        try{
            Connection con = getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT COUNT(*) as count FROM users.security WHERE "+input+" = "+"\""+value+"\";");

            ResultSet result = statement.executeQuery();

            ArrayList<String> array = new ArrayList<String>();
            while(result.next()){
                count_nr= result.getString("count");
                System.out.print(" ");

                array.add(result.getString("count"));

            }
            System.out.print(count_nr);
            return count_nr;


        }
        catch(Exception e){

        }
        return null;
    }

    //sending data to database
    public static void post(final String command) throws Exception{

        try{
            Connection con = getConnection();
            PreparedStatement posted = con.prepareStatement(command);

            posted.executeUpdate();
        } catch(Exception e){System.out.println(e);}
        finally {
            System.out.println("Insert Completed.");
        }
    }

    //hashing password

    public static String getSecurePassword(String password, byte[] salt) {

        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public byte[] generateSalt() throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }



}

