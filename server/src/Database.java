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

public class Database {
	public static Database createDatabase() {
		Connection connection = getConnection();
		if (connection == null) {
			return null;
			//TODO: throw exception
		}

		return new Database(connection);
	}

	private Database(Connection connection) {
		this.connection = connection;
	}

	private static Connection getConnection() {
		try {
			String url      = "jdbc:mysql://localhost/olimpis";
			String username = "root";
			String password = "pass";
			return DriverManager.getConnection(url, username, password);

		} catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}

	public void closeConnection() throws SQLException {
		connection.close();
	}

	public int sportObjectTypeId(String object) {
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
	 * @param login    countryByLoginPassword
	 * @param password countryByLoginPassword
	 * @return name Country
	 */
	public String countryByLoginPassword(String login, String password) {
		PreparedStatement stmt = null;
		String result = "";
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"SELECT country_name  FROM countryByLoginPassword WHERE login=? and hash_password=?;");
			stmt.setString(1, login);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				result = rs.getString("country_name");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public int countryId(String country) {
		PreparedStatement stmt = null;
		int countryId = 0;
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"SELECT id  FROM countryByLoginPassword WHERE country_name=?;");
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

	public long competitionDay(int competitionId) {
		Date dateCompetition = getCompetitionDate(competitionId);
		Date dateOpenOlymp = getCompetitionDate(1);
		long a = (dateCompetition.getTime() - dateOpenOlymp.getTime()) / (1000L * 60L * 60L * 24L);
		return a;
	}

	/**
	 * @param competition name
	 * @return Id Competition
	 */
	public int competitionId(String competition) {
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
			countryId = countryId(country);
			competitionId = competitionId(competition);
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
			e.printStackTrace();
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
			int typeObject = sportObjectTypeId(type);
			stmt = (PreparedStatement) connection.prepareStatement(
					"INSERT INTO sport_object  Values (NULL,?,?,?,?)");
			stmt.setString(1, title);
			stmt.setInt(2, typeObject);
			stmt.setInt(3, capacity);
			stmt.setString(4, city);
			stmt.executeUpdate();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
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
			id = competitionId(title);
			if (id != 0) {
				return;
			}
			int idObject = sportObjectTypeId(typeObject);
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
			id = countryId(name);
			if (id != 0) {
				return;
			}
			stmt = (PreparedStatement) connection.prepareStatement(
					"INSERT INTO countryByLoginPassword  Values (NULL,?,?,?)");
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
	 * Encodes the database sex value to {@link beans.Sex}.
	 * 0 -> Sex.Female
	 * 1 -> Sex.Male
	 * otherwise -> Sex.Undefined
	 * @param number value to encode.
	 * @return encoded {@link beans.Sex}.
	 */
	private Sex encodeAthleteSex(int number) {
		switch (number) {
			case 1:
				return Sex.Male;
			case 0:
				return Sex.Female;
		}
		return Sex.Undefined;
	}

	/**
	 * Returns {@link ApplicationConstrain} for the country by its name.
	 * @param country name of the country.
	 * @return {@link ApplicationConstrain} for the country.
	 */
	public ApplicationConstrain countryConstrain(String country) {
		int countryId = countryId(country);
		if (countryId == 0) {
			//TODO: throw exception
			return null;
		}

		Vector<SportConstrain> result = new Vector<SportConstrain>();
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
				Sex sex_participant = encodeAthleteSex(rs.getInt("sex_participant"));
				int number_participant = rs.getInt("number_participants");
				result.add(new SportConstrain(competition, number_participant, sex_participant));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ApplicationConstrain(result);
	}

	/**
	 * Returns number of sport objects in DB.
	 * @return number of sport objects in DB.
	 */
	public int sportObjectNumber() {
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
	public ArrayList<Competition> competitions() {
		PreparedStatement stmt = null;
		ArrayList<Competition> result = new ArrayList<Competition>();
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"SELECT id,object_type,duration,sex_participant   FROM competition;");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				int objectTypeId = rs.getInt("object_type");
				int duration = rs.getInt("duration");
				Sex sexParticipant = encodeAthleteSex(rs.getInt("sex_participant"));
				ArrayList<Integer> temp = new ArrayList<Integer>();
				stmt = (PreparedStatement) connection.prepareStatement(
						"select sport_object.id from sport_object join sportobject_type on " +
								"sport_object.object_type_id = sportobject_type.id where object_type_id = ?;");
				stmt.setInt(1, objectTypeId);
				ResultSet rs2 = stmt.executeQuery();
				while (rs2.next()) {
					temp.add(rs2.getInt("id"));
				}
				result.add(new Competition(id, duration, sexParticipant, temp));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	private Connection connection;
}
