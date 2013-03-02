import java.io.*;
import java.net.InetSocketAddress;
import java.util.logging.Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Server {
	public static void main(String[] args) {
		System.out.println("Test http server");
		startNewServer(new InetSocketAddress(8888), 10);
	}

	public static void startNewServer(InetSocketAddress socketAddress, int backlog) {
		Server server = new Server(socketAddress, backlog);
		server.start();
	}
	private Server(InetSocketAddress socketAddress, int backlog) {
		try {
			server = HttpServer.create(socketAddress, backlog);
		} catch (IOException e) {
			System.err.println("Oops!");
			e.printStackTrace();
		}
		server.createContext("/", new Handler());
	}

	private void start() {
		server.start();
		System.out.println("Server started\nPress any key to stop...");
		try {
			System.in.read();
		} catch (IOException e) {
			System.err.println("Oops!");
		} finally {
			server.stop(0);
			System.out.println("Server stopped");
		}
	}

	private class Handler implements HttpHandler {
		public void handle(HttpExchange exc) throws IOException {
			exc.sendResponseHeaders(200, 0);
			BufferedReader in = new BufferedReader(
					new InputStreamReader(exc.getRequestBody()));
			String command = in.readLine();
			in.close();

			System.out.println(command);

			String result = "";
			try {
				result = (new XmlHandler()).handle(command);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			System.out.println("Result: " + result);

			PrintWriter out = new PrintWriter(exc.getResponseBody());
			out.println(result);
			out.close();
			exc.close();
		}
	}

	private class XmlHandler {
		public String handle(String xmlString) throws UnknownXmlDocumentException {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document dom  = builder.parse(new InputSource(new StringReader(xmlString)));
				Element  root = dom.getDocumentElement();
				String tagName = root.getTagName();
				if (tagName.equalsIgnoreCase("login-request")) {
					return (new LoginHandler(dom)).exec();
				}

			} catch (ParserConfigurationException e){
				System.err.println(e.toString());
			} catch (SAXException e) {
				System.err.println(e.toString());
			} catch (IOException e) {
				System.err.println(e.toString());
			}

			throw new UnknownXmlDocumentException();
		}

		public class UnknownXmlDocumentException extends Exception {
		}
	}

	private HttpServer server;
}