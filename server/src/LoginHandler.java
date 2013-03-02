import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class LoginHandler {
	public LoginHandler(Document dom) {
		this.dom = dom;
	}

	public String exec() {
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

			String result = XmlUtils.domToXmlString(outDoc);
			System.out.println("response: " + result);
			return result;

		} catch (ParserConfigurationException e) {
			System.err.println(e.toString());
		} catch (XmlUtils.DomToStringTranslationException e) {
			System.err.println(e.toString());
		}

		return "";
	}

	private static String getCountryName(String login, String password) {
		//TODO: checking in base
		return "RUSLAND";
	}

	private Document dom;
}
