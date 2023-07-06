
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

	static Vector<ClientHandler> clients = new Vector<>();
	private static int port = 4454;
	private static ServerSocket serverSocket;
	private static DataInputStream input;
	private static DataOutputStream output;
	static int clientsConnected = 0;

	private static void connectClients(ServerSocket clientSocket) throws IOException {

		Socket s = clientSocket.accept();

		System.out.println("A new client has connected to this [server]");

		input = new DataInputStream(s.getInputStream());
		output = new DataOutputStream(s.getOutputStream());

		ClientHandler mtch = new ClientHandler(s, "clients", input, output);

		clienthandlermethod(mtch);

	}

	private static void clienthandlermethod(ClientHandler mtch) {

		Thread thread = new Thread(mtch);

		clients.add(mtch);

		thread.start();

		clientsConnected++;

		System.out.println("Currently there are: " + clientsConnected + " clients connected");
		System.out.println("Updated list of people online");
		for (ClientHandler mcc : clients) {
			System.out.println(mcc.name);

		}
	}

	public static void main(String[] args) throws IOException {

		serverSocket = new ServerSocket(port);

		while (true) {

			connectClients(serverSocket);

		}

	}

}
