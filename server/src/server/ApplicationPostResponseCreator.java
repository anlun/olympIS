package server;

import beans.CompetitionList;
import beans.CountryApplication;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import utils.Utils;

public class ApplicationPostResponseCreator extends ResponseCreator {
	public ApplicationPostResponseCreator(Document dom) {
		this.dom = dom;
	}

	public String createResponse() {
		Element root     = dom.getDocumentElement();
		String  login    = root.getAttribute("login");
		String  password = root.getAttribute("password");

		CountryApplication application = getApplicationByCountry(
				getCountryName(login, password)
		);

		return Utils.beanToString(application);
	}

	//TODO: Need to be implemented with data from database.
	//If login-password incorrect this method must return ""
	private String getCountryName(String login, String name) {
		return "RUSLAND";
	}

	private CountryApplication getApplicationByCountry(String countryName) {
		if (countryName.equals("")) {
			//return new CountryApplication();
		}

		//TODO: reading from database, change this return
        int[]mas={2,3,4};
        String[] st={"a","b","c"};
		return new CountryApplication("","",new CompetitionList(st, mas));
	}

	private Document dom;
}
