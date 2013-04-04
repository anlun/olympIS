package server;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Extends the {@link ResponseCreator} for login task.
 * @author Podkopaev Anton
 */
public class LoginResponseCreator extends ResponseCreator {
	public LoginResponseCreator(Document dom) {
		this.dom = dom;
	}

	public String createResponse() {
		Element root     = dom.getDocumentElement();
		String  login    = root.getAttribute("login");
		String  password = root.getAttribute("password");

		String country = getCountryName(login, password);

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder        builder = factory.newDocumentBuilder();
			Document outDoc  = builder.newDocument();
			Element  outRoot = outDoc.createElement("login-response");
			Attr     outAttr = outDoc.createAttribute("country");
			outAttr.setValue(country);
			outRoot.setAttributeNode(outAttr);
			outDoc.appendChild(outRoot);

			String result = XMLutils.domToXmlString(outDoc);
			System.out.println("response: " + result);
			return result;

		} catch (ParserConfigurationException e) {
			System.err.println(e.toString());
		} catch (XMLutils.DomToStringTranslationException e) {
			System.err.println(e.toString());
		}

		return "";
	}

	//TODO: Need to be implemented with data from database.
	//If login-password incorrect this method must return ""
	private static String getCountryName(String login, String password) {
		return "RUSLAND";
	}

	private Document dom;
}
