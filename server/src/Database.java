import beans.ApplicationConstrain;
import beans.ApplicationConstrain.SportConstrain;
import beans.Competition;
import beans.Sex;
import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import org.gjt.mm.mysql.Driver;

public class Database {
	public Database() throws Exception {
		connection = getConnection();
	}

	/**
	 * @return connection MySQL
	 */


	private Connection getConnection() {
		try {
			Driver driver   = new Driver();
			String url      = "jdbc:mysql://localhost/olimpis";
			String username = "root";
			String password = "pass";
			return DriverManager.getConnection(url, username, password);

		} catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}

	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
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
			stmt = (PreparedStatement) connection.prepareStatement(
					"SELECT id  FROM sportobject_type WHERE object_type=?;");
			stmt.setString(1, object);
			ResultSet rs = stmt.executeQuery();
			if (!rs.first()) {
				stmt = (PreparedStatement) connection.prepareStatement(
						"INSERT INTO sportobject_type  Values (NULL,?)");
				stmt.setString(1, object);
				stmt.executeUpdate();
			}
			stmt = (PreparedStatement) connection.prepareStatement(
					"SELECT id  FROM sportobject_type WHERE object_type=?;");
			stmt.setString(1, object);
			rs = stmt.executeQuery();
			rs.next();
			id = rs.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
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
			stmt = (PreparedStatement) connection.prepareStatement(
					"SELECT country_name  FROM country WHERE login=? and hash_password=?;");
			stmt.setString(1, login);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				res = rs.getString("country_name");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * @param country
	 * @return Country Id
	 */
	public int getCountryId(String country) {
		PreparedStatement stmt = null;
		int countryId = 0;
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"SELECT id  FROM country WHERE country_name=?;");
			stmt.setString(1, country);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				countryId = rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return countryId;
	}

	/**
	 * @param competitionId - id Competition
	 * @return Competition date
	 */
	public Date getCompetitionDate(int competitionId) {
		PreparedStatement stmt = null;
		java.util.Date dateCompetition = new java.util.Date();
		java.text.SimpleDateFormat sdf =
				new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"SELECT start_time  FROM schedule_olymp WHERE competition_id=?;");
			stmt.setInt(1, competitionId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				dateCompetition = rs.getDate("start_time");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dateCompetition;
	}

	/**
	 * @param competitionId
	 * @return Ordinal day of competition
	 */
	public long getCompetitionDay(int competitionId) {
		Date dateCompetition = getCompetitionDate(competitionId);
		Date dateOpenOlymp = getCompetitionDate(1);
		long a = (dateCompetition.getTime() - dateOpenOlymp.getTime()) / (1000L * 60L * 60L * 24L);
		return a;
	}

	/**
	 * @param competition name
	 * @return Id Competition
	 */
	public int getCompetitionId(String competition) {
		PreparedStatement stmt = null;
		int competitionId = 0;
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"SELECT id  FROM competition WHERE competition_name=?;");
			stmt.setString(1, competition);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				competitionId = rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
	public void insertIntoQuotes(String country, String competition, int countQuotes) {
		PreparedStatement stmt = null;
		int countryId = 0;
		int competitionId = 0;
		try {
			countryId = getCountryId(country);
			competitionId = getCompetitionId(competition);
			if (competitionId == 0 || countryId == 0) {
				return;
			}
			stmt = (PreparedStatement) connection.prepareStatement(
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
	public void insertIntoSportObject(String title, String type, String city, int capacity) {
		PreparedStatement stmt = null;
		int id = 0;
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"SELECT id  FROM sport_object WHERE title=?;");
			stmt.setString(1, title);
			ResultSet rs = stmt.executeQuery();
			if (rs.first()) {
				stmt.close();
				return;
			}
			int typeObject = getIdTypeObject(type);
			stmt = (PreparedStatement) connection.prepareStatement(
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
	public void insertIntoCompetition(String title, String typeObject, int sexParticipants, int duration) {
		PreparedStatement stmt = null;
		int id = 0;
		try {
			id = getCompetitionId(title);
			if (id != 0) {
				return;
			}
			int idObject = getIdTypeObject(typeObject);
			stmt = (PreparedStatement) connection.prepareStatement(
					"INSERT INTO competition  Values (NULL,?,?,?,?)");
			stmt.setString(1, title);
			stmt.setInt(2, idObject);
			stmt.setInt(3, sexParticipants);
			stmt.setInt(4, duration);
			stmt.executeUpdate();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
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
			id = getCountryId(name);
			if (id != 0) {
				return;
			}
			stmt = (PreparedStatement) connection.prepareStatement(
					"INSERT INTO country  Values (NULL,?,?,?)");
			stmt.setString(1, name);
			stmt.setString(2, login);
			stmt.setString(3, pass);
			stmt.executeUpdate();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param number 0 - female, 1 - male, other - undefined
	 * @return class Sex
	 */
	private Sex getAthleteSex(int number) {
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
		int countryId = getCountryId(country);
		if (countryId == 0) {
			return null;
		}
		PreparedStatement stmt = null;
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"select competition_name, sex_participant, number_participants\n" +
							"\tfrom country_quote join competition on\n" +
							"\t\tcompetition_id = competition.id\n" +
							"    where country_id = ?;");
			stmt.setInt(1, countryId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				String competition = rs.getString("competition_name");
				Sex sex_participant = getAthleteSex(rs.getInt("sex_participant"));
				int number_participant = rs.getInt("number_participants");
				res.add(new SportConstrain(competition, number_participant, sex_participant));
			}
		} catch (SQLException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
		return new ApplicationConstrain(res);
	}

	/**
	 * @return number of sport objects in DB
	 */
	public int getSportObjectNumber() {
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"SELECT count(*)  FROM sport_object;");
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				res = rs.getInt("count(*)");
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
			stmt = (PreparedStatement) connection.prepareStatement(
					"SELECT id,object_type,duration,sex_participant   FROM competition;");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				int objectTypeId = rs.getInt("object_type");
				int duration = rs.getInt("duration");
				Sex sexParticipant = getAthleteSex(rs.getInt("sex_participant"));
				ArrayList<Integer> temp = new ArrayList<Integer>();
				stmt = (PreparedStatement) connection.prepareStatement(
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
			e.printStackTrace();
		}
		return res;
	}

	private Connection connection;
}
