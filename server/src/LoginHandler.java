import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class LoginHandler {
	public LoginHandler(Document doc) {
		this.doc = doc;
	}

	public String exec() {
		Element root     = doc.getDocumentElement();
		String  login    = root.getAttribute("login");
		String  password = root.getAttribute("password");

		String countryName = getCountryName(login, password);

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder        builder = factory.newDocumentBuilder();
			Document outDoc  = builder.newDocument();
			Element  outRoot = outDoc.createElement("login-response");
			Attr     outAttr = outDoc.createAttribute("country");
			outAttr.setValue(countryName);
			outRoot.setAttributeNode(outAttr);
			outDoc.appendChild(outRoot);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer        transformer        = transformerFactory.newTransformer();
			DOMSource          source             = new DOMSource(outDoc);
			StringWriter       writer             = new StringWriter();
			StreamResult       resultDOMstring    = new StreamResult(writer);
			transformer.transform(source, resultDOMstring);

			String result = writer.toString();
			System.out.println("response: " + result);
			return result;

		} catch (ParserConfigurationException e) {
			System.err.println(e.toString());
		} catch (TransformerConfigurationException e) {
			System.err.println(e.toString());
		} catch (TransformerException e) {
			System.err.println(e.toString());
		}

		return "";
	}

	private static String getCountryName(String login, String password) {
		//TODO: checking in base
		return "RUSLAND";
	}

	private Document doc;
}
