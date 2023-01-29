package chat;

import java.io.IOException;
import utils.PropertyHandler;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.NetworkUtilities;

/**
 * Chat Client class
 *
 * Reads configuration information and starts up the chat Client
 */
public class ChatClient implements Runnable {

	// set variables for the receiver and the sender
	static Receiver receiver = null;
	static Sender sender = null;

	// set the client connectivity information
	public static NodeInfo myNodeInfo = null;
	public static NodeInfo serverNodeInfo = null;

	// ChatClient constructor
	public ChatClient(String propertiesFile) {
		// try getting the properties from the file
		Properties properties = null;
		try {
			properties = new PropertyHandler(propertiesFile);
		}

		// if properties were not found then close the program
		catch (IOException ex) {
			Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Could not open the properties file", ex);
			System.exit(1);
		}

		// get the port number for the Receiver
		int myPort = 0;
		try {
			myPort = Integer.parseInt(properties.getProperty("MY_PORT"));
		}

		// if port number was not able to be read then close the program
		catch (NumberFormatException ex) {
			Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Could not read receiver port", ex);
			System.exit(1);
		}

		// get the name of the Client
		String myName = properties.getProperty("MY_NAME");

		// check if the name is null, if so then close the program
		if (myName == null) {
			Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Could not read the clients name");
			System.exit(1);
		}

		// create the node info for the Client
		myNodeInfo = new NodeInfo(NetworkUtilities.getMyIP(), myPort, myName);

		// get the server's default port number
		int serverPort = 0;
		try {
			serverPort = Integer.parseInt(properties.getProperty("SERVER_PORT"));
		} catch (NumberFormatException ex) {
			Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Could not read server port", ex);
		}

		// get the server's default IP address
		String serverIP = null;
		serverIP = properties.getProperty("SERVER_IP");
		if (serverIP == null) {
			Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Could not read server IP address");
		}

		// create the default connectivity information for the server
		if (serverPort != 0 && serverIP != null) {
			serverNodeInfo = new NodeInfo(serverIP, serverPort);
		}
	}

	// code entry point
	@Override
	public void run() {
		// start the Receiver
		(receiver = new Receiver()).start();

		// start the sender
		(sender = new Sender()).start();
	}

	// main for ChatClient
	public static void main(String[] args) {
		// set the properties file to null
		String propertiesFile = null;

		// read any properties files as arguments
		try {
			propertiesFile = args[0];
		} catch (ArrayIndexOutOfBoundsException ex) {
			propertiesFile = "config/ChatNodeDefaults.properties";
		}

		// start ChatNode
		(new ChatClient(propertiesFile)).run();
	}
}