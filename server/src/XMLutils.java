import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;

/**
 * Class with useful functions for work with XML.
 * @author Podkopaev Anton
 */
public class XMLutils {
	public static String domToXmlString(Document dom) throws DomToStringTranslationException {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer               = transformerFactory.newTransformer();
			DOMSource source                      = new DOMSource(dom);
			StringWriter writer                   = new StringWriter();
			StreamResult resultDOMstring          = new StreamResult(writer);
			transformer.transform(source, resultDOMstring);
			return writer.toString();

		} catch (TransformerConfigurationException e) {
			System.err.println(e.toString());
		} catch (TransformerException e) {
			System.err.println(e.toString());
		}

		throw new DomToStringTranslationException();
	}

    public static void parserSportObjects(String filePath){
        try {
            Database db = new Database();
            File fXmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("object");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String title =eElement.getAttribute("title");
                    String type = eElement.getElementsByTagName("type").item(0).getTextContent();
                    String city = eElement.getElementsByTagName("city").item(0).getTextContent();
                    Integer capacity = Integer.parseInt(eElement.getElementsByTagName("capacity").item(0).getTextContent());
                    db.insertInSportObject(title,type,city,capacity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void parserContries(String filePath){
        try {
            Database db = new Database();
            File fXmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("country");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String name =eElement.getAttribute("name");
                    String login = eElement.getElementsByTagName("login").item(0).getTextContent();
                    String password = eElement.getElementsByTagName("password").item(0).getTextContent();
                    db.insertInCountries(name,login, password);
                }
            }
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void parserCompetitions(String filePath){
        try {
            Database db = new Database();
            File fXmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("competition");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String title =eElement.getAttribute("title");
                    String type = eElement.getElementsByTagName("type_object").item(0).getTextContent();
                    int sex_participants = Integer.parseInt(eElement.getElementsByTagName("sex_participants").item(0).getTextContent());
                    Integer duration = Integer.parseInt(eElement.getElementsByTagName("duration").item(0).getTextContent());
                    db.insertInCompetition(title,type,sex_participants,duration);
                }
            }
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


	public static class DomToStringTranslationException extends Exception {
	}

	// Just for masking construhttps://github.com/MOHAX239/olympISctor
	private XMLutils() {
	}
}
