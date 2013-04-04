package server;

import beans.CompetitionList;
import beans.CountryApplication;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import utils.Utils;

import java.sql.SQLException;

public class ApplicationPostResponseCreator extends ResponseCreator {
	public ApplicationPostResponseCreator(Document dom) {
		this.dom = dom;
	}

	public String createResponse() {
		Element root     = dom.getDocumentElement();
		String  login    = root.getAttribute("login");
		String  password = root.getAttribute("password");

		CountryApplication application = getApplicationByCountry(login, password);

		return Utils.beanToString(application);
	}

	private CountryApplication getApplicationByCountry(String login, String password) {
		try {
			Database db = Database.createDatabase();
			CountryApplication result = db.getCountryApplication(login, password);
			db.closeConnection();
			return result;

		} catch (SQLException e) {
			//TODO: норм комментарий
			System.err.println("Проблема с базой при ApplicationConstrain!");
		}

		return new CountryApplication();
	}

	private Document dom;
}
