import beans.CountryApplication;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

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

			//TODO: запись в базу

			return successResponse();

		} catch (UnsupportedEncodingException e) {
			System.err.println("Unsupported encoding error!");
		}

		return failResponse();
	}

	//TODO
	private static String failResponse() {
		return "";
	}

	//TODO
	private static String successResponse() {
		return "";
	}

	String applicationXML;
}
