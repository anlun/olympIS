package server;

import beans.CountryApplication;
import utils.RequestResponseConst;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

public class ApplicationResponseCreator extends ResponseCreator {
	public ApplicationResponseCreator(String applicationXML) {
		this.applicationXML = applicationXML;
	}

	public String createResponse() {
		try {
			XMLDecoder decoder = new XMLDecoder(
					new ByteArrayInputStream(applicationXML.getBytes("UTF-8"))
					);
			CountryApplication app = (CountryApplication) decoder.readObject();
			if (app == null) {
				return failResponse();
			}

			try {
				Database db = Database.createDatabase();
				if (!db.countryByLoginPassword(app.getLogin(), app.getPassword()).equals("")){
					db.insertCountryApplication(app);
					return successResponse();
				}
				db.closeConnection();
			} catch (SQLException e) {
				//TODO: норм комментарий
				System.err.println("Проблема с базой при добавлении countryApplication!");
			}

			return failResponse();

		} catch (UnsupportedEncodingException e) {
			System.err.println("Unsupported encoding error!");
		}

		return failResponse();
	}

	private static String failResponse() {
		return RequestResponseConst.failCountryApplicationResponse();
	}

	private static String successResponse() {
		return RequestResponseConst.successCountryApplicationResponse();
	}

	String applicationXML;
}
