import beans.Athlete;
import beans.CountryApplication;
import beans.Sex;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import sun.misc.IOUtils;
import utils.Utils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;

/**
 * {@link ServerConsoleWrapper} is the center class of server application.
 * So actually it IS the server.
 * To start server must be called main method, after that server
 * can be stopped by key press.
 * Alternatively server console wrapper can be started by {@link ServerConsoleWrapper#startServer(java.net.InetSocketAddress)} method.
 *
 * @author Podkopaev Anton
 */
public class ServerConsoleWrapper {
	/**
	 * Creates {@link ServerConsoleWrapper}. The start point for all server application.
	 *
	 * @param args Just ignores now.
	 */
	public static void main(String[] args) {
		ArrayList<Athlete> athleteList = new ArrayList<Athlete>();
		athleteList.add(new Athlete("asd", new Sex(Sex.male), 123, 124, "asd"));
		CountryApplication app = new CountryApplication(
				"log", "pas"
				, athleteList
		);
		String xmlView = Utils.beanToString(app);
		System.out.println(xmlView);

		System.out.println("Test http server");
		startServer(new InetSocketAddress(8888));
	}

	/**
	 * Starts server that will be listened socketAddress socket.
	 *
	 * @param socketAddress Socket address to listen.
	 */
	public static void startServer(InetSocketAddress socketAddress) {
		ServerConsoleWrapper serverConsoleWrapper = new ServerConsoleWrapper(socketAddress);
		serverConsoleWrapper.start();
	}

	/**
	 * Creates HttpServer {@link ServerConsoleWrapper#server}.
	 *
	 * @param socketAddress Socket address to listen by {@link ServerConsoleWrapper#server}.
	 */
	private ServerConsoleWrapper(InetSocketAddress socketAddress) {
		try {
			int backlog = 10; // Just default value for HttpServer
			server = HttpServer.create(socketAddress, backlog);
		} catch (IOException e) {
			System.err.println("Oops!");
			e.printStackTrace();
		}
		server.createContext("/", new Handler());
	}

	/**
	 * Starts {@link ServerConsoleWrapper#server} and listens for user key press.
	 */
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

	/**
	 * The {@link Handler} class implements {@link Handler#handle(com.sun.net.httpserver.HttpExchange)} method.
	 * It is the central class for processing data that exchanges through the Http.
	 */
	private class Handler implements HttpHandler {
		public void handle(HttpExchange exc) throws IOException {
			// Reading the http request to string
			String command = new String(
					IOUtils.readFully(exc.getRequestBody(), -1, false)
					, "UTF-8");

			//for debug purposes
			//System.out.println(command);

			// Forming the answer
			String answer = "";
			try {
				answer = handleXML(command);
			} catch (UnknownXMLdocumentException e) {
				System.out.println();
				throw new IOException();
			}
			//for debug purposes
			//System.out.println("Result: " + result);

			// Transfer answer to client
			exc.sendResponseHeaders(200, 0);
			PrintWriter out = new PrintWriter(exc.getResponseBody());
			out.println(answer);
			out.close();
			exc.close();
		}

		private String handleXML(String xmlString) throws UnknownXMLdocumentException {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			try {
				// Creates DOM for XML
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document dom = builder.parse(new InputSource(new StringReader(xmlString)));
				Element root = dom.getDocumentElement();
				String tagName = root.getTagName();

				//TODO: add handlers for another cases of protocol
				if (tagName.equalsIgnoreCase("login-request")) {
					return (new LoginResponseCreator(dom)).createResponse();

				} else if (tagName.equalsIgnoreCase("application-constrain-request")) {
					return (new ApplicationConstrainResponseCreator(dom)).createResponse();
				}

				System.err.println(tagName);

			} catch (ParserConfigurationException e) {
				System.err.println(e.toString());
			} catch (SAXException e) {
				System.err.println(e.toString());
			} catch (IOException e) {
				System.err.println(e.toString());
			}

			throw new UnknownXMLdocumentException();
		}

		private class UnknownXMLdocumentException extends Exception {
		}
	}

	private HttpServer server;
}