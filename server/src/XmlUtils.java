import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class XmlUtils {
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

	public static class DomToStringTranslationException extends Exception {
	}

	// Just for masking constructor
	private XmlUtils() {
	}
}
