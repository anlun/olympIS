package beans;

import java.io.Serializable;
import java.util.ArrayList;

public class CountryApplication implements Serializable {

	public CountryApplication() {
	}

	public CountryApplication(String login, String password, ArrayList<Athlete> athleteList) {
		this.athleteList = athleteList;
		this.login = login;
		this.password = password;
	}

	public void setAthleteList(ArrayList<Athlete> athleteList) {
		this.athleteList = athleteList;
	}

	public ArrayList<Athlete> getAthleteList(int index) {
		return athleteList;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getLogin() {
		return login;
	}

	private String login;
	private String password;
	private ArrayList<Athlete> athleteList;
}
