import java.io.*;
import java.net.InetSocketAddress;
import java.util.logging.Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

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
			System.out.println("Oops!");
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
			System.out.println("Oops!");
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

			//String result = executor.execute(command);
			String result = "PPSPPAPSPAPSPA";

			PrintWriter out = new PrintWriter(exc.getResponseBody());
			out.println(result);
			out.close();
			exc.close();
		}
	}

	private HttpServer server;
}