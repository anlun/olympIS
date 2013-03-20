/**
 * Created with IntelliJ IDEA.
 * User: Владимир
 * Date: 05.03.13
 * Time: 22:12
 * To change this template use File | Settings | File Templates.
 */

import beans.ApplicationConstrain;
import beans.ApplicationConstrain.Sex;
import beans.ApplicationConstrain.SportConstrain;
import beans.Competition;
import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class Database {
    private Connection conn;

    public Database() throws Exception {
        conn = getConnection();
    }

    /**
     * @return connection MySQL
     */
    public static Connection getConnection() throws Exception {
        Class.forName("org.gjt.mm.mysql.Driver").newInstance();
        String url = "jdbc:mysql://localhost/olimpis";
        String username = "root";
        String password = "pass";
        return DriverManager.getConnection(url, username, password);
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * @param object - name sportObject
     * @return id type object
     */
    public int getIdTypeObject(String object) {
        PreparedStatement stmt = null;
        int id = 0;
        try {
            // Check whether id in the database
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
            stmt.setString(1, object);
            rs = stmt.executeQuery();
            rs.next();
            id = rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return id;
    }

    /**
     * @param login    country
     * @param password country
     * @return name Country
     */
    public String getCountry(String login, String password) {
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

    /**
     * @param country
     * @return Id Country
     */
    public int getIdCountry(String country) {
        PreparedStatement stmt = null;
        int countryId = 0;
        try {
            stmt = (PreparedStatement) conn.prepareStatement(
                    "SELECT id  FROM country WHERE country_name=?;");
            stmt.setString(1, country);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                countryId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return countryId;
    }

    /**
     * @param competitionId - id Competition
     * @return real Date Comeptition
     */
    public Date getDateCompetition(int competitionId) {
        PreparedStatement stmt = null;
        java.util.Date dateCompetition = new java.util.Date();
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            stmt = (PreparedStatement) conn.prepareStatement(
                    "SELECT start_time  FROM schedule_olymp WHERE competition_id=?;");
            stmt.setInt(1, competitionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                dateCompetition = rs.getDate("start_time");
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return dateCompetition;
    }

    /**
     * @param competitionId
     * @return Ordinal day of competition
     */
    public long getDayCompetition(int competitionId) {
        Date dateCompetition = getDateCompetition(competitionId);
        Date dateOpenOlymp = getDateCompetition(1);
        long a = (dateCompetition.getTime() - dateOpenOlymp.getTime()) / (1000L * 60L * 60L * 24L);
        return a;
    }

    /**
     * @param competition name
     * @return Id Competition
     */
    public int getIdCompetition(String competition) {
        PreparedStatement stmt = null;
        int competitionId = 0;
        try {
            stmt = (PreparedStatement) conn.prepareStatement(
                    "SELECT id  FROM competition WHERE competition_name=?;");
            stmt.setString(1, competition);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                competitionId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return competitionId;
    }

    /**
     * Insert into country_quote
     *
     * @param country
     * @param competition
     * @param countQuotes
     */
    public void insertInQuotes(String country, String competition, int countQuotes) {
        PreparedStatement stmt = null;
        int countryId = 0;
        int competitionId = 0;
        try {
            countryId = getIdCountry(country);
            competitionId = getIdCompetition(competition);
            if (competitionId == 0 || countryId == 0) {
                return;
            }
            stmt = (PreparedStatement) conn.prepareStatement(
                    "INSERT INTO country_quote  Values (?,?,?)");
            stmt.setInt(1, competitionId);
            stmt.setInt(2, countryId);
            stmt.setInt(3, countQuotes);
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    /**
     * Insert Into Sport Object
     *
     * @param title
     * @param type
     * @param city
     * @param capacity
     */
    public void insertInSportObject(String title, String type, String city, int capacity) {
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
            stmt.setString(1, title);
            stmt.setInt(2, typeObject);
            stmt.setInt(3, capacity);
            stmt.setString(4, city);
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * insert into competition
     *
     * @param title
     * @param typeObject
     * @param sexParticipants
     * @param duration
     */
    public void insertInCompetition(String title, String typeObject, int sexParticipants, int duration) {
        PreparedStatement stmt = null;
        int id = 0;
        try {
            id = getIdCompetition(title);
            if (id != 0) {
                return;
            }
            int idObject = getIdTypeObject(typeObject);
            stmt = (PreparedStatement) conn.prepareStatement(
                    "INSERT INTO competition  Values (NULL,?,?,?,?)");
            stmt.setString(1, title);
            stmt.setInt(2, idObject);
            stmt.setInt(3, sexParticipants);
            stmt.setInt(4, duration);
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * insert into countries
     *
     * @param name
     * @param login
     * @param pass
     */
    public void insertInCountries(String name, String login, String pass) {
        PreparedStatement stmt = null;
        int id = 0;
        try {
            id = getIdCountry(name);
            if (id != 0) {
                return;
            }
            stmt = (PreparedStatement) conn.prepareStatement(
                    "INSERT INTO country  Values (NULL,?,?,?)");
            stmt.setString(1, name);
            stmt.setString(2, login);
            stmt.setString(3, pass);
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * @param number 0 - female, 1 - male, other - undefined
     * @return class Sex
     */
    private Sex getSexParticipant(int number) {
        switch (number) {
            case 1:
                return Sex.Male;
            case 0:
                return Sex.Female;
        }
        return Sex.Undefined;
    }

    /**
     * @param country
     * @return ApplicationConstrain By country name
     */
    public ApplicationConstrain getConstrain(String country) {
        Vector<SportConstrain> res = new Vector<SportConstrain>();
        int countryId = getIdCountry(country);
        if (countryId == 0) {
            return null;
        }
        PreparedStatement stmt = null;
        try {
            stmt = (PreparedStatement) conn.prepareStatement(
                    "select competition_name, sex_participant, number_participants\n" +
                            "\tfrom country_quote join competition on\n" +
                            "\t\tcompetition_id = competition.id\n" +
                            "    where country_id = ?;");
            stmt.setInt(1, countryId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String competition = rs.getString("competition_name");
                Sex sex_participant = getSexParticipant(rs.getInt("sex_participant"));
                int number_participant = rs.getInt("number_participants");
                res.add(new SportConstrain(competition, number_participant, sex_participant));
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return new ApplicationConstrain(res);
    }

    /**
     * @return count sport_objects in DB
     */
    public int getCountSportObject() {
        PreparedStatement stmt = null;
        int res = 0;
        try {
            stmt = (PreparedStatement) conn.prepareStatement(
                    "SELECT count(*)  FROM sport_object;");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                res = rs.getInt("count(*)");
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return res;
    }

    /**
     * @return all competitions in database
     */
    public ArrayList<Competition> getCompetitions() {
        PreparedStatement stmt = null;
        ArrayList<Competition> res = new ArrayList<Competition>();
        try {
            stmt = (PreparedStatement) conn.prepareStatement(
                    "SELECT id,object_type,duration,sex_participant   FROM competition;");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int objectTypeId = rs.getInt("object_type");
                int duration = rs.getInt("duration");
                Sex sexParticipant = getSexParticipant(rs.getInt("sex_participant"));
                ArrayList<Integer> temp = new ArrayList<Integer>();
                stmt = (PreparedStatement) conn.prepareStatement(
                        "select sport_object.id from sport_object join sportobject_type on " +
                                "sport_object.object_type_id = sportobject_type.id where object_type_id = ?;");
                stmt.setInt(1, objectTypeId);
                ResultSet rs2 = stmt.executeQuery();
                while (rs2.next()) {
                    temp.add(rs2.getInt("id"));
                }
                res.add(new Competition(id, duration, sexParticipant, temp));

            }

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return res;
    }


}
