
package database_test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


public class Database_test {
    static String pass_result;   
    public static void main(String[] args) throws Exception {
//java en sql connectie checken
    getConnection();  // connected betekent verbinding success
    
//een username and password in database toevoegen
    post("tom","2345");

//get the password from this user
    get("tom"); 

    }
    
    
public static Connection getConnection() throws Exception
{
  try{
   String driver = "com.mysql.jdbc.Driver";
   String url = "jdbc:mysql://localhost:3306/users";// jouw eigen ding invullen
   String username = "root"; //jouw eigen ding invullen
   String password = "root";//jouw eigen ding invullen
   Class.forName(driver);
   
   Connection conn = DriverManager.getConnection(url,username,password);
   System.out.println("Connected");
   return conn;
  } catch(Exception e){System.out.println(e);}
  
  
  return null;
 }

public static String get(String username) throws Exception{
        try{
            Connection con = getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT password FROM users.security WHERE username = \""+username+"\"");
            
            ResultSet result = statement.executeQuery();
            
            ArrayList<String> array = new ArrayList<String>();
            while(result.next()){
                pass_result= result.getString("password");
                System.out.print(" ");
                System.out.print(result.getString("password"));
                array.add(result.getString("password"));
            }
            return result.getString("password");

            
        }
        catch(Exception e){
     
        }
        return null;
    }


public static void post(final String var1,final String var2 ) throws Exception{

        try{
            Connection con = getConnection();
            PreparedStatement posted = con.prepareStatement("INSERT INTO users.security(username, password) VALUES ('"+var1+"', '"+var2+"')");
            
            posted.executeUpdate();
        } catch(Exception e){System.out.println(e);}
        finally {
            System.out.println("Insert Completed.");
        }
    }

}
