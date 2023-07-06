
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

	final static int ServerPort = 4454;
	private DataOutputStream dataoutput;
	private DataInputStream datainput;
	private Socket socket;

	public Client(Socket socket) throws IOException {

		this.dataoutput = new DataOutputStream(socket.getOutputStream());
		this.datainput = new DataInputStream(socket.getInputStream());
		this.socket = socket;

	}

	public void start_connection(String serverAddress, int ServerPort) {

		try {

			Scanner scn = new Scanner(System.in);
			String user_ID = "";
			System.out.print("Enter your name : ");
			user_ID = scn.nextLine();
			dataoutput.writeUTF(user_ID);
			System.out.println("Welcome " + user_ID + ", You may start typing your text now!!");

			Thread message_sent = new Thread(new Runnable() {
				@Override
				public void run() {

					String message_to_server = "";
					while (!message_to_server.equals("exit")) {

						message_to_server = scn.nextLine();

						try {

							dataoutput.writeUTF(message_to_server);
						} catch (IOException e) {
							System.out.println("Message is equal to exit, could not write");

							try {
								dataoutput.close();
								scn.close();

							} catch (Exception s) {
								s.printStackTrace();
							}

						}
					}
				}
			});

			Thread message_received = new Thread(new Runnable() {
				@Override
				public void run() {

					while (true) {
						try {
							String message_from_server;
							message_from_server = datainput.readUTF();
							if (message_from_server.equals("exit")) {
								System.out.println("A client has left the chat");

								try {
									datainput.close();
									scn.close();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								break;
							}
							System.out.println(message_from_server);

						} catch (IOException e) {
							System.out.println("You have left the chat!");

							break;
						}
					}
				}
			});
			message_sent.start();
			message_received.start();

		} catch (Exception e) {
			System.out.println("Error in connection to the server");

		}

	}

	public static void main(String args[]) throws UnknownHostException, IOException

	{
		InetAddress ip = InetAddress.getByName("localhost");
		Socket socket = new Socket(ip, ServerPort);
		Client c = new Client(socket);
		c.start_connection("localhost", Client.ServerPort);

	}

}
