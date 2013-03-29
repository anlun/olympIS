package beans;

import java.io.Serializable;
import java.util.ArrayList;

public class CountryApplication implements Serializable {

	public CountryApplication() {
	}

	public CountryApplication(String login, String password, CompetitionList competitionList) {
		this.competitionList = competitionList;
		this.login = login;
		this.password = password;
	}

	public void setCompetitionList(CompetitionList competitionList) {
		this.competitionList = competitionList;
	}

	public CompetitionList getCompetitionList() {
		return competitionList;
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
	private CompetitionList competitionList;
}
