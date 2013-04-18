package server;

import beans.*;
import beans.DayTimetable.DaySportElement;
import beans.ApplicationConstrain.SportConstrain;
import com.mysql.jdbc.PreparedStatement;
import com.sun.imageio.plugins.bmp.BMPConstants;
import com.sun.org.apache.xalan.internal.xsltc.dom.FilterIterator;
import utils.Utils;

import java.lang.reflect.Array;
import java.nio.channels.AsynchronousFileChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
			String url = "jdbc:mysql://178.130.32.141:3306/olimpis";
			String username = "vova";
			String password = "pass";
			return DriverManager.getConnection(url, username, password);

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}


	public void closeConnection() throws SQLException {
		connection.close();
	}

	public int sportObjectTypeId(String objectType) {
		PreparedStatement stmt = null;
		int id = 0;
		try {
			// Check whether id in the database
			stmt = (PreparedStatement) connection.prepareStatement(
					"SELECT id  FROM sportobject_type WHERE object_type=?;");
			stmt.setString(1, objectType);
			ResultSet rs = stmt.executeQuery();
			if (!rs.first()) {
				stmt = (PreparedStatement) connection.prepareStatement(
						"INSERT INTO sportobject_type  Values (NULL,?)");
				stmt.setString(1, objectType);
				stmt.executeUpdate();
			}
			stmt = (PreparedStatement) connection.prepareStatement(
					"SELECT id  FROM sportobject_type WHERE object_type=?;");
			stmt.setString(1, objectType);
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
	 *                 * @return name Country
	 */
	public String countryByLoginPassword(String login, String password) {
		PreparedStatement stmt = null;
		String result = "";
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"SELECT country_name  FROM country WHERE login=? and hash_password=?;");
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

	public Date getCompetitionDate(int competitionId) {
		PreparedStatement stmt = null;
		java.util.Date dateCompetition = null;
		DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.text.SimpleDateFormat sdf =
				new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"SELECT start_time  FROM schedule_olymp WHERE competition_id=?;");
			stmt.setInt(1, competitionId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				try {
					dateCompetition =inputFormat.parse(rs.getString("start_time"));
				} catch (ParseException e) {
					e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
				}
			}

			System.out.println(inputFormat.format(dateCompetition).toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (dateCompetition== null){
			return getCompetitionDate(1);
		}
		return dateCompetition;
	}

	public long competitionDay(int competitionId) {
		Date dateCompetition = getCompetitionDate(competitionId);
		Date dateOpenOlymp = getCompetitionDate(1);
		long a = 0;
	    a = (dateCompetition.getTime() - dateOpenOlymp.getTime()) / (1000L * 60L * 60L * 24L);
		return a;
	}

	public ArrayList<Integer> countryDays(int countryId) {
		ArrayList<Date> datesCountry = getCountryDates(countryId);
		ArrayList<Integer> res = new ArrayList<Integer>();
		Date dateOpenOlymp = getCompetitionDate(1);
		for (Date date : datesCountry) {
			res.add((int) ((date.getTime() - dateOpenOlymp.getTime()) / (1000L * 60L * 60L * 24L)));
		}
		return res;
	}


	public ArrayList<Date> getCountryDates(int countryId) {
		PreparedStatement stmt = null;
		java.util.Date dateCompetition = new java.util.Date();
		ArrayList<Date> res = new ArrayList<Date>();
		java.text.SimpleDateFormat sdf =
				new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"select start_time from participation_athletes join schedule_olymp\n" +
							"\ton participation_athletes.competition_id = schedule_olymp.competition_id\n" +
							"\t\t\t\t\t\t\t\t\t\t\t  join athlete\n" +
							"\ton participation_athletes.athlete_id = athlete.id\n" +
							"where athlete.country_id in (select id from country where country.id = ?);");
			stmt.setInt(1, countryId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
					res.add(rs.getDate("start_time"));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * @param competition name
	 * @return Id server.Competition
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

	public int athleteId(Athlete athlete) {
		PreparedStatement stmt = null;
		int athleteId = 0;
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"SELECT id  FROM athlete WHERE athlete_name=?;");
			stmt.setString(1, athlete.getName());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				athleteId = rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return athleteId;

	}

	public void insertInParticipantsAthlets(int competitionId, int athleteId) {
		PreparedStatement stmt = null;
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"INSERT INTO participation_athletes Values (?,?)");
			stmt.setInt(1, competitionId);
			stmt.setInt(2, athleteId);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public boolean isSportInDay(String sport, Date day) {
		int competitionId = competitionId(sport);
		PreparedStatement stmt = null;
		java.text.SimpleDateFormat sdf =
				new java.text.SimpleDateFormat("yyyy-MM-dd");
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"SELECT *  FROM schedule_olymp WHERE competition_id=? and start_time like ?;");
			stmt.setInt(1, competitionId);
			stmt.setString(2, sdf.format(day) + "%");
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public ArrayList<String> competitionsInDay(Date day) {
		ArrayList<String> res = new ArrayList<String>();
		PreparedStatement stmt = null;
		java.text.SimpleDateFormat sdf =
				new java.text.SimpleDateFormat("yyyy-MM-dd");
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"SELECT competition_name  FROM schedule_olymp join competition" +
							" on competition.id = competition_id WHERE start_time like ?;");
			System.out.println(sdf.format(day));
			stmt.setString(1, sdf.format(day)+"%");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				res.add(rs.getString("competition_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public ArrayList<Integer> isCountryInDay(String country, Date day) {
		int countryId = countryId(country);
		ArrayList<Integer> res = new ArrayList<Integer>();
		PreparedStatement stmt = null;
		java.text.SimpleDateFormat sdf =
				new java.text.SimpleDateFormat("yyyy-MM-dd");
		System.out.println("id= "+ countryId);
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"select schedule_olymp.competition_id from participation_athletes join schedule_olymp" +
							" on participation_athletes.competition_id = schedule_olymp.competition_id" +
							" join athlete on participation_athletes.athlete_id = athlete.id " +
							" where athlete.country_id in (select id from country where country.id = ?)" +
							" and start_time like ?;");
			stmt.setInt(1, countryId);
			stmt.setString(2, sdf.format(day) + "%");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				res.add(rs.getInt("competition_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
	}

	public String competition(int competitionId) {
		PreparedStatement stmt = null;
		String result = "";
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"SELECT competition_name  FROM competition WHERE id=?;");
			stmt.setInt(1, competitionId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				result = rs.getString("competition_name");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public DayTimetable getTimeTable(ArrayList<Filter> filters, int numberDay) {
		if (filters.size() == 0){
			return new DayTimetable();
		}
	    Date dayOlimp = getCompetitionDate(1);
		numberDay--;
	   Date day = new Date(dayOlimp.getTime() + numberDay * 1000L * 60L * 60L * 24L);
       ArrayList<String> filtSport = new ArrayList<String>();
		ArrayList<String> filtCountry = new ArrayList<String>();
		ArrayList<String> competitionInDay = competitionsInDay(day);
		competitionInDay.remove(0);
		ArrayList<DaySportElement> res = new ArrayList<DaySportElement>();
		for (Filter filter : filters) {
			if (filter.getFilterName().equals("sportFilter")) {
				for (String sport : filter.getFilter()) {
					if (isSportInDay(sport, day)) {
						filtSport.add(sport);
					}
				}
			}
				if (filter.getFilterName().equals("countryFilter")) {
					for (String country : filter.getFilter()) {
						ArrayList<Integer> temp = isCountryInDay(country, day);
						for (int temp2 : temp){
							filtCountry.add(competition(temp2));
						}
					}
				}
			    System.out.println(filtCountry);
				System.out.println(competitionInDay);
			    System.out.println(filtSport);

			}
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		for (String competition : competitionInDay) {
			if (filtCountry.contains(competition) || filtSport.contains(competition)) {
				res.add(new DaySportElement(sdf.format(getCompetitionDate(competitionId(competition)))+ " " + competition, true));
			} else {
				res.add(new DaySportElement(sdf.format(getCompetitionDate(competitionId(competition)))+ " " + competition, false));
			}
		}

		return new DayTimetable(res);

	}


	public void insertInSheduleOlymp(ArrayList<Competition> competitions) {
		PreparedStatement stmt = null;
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"delete from schedule_olymp;");
			stmt.executeUpdate();

            stmt = (PreparedStatement) connection.prepareStatement(
                    "INSERT INTO schedule_olymp Values (?,?,?,?);");
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date openOlimp = null;
            java.util.Date finishOpenOliDate = null;
            java.util.Date temp = null;
            try {
                temp = inputFormat.parse(Utils.openOlimp);
                openOlimp = new Date(temp.getTime());
                finishOpenOliDate = new Date(temp.getTime() +7200000);
            } catch (ParseException e) {

                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            stmt.setInt(1,1);
            stmt.setInt(2,1);
            stmt.setString(3,inputFormat.format(openOlimp));
            stmt.setString(4,inputFormat.format(finishOpenOliDate));
            stmt.executeUpdate();

            for (Competition competition : competitions) {
				stmt = (PreparedStatement) connection.prepareStatement(
						"INSERT INTO schedule_olymp Values (?,?,?,?);");

				stmt.setInt(1, competition.getId());
				stmt.setInt(2, competition.getSportObjectId());
                java.util.Date dayOlimp = getCompetitionDate(1);
				java.util.Date startTime = new Date(dayOlimp.getTime() + ((competition.getBeginHour() - 1)/PlanGenerator.DAY_LENGTH)*24L * 1000L * 60L * 60L + ((competition.getBeginHour()-1)%PlanGenerator.DAY_LENGTH)*1000L * 60L * 60L);
                java.util.Date finishTime = new Date(startTime.getTime() + competition.getDuration() * 60L * 60L * 1000L);//((competition.getEndHour() - 1)/PlanGenerator.DAY_LENGTH)*24L * 1000L * 60L * 60L + ((competition.getEndHour()-1)%PlanGenerator.DAY_LENGTH)*1000L * 60L * 60L);
				stmt.setString(3, inputFormat.format(startTime));
				stmt.setString(4, inputFormat.format(finishTime));
				stmt.executeUpdate();
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public ArrayList<String> getCountryNames(){
		PreparedStatement stmt = null;
		ArrayList<String> result = new ArrayList<String>();
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"SELECT country_name  FROM country;");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result.add(rs.getString("country_name"));
			}

		} catch (SQLException e) {
			System.out.println("проблема с выдачей имен стран");
		}
		return result;
	}

	public ArrayList<String> getCompetitionNames(){
		PreparedStatement stmt = null;
		ArrayList<String> result = new ArrayList<String>();
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"SELECT competition_name  FROM competition;");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result.add(rs.getString("competition_name"));

			}

		} catch (SQLException e) {
			System.out.println("проблема с выдачей имен соревнований");
		}
		return result;
	}

	public ArrayList<Filter> getFilters(){
		ArrayList<Filter> res = new ArrayList<Filter>();
		res.add( new Filter("countryFilter",getCountryNames()));
		res.add( new Filter("sportsFilter", getCompetitionNames()));
		return res;
	}

	public void insertCountryApplication(CountryApplication countryApplication) {
		PreparedStatement stmt = null;
		String login = countryApplication.getLogin();
		String password = countryApplication.getPassword();
		System.out.println(login);
		System.out.println(password);
		System.out.println(countryApplication.getCompetitionList().getMaxAthleteNumber("ski race"));

		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"delete from participation_athletes where athlete_id in \n" +
							"\t\t\t\t\t\t\t    (Select id from athlete where country_id in \n" +
							"\t\t\t\t\t\t\t (Select id from country where login = ? and hash_password = ?));\n");

			stmt.setString(1, login);
			stmt.setString(2, password);

			stmt.executeUpdate();
			stmt = (PreparedStatement) connection.prepareStatement(
							"\t\t\t\t\t\t\tdelete from athlete where country_id in \n" +
							"\t\t\t\t\t\t\t(Select id from country where login = ? and hash_password = ?);");
			stmt.setString(1, login);
			stmt.setString(2, password);
			stmt.executeUpdate();
			String country = countryByLoginPassword(login, password);
			int countryId = countryId(country);
			for (ClientCompetition clientCompetition : countryApplication.getCompetitionList().getCompetitionList()) {
				String competition = clientCompetition.getCompetition();
				for (Athlete athlete : clientCompetition.getAthleteCompetitionList()) {
					insertInAthlets(athlete, countryId);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static int[] getFilterSportArray(ArrayList<String> sports) {
		int[] res = new int[Utils.maxCountDays];
		try {
			Database db = Database.createDatabase();
			for (int i = 0; i < Utils.maxCountDays; ++i) {
				res[i] = 0;
			}
			for (String sport : sports) {
				int competitionId = db.competitionId(sport);
				res[(int) db.competitionDay(competitionId)] = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("sport day:");
		for( int i = 0; i < res.length; ++i){
			System.out.print(res[i] + " ");
		}
		return res;
	}


	private static int[] getFilterCountryArray(ArrayList<String> countries) {
		int[] res = new int[Utils.maxCountDays];
		try {
			Database db = Database.createDatabase();
			for (int i = 0; i < Utils.maxCountDays; ++i) {
				res[i] = 0;
			}
			for (String country : countries) {
				int countryId = db.countryId(country);
				ArrayList<Integer> temp = db.countryDays(countryId);
				for (int a : temp) {
					res[a] = 1;
				}
			}
			System.out.println("country day:");
			for( int i = 0; i < res.length; ++i){
				System.out.print(res[i] + " ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public DayList getDayList(ArrayList<Filter> filters) {
		int[] dayList = new int[Utils.maxCountDays + 1];
		try{
		for (int i = 0; i < Utils.maxCountDays + 1; ++i) {
			dayList[i] = 1;
		}
			System.out.println(filters.size());
		for (Filter filt : filters) {
			int[] temp = new int[Utils.maxCountDays + 1];
			int[] temp2 = new int[Utils.maxCountDays + 1];
			//in this "if" we iterate all filters/
			if (filt.getFilterName().equals("sportsFilter")) {
				temp = getFilterSportArray(filt.getFilter());
				for (int i = 0; i < Utils.maxCountDays; ++i) {
					if (temp[i] == 0) {
						dayList[i] = 0;
					}
				}
			}
			if (filt.getFilterName().equals("countryFilter")) {
				temp2 = getFilterCountryArray(filt.getFilter());
				for (int i = 0; i < Utils.maxCountDays; ++i) {
					if (temp2[i] == 0) {
						dayList[i] = 0;
					}
				}
			}

		}
		} catch (Exception e){
			System.out.println("Проблема с выдачей DayList");
		}
		ArrayList<Integer> res = new ArrayList<Integer>();
		for (int i = 0; i < Utils.maxCountDays; ++i) {
			if (dayList[i] == 1) {
				res.add(i+1);
			}
		}
		System.out.println("DayList :" + res );

		return new DayList(res);
	}


	public ArrayList<Athlete> getAthletesByCompetitionAthlete(String country, String competition){
		PreparedStatement stmt = null;
		int countryID = countryId(country);
		ArrayList<Athlete> result = new ArrayList<Athlete>();
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"SELECT athlete_name, sex, weight,growth  FROM participation_athletes join " +
							"competition on competition.id = competition_id " +
							"join athlete on athlete.id = athlete_id WHERE athlete.country_id = ? and competition_name = ? ;");
			stmt.setInt(1, countryID);
			stmt.setString(2,competition);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String name = rs.getString("athlete_name");
				Sex sex = encodeAthleteSex(rs.getInt("sex"));
				int weight = rs.getInt("weight");
				int height = rs.getInt("growth");
				Athlete atl = new Athlete(name, sex,weight,height,competition);
			    result.add(atl);
			}
	} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public CountryApplication getCountryApplication(String login, String password) {
		CountryApplication res = null;
		PreparedStatement stmt = null;
		ArrayList<String> competitions = new ArrayList<String>();
		ArrayList<Integer> maxNumberAthlets = new ArrayList<Integer>();
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"select competition_name, number_participants from country_quote join country on country_id = country.id\n" +
							"\t\t\t\t\t\t\tjoin competition on competition_id = competition.id\n" +
							"where country.login = ? and country.hash_password =?;\n");
			stmt.setString(1, login);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {

				String competition_name = rs.getString("competition_name");
				int number_participants = rs.getInt("number_participants");
				competitions.add(competition_name);
				maxNumberAthlets.add(number_participants);
			}
			int size = competitions.size();
			String[] temp1 = new String[size];
			int[] temp2 = new int[size];
			for (int i = 0; i < size; ++i) {
				temp1[i] = competitions.get(i);
				temp2[i] = maxNumberAthlets.get(i);
			}
			CompetitionList competitionList = new CompetitionList(temp1, temp2);
			for (String competition : competitions){
				ArrayList<Athlete> athletes = getAthletesByCompetitionAthlete(countryByLoginPassword(login,password), competition);
				for (Athlete atl : athletes){
					competitionList.addAthlete(0,competition,atl);
				}
			}
			return new CountryApplication(login, password, competitionList);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return new CountryApplication();

	}


	public void insertInAthlets(Athlete athlete, int country_id) {
		PreparedStatement stmt = null;
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"select * from athlete where athlete_name = ? ");
			stmt.setString(1,athlete.getName());
			ResultSet rs = stmt.executeQuery();
			if (!rs.first()) {
				stmt = (PreparedStatement) connection.prepareStatement(
						"INSERT INTO athlete Values (NULL,?,?,?,?,?)");
				stmt.setString(1, athlete.getName());
				stmt.setInt(2, athlete.getSex().getSex());
				stmt.setFloat(3, athlete.getWeight());
				stmt.setFloat(4, athlete.getHeight());
				stmt.setInt(5, country_id);
				stmt.executeUpdate();

			}
			stmt.close();
			int athleteId = athleteId(athlete);
			int competitionId = competitionId(athlete.getCompetition());
			insertInParticipantsAthlets(competitionId, athleteId);
		} catch (SQLException e) {
			e.printStackTrace();
		}

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
	 * Encodes the database sex value to {@link beans.Sex}.
	 * 0 -> Sex.Female
	 * 1 -> Sex.Male
	 * otherwise -> Sex.Undefined
	 *
	 * @param number value to encode.
	 * @return encoded {@link beans.Sex}.
	 */
	private Sex encodeAthleteSex(int number) {
		if ((number == Sex.male) || (number == Sex.female)) {
			return new Sex(number);
		}
		return new Sex(Sex.undefined);
	}

	/**
	 * Returns {@link ApplicationConstrain} for the country by its name.
	 *
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
			while (rs.next()) {
				String competition = rs.getString("competition_name");
				Sex sex_participant = encodeAthleteSex(rs.getInt("sex_participant"));
				int number_participant = rs.getInt("number_participants");
				result.add(new SportConstrain(competition, number_participant, sex_participant));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (result.size() > 0) {
			return new ApplicationConstrain(result);
		} else {
			return new ApplicationConstrain();
		}
	}

	/**
	 * Returns number of sport objects in DB.
	 *
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
				result.add(new Competition(id, duration, sexParticipant, objectTypeId));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @param competitionID1
	 * @param competitionID2
	 * @return true If the competition have the same participants
	 */
	public boolean athleteCollision(int competitionID1, int competitionID2) {
		PreparedStatement stmt = null;
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"SELECT  athlete_id FROM participation_athletes\n" +
							"WHERE competition_id = ? and (athlete_id) IN\n" +
							"(SELECT athlete_id FROM participation_athletes\n" +
							" where competition_id = ?);");
			stmt.setInt(1, competitionID1);
			stmt.setInt(2, competitionID2);
			ResultSet rs = stmt.executeQuery();
			if (rs.first()) {
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @return lists sportobjects where index=typeIdSportObjects
	 */
	public ArrayList<Integer>[] allSportObjectsByType() {
		PreparedStatement stmt = null;
		int countTypeObject = countTypeSportObjects();
		ArrayList<Integer>[] result = new ArrayList[countTypeObject + 1];
		result[0]= new ArrayList<Integer>();
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"SELECT id FROM sportobject_type");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int typeId = rs.getInt("id");
				result[typeId] = sportObjectsIdByTypeID(typeId);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @return count types sport objects
	 */
	public int countTypeSportObjects() {
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"SELECT count(*)  FROM sportobject_type;");
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
	 * @param typeIdSportobject
	 * @return all sport objects with typeID = typeIdSportobject
	 */
	public ArrayList<Integer> sportObjectsIdByTypeID(int typeIdSportobject) {
		PreparedStatement stmt = null;
		ArrayList<Integer> result = new ArrayList<Integer>();
		try {
			stmt = (PreparedStatement) connection.prepareStatement(
					"SELECT id FROM sport_object where object_type_id = ?");
			stmt.setInt(1, typeIdSportobject);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int sportObjectId = rs.getInt("id");
				result.add(sportObjectId);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	private Connection connection;
}

