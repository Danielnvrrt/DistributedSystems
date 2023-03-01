package chat;

import java.io.IOException;

import org.w3c.dom.Node;
import utils.PropertyHandler;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import utils.NetworkUtilities;

/**
 * Chat Client class
 *
 * Reads configuration information and starts up the chat Client
 */
public class ChatClient implements Runnable {

	// set variables for the receiver and the sender
	static Receiver receiver = null;
	static Sender[] sender = new Sender[10];


	// set the client connectivity information
	public static NodeInfo myNodeInfo = null;
	public static NodeInfo serverNodeInfo = null;


	public static ArrayList<NodeInfo> participantsInfo = new ArrayList<>();
	public static ArrayList<String> currentParticipants = new ArrayList<>();
	public static ArrayList<String> visitedParticipant = new ArrayList<>();
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
		String addres = NetworkUtilities.getMyIP();
		myNodeInfo = new NodeInfo(addres, myPort, myName, false);

		participantsInfo.add(myNodeInfo);
		currentParticipants.add(addres + String.valueOf(myPort));
	}

	// code entry point
	@Override
	public void run() {
		// start the Receiver
		(receiver = new Receiver()).start();
		
		// start the sender
		(sender[0] = new Sender()).start();
				
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
		new ChatClient(propertiesFile).run();
		
		while(true) {
			for (int count = 1; count < participantsInfo.size(); count++) {
				if(participantsInfo.get(count).getJoined() == false) {
					(sender[count] = new Sender("JOIN " + participantsInfo.get(count).getAddress() + " " + String.valueOf(participantsInfo.get(count).getPort()), count)).start();
					try {
						Thread.sleep(2);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					participantsInfo.get(count).setJoined(true);
				}
			}
		}
	}
}