/**
 * Created with IntelliJ IDEA.
 * User: Владимир
 * Date: 05.03.13
 * Time: 22:12
 * To change this template use File | Settings | File Templates.
 */

import com.mysql.jdbc.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database
{
    private Connection conn;

    public static Connection getConnection() throws Exception {
        Class.forName("org.gjt.mm.mysql.Driver").newInstance();

        // String url = "jdbc:mysql://localhost/commining";
        String url = "jdbc:mysql://localhost/olimpis";
        String username = "root";
        String password = "pass";
        return DriverManager.getConnection(url, username, password);
    }

    public Database() throws Exception{
        conn = getConnection();
    }
    public void closeConnection() throws SQLException {
        conn.close();
    }

    public int getIdTypeObject(String object){
        PreparedStatement stmt = null;
        int id = 0;
        try {
            stmt = (PreparedStatement) conn.prepareStatement(
                    "SELECT id  FROM sportobject_type WHERE object_type=?;");
            stmt.setString(1, object);
            ResultSet rs = stmt.executeQuery();
            if (!rs.first()) {
                stmt = (PreparedStatement) conn.prepareStatement(
                     "INSERT INTO sportobject_type  Values (NULL,?)");
                stmt.setString(1, object);
                stmt.executeUpdate();
            }
            stmt = (PreparedStatement) conn.prepareStatement(
                    "SELECT id  FROM sportobject_type WHERE object_type=?;");
            stmt.setString(1,object);
            rs = stmt.executeQuery();
            rs.next();
            id =  rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return id;
    }

    public String getCountry(String login, String password){
        PreparedStatement stmt = null;
        String res = "";
        try {
            stmt = (PreparedStatement) conn.prepareStatement(
                    "SELECT country_name  FROM country WHERE login=? and hash_password=?;");
            stmt.setString(1, login);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                res = rs.getString("country_name");
            }

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return res;
    }

    public void  insertInSportObject(String title, String type, String city, int capacity){
        PreparedStatement stmt = null;
        int id = 0;
        try {
            stmt = (PreparedStatement) conn.prepareStatement(
                    "SELECT id  FROM sport_object WHERE title=?;");
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();
            if (rs.first()) {
                stmt.close();
                return;
            }
            int typeObject = getIdTypeObject(type);
          stmt = (PreparedStatement) conn.prepareStatement(
                    "INSERT INTO sport_object  Values (NULL,?,?,?,?)");
           stmt.setString(1,title);
           stmt.setInt(2,typeObject);
           stmt.setInt(3,capacity);
           stmt.setString(4,city);
           stmt.executeUpdate();
           stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void  insertInCompetition(String title, String typeObject, int sexParticipants, int duration){
        PreparedStatement stmt = null;
        int id = 0;
        try {
            stmt = (PreparedStatement) conn.prepareStatement(
                    "SELECT id  FROM competition WHERE competition_name=?;");
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();
            if (rs.first()) {
                stmt.close();
                return;
            }
            int idObject = getIdTypeObject(typeObject);
            stmt = (PreparedStatement) conn.prepareStatement(
                    "INSERT INTO competition  Values (NULL,?,?,?,?)");
            stmt.setString(1,title);
            stmt.setInt(2,idObject);
            stmt.setInt(3,sexParticipants);
            stmt.setInt(4,duration);
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void  insertInCountries(String name, String login,String pass){
        PreparedStatement stmt = null;
        int id = 0;
        try {
            stmt = (PreparedStatement) conn.prepareStatement(
                    "SELECT id  FROM country WHERE country_name=?;");
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.first()) {
                stmt.close();
                return;
            }
            stmt = (PreparedStatement) conn.prepareStatement(
                    "INSERT INTO country  Values (NULL,?,?,?)");
            stmt.setString(1,name);
            stmt.setString(2,login);
            stmt.setString(3,pass);
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
