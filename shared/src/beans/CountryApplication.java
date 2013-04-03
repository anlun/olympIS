package beans;

import utils.CustomSerializable;
import utils.Utils;

import java.io.Serializable;

public class CountryApplication implements Serializable, CustomSerializable {
	public CountryApplication() {
		competitionList = new CompetitionList();
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

	public String serialize() {
		String result = "<object class=\"beans.CountryApplication\">";

		//Tags for fields
		//May be need to be in alphabetical order

		//competitionList
		result += Utils.objectToBeanField("competitionList", competitionList);

		//login
		result += Utils.stringToBeanField("login", login);

		//password
		result += Utils.stringToBeanField("password", password);
		result += "</object>";

		return result;
	}

	private String login;
	private String password;
	private CompetitionList competitionList;
}
