package beans;

import utils.CustomSerializable;
import utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;

//TODO: сделать кустарную сериализацию
public class CountryApplication implements Serializable, CustomSerializable {
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

	public ArrayList<Athlete> getAthleteList() {
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

	public String serialize(boolean withBeansHead) {
		String result = "<object class=\"beans.CountryApplication\">";

		//Tags for fields
		//May be need to be in alphabetical order

		//athleteList
		result += Utils.arrayListToBeanField("athleteList", athleteList);

		//login
		result += Utils.stringToBeanField("login", login);

		//password
		result += Utils.stringToBeanField("password", password);

		result += "</object>";

		if (withBeansHead) {
			result =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n"
					+ "<java version=\"1.7.0_09\" class=\"com.googlecode.openbeans.XMLDecoder\">"
					+ result
					+ "</java>";
		}

		return result;
	}

	private String login;
	private String password;
	private ArrayList<Athlete> athleteList;
}
